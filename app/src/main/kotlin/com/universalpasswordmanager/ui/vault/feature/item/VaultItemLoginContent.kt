package com.universalpasswordmanager.ui.vault.feature.item

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
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toListItemCardStyle
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalHelpButtonData
import com.universalpasswordmanager.ui.platform.components.field.UniversalHiddenPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.indicator.UniversalCircularCountdownIndicator
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.vault.feature.item.component.itemHeader
import com.universalpasswordmanager.ui.vault.feature.item.component.vaultItemAttachments
import com.universalpasswordmanager.ui.vault.feature.item.component.vaultItemCustomFields
import com.universalpasswordmanager.ui.vault.feature.item.component.vaultItemHistory
import com.universalpasswordmanager.ui.vault.feature.item.component.vaultItemNotes
import com.universalpasswordmanager.ui.vault.feature.item.handlers.VaultCommonItemTypeHandlers
import com.universalpasswordmanager.ui.vault.feature.item.handlers.VaultLoginItemTypeHandlers
import com.universalpasswordmanager.ui.vault.feature.item.model.TotpCodeItemData

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
                UniversalListHeaderText(
                    label = stringResource(id = UniversalString.login_credentials),
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
                UniversalListHeaderText(
                    label = stringResource(id = UniversalString.autofill_options),
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
    UniversalTextField(
        label = stringResource(id = UniversalString.passkey),
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
        UniversalPasswordField(
            label = stringResource(id = UniversalString.password),
            value = passwordData.password,
            showPasswordChange = { onShowPasswordClick(it) },
            showPassword = passwordData.isVisible,
            onValueChange = { },
            readOnly = true,
            singleLine = false,
            actions = {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_copy,
                    contentDescription = stringResource(id = UniversalString.copy_password),
                    onClick = onCopyPasswordClick,
                    modifier = Modifier.testTag(tag = "LoginCopyPasswordButton"),
                )
            },
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                UniversalClickableText(
                    label = stringResource(id = UniversalString.check_password_for_data_breaches),
                    style = UniversalTheme.typography.labelMedium,
                    onClick = onCheckForBreachClick,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = UniversalTheme.shapes.contentBottom,
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
        UniversalHiddenPasswordField(
            label = stringResource(id = UniversalString.password),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.authenticator_key),
            value = totpCodeItemData.verificationCode
                .chunked(AUTH_CODE_SPACING_INTERVAL)
                .joinToString(" "),
            onValueChange = { },
            textStyle = UniversalTheme.typography.sensitiveInfoSmall,
            readOnly = true,
            singleLine = true,
            helpData = UniversalHelpButtonData(
                onClick = onAuthenticatorHelpToolTipClick,
                contentDescription = stringResource(id = UniversalString.authenticator_key_help),
                isExternalLink = true,
            ),
            actions = {
                UniversalCircularCountdownIndicator(
                    timeLeftSeconds = totpCodeItemData.timeLeftSeconds,
                    periodSeconds = totpCodeItemData.periodSeconds,
                )
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_copy,
                    contentDescription = stringResource(id = UniversalString.copy_totp),
                    onClick = onCopyTotpClick,
                    modifier = Modifier.testTag(tag = "LoginCopyTotpButton"),
                )
            },
            textFieldTestTag = "LoginTotpEntry",
            cardStyle = CardStyle.Full,
            modifier = modifier,
        )
    } else {
        UniversalTextField(
            label = stringResource(id = UniversalString.authenticator_key),
            value = "",
            helpData = UniversalHelpButtonData(
                onClick = onAuthenticatorHelpToolTipClick,
                contentDescription = stringResource(id = UniversalString.authenticator_key_help),
                isExternalLink = true,
            ),
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                UniversalClickableText(
                    label = stringResource(id = UniversalString.premium_subscription_required),
                    onClick = onPremiumRequiredClick,
                    style = UniversalTheme.typography.labelMedium,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = UniversalTheme.shapes.contentBottom,
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
    UniversalTextField(
        label = stringResource(id = UniversalString.website_uri),
        value = uriData.uri,
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        actions = {
            if (uriData.isLaunchable) {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_external_link,
                    contentDescription = stringResource(
                        id = UniversalString.external_link_format,
                        formatArgs = arrayOf(stringResource(id = UniversalString.launch)),
                    ),
                    onClick = { onLaunchUriClick(uriData.uri) },
                    modifier = Modifier.testTag(tag = "LoginLaunchUriButton"),
                )
            }
            if (uriData.isCopyable) {
                UniversalStandardIconButton(
                    vectorIconRes = UniversalDrawable.ic_copy,
                    contentDescription = stringResource(id = UniversalString.copy),
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
    UniversalTextField(
        label = stringResource(id = UniversalString.username),
        value = username,
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        actions = {
            UniversalStandardIconButton(
                vectorIconRes = UniversalDrawable.ic_copy,
                contentDescription = stringResource(id = UniversalString.copy_username),
                onClick = onCopyUsernameClick,
                modifier = Modifier.testTag(tag = "LoginCopyUsernameButton"),
            )
        },
        textFieldTestTag = "LoginUsernameEntry",
        cardStyle = cardStyle,
        modifier = modifier,
    )
}
