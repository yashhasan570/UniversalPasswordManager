package com.universalpasswordmanager.ui.vault.feature.addedit

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
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalHelpButtonData
import com.universalpasswordmanager.ui.platform.components.coachmark.CoachMarkActionText
import com.universalpasswordmanager.ui.platform.components.coachmark.model.CoachMarkHighlightShape
import com.universalpasswordmanager.ui.platform.components.coachmark.scope.CoachMarkScope
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalHiddenPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.UriMatchType
import com.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditLoginTypeHandlers

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
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.login_credentials),
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
        title = UniversalString.coachmark_2_of_3.asText(),
        description = UniversalString.you_ll_only_need_to_set_up_authenticator_key.asText(),
        onDismiss = onCoachMarkDismissed,
        leftAction = {
            CoachMarkActionText(
                actionLabel = stringResource(UniversalString.back),
                onActionClick = onPreviousCoachMark,
            )
        },
        rightAction = {
            CoachMarkActionText(
                actionLabel = stringResource(UniversalString.next),
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
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.autofill_options),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
    }

    coachMarkHighlightItems(
        key = AddEditItemCoachMark.URI,
        title = UniversalString.coachmark_3_of_3.asText(),
        description = UniversalString
            .you_must_add_a_web_address_to_use_autofill_to_access_this_account
            .asText(),
        leftAction = {
            CoachMarkActionText(
                actionLabel = stringResource(UniversalString.back),
                onActionClick = onPreviousCoachMark,
            )
        },
        onDismiss = onCoachMarkDismissed,
        rightAction = {
            CoachMarkActionText(
                actionLabel = stringResource(UniversalString.done_text),
                onActionClick = onCoachMarkTourComplete,
            )
        },
        trailingContentIsBottomCard = true,
        trailingStaticContent = {
            UniversalClickableText(
                label = stringResource(id = UniversalString.add_website),
                onClick = loginItemTypeHandlers.onAddNewUriClick,
                leadingIcon = painterResource(id = UniversalDrawable.ic_plus_small),
                style = UniversalTheme.typography.labelMedium,
                innerPadding = PaddingValues(all = 16.dp),
                shape = UniversalTheme.shapes.contentBottom,
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

    UniversalTextField(
        label = stringResource(id = UniversalString.username),
        value = username,
        onValueChange = loginItemTypeHandlers.onUsernameTextChange,
        actions = {
            UniversalStandardIconButton(
                vectorIconRes = UniversalDrawable.ic_generate,
                contentDescription = stringResource(id = UniversalString.generate_username),
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
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.username),
            message = stringResource(
                id = UniversalString.are_you_sure_you_want_to_overwrite_the_current_username,
            ),
            confirmButtonText = stringResource(id = UniversalString.yes),
            dismissButtonText = stringResource(id = UniversalString.no),
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
        UniversalPasswordField(
            label = stringResource(id = UniversalString.password),
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
                UniversalClickableText(
                    label = stringResource(id = UniversalString.check_password_for_data_breaches),
                    style = UniversalTheme.typography.labelMedium,
                    onClick = loginItemTypeHandlers.onPasswordCheckerClick,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = UniversalTheme.shapes.contentBottom,
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
                title = stringResource(UniversalString.coachmark_1_of_3),
                description = stringResource(
                    UniversalString.use_this_button_to_generate_a_new_unique_password,
                ),
                shape = CoachMarkHighlightShape.Oval,
                onDismiss = onCoachMarkDismissed,
                leftAction = null,
                rightAction = {
                    CoachMarkActionText(
                        actionLabel = stringResource(UniversalString.next),
                        onActionClick = onGenerateCoachMarkActionClick,
                    )
                },
            ) {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_generate,
                    contentDescription = stringResource(id = UniversalString.generate_password),
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
                UniversalTwoButtonDialog(
                    title = stringResource(id = UniversalString.password),
                    message = stringResource(id = UniversalString.password_override_alert),
                    confirmButtonText = stringResource(id = UniversalString.yes),
                    dismissButtonText = stringResource(id = UniversalString.no),
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
        UniversalHiddenPasswordField(
            label = stringResource(id = UniversalString.password),
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
        UniversalHiddenPasswordField(
            label = stringResource(id = UniversalString.authenticator_key),
            value = totpKey,
            passwordFieldTestTag = "LoginTotpEntry",
            cardStyle = CardStyle.Full,
            modifier = modifier.fillMaxWidth(),
        )
        return
    }
    UniversalPasswordField(
        label = stringResource(id = UniversalString.authenticator_key),
        value = totpKey.orEmpty(),
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        actions = {
            totpKey?.let {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_clear,
                    contentDescription = stringResource(id = UniversalString.delete),
                    onClick = loginItemTypeHandlers.onClearTotpKeyClick,
                )
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_copy,
                    contentDescription = stringResource(id = UniversalString.copy_totp),
                    onClick = { loginItemTypeHandlers.onCopyTotpKeyClick(totpKey) },
                )
            }
        },
        helpData = UniversalHelpButtonData(
            onClick = loginItemTypeHandlers.onAuthenticatorHelpToolTipClick,
            contentDescription = stringResource(id = UniversalString.authenticator_key_help),
            isExternalLink = true,
        ),
        supportingContentPadding = PaddingValues(),
        supportingContent = {
            UniversalClickableText(
                label = stringResource(id = UniversalString.set_up_authenticator_key),
                onClick = onTotpSetupClick,
                leadingIcon = painterResource(id = UniversalDrawable.ic_camera_small),
                style = UniversalTheme.typography.labelMedium,
                innerPadding = PaddingValues(all = 16.dp),
                isEnabled = canViewTotp,
                shape = UniversalTheme.shapes.contentBottom,
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
    UniversalTextField(
        label = stringResource(id = UniversalString.passkey),
        value = creationDateTime.invoke(),
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        actions = {
            if (canRemovePasskey) {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_minus,
                    contentDescription = stringResource(id = UniversalString.remove_passkey),
                    onClick = loginItemTypeHandlers.onClearFido2CredentialClick,
                    modifier = Modifier.testTag(tag = "RemovePasskeyButton"),
                )
            }
        },
        cardStyle = CardStyle.Full,
        modifier = modifier,
    )
}
