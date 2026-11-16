package .ui.platform.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.base.util.interactiveBorder
import .ui.platform.components.button.color.universalpasswordmanagerTonalIconButtonColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.ripple.NoFocusRippleConfig

/**
 * A tonal icon button that displays an icon.
 *
 * @param vectorIconRes Icon to display on the button.
 * @param contentDescription The content description for this icon button.
 * @param onClick Callback for when the icon button is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 * @param isEnabled Whether the button should be enabled.
 */
@Composable
fun Universal Password ManagerTonalIconButton(
    @DrawableRes vectorIconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isEnabled: Boolean = true,
) {
    val colors = universalpasswordmanagerTonalIconButtonColors()
    CompositionLocalProvider(LocalRippleConfiguration provides NoFocusRippleConfig) {
        FilledIconButton(
            onClick = onClick,
            colors = colors,
            enabled = isEnabled,
            shape = Universal Password ManagerTheme.shapes.button,
            interactionSource = interactionSource,
            modifier = modifier
                .interactiveBorder(
                    interactionSource = interactionSource,
                    shape = Universal Password ManagerTheme.shapes.button,
                    color = colors.containerColor,
                )
                .semantics(mergeDescendants = true) {
                    this.contentDescription = contentDescription
                },
        ) {
            Icon(
                painter = rememberVectorPainter(id = vectorIconRes),
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerTonalIconButton_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerTonalIconButton(
            vectorIconRes = Universal Password ManagerDrawable.ic_question_circle,
            contentDescription = "Sample Icon",
            onClick = {},
        )
    }
}
