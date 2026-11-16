@file:OmitFromCoverage

package .ui.platform.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .annotation.OmitFromCoverage
import .ui.platform.base.util.nullableTestTag
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText

/**
 * A Universal Password Manager-themed, re-usable empty state.
 *
 * @param text The primary text to display.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param illustrationData Optional illustration to display above the text.
 * @param labelTestTag A test tag for the primary text.
 * @param title Optional title to display above the primary text.
 * @param titleTestTag A test tag for the title.
 * @param primaryButton Optional primary button to display.
 * @param secondaryButton Optional secondary button to display.
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerEmptyContent(
    text: String,
    modifier: Modifier = Modifier,
    illustrationData: IconData? = null,
    labelTestTag: String? = null,
    title: String? = null,
    titleTestTag: String? = null,
    primaryButton: Universal Password ManagerButtonData? = null,
    secondaryButton: Universal Password ManagerButtonData? = null,
) {
    Column(
        modifier = modifier,
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
        title?.let {
            Text(
                text = title,
                style = Universal Password ManagerTheme.typography.titleMedium,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .nullableTestTag(tag = titleTestTag),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
        }
        Text(
            text = text,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .nullableTestTag(tag = labelTestTag),
        )

        // If either of the optional buttons are present add a spacer between the text and the
        // buttons.
        if (primaryButton != null || secondaryButton != null) {
            Spacer(Modifier.height(12.dp))
        }

        primaryButton?.let {
            Universal Password ManagerFilledButton(
                buttonData = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        // If both buttons are visible add the standard button spacing.
        if (primaryButton != null && secondaryButton != null) {
            Spacer(Modifier.height(8.dp))
        }

        secondaryButton?.let {
            Universal Password ManagerOutlinedButton(
                buttonData = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true, name = "Universal Password Manager empty content")
@Composable
private fun Universal Password ManagerEmptyContent_preview() {
    Universal Password ManagerScaffold {
        Universal Password ManagerEmptyContent(
            title = "Empty content",
            titleTestTag = "TitleTestTag",
            text = "There is no content to display",
            labelTestTag = "EmptyContentLabel",
            illustrationData = IconData.Local(Universal Password ManagerDrawable.ill_pin),
            primaryButton = Universal Password ManagerButtonData(
                label = "Primary button".asText(),
                testTag = "EmptyContentPositiveButton",
                onClick = { },
            ),
            secondaryButton = Universal Password ManagerButtonData(
                label = "Secondary button".asText(),
                testTag = "EmptyContentNegativeButton",
                onClick = { },
            ),
            modifier = Modifier
                .fillMaxSize()
                .standardHorizontalMargin(),
        )
    }
}
