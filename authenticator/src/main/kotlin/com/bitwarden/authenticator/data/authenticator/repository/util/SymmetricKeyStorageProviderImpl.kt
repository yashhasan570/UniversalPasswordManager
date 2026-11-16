package .authenticator.data.authenticator.repository.util

import .authenticator.data.auth.datasource.disk.AuthDiskSource
import .authenticatorbridge.model.SymmetricEncryptionKeyData
import .authenticatorbridge.provider.SymmetricKeyStorageProvider
import .authenticatorbridge.util.toSymmetricEncryptionKeyData

/**
 * Implementation of [SymmetricKeyStorageProvider] that stores symmetric key data in encrypted
 * shared preferences.
 */
class SymmetricKeyStorageProviderImpl(
    private val authDiskSource: AuthDiskSource,
) : SymmetricKeyStorageProvider {

    override var symmetricKey: SymmetricEncryptionKeyData?
        get() = authDiskSource.authenticatorBridgeSymmetricSyncKey?.toSymmetricEncryptionKeyData()
        set(value) {
            authDiskSource.authenticatorBridgeSymmetricSyncKey =
                value?.symmetricEncryptionKey?.byteArray
        }
}
