package com.work.service

import com.work.api.AuthenticatedDevicesApi
import com.work.api.UnauthenticatedDevicesApi
import com.work.model.DevicesResponseJson
import com.work.model.TrustedDeviceKeysRequestJson
import com.work.model.TrustedDeviceKeysResponseJson
import com.work.util.base64UrlEncode
import com.work.util.toResult

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
