package .ui.platform.components.account

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.interactiveBorder
import .ui.platform.base.util.toSafeOverlayColor
import .ui.platform.base.util.toUnscaledTextUnit
import .ui.platform.components.button.color.universalpasswordmanagerStandardIconButtonColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.type.universalpasswordmanagerFontFamily

/**
 * Displays an icon representing a Universal Password Manager account with the user's initials superimposed.
 * The icon is typically a colored circle with the initials centered on it.
 *
 * @param initials The initials of the user to be displayed on top of the icon.
 * @param color The color to be applied as the tint for the icon.
 * @param onClick An action to be invoked when the icon is clicked.
 * @param modifier A [Modifier] for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 */
@Composable
fun Universal Password ManagerAccountActionItem(
    initials: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val iconPainter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_account_initials_container)
    val contentDescription = stringResource(id = Universal Password ManagerString.account)
    IconButton(
        onClick = onClick,
        colors = universalpasswordmanagerStandardIconButtonColors(),
        interactionSource = interactionSource,
        modifier = modifier
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = Universal Password ManagerTheme.shapes.button,
            )
            .testTag(tag = "CurrentActiveAccount"),
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription,
            tint = color,
        )
        Text(
            text = initials,
            style = TextStyle(
                fontSize = 11.dp.toUnscaledTextUnit(),
                lineHeight = 13.dp.toUnscaledTextUnit(),
                fontFamily = universalpasswordmanagerFontFamily,
                fontWeight = FontWeight.SemiBold,
            ),
            color = color.toSafeOverlayColor(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerAccountActionItem_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerAccountActionItem(
            initials = "BW",
            color = Universal Password ManagerTheme.colorScheme.icon.primary,
            onClick = {},
        )
    }
}
