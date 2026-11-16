package com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.work.model.SendAccessTypeJson
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.animation.AnimateNullableContentVisibility
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.button.Universal Password ManagerOutlinedErrorButton
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerExpandingHeader
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.stepper.Universal Password ManagerStepper
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.components.AddEditSendAuthTypeChooser
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.components.AddEditSendCustomDateChooser
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.components.AddEditSendDeletionDateChooser
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.handlers.AddEditSendHandlers

/**
 * Content view for the [AddEditSendScreen].
 */
@Suppress("LongMethod")
@Composable
fun AddEditSendContent(
    state: AddEditSendState.ViewState.Content,
    enforcedDeletionHours: Int?,
    enforcedWhoCanAccess: SendAccessTypeJson?,
    policyDisablesSend: Boolean,
    policySendOptionsInEffect: Boolean,
    shouldHideEmailAddressToggle: Boolean,
    isNewSend: Boolean,
    isShared: Boolean,
    addSendHandlers: AddEditSendHandlers,
    permissionsManager: PermissionsManager,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        if (policyDisablesSend) {
            Universal Password ManagerInfoCalloutCard(
                text = stringResource(id = Universal Password ManagerString.send_disabled_warning),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth()
                    .testTag("SendPolicyInEffectLabel"),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (policySendOptionsInEffect) {
            Universal Password ManagerInfoCalloutCard(
                text = stringResource(id = Universal Password ManagerString.send_options_policy_in_effect),
                modifier = Modifier
                    .testTag(tag = "SendPolicyInEffectLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.send_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Universal Password ManagerTextField(
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
            label = stringResource(id = Universal Password ManagerString.send_name_required),
            readOnly = policyDisablesSend,
            value = state.common.name,
            onValueChange = addSendHandlers.onNameChange,
            textFieldTestTag = "SendNameEntry",
            cardStyle = CardStyle.Full,
        )

        when (val type = state.selectedType) {
            is AddEditSendState.ViewState.Content.SendType.File -> {
                FileTypeContent(
                    fileType = type,
                    addSendHandlers = addSendHandlers,
                    permissionsManager = permissionsManager,
                    isNewSend = isNewSend,
                    isShared = isShared,
                )
            }

            is AddEditSendState.ViewState.Content.SendType.Text -> {
                TextTypeContent(
                    textType = type,
                    addSendHandlers = addSendHandlers,
                    policyDisablesSend = policyDisablesSend,
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        if (isNewSend) {
            AddEditSendDeletionDateChooser(
                enforcedDeletionHours = enforcedDeletionHours,
                onDateSelect = addSendHandlers.onDeletionDateChange,
                isEnabled = !policyDisablesSend,
                modifier = Modifier
                    .testTag("SendDeletionOptionsPicker")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        } else {
            AddEditSendCustomDateChooser(
                originalSelection = state.common.deletionDate,
                onDateSelect = addSendHandlers.onDeletionDateChange,
                isDeletionDateEnforced = enforcedDeletionHours != null,
                isEnabled = !policyDisablesSend,
                modifier = Modifier
                    .testTag("SendCustomDeletionDatePicker")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(modifier = Modifier.height(height = 8.dp))
        AddEditSendAuthTypeChooser(
            sendAuth = state.common.sendAuth,
            onAuthTypeSelect = addSendHandlers.onAuthTypeSelect,
            onPasswordChange = addSendHandlers.onAuthPasswordChange,
            onEmailValueChange = addSendHandlers.onEmailValueChange,
            onRemoveEmailClick = addSendHandlers.onEmailsRemoveClick,
            onAddNewEmailClick = addSendHandlers.onAddNewEmailClick,
            onOpenPasswordGeneratorClick = addSendHandlers.onOpenPasswordGeneratorClick,
            onPasswordCopyClick = addSendHandlers.onPasswordCopyClick,
            password = state.common.passwordInput,
            isEnabled = !policyDisablesSend && enforcedWhoCanAccess == null,
            isExistingPasswordLocked = state.common.hasPassword,
            isSendsRestrictedByPolicy = policyDisablesSend,
            modifier = Modifier
                .testTag("SendAuthTypeChooser")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        AddEditSendOptions(
            state = state,
            isSendsRestrictedByPolicy = policyDisablesSend,
            shouldHideEmailAddressToggle = shouldHideEmailAddressToggle,
            isNewSend = isNewSend,
            addSendHandlers = addSendHandlers,
        )

        if (!isNewSend) {
            DeleteButton(
                onDeleteClick = addSendHandlers.onDeleteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun DeleteButton(
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowDeleteConfirmationDialog by rememberSaveable { mutableStateOf(value = false) }
    if (shouldShowDeleteConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.delete),
            message = stringResource(id = Universal Password ManagerString.are_you_sure_delete_send),
            confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                onDeleteClick()
                shouldShowDeleteConfirmationDialog = false
            },
            onDismissClick = { shouldShowDeleteConfirmationDialog = false },
            onDismissRequest = { shouldShowDeleteConfirmationDialog = false },
        )
    }
    Universal Password ManagerOutlinedErrorButton(
        label = stringResource(id = Universal Password ManagerString.delete_send),
        onClick = { shouldShowDeleteConfirmationDialog = true },
        icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_trash_small),
        modifier = modifier,
    )
}

@Composable
private fun ColumnScope.TextTypeContent(
    textType: AddEditSendState.ViewState.Content.SendType.Text,
    addSendHandlers: AddEditSendHandlers,
    policyDisablesSend: Boolean,
) {
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.text_to_share),
        readOnly = policyDisablesSend,
        value = textType.input,
        singleLine = false,
        onValueChange = addSendHandlers.onTextChange,
        textFieldTestTag = "SendTextContentEntry",
        cardStyle = CardStyle.Full,
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerSwitch(
        label = stringResource(id = Universal Password ManagerString.hide_text_by_default),
        isChecked = textType.isHideByDefaultChecked,
        onCheckedChange = addSendHandlers.onIsHideByDefaultToggle,
        readOnly = policyDisablesSend,
        cardStyle = CardStyle.Full,
        modifier = Modifier
            .testTag(tag = "SendHideTextByDefaultToggle")
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
}

@Suppress("LongMethod")
@Composable
private fun ColumnScope.FileTypeContent(
    fileType: AddEditSendState.ViewState.Content.SendType.File,
    addSendHandlers: AddEditSendHandlers,
    permissionsManager: PermissionsManager,
    isNewSend: Boolean,
    isShared: Boolean,
) {
    val chooseFileCameraPermissionLauncher = permissionsManager.getLauncher { isGranted ->
        addSendHandlers.onChooseFileClick(isGranted)
    }
    Spacer(modifier = Modifier.height(height = 8.dp))
    if (isShared) {
        Text(
            text = fileType.name.orEmpty(),
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.required_max_file_size),
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            style = Universal Password ManagerTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    } else if (isNewSend) {
        fileType.name?.let {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .defaultMinSize(minHeight = 60.dp)
                    .cardStyle(cardStyle = CardStyle.Full, paddingHorizontal = 16.dp),
            ) {
                Text(
                    text = it,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    style = Universal Password ManagerTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "SendCurrentFileNameLabel"),
                )
            }
            Spacer(modifier = Modifier.height(height = 8.dp))
        }
        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.choose_file),
            onClick = {
                if (permissionsManager.checkPermission(permission = Manifest.permission.CAMERA)) {
                    addSendHandlers.onChooseFileClick(true)
                } else {
                    chooseFileCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            isExternalLink = true,
            modifier = Modifier
                .testTag(tag = "SendChooseFileButton")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.required_max_file_size),
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            style = Universal Password ManagerTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .defaultMinSize(minHeight = 60.dp)
                .cardStyle(cardStyle = CardStyle.Full, paddingHorizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = fileType.name.orEmpty(),
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(weight = 1f),
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = fileType.displaySize.orEmpty(),
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                style = Universal Password ManagerTheme.typography.bodyMedium,
            )
        }
    }
}

/**
 * Displays a collapsable set of new send options.
 *
 * @param state The content state.
 * @param isSendsRestrictedByPolicy When `true`, indicates that there's a policy preventing the user
 * from editing or creating sends.
 * @param isNewSend When `true`, indicates that we are creating a new send and `false` when editing
 * an existing send. Copying a send creates a new one, so it is also `true` in that case.
 * @param addSendHandlers THe handlers various events.
 */
@Suppress("LongMethod")
@Composable
private fun AddEditSendOptions(
    state: AddEditSendState.ViewState.Content,
    isSendsRestrictedByPolicy: Boolean,
    shouldHideEmailAddressToggle: Boolean,
    isNewSend: Boolean,
    addSendHandlers: AddEditSendHandlers,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var shouldShowDialog by rememberSaveable { mutableStateOf(false) }
    Universal Password ManagerExpandingHeader(
        isExpanded = isExpanded,
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier
            .testTag(tag = "SendShowHideOptionsButton")
            .standardHorizontalMargin()
            .fillMaxWidth(),
    )
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
        modifier = Modifier.clipToBounds(),
    ) {
        Column {
            Universal Password ManagerStepper(
                label = stringResource(id = Universal Password ManagerString.maximum_access_count),
                supportingContent = {
                    Text(
                        text = stringResource(id = Universal Password ManagerString.maximum_access_count_info),
                        style = Universal Password ManagerTheme.typography.bodySmall,
                        color = Universal Password ManagerTheme.colorScheme.text.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AnimateNullableContentVisibility(
                        targetState = state
                            .common
                            .currentAccessCount
                            ?.takeUnless { isNewSend || state.common.maxAccessCount == null },
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically(),
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(
                                    id = Universal Password ManagerString.current_access_count,
                                    formatArgs = arrayOf(it),
                                ),
                                style = Universal Password ManagerTheme.typography.bodySmall,
                                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                },
                value = state.common.maxAccessCount,
                onValueChange = addSendHandlers.onMaxAccessCountChange,
                isDecrementEnabled = state.common.maxAccessCount != null &&
                    !isSendsRestrictedByPolicy,
                isIncrementEnabled = !isSendsRestrictedByPolicy,
                range = 0..Int.MAX_VALUE,
                textFieldReadOnly = isSendsRestrictedByPolicy,
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .testTag("SendMaxAccessCountEntry")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            if (shouldShowDialog) {
                Universal Password ManagerTwoButtonDialog(
                    title = stringResource(id = Universal Password ManagerString.password),
                    message = stringResource(id = Universal Password ManagerString.password_override_alert),
                    confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
                    dismissButtonText = stringResource(id = Universal Password ManagerString.no),
                    onConfirmClick = {
                        shouldShowDialog = false
                        addSendHandlers.onOpenPasswordGeneratorClick()
                    },
                    onDismissClick = {
                        shouldShowDialog = false
                    },
                    onDismissRequest = {
                        shouldShowDialog = false
                    },
                )
            }
            if (!shouldHideEmailAddressToggle) {
                Spacer(modifier = Modifier.height(height = 8.dp))
                Universal Password ManagerSwitch(
                    modifier = Modifier
                        .testTag("SendHideEmailSwitch")
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                    label = stringResource(id = Universal Password ManagerString.hide_email),
                    isChecked = state.common.isHideEmailChecked,
                    onCheckedChange = addSendHandlers.onHideEmailToggle,
                    readOnly = isSendsRestrictedByPolicy,
                    enabled = state.common.isHideEmailChecked ||
                        state.common.isHideEmailAddressEnabled,
                    cardStyle = CardStyle.Full,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.private_notes),
                readOnly = isSendsRestrictedByPolicy,
                value = state.common.noteInput,
                singleLine = false,
                onValueChange = addSendHandlers.onNoteChange,
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}
