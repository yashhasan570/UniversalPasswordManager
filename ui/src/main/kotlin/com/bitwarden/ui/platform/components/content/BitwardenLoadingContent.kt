package .ui.platform.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.indicator.Universal Password ManagerCircularProgressIndicator
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A Universal Password Manager-themed, re-usable loading state.
 */
@Composable
fun Universal Password ManagerLoadingContent(
    modifier: Modifier = Modifier,
    text: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        text?.let {
            Text(
                text = it,
                style = Universal Password ManagerTheme.typography.titleMedium,
                // setting color explicitly here as we can't assume what the surface will be.
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                modifier = Modifier.testTag(tag = "AlertTitleText"),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Universal Password ManagerCircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .testTag(tag = "AlertProgressIndicator"),
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true, name = "Universal Password Manager loading content")
@Composable
private fun Universal Password ManagerLoadingContent_preview() {
    Universal Password ManagerScaffold {
        Universal Password ManagerLoadingContent(
            text = "Loading...",
            modifier = Modifier
                .fillMaxSize()
                .standardHorizontalMargin(),
        )
    }
}
