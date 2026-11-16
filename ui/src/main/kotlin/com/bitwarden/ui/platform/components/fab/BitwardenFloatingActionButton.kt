package .ui.platform.components.fab

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.interactiveBorder
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.ripple.NoFocusRippleConfig

/**
 * Represents a Universal Password Manager-styled [FloatingActionButton].
 *
 * @param onClick The callback when the button is clicked.
 * @param painter The icon for the button.
 * @param contentDescription The content description for the button.
 * @param modifier The [Modifier] to be applied to the button.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param windowInsets The insets to be applied to this composable. By default this will account for
 * the insets that are on the sides and bottom of the screen (Display Cutout and Navigation bars).
 */
@Composable
fun Universal Password ManagerFloatingActionButton(
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .only(sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        .union(insets = WindowInsets.navigationBars),
) {
    val contentColor = Universal Password ManagerTheme.colorScheme.filledButton.foreground
    CompositionLocalProvider(LocalRippleConfiguration provides NoFocusRippleConfig) {
        FloatingActionButton(
            containerColor = Universal Password ManagerTheme.colorScheme.filledButton.background,
            contentColor = contentColor,
            onClick = onClick,
            shape = Universal Password ManagerTheme.shapes.fab,
            interactionSource = interactionSource,
            modifier = modifier
                .windowInsetsPadding(insets = windowInsets)
                .interactiveBorder(
                    interactionSource = interactionSource,
                    shape = Universal Password ManagerTheme.shapes.fab,
                    color = contentColor,
                    inset = 2.dp,
                ),
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
            )
        }
    }
}
