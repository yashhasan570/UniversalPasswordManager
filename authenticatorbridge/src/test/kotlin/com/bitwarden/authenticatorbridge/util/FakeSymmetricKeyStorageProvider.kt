package .authenticatorbridge.util

import .authenticatorbridge.model.SymmetricEncryptionKeyData
import .authenticatorbridge.provider.SymmetricKeyStorageProvider

/**
 * A fake implementation of [SymmetricKeyStorageProvider] for testing purposes.
 */
class FakeSymmetricKeyStorageProvider : SymmetricKeyStorageProvider {
    override var symmetricKey: SymmetricEncryptionKeyData? = null
}
