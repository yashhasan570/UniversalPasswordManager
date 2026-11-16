package .authenticatorbridge.manager.util

import .authenticatorbridge.manager.model.AuthenticatorBridgeConnectionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthenticatorBridgeConnectionTypeExtensionsTest {

    @Test
    fun `toPackageName RELEASE should map to correct release package`() {
        assertEquals(
            "com.x8bit.universalpasswordmanager",
            AuthenticatorBridgeConnectionType.RELEASE.toPackageName(),
        )
    }

    @Test
    fun `toPackageName DEV should map to correct dev package`() {
        assertEquals(
            "com.x8bit.universalpasswordmanager.dev",
            AuthenticatorBridgeConnectionType.DEV.toPackageName(),
        )
    }
}
