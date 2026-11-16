package .ui.platform.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import .ui.platform.base.util.cardBackground
import .ui.platform.components.content.Universal Password ManagerContentBlock
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.model.CardStyle
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.ImmutableList

/**
 * Reusable card for displaying content block components in a vertical column with the card
 * shape. Content is drawn with a [Universal Password ManagerContentBlock].
 *
 * @param contentItems list of [ContentBlockData] items to display.
 * @param contentHeaderTextStyle the text style to use for the header text of the content.
 * @param contentSubtitleTextStyle the text style to use for the subtitle text of the content.
 * @param contentSubtitleColor the color that should be applied to subtitle text of the content.
 * @param contentBackgroundColor the background color to use for the content.
 */
@Composable
fun Universal Password ManagerContentCard(
    contentItems: ImmutableList<ContentBlockData>,
    modifier: Modifier = Modifier,
    contentHeaderTextStyle: TextStyle = Universal Password ManagerTheme.typography.titleSmall,
    contentSubtitleTextStyle: TextStyle = Universal Password ManagerTheme.typography.bodyMedium,
    contentSubtitleColor: Color = Universal Password ManagerTheme.colorScheme.text.secondary,
    contentBackgroundColor: Color = Universal Password ManagerTheme.colorScheme.background.secondary,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .cardBackground(cardStyle = CardStyle.Full, color = contentBackgroundColor),
    ) {
        contentItems.forEachIndexed { index, item ->
            Universal Password ManagerContentBlock(
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
