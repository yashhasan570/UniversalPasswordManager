package com.universalpasswordmanager.ui.platform.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.universalpasswordmanager.ui.platform.base.util.cardBackground
import com.universalpasswordmanager.ui.platform.components.content.UniversalContentBlock
import com.universalpasswordmanager.ui.platform.components.content.model.ContentBlockData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import kotlinx.collections.immutable.ImmutableList

/**
 * Reusable card for displaying content block components in a vertical column with the card
 * shape. Content is drawn with a [UniversalContentBlock].
 *
 * @param contentItems list of [ContentBlockData] items to display.
 * @param contentHeaderTextStyle the text style to use for the header text of the content.
 * @param contentSubtitleTextStyle the text style to use for the subtitle text of the content.
 * @param contentSubtitleColor the color that should be applied to subtitle text of the content.
 * @param contentBackgroundColor the background color to use for the content.
 */
@Composable
fun UniversalContentCard(
    contentItems: ImmutableList<ContentBlockData>,
    modifier: Modifier = Modifier,
    contentHeaderTextStyle: TextStyle = UniversalTheme.typography.titleSmall,
    contentSubtitleTextStyle: TextStyle = UniversalTheme.typography.bodyMedium,
    contentSubtitleColor: Color = UniversalTheme.colorScheme.text.secondary,
    contentBackgroundColor: Color = UniversalTheme.colorScheme.background.secondary,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .cardBackground(cardStyle = CardStyle.Full, color = contentBackgroundColor),
    ) {
        contentItems.forEachIndexed { index, item ->
            UniversalContentBlock(
                data = item,
                showDivider = index != contentItems.lastIndex,
                headerTextStyle = contentHeaderTextStyle,
                subtitleTextStyle = contentSubtitleTextStyle,
                subtitleColor = contentSubtitleColor,
                backgroundColor = contentBackgroundColor,
            )
        }
    }
}
