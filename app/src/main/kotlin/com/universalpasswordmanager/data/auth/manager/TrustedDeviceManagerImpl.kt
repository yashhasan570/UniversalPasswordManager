package com.universalpasswordmanager.data.auth.manager

import com.universalpasswordmanager.core.data.util.asSuccess
import com.universalpasswordmanager.core.data.util.flatMap
import com.universalpasswordmanager.crypto.TrustDeviceResponse
import com.universalpasswordmanager.network.service.DevicesService
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.auth.manager.util.toUserStateJson
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource

/**
 * Default implementation of the [TrustedDeviceManager] used to establish trust with this device.
 */
class TrustedDeviceManagerImpl(
    private val authDiskSource: AuthDiskSource,
    private val vaultSdkSource: VaultSdkSource,
    private val devicesService: DevicesService,
) : TrustedDeviceManager {
    override suspend fun trustThisDeviceIfNecessary(userId: String): Result<Boolean> =
        if (authDiskSource.getShouldTrustDevice(userId = userId) != true) {
            authDiskSource.storeIsTdeLoginComplete(userId = userId, isTdeLoginComplete = true)
            false.asSuccess()
        } else {
            vaultSdkSource
                .getTrustDevice(userId = userId)
                .flatMap { trustThisDevice(userId = userId, trustDeviceResponse = it) }
                .also {
                    authDiskSource.storeShouldTrustDevice(
                        userId = userId,
                        shouldTrustDevice = null,
                    )
                }
                .map { true }
        }

    override suspend fun trustThisDevice(
        userId: String,
        trustDeviceResponse: TrustDeviceResponse,
    ): Result<Unit> = devicesService
        .trustDevice(
            appId = authDiskSource.uniqueAppId,
            encryptedDevicePrivateKey = trustDeviceResponse.protectedDevicePrivateKey,
            encryptedDevicePublicKey = trustDeviceResponse.protectedDevicePublicKey,
            encryptedUserKey = trustDeviceResponse.protectedUserKey,
        )
        .onSuccess {
            authDiskSource.storeDeviceKey(
                userId = userId,
                deviceKey = trustDeviceResponse.deviceKey,
            )
            authDiskSource.userState = trustDeviceResponse.toUserStateJson(
                userId = userId,
                previousUserState = requireNotNull(authDiskSource.userState),
            )
            authDiskSource.storeIsTdeLoginComplete(userId = userId, isTdeLoginComplete = true)
        }
        .also { authDiskSource.storeShouldTrustDevice(userId = userId, shouldTrustDevice = null) }
        .map { }
}
