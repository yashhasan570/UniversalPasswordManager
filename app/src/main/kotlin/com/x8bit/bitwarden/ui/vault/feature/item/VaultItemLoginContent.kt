package com.x8bit.universalpasswordmanager.ui.vault.feature.item

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.field.Universal Password ManagerHiddenPasswordField
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.indicator.Universal Password ManagerCircularCountdownIndicator
import .ui.platform.components.model.CardStyle
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.itemHeader
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemAttachments
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemCustomFields
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemHistory
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemNotes
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.handlers.VaultCommonItemTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.handlers.VaultLoginItemTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.model.TotpCodeItemData

private const val AUTH_CODE_SPACING_INTERVAL = 3

/**
 * The top level content UI state for the [VaultItemScreen] when viewing a Login cipher.
 */
@Suppress("LongMethod")
@Composable
fun VaultItemLoginContent(
    commonState: VaultItemState.ViewState.Content.Common,
    loginItemState: VaultItemState.ViewState.Content.ItemType.Login,
    vaultCommonItemTypeHandlers: VaultCommonItemTypeHandlers,
    vaultLoginItemTypeHandlers: VaultLoginItemTypeHandlers,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(value = false) }
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {
        item {
            Spacer(Modifier.height(height = 12.dp))
        }
        itemHeader(
            value = commonState.name,
            isFavorite = commonState.favorite,
            isArchived = commonState.archived,
            iconData = commonState.iconData,
            relatedLocations = commonState.relatedLocations,
            iconTestTag = "LoginItemNameIcon",
            textFieldTestTag = "LoginItemNameEntry",
            isExpanded = isExpanded,
            onExpandClick = { isExpanded = !isExpanded },
            applyIconBackground = commonState.iconData is IconData.Local,
        )
        if (loginItemState.hasLoginCredentials) {
            item(key = "loginCredentialsHeader") {
                Spacer(modifier = Modifier.height(height = 16.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.login_credentials),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp)
                        .animateItem(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
        }

        loginItemState.username?.let { username ->
            item(key = "username") {
                UsernameField(
                    username = username,
                    onCopyUsernameClick = vaultLoginItemTypeHandlers.onCopyUsernameClick,
                    cardStyle = loginItemState
                        .passwordData
                        ?.let { CardStyle.Top(dividerPadding = 0.dp) }
                        ?: CardStyle.Full,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        loginItemState.passwordData?.let { passwordData ->
            item(key = "passwordData") {
                PasswordField(
                    passwordData = passwordData,
                    onShowPasswordClick = vaultLoginItemTypeHandlers.onShowPasswordClick,
                    onCheckForBreachClick = vaultLoginItemTypeHandlers.onCheckForBreachClick,
                    onCopyPasswordClick = vaultLoginItemTypeHandlers.onCopyPasswordClick,
                    cardStyle = loginItemState
                        .username
                        ?.let { CardStyle.Bottom }
                        ?: CardStyle.Full,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        loginItemState.fido2CredentialCreationDateText?.let { creationDate ->
            item(key = "creationDate") {
                Spacer(modifier = Modifier.height(8.dp))
                Fido2CredentialField(
                    creationDate = creationDate(),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        loginItemState.totpCodeItemData?.let { totpCodeItemData ->
            item(key = "totpCode") {
                Spacer(modifier = Modifier.height(8.dp))
                TotpField(
                    totpCodeItemData = totpCodeItemData,
                    enabled = loginItemState.canViewTotpCode,
                    onCopyTotpClick = vaultLoginItemTypeHandlers.onCopyTotpCodeClick,
                    onAuthenticatorHelpToolTipClick = vaultLoginItemTypeHandlers
                        .onAuthenticatorHelpToolTipClick,
                    onPremiumRequiredClick = vaultCommonItemTypeHandlers.onPremiumRequiredClick,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        loginItemState.uris.takeUnless { it.isEmpty() }?.let { uris ->
            item(key = "urisHeader") {
                Spacer(modifier = Modifier.height(height = 16.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.autofill_options),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp)
                        .animateItem(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }

            itemsIndexed(
                items = uris,
                key = { index, _ -> "uri_$index" },
            ) { index, uriData ->
                UriField(
                    uriData = uriData,
                    onCopyUriClick = vaultLoginItemTypeHandlers.onCopyUriClick,
                    onLaunchUriClick = vaultLoginItemTypeHandlers.onLaunchUriClick,
                    cardStyle = uris.toListItemCardStyle(index = index, dividerPadding = 0.dp),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        vaultItemNotes(
            notes = commonState.notes,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
        )

        vaultItemCustomFields(
            customFields = commonState.customFields,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
        )

        vaultItemAttachments(
            isInAppUpgradeAvailable = commonState.isInAppUpgradeAvailable,
            attachments = commonState.attachments,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
        )

        vaultItemHistory(
            commonState = commonState,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
            loginPasswordRevisionDate = loginItemState.passwordRevisionDate,
        )

        item {
            Spacer(modifier = Modifier.height(88.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun Fido2CredentialField(
    creationDate: String,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.passkey),
        value = creationDate,
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        cardStyle = CardStyle.Full,
        modifier = modifier,
    )
}

@Composable
private fun PasswordField(
    passwordData: VaultItemState.ViewState.Content.ItemType.Login.PasswordData,
    onShowPasswordClick: (Boolean) -> Unit,
    onCheckForBreachClick: () -> Unit,
    onCopyPasswordClick: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    if (passwordData.canViewPassword) {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.password),
            value = passwordData.password,
            showPasswordChange = { onShowPasswordClick(it) },
            showPassword = passwordData.isVisible,
            onValueChange = { },
            readOnly = true,
            singleLine = false,
            actions = {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                    contentDescription = stringResource(id = Universal Password ManagerString.copy_password),
                    onClick = onCopyPasswordClick,
                    modifier = Modifier.testTag(tag = "LoginCopyPasswordButton"),
                )
            },
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                Universal Password ManagerClickableText(
                    label = stringResource(id = Universal Password ManagerString.check_password_for_data_breaches),
                    style = Universal Password ManagerTheme.typography.labelMedium,
                    onClick = onCheckForBreachClick,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = Universal Password ManagerTheme.shapes.contentBottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "LoginCheckPasswordButton"),
                )
            },
            showPasswordTestTag = "LoginViewPasswordButton",
            passwordFieldTestTag = "LoginPasswordEntry",
            cardStyle = cardStyle,
            modifier = modifier,
        )
    } else {
        Universal Password ManagerHiddenPasswordField(
            label = stringResource(id = Universal Password ManagerString.password),
            value = passwordData.password,
            passwordFieldTestTag = "LoginPasswordEntry",
            cardStyle = cardStyle,
            modifier = modifier,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun TotpField(
    totpCodeItemData: TotpCodeItemData,
    enabled: Boolean,
    onCopyTotpClick: () -> Unit,
    onAuthenticatorHelpToolTipClick: () -> Unit,
    onPremiumRequiredClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (enabled) {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.authenticator_key),
            value = totpCodeItemData.verificationCode
                .chunked(AUTH_CODE_SPACING_INTERVAL)
                .joinToString(" "),
            onValueChange = { },
            textStyle = Universal Password ManagerTheme.typography.sensitiveInfoSmall,
            readOnly = true,
            singleLine = true,
            helpData = Universal Password ManagerHelpButtonData(
                onClick = onAuthenticatorHelpToolTipClick,
                contentDescription = stringResource(id = Universal Password ManagerString.authenticator_key_help),
                isExternalLink = true,
            ),
            actions = {
                Universal Password ManagerCircularCountdownIndicator(
                    timeLeftSeconds = totpCodeItemData.timeLeftSeconds,
                    periodSeconds = totpCodeItemData.periodSeconds,
                )
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                    contentDescription = stringResource(id = Universal Password ManagerString.copy_totp),
                    onClick = onCopyTotpClick,
                    modifier = Modifier.testTag(tag = "LoginCopyTotpButton"),
                )
            },
            textFieldTestTag = "LoginTotpEntry",
            cardStyle = CardStyle.Full,
            modifier = modifier,
        )
    } else {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.authenticator_key),
            value = "",
            helpData = Universal Password ManagerHelpButtonData(
                onClick = onAuthenticatorHelpToolTipClick,
                contentDescription = stringResource(id = Universal Password ManagerString.authenticator_key_help),
                isExternalLink = true,
            ),
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                Universal Password ManagerClickableText(
                    label = stringResource(id = Universal Password ManagerString.premium_subscription_required),
                    onClick = onPremiumRequiredClick,
                    style = Universal Password ManagerTheme.typography.labelMedium,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = Universal Password ManagerTheme.shapes.contentBottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "TotpPremiumRequiredButton"),
                )
            },
            enabled = false,
            singleLine = false,
            onValueChange = { },
            readOnly = true,
            cardStyle = CardStyle.Full,
            modifier = modifier,
        )
    }
}

@Composable
private fun UriField(
    uriData: VaultItemState.ViewState.Content.ItemType.Login.UriData,
    onCopyUriClick: (String) -> Unit,
    onLaunchUriClick: (String) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.website_uri),
        value = uriData.uri,
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        actions = {
            if (uriData.isLaunchable) {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_external_link,
                    contentDescription = stringResource(
                        id = Universal Password ManagerString.external_link_format,
                        formatArgs = arrayOf(stringResource(id = Universal Password ManagerString.launch)),
                    ),
                    onClick = { onLaunchUriClick(uriData.uri) },
                    modifier = Modifier.testTag(tag = "LoginLaunchUriButton"),
                )
            }
            if (uriData.isCopyable) {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                    contentDescription = stringResource(id = Universal Password ManagerString.copy),
                    onClick = { onCopyUriClick(uriData.uri) },
                    modifier = Modifier.testTag(tag = "LoginCopyUriButton"),
                )
            }
        },
        textFieldTestTag = "LoginUriEntry",
        cardStyle = cardStyle,
        modifier = modifier,
    )
}

@Composable
private fun UsernameField(
    username: String,
    onCopyUsernameClick: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.username),
        value = username,
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        actions = {
            Universal Password ManagerStandardIconButton(
                vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                contentDescription = stringResource(id = Universal Password ManagerString.copy_username),
                onClick = onCopyUsernameClick,
                modifier = Modifier.testTag(tag = "LoginCopyUsernameButton"),
            )
        },
        textFieldTestTag = "LoginUsernameEntry",
        cardStyle = cardStyle,
        modifier = modifier,
    )
}
