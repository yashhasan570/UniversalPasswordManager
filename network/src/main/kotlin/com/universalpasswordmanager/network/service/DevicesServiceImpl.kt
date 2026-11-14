package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.AuthenticatedDevicesApi
import com.universalpasswordmanager.network.api.UnauthenticatedDevicesApi
import com.universalpasswordmanager.network.model.DevicesResponseJson
import com.universalpasswordmanager.network.model.TrustedDeviceKeysRequestJson
import com.universalpasswordmanager.network.model.TrustedDeviceKeysResponseJson
import com.universalpasswordmanager.network.util.base64UrlEncode
import com.universalpasswordmanager.network.util.toResult

/**
 * The default implementation of the [DevicesService].
 */
internal class DevicesServiceImpl(
    private val authenticatedDevicesApi: AuthenticatedDevicesApi,
    private val unauthenticatedDevicesApi: UnauthenticatedDevicesApi,
) : DevicesService {
    override suspend fun getDevices(): Result<DevicesResponseJson> =
        authenticatedDevicesApi.getDevices().toResult()

    override suspend fun getIsKnownDevice(
        emailAddress: String,
        deviceId: String,
    ): Result<Boolean> = unauthenticatedDevicesApi
        .getIsKnownDevice(
            emailAddress = emailAddress.base64UrlEncode(),
            deviceId = deviceId,
        )
        .toResult()

    override suspend fun trustDevice(
        appId: String,
        encryptedUserKey: String,
        encryptedDevicePublicKey: String,
        encryptedDevicePrivateKey: String,
    ): Result<TrustedDeviceKeysResponseJson> = authenticatedDevicesApi
        .updateTrustedDeviceKeys(
            appId = appId,
            request = TrustedDeviceKeysRequestJson(
                encryptedUserKey = encryptedUserKey,
                encryptedDevicePublicKey = encryptedDevicePublicKey,
                encryptedDevicePrivateKey = encryptedDevicePrivateKey,
            ),
        )
        .toResult()
}
