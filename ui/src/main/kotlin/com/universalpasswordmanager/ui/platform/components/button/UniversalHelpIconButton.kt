package com.universalpasswordmanager.ui.platform.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalHelpButtonData
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * A filled icon button that displays an icon.
 *
 * @param helpData All the relevant data for displaying a help icon button.
 * @param modifier A [Modifier] for the composable.
 */
@Composable
fun UniversalHelpIconButton(
    helpData: UniversalHelpButtonData,
    modifier: Modifier = Modifier,
) {
    UniversalStandardIconButton(
        vectorIconRes = UniversalDrawable.ic_question_circle_small,
        contentDescription = if (helpData.isExternalLink) {
            stringResource(
                id = UniversalString.external_link_format,
                formatArgs = arrayOf(helpData.contentDescription),
            )
        } else {
            helpData.contentDescription
        },
        onClick = helpData.onClick,
        contentColor = UniversalTheme.colorScheme.icon.secondary,
        modifier = modifier,
    )
}
