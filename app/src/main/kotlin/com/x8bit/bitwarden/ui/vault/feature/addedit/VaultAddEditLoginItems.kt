package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.coachmark.CoachMarkActionText
import .ui.platform.components.coachmark.model.CoachMarkHighlightShape
import .ui.platform.components.coachmark.scope.CoachMarkScope
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerHiddenPasswordField
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.UriMatchType
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditLoginTypeHandlers

/**
 * The UI for adding and editing a login cipher.
 */
@Suppress("LongMethod", "LongParameterList")
fun LazyListScope.vaultAddEditLoginItems(
    coachMarkScope: CoachMarkScope<AddEditItemCoachMark>,
    loginState: VaultAddEditState.ViewState.Content.ItemType.Login,
    loginItemTypeHandlers: VaultAddEditLoginTypeHandlers,
    windowAdaptiveInfo: WindowAdaptiveInfo,
    onTotpSetupClick: () -> Unit,
    onNextCoachMark: () -> Unit,
    onPreviousCoachMark: () -> Unit,
    onCoachMarkTourComplete: () -> Unit,
    onCoachMarkDismissed: () -> Unit,
    defaultUriMatchType: UriMatchType,
) = coachMarkScope.run {
    item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.login_credentials),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
        UsernameRow(
            username = loginState.username,
            loginItemTypeHandlers = loginItemTypeHandlers,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item(key = AddEditItemCoachMark.GENERATE_PASSWORD) {
        PasswordRow(
            password = loginState.password,
            canViewPassword = loginState.canViewPassword,
            loginItemTypeHandlers = loginItemTypeHandlers,
            onGenerateCoachMarkActionClick = onNextCoachMark,
            onCoachMarkDismissed = onCoachMarkDismissed,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    loginState.fido2CredentialCreationDateTime?.let { creationDateTime ->
        item {
            Spacer(modifier = Modifier.height(8.dp))
            PasskeyField(
                creationDateTime = creationDateTime,
                canRemovePasskey = loginState.canRemovePasskey,
                loginItemTypeHandlers = loginItemTypeHandlers,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }
    }

    item {
        Spacer(modifier = Modifier.height(height = 8.dp))
    }

    coachMarkHighlightItem(
        key = AddEditItemCoachMark.TOTP,
        title = Universal Password ManagerString.coachmark_2_of_3.asText(),
        description = Universal Password ManagerString.you_ll_only_need_to_set_up_authenticator_key.asText(),
        onDismiss = onCoachMarkDismissed,
        leftAction = {
            CoachMarkActionText(
                actionLabel = stringResource(Universal Password ManagerString.back),
                onActionClick = onPreviousCoachMark,
            )
        },
        rightAction = {
            CoachMarkActionText(
                actionLabel = stringResource(Universal Password ManagerString.next),
                onActionClick = onNextCoachMark,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(windowAdaptiveInfo = windowAdaptiveInfo),
    ) {
        TotpRow(
            totpKey = loginState.totp,
            canViewTotp = loginState.canViewPassword,
            loginItemTypeHandlers = loginItemTypeHandlers,
            onTotpSetupClick = onTotpSetupClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.autofill_options),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
    }

    coachMarkHighlightItems(
        key = AddEditItemCoachMark.URI,
        title = Universal Password ManagerString.coachmark_3_of_3.asText(),
        description = Universal Password ManagerString
            .you_must_add_a_web_address_to_use_autofill_to_access_this_account
            .asText(),
        leftAction = {
            CoachMarkActionText(
                actionLabel = stringResource(Universal Password ManagerString.back),
                onActionClick = onPreviousCoachMark,
            )
        },
        onDismiss = onCoachMarkDismissed,
        rightAction = {
            CoachMarkActionText(
                actionLabel = stringResource(Universal Password ManagerString.done_text),
                onActionClick = onCoachMarkTourComplete,
            )
        },
        trailingContentIsBottomCard = true,
        trailingStaticContent = {
            Universal Password ManagerClickableText(
                label = stringResource(id = Universal Password ManagerString.add_website),
                onClick = loginItemTypeHandlers.onAddNewUriClick,
                leadingIcon = painterResource(id = Universal Password ManagerDrawable.ic_plus_small),
                style = Universal Password ManagerTheme.typography.labelMedium,
                innerPadding = PaddingValues(all = 16.dp),
                shape = Universal Password ManagerTheme.shapes.contentBottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(tag = "LoginAddNewUriButton")
                    .cardStyle(cardStyle = CardStyle.Bottom, paddingVertical = 0.dp),
            )
        },
        items = loginState.uriList,
        modifier = Modifier
            .standardHorizontalMargin(windowAdaptiveInfo = windowAdaptiveInfo),
    ) { uriItem, cardStyle ->
        VaultAddEditUriItem(
            uriItem = uriItem,
            onUriValueChange = loginItemTypeHandlers.onUriValueChange,
            onUriItemRemoved = loginItemTypeHandlers.onRemoveUriClick,
            onLearnMoreClick = loginItemTypeHandlers.onLearnMoreClick,
            cardStyle = cardStyle,
            defaultUriMatchType = defaultUriMatchType,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun UsernameRow(
    username: String,
    loginItemTypeHandlers: VaultAddEditLoginTypeHandlers,
    modifier: Modifier = Modifier,
) {
    var shouldShowDialog by rememberSaveable { mutableStateOf(false) }

    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.username),
        value = username,
        onValueChange = loginItemTypeHandlers.onUsernameTextChange,
        actions = {
            Universal Password ManagerStandardIconButton(
                vectorIconRes = Universal Password ManagerDrawable.ic_generate,
                contentDescription = stringResource(id = Universal Password ManagerString.generate_username),
                onClick = {
                    if (username.isEmpty()) {
                        loginItemTypeHandlers.onOpenUsernameGeneratorClick()
                    } else {
                        shouldShowDialog = true
                    }
                },
                modifier = Modifier.testTag(tag = "GenerateUsernameButton"),
            )
        },
        textFieldTestTag = "LoginUsernameEntry",
        cardStyle = CardStyle.Top(dividerPadding = 0.dp),
        modifier = modifier,
    )

    if (shouldShowDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.username),
            message = stringResource(
                id = Universal Password ManagerString.are_you_sure_you_want_to_overwrite_the_current_username,
            ),
            confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.no),
            onConfirmClick = {
                shouldShowDialog = false
                loginItemTypeHandlers.onOpenUsernameGeneratorClick()
            },
            onDismissClick = {
                shouldShowDialog = false
            },
            onDismissRequest = {
                shouldShowDialog = false
            },
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun CoachMarkScope<AddEditItemCoachMark>.PasswordRow(
    password: String,
    canViewPassword: Boolean,
    loginItemTypeHandlers: VaultAddEditLoginTypeHandlers,
    onGenerateCoachMarkActionClick: () -> Unit,
    onCoachMarkDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowDialog by rememberSaveable { mutableStateOf(false) }

    if (canViewPassword) {
        var shouldShowPassword by remember { mutableStateOf(false) }
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.password),
            value = password,
            onValueChange = loginItemTypeHandlers.onPasswordTextChange,
            showPassword = shouldShowPassword,
            showPasswordChange = {
                shouldShowPassword = !shouldShowPassword
                loginItemTypeHandlers.onPasswordVisibilityChange(shouldShowPassword)
            },
            showPasswordTestTag = "ViewPasswordButton",
            passwordFieldTestTag = "LoginPasswordEntry",
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                Universal Password ManagerClickableText(
                    label = stringResource(id = Universal Password ManagerString.check_password_for_data_breaches),
                    style = Universal Password ManagerTheme.typography.labelMedium,
                    onClick = loginItemTypeHandlers.onPasswordCheckerClick,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = Universal Password ManagerTheme.shapes.contentBottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "CheckPasswordButton"),
                )
            },
            cardStyle = CardStyle.Bottom,
            modifier = modifier,
        ) {
            CoachMarkHighlight(
                key = AddEditItemCoachMark.GENERATE_PASSWORD,
                title = stringResource(Universal Password ManagerString.coachmark_1_of_3),
                description = stringResource(
                    Universal Password ManagerString.use_this_button_to_generate_a_new_unique_password,
                ),
                shape = CoachMarkHighlightShape.Oval,
                onDismiss = onCoachMarkDismissed,
                leftAction = null,
                rightAction = {
                    CoachMarkActionText(
                        actionLabel = stringResource(Universal Password ManagerString.next),
                        onActionClick = onGenerateCoachMarkActionClick,
                    )
                },
            ) {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_generate,
                    contentDescription = stringResource(id = Universal Password ManagerString.generate_password),
                    onClick = {
                        if (password.isEmpty()) {
                            loginItemTypeHandlers.onOpenPasswordGeneratorClick()
                        } else {
                            shouldShowDialog = true
                        }
                    },
                    modifier = Modifier.testTag(tag = "RegeneratePasswordButton"),
                )
            }

            if (shouldShowDialog) {
                Universal Password ManagerTwoButtonDialog(
                    title = stringResource(id = Universal Password ManagerString.password),
                    message = stringResource(id = Universal Password ManagerString.password_override_alert),
                    confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
                    dismissButtonText = stringResource(id = Universal Password ManagerString.no),
                    onConfirmClick = {
                        shouldShowDialog = false
                        loginItemTypeHandlers.onOpenPasswordGeneratorClick()
                    },
                    onDismissClick = {
                        shouldShowDialog = false
                    },
                    onDismissRequest = {
                        shouldShowDialog = false
                    },
                )
            }
        }
    } else {
        Universal Password ManagerHiddenPasswordField(
            label = stringResource(id = Universal Password ManagerString.password),
            value = password,
            passwordFieldTestTag = "LoginPasswordEntry",
            cardStyle = CardStyle.Bottom,
            modifier = modifier,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun TotpRow(
    totpKey: String?,
    canViewTotp: Boolean,
    loginItemTypeHandlers: VaultAddEditLoginTypeHandlers,
    onTotpSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!canViewTotp && totpKey != null) {
        Universal Password ManagerHiddenPasswordField(
            label = stringResource(id = Universal Password ManagerString.authenticator_key),
            value = totpKey,
            passwordFieldTestTag = "LoginTotpEntry",
            cardStyle = CardStyle.Full,
            modifier = modifier.fillMaxWidth(),
        )
        return
    }
    Universal Password ManagerPasswordField(
        label = stringResource(id = Universal Password ManagerString.authenticator_key),
        value = totpKey.orEmpty(),
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        actions = {
            totpKey?.let {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_clear,
                    contentDescription = stringResource(id = Universal Password ManagerString.delete),
                    onClick = loginItemTypeHandlers.onClearTotpKeyClick,
                )
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                    contentDescription = stringResource(id = Universal Password ManagerString.copy_totp),
                    onClick = { loginItemTypeHandlers.onCopyTotpKeyClick(totpKey) },
                )
            }
        },
        helpData = Universal Password ManagerHelpButtonData(
            onClick = loginItemTypeHandlers.onAuthenticatorHelpToolTipClick,
            contentDescription = stringResource(id = Universal Password ManagerString.authenticator_key_help),
            isExternalLink = true,
        ),
        supportingContentPadding = PaddingValues(),
        supportingContent = {
            Universal Password ManagerClickableText(
                label = stringResource(id = Universal Password ManagerString.set_up_authenticator_key),
                onClick = onTotpSetupClick,
                leadingIcon = painterResource(id = Universal Password ManagerDrawable.ic_camera_small),
                style = Universal Password ManagerTheme.typography.labelMedium,
                innerPadding = PaddingValues(all = 16.dp),
                isEnabled = canViewTotp,
                shape = Universal Password ManagerTheme.shapes.contentBottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("SetupTotpButton"),
            )
        },
        passwordFieldTestTag = "LoginTotpEntry",
        cardStyle = CardStyle.Full,
        modifier = modifier
            .fillMaxWidth()
            .focusProperties { canFocus = false },
    )
}

@Composable
private fun PasskeyField(
    creationDateTime: Text,
    canRemovePasskey: Boolean,
    loginItemTypeHandlers: VaultAddEditLoginTypeHandlers,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.passkey),
        value = creationDateTime.invoke(),
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        actions = {
            if (canRemovePasskey) {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_minus,
                    contentDescription = stringResource(id = Universal Password ManagerString.remove_passkey),
                    onClick = loginItemTypeHandlers.onClearFido2CredentialClick,
                    modifier = Modifier.testTag(tag = "RemovePasskeyButton"),
                )
            }
        },
        cardStyle = CardStyle.Full,
        modifier = modifier,
    )
}
