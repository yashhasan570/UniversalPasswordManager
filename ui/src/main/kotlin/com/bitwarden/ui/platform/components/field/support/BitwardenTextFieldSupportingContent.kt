package .ui.platform.components.field.support

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import .ui.platform.components.divider.Universal Password ManagerHorizontalDivider
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.support.Universal Password ManagerSupportingContent

/**
 * Lays out the Universal Password Manager-styled supporting content for a text field.
 *
 * Intended to be reused for all implementations of a TextField, such as the [Universal Password ManagerTextField]
 * and [Universal Password ManagerPasswordField].
 */
@Composable
internal fun ColumnScope.Universal Password ManagerTextFieldSupportingContent(
    content: (@Composable ColumnScope.() -> Unit)?,
    supportingContentPadding: PaddingValues,
    noContentCardPadding: Dp,
) {
    if (content != null) {
        Spacer(modifier = Modifier.height(height = 6.dp))
        Universal Password ManagerHorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        )
        Universal Password ManagerSupportingContent(
            cardStyle = null,
            insets = supportingContentPadding,
            content = content,
        )
    } else {
        // Pad out the card when there is no content.
        Spacer(modifier = Modifier.height(height = noContentCardPadding))
    }
}
