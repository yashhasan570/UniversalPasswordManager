package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditSshKeyTypeHandlers

/**
 * The UI for adding and editing a SSH key cipher.
 */
fun LazyListScope.vaultAddEditSshKeyItems(
    sshKeyState: VaultAddEditState.ViewState.Content.ItemType.SshKey,
    sshKeyTypeHandlers: VaultAddEditSshKeyTypeHandlers,
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.private_key),
            value = sshKeyState.privateKey,
            readOnly = true,
            onValueChange = { /* no-op */ },
            showPassword = sshKeyState.showPrivateKey,
            showPasswordChange = { sshKeyTypeHandlers.onPrivateKeyVisibilityChange(it) },
            showPasswordTestTag = "ViewPrivateKeyButton",
            passwordFieldTestTag = "PrivateKeyEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.public_key),
            value = sshKeyState.publicKey,
            readOnly = true,
            onValueChange = { },
            textFieldTestTag = "PublicKeyEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.fingerprint),
            value = sshKeyState.fingerprint,
            readOnly = true,
            onValueChange = { /* no-op */ },
            textFieldTestTag = "FingerprintEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Preview
@Composable
private fun VaultAddEditSshKeyItems_preview() {
    Universal Password ManagerTheme {
        LazyColumn {
            vaultAddEditSshKeyItems(
                sshKeyState = VaultAddEditState.ViewState.Content.ItemType.SshKey(
                    publicKey = "public key",
                    privateKey = "private key",
                    fingerprint = "fingerprint",
                    showPublicKey = false,
                    showPrivateKey = false,
                    showFingerprint = false,
                ),
                sshKeyTypeHandlers = VaultAddEditSshKeyTypeHandlers(
                    onPrivateKeyVisibilityChange = { },
                ),
            )
        }
    }
}
