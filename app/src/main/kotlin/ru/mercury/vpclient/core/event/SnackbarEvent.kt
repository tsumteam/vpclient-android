package ru.mercury.vpclient.core.event

interface BaseSnackbarEvent {
    val message: Any
}

/**
 * Обычный снакбар.
 */
data class SnackbarEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent

/**
 * Красный снакбар.
 */
data class SnackbarErrorEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent

/**
 * Cнакбар над кнопкой FAB.
 */
data class SnackbarFabEvent(
    override val message: Any
): BaseSnackbarEvent

/**
 * Красный снакбар над кнопкой FAB.
 */
data class SnackbarFabErrorEvent(
    override val message: Any
): BaseSnackbarEvent

/**
 * Обычный снакбар над BottomNavigationBar.
 */
data class SnackbarBottomBarEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent

/**
 * Красный снакбар над BottomNavigationBar.
 */
data class SnackbarBottomBarErrorEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent

/**
 * Обычный снакбар над BottomNavigationBar + Fab.
 */
data class SnackbarBottomBarFabEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent

/**
 * Красный снакбар над BottomNavigationBar + Fab.
 */
data class SnackbarBottomBarFabErrorEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent

/**
 * Красный снакбар под StatusBar.
 */
data class SnackbarTopErrorEvent(
    override val message: Any,
    val argument: String = ""
): BaseSnackbarEvent
