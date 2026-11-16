package com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.field.Universal Password ManagerHiddenPasswordField
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.model.AuthEmail
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.model.SendAuth
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * Stand-in value for a locked password field. The existing password is never readable, and
 * [Universal Password ManagerHiddenPasswordField] masks any non-empty value with a fixed number of dots, so this
 * only signals that a password is set.
 */
private const val LOCKED_PASSWORD_PLACEHOLDER: String = "locked"

/**
 * Displays UX for choosing authentication type for a send.
 *
 * @param sendAuth The current authentication configuration.
 * @param onAuthTypeSelect Callback invoked when the authentication type is selected.
 * @param onPasswordChange Callback invoked when the password value changes
 * (only relevant for [SendAuth.Password]).
 * @param onEmailValueChange Callback invoked when an email value changes
 * (only relevant for [SendAuth.Email]).
 * @param onAddNewEmailClick Callback invoked when adding a new email.
 * @param onRemoveEmailClick Callback invoked when removing an email.
 * @param onOpenPasswordGeneratorClick Callback invoked when the Generator button is clicked
 * @param onPasswordCopyClick Callback invoked when the Copy button is clicked
 * @param password The current password value (only relevant for [SendAuth.Password]).
 * @param isEnabled Whether the chooser is enabled.
 * @param isExistingPasswordLocked Whether the send already has a password that cannot be
 * overwritten and must be removed before a new one can be set.
 * @param isSendsRestrictedByPolicy if sends are restricted by a policy.
 * @param modifier Modifier for the composable.
 */
@Suppress("LongMethod")
@Composable
fun AddEditSendAuthTypeChooser(
    sendAuth: SendAuth,
    onAuthTypeSelect: (SendAuth) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailValueChange: (AuthEmail) -> Unit,
    onAddNewEmailClick: () -> Unit,
    onRemoveEmailClick: (AuthEmail) -> Unit,
    onOpenPasswordGeneratorClick: () -> Unit,
    onPasswordCopyClick: (String) -> Unit,
    password: String,
    isEnabled: Boolean,
    isExistingPasswordLocked: Boolean,
    isSendsRestrictedByPolicy: Boolean,
    modifier: Modifier = Modifier,
) {
    var shouldShowDialog by rememberSaveable { mutableStateOf(false) }
    // Map option texts to their corresponding SendAuth factory functions
    val textToNoneAuth = stringResource(id = Universal Password ManagerString.anyone_with_the_link)
    val textToEmailAuth = stringResource(id = Universal Password ManagerString.specific_people)
    val textToPasswordAuth = stringResource(id = Universal Password ManagerString.anyone_with_password)

    val options = listOf(
        textToNoneAuth,
        textToEmailAuth,
        textToPasswordAuth,
    )

    Column(modifier = modifier) {
        Universal Password ManagerMultiSelectButton(
            label = stringResource(id = Universal Password ManagerString.who_can_view),
            isEnabled = isEnabled,
            options = options.toPersistentList(),
            selectedOption = sendAuth.text(),
            onOptionSelected = { selected ->
                val newAuth = when (selected) {
                    textToNoneAuth -> SendAuth.None
                    textToEmailAuth -> SendAuth.Email()
                    textToPasswordAuth -> SendAuth.Password
                    else -> SendAuth.None // fallback
                }
                onAuthTypeSelect(newAuth)
            },
            supportingText = sendAuth.supportingText?.let { it() },
            insets = PaddingValues(top = 6.dp, bottom = 4.dp),
            cardStyle = if (sendAuth is SendAuth.None) {
                CardStyle.Full
            } else {
                CardStyle.Top()
            },
            modifier = Modifier.testTag("SendVisibilityChooser"),
        )

        when (sendAuth) {
            is SendAuth.Email -> {
                SpecificPeopleEmailContent(
                    emails = sendAuth.emails,
                    onEmailValueChange = onEmailValueChange,
                    onAddNewEmailClick = onAddNewEmailClick,
                    onRemoveEmailClick = onRemoveEmailClick,
                )
            }

            is SendAuth.Password -> {
                if (isExistingPasswordLocked) {
                    Universal Password ManagerHiddenPasswordField(
                        label = stringResource(id = Universal Password ManagerString.password),
                        value = LOCKED_PASSWORD_PLACEHOLDER,
                        cardStyle = CardStyle.Bottom,
                        passwordFieldTestTag = "SendPasswordEntry",
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Universal Password ManagerPasswordField(
                        label = stringResource(id = Universal Password ManagerString.password),
                        value = password,
                        onValueChange = onPasswordChange,
                        cardStyle = CardStyle.Bottom,
                        passwordFieldTestTag = "SendPasswordEntry",
                        readOnly = isSendsRestrictedByPolicy,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_generate,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.generate_password,
                            ),
                            onClick = {
                                if (password.isEmpty()) {
                                    onOpenPasswordGeneratorClick()
                                } else {
                                    shouldShowDialog = true
                                }
                            },
                            modifier = Modifier.testTag(tag = "RegeneratePasswordButton"),
                        )
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(id = Universal Password ManagerString.copy_password),
                            isEnabled = password.isNotEmpty(),
                            onClick = {
                                onPasswordCopyClick(password)
                            },
                            modifier = Modifier.testTag(tag = "CopyPasswordButton"),
                        )
                    }
                }
            }

            is SendAuth.None -> Unit
        }
    }

    if (shouldShowDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.password),
            message = stringResource(id = Universal Password ManagerString.password_override_alert),
            confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.no),
            onConfirmClick = {
                shouldShowDialog = false
                onOpenPasswordGeneratorClick()
            },
            onDismissClick = { shouldShowDialog = false },
            onDismissRequest = { shouldShowDialog = false },
        )
    }
}

@Composable
private fun ColumnScope.SpecificPeopleEmailContent(
    emails: ImmutableList<AuthEmail>,
    onEmailValueChange: (AuthEmail) -> Unit,
    onAddNewEmailClick: () -> Unit,
    onRemoveEmailClick: (AuthEmail) -> Unit,
) {
    emails.forEachIndexed { index, authEmail ->
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.email),
            value = authEmail.value,
            onValueChange = { onEmailValueChange(authEmail.copy(value = it)) },
            singleLine = false,
            keyboardType = KeyboardType.Email,
            actions = {
                if (index > 0 || authEmail.value.isNotEmpty()) {
                    Universal Password ManagerStandardIconButton(
                        vectorIconRes = Universal Password ManagerDrawable.ic_delete,
                        contentDescription = stringResource(id = Universal Password ManagerString.delete),
                        contentColor = Universal Password ManagerTheme.colorScheme.status.error,
                        onClick = {
                            onRemoveEmailClick(authEmail)
                        },
                        modifier = Modifier.testTag("RemoveRecipientEmailButton"),
                    )
                }
            },
            textFieldTestTag = "SendRecipientEmailEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    Universal Password ManagerClickableText(
        label = stringResource(id = Universal Password ManagerString.add_email),
        onClick = onAddNewEmailClick,
        leadingIcon = painterResource(id = Universal Password ManagerDrawable.ic_plus_small),
        style = Universal Password ManagerTheme.typography.labelMedium,
        innerPadding = PaddingValues(all = 16.dp),
        shape = Universal Password ManagerTheme.shapes.contentBottom,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag = "AddEditSendAddEmailButton")
            .cardStyle(cardStyle = CardStyle.Bottom, paddingVertical = 0.dp),
    )
}
