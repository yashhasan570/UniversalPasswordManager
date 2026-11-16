package .ui.platform.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A filled icon button that displays an icon.
 *
 * @param helpData All the relevant data for displaying a help icon button.
 * @param modifier A [Modifier] for the composable.
 */
@Composable
fun Universal Password ManagerHelpIconButton(
    helpData: Universal Password ManagerHelpButtonData,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerStandardIconButton(
        vectorIconRes = Universal Password ManagerDrawable.ic_question_circle_small,
        contentDescription = if (helpData.isExternalLink) {
            stringResource(
                id = Universal Password ManagerString.external_link_format,
                formatArgs = arrayOf(helpData.contentDescription),
            )
        } else {
            helpData.contentDescription
        },
        onClick = helpData.onClick,
        contentColor = Universal Password ManagerTheme.colorScheme.icon.secondary,
        modifier = modifier,
    )
}
