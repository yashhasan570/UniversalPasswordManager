package com.universalpasswordmanager.ui.tools.feature.send.addedit

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
import com.universalpasswordmanager.network.model.SendAccessTypeJson
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.animation.AnimateNullableContentVisibility
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedErrorButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalInfoCalloutCard
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.header.UniversalExpandingHeader
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.stepper.UniversalStepper
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager
import com.universalpasswordmanager.ui.tools.feature.send.addedit.components.AddEditSendAuthTypeChooser
import com.universalpasswordmanager.ui.tools.feature.send.addedit.components.AddEditSendCustomDateChooser
import com.universalpasswordmanager.ui.tools.feature.send.addedit.components.AddEditSendDeletionDateChooser
import com.universalpasswordmanager.ui.tools.feature.send.addedit.handlers.AddEditSendHandlers

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
            UniversalInfoCalloutCard(
                text = stringResource(id = UniversalString.send_disabled_warning),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth()
                    .testTag("SendPolicyInEffectLabel"),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (policySendOptionsInEffect) {
            UniversalInfoCalloutCard(
                text = stringResource(id = UniversalString.send_options_policy_in_effect),
                modifier = Modifier
                    .testTag(tag = "SendPolicyInEffectLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        UniversalListHeaderText(
            label = stringResource(id = UniversalString.send_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        UniversalTextField(
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
            label = stringResource(id = UniversalString.send_name_required),
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
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.delete),
            message = stringResource(id = UniversalString.are_you_sure_delete_send),
            confirmButtonText = stringResource(id = UniversalString.yes),
            dismissButtonText = stringResource(id = UniversalString.cancel),
            onConfirmClick = {
                onDeleteClick()
                shouldShowDeleteConfirmationDialog = false
            },
            onDismissClick = { shouldShowDeleteConfirmationDialog = false },
            onDismissRequest = { shouldShowDeleteConfirmationDialog = false },
        )
    }
    UniversalOutlinedErrorButton(
        label = stringResource(id = UniversalString.delete_send),
        onClick = { shouldShowDeleteConfirmationDialog = true },
        icon = rememberVectorPainter(id = UniversalDrawable.ic_trash_small),
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
    UniversalTextField(
        label = stringResource(id = UniversalString.text_to_share),
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
    UniversalSwitch(
        label = stringResource(id = UniversalString.hide_text_by_default),
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
            color = UniversalTheme.colorScheme.text.primary,
            style = UniversalTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = stringResource(id = UniversalString.required_max_file_size),
            color = UniversalTheme.colorScheme.text.secondary,
            style = UniversalTheme.typography.bodySmall,
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
                    color = UniversalTheme.colorScheme.text.primary,
                    style = UniversalTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "SendCurrentFileNameLabel"),
                )
            }
            Spacer(modifier = Modifier.height(height = 8.dp))
        }
        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.choose_file),
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
            text = stringResource(id = UniversalString.required_max_file_size),
            color = UniversalTheme.colorScheme.text.secondary,
            style = UniversalTheme.typography.bodySmall,
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
                color = UniversalTheme.colorScheme.text.primary,
                style = UniversalTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(weight = 1f),
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = fileType.displaySize.orEmpty(),
                color = UniversalTheme.colorScheme.text.secondary,
                style = UniversalTheme.typography.bodyMedium,
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
    UniversalExpandingHeader(
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
            UniversalStepper(
                label = stringResource(id = UniversalString.maximum_access_count),
                supportingContent = {
                    Text(
                        text = stringResource(id = UniversalString.maximum_access_count_info),
                        style = UniversalTheme.typography.bodySmall,
                        color = UniversalTheme.colorScheme.text.secondary,
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
                                    id = UniversalString.current_access_count,
                                    formatArgs = arrayOf(it),
                                ),
                                style = UniversalTheme.typography.bodySmall,
                                color = UniversalTheme.colorScheme.text.secondary,
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
                UniversalTwoButtonDialog(
                    title = stringResource(id = UniversalString.password),
                    message = stringResource(id = UniversalString.password_override_alert),
                    confirmButtonText = stringResource(id = UniversalString.yes),
                    dismissButtonText = stringResource(id = UniversalString.no),
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
                UniversalSwitch(
                    modifier = Modifier
                        .testTag("SendHideEmailSwitch")
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                    label = stringResource(id = UniversalString.hide_email),
                    isChecked = state.common.isHideEmailChecked,
                    onCheckedChange = addSendHandlers.onHideEmailToggle,
                    readOnly = isSendsRestrictedByPolicy,
                    enabled = state.common.isHideEmailChecked ||
                        state.common.isHideEmailAddressEnabled,
                    cardStyle = CardStyle.Full,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            UniversalTextField(
                label = stringResource(id = UniversalString.private_notes),
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
