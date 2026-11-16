package .authenticatorbridge.manager.util

import .authenticatorbridge.manager.model.AuthenticatorBridgeConnectionType

/**
 * Convert a [AuthenticatorBridgeConnectionType] to raw package name for connection.
 */
internal fun AuthenticatorBridgeConnectionType.toPackageName() =
    when (this) {
        AuthenticatorBridgeConnectionType.RELEASE -> "com.x8bit.universalpasswordmanager"
        AuthenticatorBridgeConnectionType.DEV -> "com.x8bit.universalpasswordmanager.dev"
    }
