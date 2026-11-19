package .ui.platform.theme.ripple

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.RippleDefaults
import .ui.platform.theme.color.Universal Password ManagerColorScheme

/**
 * A default [RippleConfiguration] with the [RippleAlpha.focusedAlpha] cleared to disable the focus
 * state.
 */
internal val NoFocusRippleConfig: RippleConfiguration = RippleConfiguration(
    rippleAlpha = RippleAlpha(
        draggedAlpha = RippleDefaults.RippleAlpha.draggedAlpha,
        focusedAlpha = 0f,
        hoveredAlpha = 0f,
        pressedAlpha = RippleDefaults.RippleAlpha.pressedAlpha,
    ),
)

/**
 * Derives a standard [RippleConfiguration] with the color overridden using the
 * [Universal Password ManagerColorScheme].
 */
internal fun Universal Password ManagerColorScheme.toRippleConfig(): RippleConfiguration = RippleConfiguration(
    color = this.background.pressed,
)
