package ru.mercury.vpclient.shared.ui

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Float.max
import kotlin.math.abs

@Stable
class ZoomState(
    @field:FloatRange(from = 1.0) private val maxScale: Float = 5F,
    private var contentSize: Size = Size.Zero,
    private val velocityDecay: DecayAnimationSpec<Float> = exponentialDecay(),
) {
    init {
        require(maxScale >= 1.0F) { "maxScale must be at least 1.0." }
    }

    private var _scale = Animatable(1F).apply {
        updateBounds(.9F, maxScale)
    }

    val scale: Float
        get() = _scale.value

    private var _offsetX = Animatable(0F)

    val offsetX: Float
        get() = _offsetX.value

    private var _offsetY = Animatable(0F)

    val offsetY: Float
        get() = _offsetY.value

    private var layoutSize = Size.Zero

    val isScaled: Boolean
        get() = scale != 1F

    fun setLayoutSize(size: Size) {
        layoutSize = size
        updateFitContentSize()
    }

    fun setContentSize(size: Size) {
        contentSize = size
        updateFitContentSize()
    }

    private var fitContentSize = Size.Zero

    private fun updateFitContentSize() {
        if (layoutSize == Size.Zero) {
            fitContentSize = Size.Zero
            return
        }

        if (contentSize == Size.Zero) {
            fitContentSize = layoutSize
            return
        }

        val contentAspectRatio = contentSize.width / contentSize.height
        val layoutAspectRatio = layoutSize.width / layoutSize.height

        fitContentSize = if (contentAspectRatio > layoutAspectRatio) {
            contentSize * (layoutSize.width / contentSize.width)
        } else {
            contentSize * (layoutSize.height / contentSize.height)
        }
    }

    suspend fun reset(animationSpec: AnimationSpec<Float> = spring()) = coroutineScope {
        launch {
            _offsetX.updateBounds(null, null)
            _offsetX.animateTo(0F, animationSpec)
            _offsetX.updateBounds(0F, 0F)
        }
        launch {
            _offsetX.updateBounds(null, null)
            _offsetY.animateTo(0F, animationSpec)
            _offsetY.updateBounds(0F, 0F)
        }
        _scale.animateTo(1F, animationSpec)
    }

    private var shouldConsumeEvent: Boolean? = null
    private val velocityTracker = VelocityTracker()

    fun startGesture() {
        shouldConsumeEvent = null
        velocityTracker.resetTracking()
    }

    fun canConsumeGesture(pan: Offset, zoom: Float): Boolean {
        return shouldConsumeEvent ?: run {
            var consume = true
            if (zoom == 1F) {
                if (scale == 1F) {
                    consume = false
                } else {
                    val ratio = (abs(pan.x) / abs(pan.y))
                    if (ratio > 3) {
                        if ((pan.x < 0) && (_offsetX.value == _offsetX.lowerBound)) {
                            consume = false
                        }
                        if ((pan.x > 0) && (_offsetX.value == _offsetX.upperBound)) {
                            consume = false
                        }
                    } else if (ratio < .33) {
                        if ((pan.y < 0) && (_offsetY.value == _offsetY.lowerBound)) {
                            consume = false
                        }
                        if ((pan.y > 0) && (_offsetY.value == _offsetY.upperBound)) {
                            consume = false
                        }
                    }
                }
            }
            shouldConsumeEvent = consume
            consume
        }
    }

    suspend fun applyGesture(
        pan: Offset,
        zoom: Float,
        position: Offset,
        timeMillis: Long
    ) = coroutineScope {
        val newScale = (scale * zoom).coerceIn(.9F, maxScale)
        val newOffset = calculateNewOffset(newScale, position, pan)
        val newBounds = calculateNewBounds(newScale)

        _offsetX.updateBounds(newBounds.left, newBounds.right)
        launch { _offsetX.snapTo(newOffset.x) }

        _offsetY.updateBounds(newBounds.top, newBounds.bottom)
        launch { _offsetY.snapTo(newOffset.y) }

        launch { _scale.snapTo(newScale) }

        if (zoom == 1F) {
            velocityTracker.addPosition(timeMillis, position)
        } else {
            velocityTracker.resetTracking()
        }
    }

    suspend fun changeScale(
        targetScale: Float,
        position: Offset,
        animationSpec: AnimationSpec<Float> = spring(),
    ) = coroutineScope {
        val newScale = targetScale.coerceIn(1F, maxScale)
        val newOffset = calculateNewOffset(newScale, position, Offset.Zero)
        val newBounds = calculateNewBounds(newScale)

        val x = newOffset.x.coerceIn(newBounds.left, newBounds.right)
        launch {
            _offsetX.updateBounds(null, null)
            _offsetX.animateTo(x, animationSpec)
            _offsetX.updateBounds(newBounds.left, newBounds.right)
        }

        val y = newOffset.y.coerceIn(newBounds.top, newBounds.bottom)
        launch {
            _offsetY.updateBounds(null, null)
            _offsetY.animateTo(y, animationSpec)
            _offsetY.updateBounds(newBounds.top, newBounds.bottom)
        }

        launch { _scale.animateTo(newScale, animationSpec) }
    }

    private fun calculateNewOffset(
        newScale: Float,
        position: Offset,
        pan: Offset,
    ): Offset {
        val size = fitContentSize * scale
        val newSize = fitContentSize * newScale
        val deltaWidth = newSize.width - size.width
        val deltaHeight = newSize.height - size.height
        val xInContent = position.x - offsetX + (size.width - layoutSize.width) * .5F
        val yInContent = position.y - offsetY + (size.height - layoutSize.height) * .5F
        val deltaX = (deltaWidth * .5F) - (deltaWidth * xInContent / size.width)
        val deltaY = (deltaHeight * .5F) - (deltaHeight * yInContent / size.height)
        val x = offsetX + pan.x + deltaX
        val y = offsetY + pan.y + deltaY
        return Offset(x, y)
    }

    private fun calculateNewBounds(newScale: Float, ): Rect {
        val newSize = fitContentSize * newScale
        val boundX = max((newSize.width - layoutSize.width), 0F) * .5F
        val boundY = max((newSize.height - layoutSize.height), 0F) * .5F
        return Rect(-boundX, -boundY, boundX, boundY)
    }

    suspend fun endGesture() = coroutineScope {
        val velocity = velocityTracker.calculateVelocity()
        if (velocity.x != 0F) {
            launch { _offsetX.animateDecay(velocity.x, velocityDecay) }
        }
        if (velocity.y != 0F) {
            launch { _offsetY.animateDecay(velocity.y, velocityDecay) }
        }
        if (_scale.value < 1F) {
            launch { _scale.animateTo(1F) }
        }
    }

    suspend fun centerByContentCoordinate(
        offset: Offset,
        scale: Float = 3F,
        animationSpec: AnimationSpec<Float> = tween(700),
    ) = coroutineScope {
        val fitContentSizeFactor = fitContentSize.width / contentSize.width
        val boundX = max((fitContentSize.width * scale - layoutSize.width), 0F) / 2F
        val boundY = max((fitContentSize.height * scale - layoutSize.height), 0F) / 2F

        suspend fun executeZoomWithAnimation() {
            listOf(
                async {
                    val fixedTargetOffsetX = ((fitContentSize.width / 2 - offset.x * fitContentSizeFactor) * scale).coerceIn(minimumValue = -boundX, maximumValue = boundX)
                    _offsetX.animateTo(fixedTargetOffsetX, animationSpec)
                },
                async {
                    val fixedTargetOffsetY = ((fitContentSize.height / 2 - offset.y * fitContentSizeFactor) * scale).coerceIn(minimumValue = -boundY, maximumValue = boundY)
                    _offsetY.animateTo(fixedTargetOffsetY, animationSpec)
                },
                async { _scale.animateTo(scale, animationSpec) },
            ).awaitAll()
        }

        if (scale > _scale.value) {
            _offsetX.updateBounds(-boundX, boundX)
            _offsetY.updateBounds(-boundY, boundY)
            executeZoomWithAnimation()
        } else {
            executeZoomWithAnimation()
            _offsetX.updateBounds(-boundX, boundX)
            _offsetY.updateBounds(-boundY, boundY)
        }
    }

    suspend fun centerByLayoutCoordinate(
        offset: Offset,
        scale: Float = 3F,
        animationSpec: AnimationSpec<Float> = tween(700),
    ) = coroutineScope {
        val boundX = max((fitContentSize.width * scale - layoutSize.width), 0F) / 2F
        val boundY = max((fitContentSize.height * scale - layoutSize.height), 0F) / 2F

        suspend fun executeZoomWithAnimation() {
            listOf(
                async {
                    val fixedTargetOffsetX = ((layoutSize.width / 2 - offset.x) * scale).coerceIn(minimumValue = -boundX, maximumValue = boundX)
                    _offsetX.animateTo(fixedTargetOffsetX, animationSpec)
                },
                async {
                    val fixedTargetOffsetY = ((layoutSize.height / 2 - offset.y) * scale).coerceIn(minimumValue = -boundY, maximumValue = boundY)
                    _offsetY.animateTo(fixedTargetOffsetY, animationSpec)
                },
                async { _scale.animateTo(scale, animationSpec) },
            ).awaitAll()
        }

        if (scale > _scale.value) {
            _offsetX.updateBounds(-boundX, boundX)
            _offsetY.updateBounds(-boundY, boundY)
            executeZoomWithAnimation()
        } else {
            executeZoomWithAnimation()
            _offsetX.updateBounds(-boundX, boundX)
            _offsetY.updateBounds(-boundY, boundY)
        }
    }
}

@Composable
fun rememberZoomState(
    @FloatRange(from = 1.0) maxScale: Float = 5F,
    contentSize: Size = Size.Zero,
    velocityDecay: DecayAnimationSpec<Float> = exponentialDecay()
) = remember { ZoomState(maxScale, contentSize, velocityDecay) }

private suspend fun PointerInputScope.detectTransformGestures(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, timeMillis: Long) -> Boolean,
    onGestureStart: () -> Unit = {},
    onGestureEnd: () -> Unit = {},
    onTap: (position: Offset) -> Unit = {},
    onDoubleTap: (position: Offset) -> Unit = {},
    enableOneFingerZoom: Boolean = true,
) = awaitEachGesture {
    val firstDown = awaitFirstDown(requireUnconsumed = false)
    onGestureStart()

    var firstUp: PointerInputChange = firstDown
    var isTap = true
    val touchSlop = TouchSlop(viewConfiguration.touchSlop)
    forEachPointerEventUntilReleased { event ->
        if (touchSlop.isPast(event)) {
            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()
            if (zoomChange != 1F || panChange != Offset.Zero) {
                val centroid = event.calculateCentroid(useCurrent = false)
                val timeMillis = event.changes[0].uptimeMillis
                val canConsume = onGesture(centroid, panChange, zoomChange, timeMillis)
                if (canConsume) {
                    event.consumePositionChanges()
                }
            }
            isTap = false
        }
        if (event.changes.size > 1) {
            isTap = false
        }
        firstUp = event.changes[0]
    }

    if (firstUp.uptimeMillis - firstDown.uptimeMillis > viewConfiguration.longPressTimeoutMillis) {
        isTap = false
    }

    if (isTap) {
        val secondDown = awaitSecondDown(firstUp)
        if (secondDown == null) {
            onTap(firstUp.position)
        } else {
            var isDoubleTap = true
            var secondUp: PointerInputChange = secondDown
            val secondTouchSlop = TouchSlop(viewConfiguration.touchSlop)
            forEachPointerEventUntilReleased { event ->
                if (secondTouchSlop.isPast(event)) {
                    if (enableOneFingerZoom) {
                        val panChange = event.calculatePan()
                        val zoomChange = 1F + panChange.y * .004F
                        if (zoomChange != 1F) {
                            val centroid = event.calculateCentroid(useCurrent = false)
                            val timeMillis = event.changes[0].uptimeMillis
                            val canConsume = onGesture(centroid, Offset.Zero, zoomChange, timeMillis)
                            if (canConsume) {
                                event.consumePositionChanges()
                            }
                        }
                    }
                    isDoubleTap = false
                }
                if (event.changes.size > 1) {
                    isDoubleTap = false
                }
                secondUp = event.changes[0]
            }

            if (secondUp.uptimeMillis - secondDown.uptimeMillis > viewConfiguration.longPressTimeoutMillis) {
                isDoubleTap = false
            }

            if (isDoubleTap) {
                onDoubleTap(secondUp.position)
            }
        }
    }
    onGestureEnd()
}

private suspend fun AwaitPointerEventScope.forEachPointerEventUntilReleased(action: (PointerEvent) -> Unit) {
    do {
        val event = awaitPointerEvent()
        if (event.changes.fastAny { it.isConsumed }) {
            break
        }
        action(event)
    } while (event.changes.fastAny { it.pressed })
}

private suspend fun AwaitPointerEventScope.awaitSecondDown(firstUp: PointerInputChange): PointerInputChange? = withTimeoutOrNull(viewConfiguration.doubleTapTimeoutMillis) {
    val minUptime = firstUp.uptimeMillis + viewConfiguration.doubleTapMinTimeMillis
    var change: PointerInputChange
    do {
        change = awaitFirstDown()
    } while (change.uptimeMillis < minUptime)
    change
}

private fun PointerEvent.consumePositionChanges() {
    changes.fastForEach {
        if (it.positionChanged()) {
            it.consume()
        }
    }
}

private class TouchSlop(
    private val threshold: Float
) {
    private var zoom = 1F
    private var pan = Offset.Zero
    private var _isPast = false

    fun isPast(event: PointerEvent): Boolean {
        if (_isPast) {
            return true
        }
        zoom *= event.calculateZoom()
        pan += event.calculatePan()
        val zoomMotion = abs(1 - zoom) * event.calculateCentroidSize(useCurrent = false)
        val panMotion = pan.getDistance()
        _isPast = zoomMotion > threshold || panMotion > threshold
        return _isPast
    }
}

fun Modifier.zoomable(
    zoomState: ZoomState,
    enableOneFingerZoom: Boolean = true,
    onTap: (position: Offset) -> Unit = {},
    onDoubleTap: suspend (position: Offset) -> Unit = { position -> zoomState.toggleScale(position = position) }
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "zoomable"
        properties["zoomState"] = zoomState
    }
) {
    val scope = rememberCoroutineScope()
    Modifier
        .onSizeChanged { size ->
            zoomState.setLayoutSize(size.toSize())
        }
        .pointerInput(zoomState) {
            detectTransformGestures(
                onGestureStart = { zoomState.startGesture() },
                onGesture = { centroid, pan, zoom, timeMillis ->
                    val canConsume = zoomState.canConsumeGesture(pan = pan, zoom = zoom)
                    if (canConsume) {
                        scope.launch {
                            zoomState.applyGesture(
                                pan = pan,
                                zoom = zoom,
                                position = centroid,
                                timeMillis = timeMillis,
                            )
                        }
                    }
                    canConsume
                },
                onGestureEnd = {
                    scope.launch {
                        zoomState.endGesture()
                    }
                },
                onTap = onTap,
                onDoubleTap = { position ->
                    scope.launch {
                        onDoubleTap(position)
                    }
                },
                enableOneFingerZoom = enableOneFingerZoom,
            )
        }
        .graphicsLayer {
            scaleX = zoomState.scale
            scaleY = zoomState.scale
            translationX = zoomState.offsetX
            translationY = zoomState.offsetY
        }
}

suspend fun ZoomState.toggleScale(
    targetScale: Float = 2.5F,
    position: Offset,
    animationSpec: AnimationSpec<Float> = spring(),
) {
    val newScale = if (scale == 1F) targetScale else 1F
    changeScale(newScale, position, animationSpec)
}
