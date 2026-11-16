package .ui.platform.components.card

import android.content.res.Configuration
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.toDp
import .ui.platform.components.badge.NotificationBadge
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.color.universalpasswordmanagerCardColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText

/**
 * A design component action card, which contains a title, with an optional action button, dismiss
 * button, and leading icon content.
 *
 * @param cardTitle The title of the card.
 * @param modifier The [Modifier] to be applied to the card.
 * @param actionButton The data for the CTA button, or `null` to omit it, which suits a card that
 * only explains something.
 * @param onDismissClick Optional action to perform when the dismiss button is clicked.
 * @param cardSubtitle The subtitle of the card.
 * @param secondaryButton The optional data for a secondary button.
 * @param leadingContent Optional content to display on the leading side of the [cardTitle] [Text].
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerActionCard(
    cardTitle: String?,
    modifier: Modifier = Modifier,
    actionButton: Universal Password ManagerButtonData? = null,
    onDismissClick: (() -> Unit)? = null,
    cardSubtitle: String? = null,
    secondaryButton: Universal Password ManagerButtonData? = null,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    Card(
        modifier = modifier,
        shape = Universal Password ManagerTheme.shapes.actionCard,
        colors = universalpasswordmanagerCardColors(),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(width = 1.dp, color = Universal Password ManagerTheme.colorScheme.stroke.border),
    ) {
        var rowBottomPx by remember { mutableIntStateOf(0) }
        var titleBottomPx by remember { mutableIntStateOf(0) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    rowBottomPx = it.positionInParent().y.toInt() + it.size.height
                },
        ) {
            Spacer(modifier = Modifier.width(width = 16.dp))
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .weight(weight = 1f),
            ) {
                leadingContent?.let {
                    it()
                    Spacer(modifier = Modifier.width(width = 12.dp))
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    cardTitle?.let {
                        Text(
                            text = it,
                            style = Universal Password ManagerTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    titleBottomPx = coordinates.positionInParent().y.toInt() +
                                        coordinates.size.height
                                },
                        )
                    }
                    cardSubtitle?.let {
                        cardTitle?.let { _ ->
                            // We only need this to create a gap between the title and subtitle.
                            // So if the title is not present, we can skip the spacer.
                            Spacer(modifier = Modifier.height(height = 4.dp))
                        }
                        Text(
                            text = it,
                            style = Universal Password ManagerTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
            if (onDismissClick != null) {
                Universal Password ManagerStandardIconButton(
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                    contentDescription = stringResource(id = Universal Password ManagerString.close),
                    onClick = onDismissClick,
                )
            } else {
                // The dismiss button normally supplies the trailing inset, so without it the
                // content would otherwise run to the edge of the card.
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
        }
        if (actionButton != null || secondaryButton != null) {
            if (cardSubtitle == null && rowBottomPx > titleBottomPx) {
                // When the subtitle is missing, we want to ensure that the filled button is 16dp
                // below the title but the close button can be taller than the title which will push
                // the button further down. So we measure the difference and use that to offset the
                // spacer size.
                Spacer(
                    modifier = Modifier
                        .height(height = 16.dp - (rowBottomPx - titleBottomPx).toDp()),
                )
            } else {
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
            actionButton?.let {
                Universal Password ManagerFilledButton(
                    buttonData = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                )
            }
            secondaryButton?.let {
                Universal Password ManagerTextButton(
                    buttonData = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                )
            }
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
    }
}

/**
 * A default exit animation for [Universal Password ManagerActionCard] when using an animation wrapper like
 * [androidx.compose.animation.AnimatedVisibility].
 */
fun actionCardExitAnimation() = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Universal Password ManagerActionCardWithSubtitleNoDismiss_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerActionCard(
            cardTitle = "Title",
            actionButton = Universal Password ManagerButtonData(
                label = "Action".asText(),
                onClick = {},
            ),
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Universal Password ManagerActionCard_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerActionCard(
            cardTitle = "Title",
            actionButton = Universal Password ManagerButtonData(
                label = "Action".asText(),
                onClick = {},
            ),
            onDismissClick = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Universal Password ManagerActionCardWithLeadingContent_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerActionCard(
            cardTitle = "Title",
            actionButton = Universal Password ManagerButtonData(
                label = "Action".asText(),
                onClick = {},
            ),
            onDismissClick = {},
            leadingContent = {
                NotificationBadge(
                    notificationCount = 1,
                )
            },
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Universal Password ManagerActionCardWithSubtitle_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerActionCard(
            cardTitle = "Title",
            cardSubtitle = "Subtitle",
            actionButton = Universal Password ManagerButtonData(
                label = "Action".asText(),
                onClick = {},
            ),
            onDismissClick = {},
            secondaryButton = Universal Password ManagerButtonData(
                label = "Learn More".asText(),
                onClick = {},
            ),
            leadingContent = {
                NotificationBadge(
                    notificationCount = 1,
                )
            },
        )
    }
}
