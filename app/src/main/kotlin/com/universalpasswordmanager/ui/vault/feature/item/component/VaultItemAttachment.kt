package com.universalpasswordmanager.ui.vault.feature.item.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.spanStyleOf
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.dialog.PremiumRequiredDialog
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.vault.feature.item.VaultItemState

/**
 * Attachment UI common for all item types.
 */
@Suppress("LongMethod")
@Composable
fun VaultItemAttachment(
    isInAppUpgradeAvailable: Boolean,
    attachmentItem: VaultItemState.ViewState.Content.Common.AttachmentItem,
    onAttachmentDownloadClick: (VaultItemState.ViewState.Content.Common.AttachmentItem) -> Unit,
    onAttachmentPreviewClick: (VaultItemState.ViewState.Content.Common.AttachmentItem) -> Unit,
    onUpgradeToPremiumClick: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    var shouldShowPremiumWarningDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowSizeWarningDialog by rememberSaveable { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                padding = PaddingValues(start = 16.dp),
                onClick = {
                    if (!attachmentItem.isDownloadAllowed) {
                        shouldShowPremiumWarningDialog = true
                        return@cardStyle
                    }
                    onAttachmentPreviewClick(attachmentItem)
                },
            )
            .testTag("CipherAttachment"),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = attachmentItem.title,
                color = UniversalTheme.colorScheme.text.primary,
                style = UniversalTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .testTag("AttachmentNameLabel"),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = attachmentItem.displaySize,
                    color = UniversalTheme.colorScheme.text.secondary,
                    style = UniversalTheme.typography.bodyMedium,
                    modifier = Modifier
                        .testTag("AttachmentSizeLabel"),
                )

                Spacer(modifier = Modifier.width(8.dp))

                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_download,
                    contentDescription = stringResource(id = UniversalString.download),
                    isExternalLink = true,
                    onClick = {
                        if (!attachmentItem.isDownloadAllowed) {
                            shouldShowPremiumWarningDialog = true
                            return@UniversalStandardIconButton
                        }

                        if (attachmentItem.isLargeFile) {
                            shouldShowSizeWarningDialog = true
                            return@UniversalStandardIconButton
                        }

                        onAttachmentDownloadClick(attachmentItem)
                    },
                    modifier = Modifier
                        .testTag("AttachmentDownloadButton"),
                )
            }
        }
    }

    if (shouldShowPremiumWarningDialog) {
        PremiumRequiredDialog(
            message = stringResource(id = UniversalString.attachments_are_a_premium_feature),
            isInAppUpgradeAvailable = isInAppUpgradeAvailable,
            onUpgradeToPremiumClick = {
                shouldShowPremiumWarningDialog = false
                onUpgradeToPremiumClick()
            },
            onDismissRequest = { shouldShowPremiumWarningDialog = false },
        )
    }

    if (shouldShowSizeWarningDialog) {
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.download_attachment),
            message = annotatedStringResource(
                id = UniversalString.attachment_large_warning,
                args = arrayOf(attachmentItem.displaySize),
                style = spanStyleOf(
                    color = UniversalTheme.colorScheme.text.primary,
                    textStyle = UniversalTheme.typography.bodyMedium,
                ),
            ),
            confirmButtonText = stringResource(UniversalString.yes),
            dismissButtonText = stringResource(UniversalString.no),
            onConfirmClick = {
                shouldShowSizeWarningDialog = false
                onAttachmentDownloadClick(attachmentItem)
            },
            onDismissClick = { shouldShowSizeWarningDialog = false },
            onDismissRequest = { shouldShowSizeWarningDialog = false },
        )
    }
}
