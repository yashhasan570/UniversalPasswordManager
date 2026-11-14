package .authenticatorbridge.factory

import android.content.Context
import .authenticatorbridge.manager.AuthenticatorBridgeManager
import .authenticatorbridge.manager.AuthenticatorBridgeManagerImpl
import .authenticatorbridge.manager.model.AuthenticatorBridgeConnectionType
import .authenticatorbridge.provider.SymmetricKeyStorageProvider
import .authenticatorbridge.util.PasswordManagerSignatureVerifierImpl

/**
 * Factory for supplying implementation instances of Authenticator Bridge SDK interfaces.
 */
class AuthenticatorBridgeFactory(
    context: Context,
) {

    private val applicationContext = context.applicationContext

    /**
     * Gets a new instance of [AuthenticatorBridgeManager].
     *
     * @param connectionType Specifies which build variant to connect to.
     * @param symmetricKeyStorageProvider Provides access to local storage of the symmetric
     * encryption key.
     */
    fun getAuthenticatorBridgeManager(
        connectionType: AuthenticatorBridgeConnectionType,
        symmetricKeyStorageProvider: SymmetricKeyStorageProvider,
    ): AuthenticatorBridgeManager = AuthenticatorBridgeManagerImpl(
        context = applicationContext,
        connectionType = connectionType,
        symmetricKeyStorageProvider = symmetricKeyStorageProvider,
        passwordManagerSignatureVerifier = PasswordManagerSignatureVerifierImpl(applicationContext),
    )
}
