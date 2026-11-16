package .ui.platform.components.text

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.spanStyleOf
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Uses an annotated string resource to create a string with clickable text.
 *
 * Note: This only supports one clickable text section.
 */
@Composable
fun Universal Password ManagerHyperTextLink(
    @StringRes annotatedResId: Int,
    vararg args: String,
    annotationKey: String,
    accessibilityString: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = Universal Password ManagerTheme.typography.labelMedium,
    color: Color = Universal Password ManagerTheme.colorScheme.text.secondary,
    isExternalLink: Boolean = false,
) {
    val formattedAccessibilityString = if (isExternalLink) {
        stringResource(
            id = Universal Password ManagerString.external_link_format,
            formatArgs = arrayOf(accessibilityString),
        )
    } else {
        accessibilityString
    }
    Text(
        text = annotatedStringResource(
            id = annotatedResId,
            args = args,
            style = spanStyleOf(color = color, textStyle = style),
            onAnnotationClick = { key ->
                when (key) {
                    annotationKey -> onClick()
                }
            },
        ),
        style = style,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = formattedAccessibilityString,
                        action = {
                            onClick()
                            true
                        },
                    ),
                )
            },
    )
}
