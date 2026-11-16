package .authenticatorbridge.util

import .authenticatorbridge.IAuthenticatorBridgeServiceCallback
import .authenticatorbridge.model.EncryptedSharedAccountData
import .authenticatorbridge.provider.AuthenticatorBridgeCallbackProvider

/**
 * Test implementation of [AuthenticatorBridgeCallbackProvider] that provides a testable
 * [IAuthenticatorBridgeServiceCallback.Default] implementation.
 */
class TestAuthenticatorBridgeCallbackProvider : AuthenticatorBridgeCallbackProvider {

    override fun getCallback(
        onAccountsSync: (EncryptedSharedAccountData) -> Unit,
    ): IAuthenticatorBridgeServiceCallback =
        object : IAuthenticatorBridgeServiceCallback.Default() {
            override fun onAccountsSync(data: EncryptedSharedAccountData) =
                onAccountsSync.invoke(data)
        }
}
