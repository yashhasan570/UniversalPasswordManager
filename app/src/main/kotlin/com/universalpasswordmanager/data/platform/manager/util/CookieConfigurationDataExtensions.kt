package com.universalpasswordmanager.data.platform.manager.util

import com.universalpasswordmanager.network.model.NetworkCookie
import com.universalpasswordmanager.data.platform.datasource.disk.model.CookieConfigurationData

/**
 * Converts a list of [CookieConfigurationData.Cookie] to a list of [NetworkCookie].
 */
fun List<CookieConfigurationData.Cookie>?.toNetworkCookieList(): List<NetworkCookie> = this
    ?.map { it.toNetworkCookie() }
    .orEmpty()

/**
 * Converts a [CookieConfigurationData.Cookie] to a [NetworkCookie].
 */
fun CookieConfigurationData.Cookie.toNetworkCookie(): NetworkCookie =
    NetworkCookie(
        name = name,
        value = value,
    )
