package .authenticatorbridge.manager.util

import .authenticatorbridge.manager.model.AuthenticatorBridgeConnectionType

/**
 * Convert a [AuthenticatorBridgeConnectionType] to raw package name for connection.
 */
internal fun AuthenticatorBridgeConnectionType.toPackageName() =
    when (this) {
        AuthenticatorBridgeConnectionType.RELEASE -> "com.universalpasswordmanager"
        AuthenticatorBridgeConnectionType.DEV -> "com.universalpasswordmanager.dev"
    }
