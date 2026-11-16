package .ui.platform.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A Universal Password Manager-themed, re-usable error state.
 *
 * @param message The text content to display.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param illustrationData Optional illustration to display above the text.
 * @param buttonData Optional button to display below the text.
 */
@Composable
fun Universal Password ManagerErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    illustrationData: IconData? = null,
    buttonData: Universal Password ManagerButtonData? = null,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        illustrationData?.let {
            Universal Password ManagerIcon(
                iconData = it,
                modifier = Modifier.size(size = 124.dp),
            )
            Spacer(modifier = Modifier.height(height = 24.dp))
        }
        Text(
            text = message,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        buttonData?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Universal Password ManagerFilledButton(
                buttonData = it,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
