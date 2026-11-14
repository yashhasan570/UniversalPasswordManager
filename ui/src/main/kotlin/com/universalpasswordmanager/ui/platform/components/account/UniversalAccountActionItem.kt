package com.universalpasswordmanager.ui.platform.components.account

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
import com.universalpasswordmanager.ui.platform.base.util.interactiveBorder
import com.universalpasswordmanager.ui.platform.base.util.toSafeOverlayColor
import com.universalpasswordmanager.ui.platform.base.util.toUnscaledTextUnit
import com.universalpasswordmanager.ui.platform.components.button.color.bitwardenStandardIconButtonColors
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.theme.type.bitwardenFontFamily

/**
 * Displays an icon representing a Universal account with the user's initials superimposed.
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
fun UniversalAccountActionItem(
    initials: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val iconPainter = rememberVectorPainter(id = UniversalDrawable.ic_account_initials_container)
    val contentDescription = stringResource(id = UniversalString.account)
    IconButton(
        onClick = onClick,
        colors = bitwardenStandardIconButtonColors(),
        interactionSource = interactionSource,
        modifier = modifier
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = UniversalTheme.shapes.button,
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
                fontFamily = bitwardenFontFamily,
                fontWeight = FontWeight.SemiBold,
            ),
            color = color.toSafeOverlayColor(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UniversalAccountActionItem_preview() {
    UniversalTheme {
        UniversalAccountActionItem(
            initials = "BW",
            color = UniversalTheme.colorScheme.icon.primary,
            onClick = {},
        )
    }
}
