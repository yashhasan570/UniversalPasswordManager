package com.x8bit.universalpasswordmanager.ui.vault.feature.item.component

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
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.spanStyleOf
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dialog.PremiumRequiredDialog
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.VaultItemState

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
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.bodyLarge,
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
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    modifier = Modifier
                        .testTag("AttachmentSizeLabel"),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_download,
                    contentDescription = stringResource(id = Universal Password ManagerString.download),
                    isExternalLink = true,
                    onClick = {
                        if (!attachmentItem.isDownloadAllowed) {
                            shouldShowPremiumWarningDialog = true
                            return@Universal Password ManagerStandardIconButton
                        }

                        if (attachmentItem.isLargeFile) {
                            shouldShowSizeWarningDialog = true
                            return@Universal Password ManagerStandardIconButton
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
            message = stringResource(id = Universal Password ManagerString.attachments_are_a_premium_feature),
            isInAppUpgradeAvailable = isInAppUpgradeAvailable,
            onUpgradeToPremiumClick = {
                shouldShowPremiumWarningDialog = false
                onUpgradeToPremiumClick()
            },
            onDismissRequest = { shouldShowPremiumWarningDialog = false },
        )
    }

    if (shouldShowSizeWarningDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.download_attachment),
            message = annotatedStringResource(
                id = Universal Password ManagerString.attachment_large_warning,
                args = arrayOf(attachmentItem.displaySize),
                style = spanStyleOf(
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    textStyle = Universal Password ManagerTheme.typography.bodyMedium,
                ),
            ),
            confirmButtonText = stringResource(Universal Password ManagerString.yes),
            dismissButtonText = stringResource(Universal Password ManagerString.no),
            onConfirmClick = {
                shouldShowSizeWarningDialog = false
                onAttachmentDownloadClick(attachmentItem)
            },
            onDismissClick = { shouldShowSizeWarningDialog = false },
            onDismissRequest = { shouldShowSizeWarningDialog = false },
        )
    }
}
