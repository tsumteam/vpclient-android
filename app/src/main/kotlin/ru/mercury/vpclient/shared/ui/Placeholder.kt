package ru.mercury.vpclient.shared.ui

import androidx.annotation.FloatRange
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.LayoutDirection

object PlaceholderDefaults {
    val fadeAnimationSpec: InfiniteRepeatableSpec<Float> by lazy {
        infiniteRepeatable(
            animation = tween(delayMillis = 200, durationMillis = 600),
            repeatMode = RepeatMode.Reverse
        )
    }

    val shimmerAnimationSpec: InfiniteRepeatableSpec<Float> by lazy {
        infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    }
}

@Composable
fun PlaceholderDefaults.color(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    contentAlpha: Float = .1F
): Color = contentColor.copy(contentAlpha).compositeOver(backgroundColor)

@Composable
fun PlaceholderDefaults.shimmerHighlightColor(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    alpha: Float = .6F
): Color = backgroundColor.copy(alpha = alpha)

@Stable
interface PlaceholderHighlight {
    val animationSpec: InfiniteRepeatableSpec<Float>?

    fun brush(
        @FloatRange(from = .0, to = 1.0) progress: Float,
        size: Size
    ): Brush

    @FloatRange(from = .0, to = 1.0)
    fun alpha(progress: Float): Float

    companion object
}

fun PlaceholderHighlight.Companion.shimmer(
    highlightColor: Color,
    animationSpec: InfiniteRepeatableSpec<Float> = PlaceholderDefaults.shimmerAnimationSpec
): PlaceholderHighlight = Shimmer(
    highlightColor = highlightColor,
    animationSpec = animationSpec
)

@Composable
fun PlaceholderHighlight.Companion.shimmer(
    animationSpec: InfiniteRepeatableSpec<Float> = PlaceholderDefaults.shimmerAnimationSpec
) = PlaceholderHighlight.shimmer(
    highlightColor = PlaceholderDefaults.shimmerHighlightColor(),
    animationSpec = animationSpec
)

fun Modifier.placeholder(
    visible: Boolean,
    highlight: PlaceholderHighlight,
    color: Color = Color.Unspecified,
    shape: Shape? = null,
    placeholderFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() }
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "placeholder"
        value = visible
        properties["visible"] = visible
        properties["color"] = color
        properties["highlight"] = highlight
        properties["shape"] = shape
    }
) {
    val resolvedColor = if (color.isSpecified) color else PlaceholderDefaults.color()
    val resolvedShape = shape ?: RectangleShape
    val lastSize = remember { Ref<Size>() }
    val lastLayoutDirection = remember { Ref<LayoutDirection>() }
    val lastOutline = remember { Ref<Outline>() }
    var highlightProgress: Float by remember { mutableFloatStateOf(0F) }
    val transitionState = remember { MutableTransitionState(visible) }.apply {
        targetState = visible
    }
    val transition = rememberTransition(transitionState, "placeholder_crossfade")
    val placeholderAlpha by transition.animateFloat(
        transitionSpec = placeholderFadeTransitionSpec,
        label = "placeholder_fade",
        targetValueByState = { placeholderVisible -> if (placeholderVisible) 1F else 0F }
    )
    val contentAlpha by transition.animateFloat(
        transitionSpec = contentFadeTransitionSpec,
        label = "content_fade",
        targetValueByState = { placeholderVisible -> if (placeholderVisible) 0F else 1F }
    )
    val animationSpec = highlight.animationSpec
    if (animationSpec != null && (visible || placeholderAlpha >= .01F)) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        highlightProgress = infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 1F,
            animationSpec = animationSpec,
            label = ""
        ).value
    }

    val paint = remember { Paint() }
    remember(resolvedColor, resolvedShape, highlight) {
        drawWithContent {
            if (contentAlpha in .01F..0.99F) {
                paint.alpha = contentAlpha
                withLayer(paint) {
                    with(this@drawWithContent) {
                        drawContent()
                    }
                }
            } else if (contentAlpha >= .99F) {
                drawContent()
            }

            if (placeholderAlpha in .01F..0.99F) {
                paint.alpha = placeholderAlpha
                withLayer(paint) {
                    lastOutline.value = drawPlaceholder(
                        shape = resolvedShape,
                        color = resolvedColor,
                        highlight = highlight,
                        progress = highlightProgress,
                        lastOutline = lastOutline.value,
                        lastLayoutDirection = lastLayoutDirection.value,
                        lastSize = lastSize.value
                    )
                }
            } else if (placeholderAlpha >= .99F) {
                lastOutline.value = drawPlaceholder(
                    shape = resolvedShape,
                    color = resolvedColor,
                    highlight = highlight,
                    progress = highlightProgress,
                    lastOutline = lastOutline.value,
                    lastLayoutDirection = lastLayoutDirection.value,
                    lastSize = lastSize.value
                )
            }

            lastSize.value = size
            lastLayoutDirection.value = layoutDirection
        }
    }
}

private fun DrawScope.drawPlaceholder(
    shape: Shape,
    color: Color,
    highlight: PlaceholderHighlight?,
    progress: Float,
    lastOutline: Outline?,
    lastLayoutDirection: LayoutDirection?,
    lastSize: Size?
): Outline? {
    if (shape === RectangleShape) {
        drawRect(color = color)

        if (highlight != null) {
            drawRect(
                brush = highlight.brush(progress, size),
                alpha = highlight.alpha(progress)
            )
        }
        return null
    }

    val outline = lastOutline.takeIf {
        size == lastSize && layoutDirection == lastLayoutDirection
    } ?: shape.createOutline(size, layoutDirection, this)

    drawOutline(outline = outline, color = color)

    if (highlight != null) {
        drawOutline(
            outline = outline,
            brush = highlight.brush(progress, size),
            alpha = highlight.alpha(progress)
        )
    }

    return outline
}

private data class Shimmer(
    private val highlightColor: Color,
    override val animationSpec: InfiniteRepeatableSpec<Float>
): PlaceholderHighlight {
    override fun brush(progress: Float, size: Size): Brush {
        val shimmerWidth = size.width * .4F
        val x = progress * (size.width + shimmerWidth) - shimmerWidth
        return Brush.linearGradient(
            colors = listOf(Color.Transparent, highlightColor, Color.Transparent),
            start = Offset(x = x, y = 0F),
            end = Offset(x = x + shimmerWidth, y = 0F)
        )
    }

    override fun alpha(progress: Float): Float = 1F
}

private inline fun DrawScope.withLayer(
    paint: Paint,
    drawBlock: DrawScope.() -> Unit,
) = drawIntoCanvas { canvas ->
    canvas.saveLayer(size.toRect(), paint)
    drawBlock()
    canvas.restore()
}
