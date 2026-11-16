package com.x8bit.universalpasswordmanager.data.platform.processor

import .authenticatorbridge.IAuthenticatorBridgeService

/**
 * Provides implementation of [IAuthenticatorBridgeService] APIs in an injectable and testable
 * manner.
 */
interface AuthenticatorBridgeProcessor {

    /**
     * Binder that implements [IAuthenticatorBridgeService]. Null can be returned to represent a
     * no-op binder.
     */
    val binder: IAuthenticatorBridgeService.Stub?
}
