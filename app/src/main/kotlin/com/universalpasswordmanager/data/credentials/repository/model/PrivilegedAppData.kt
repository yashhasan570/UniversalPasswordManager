package com.universalpasswordmanager.data.credentials.repository.model

import com.universalpasswordmanager.data.credentials.model.PrivilegedAppAllowListJson

/**
 * Represents privileged applications that are trusted by various sources.
 */
data class PrivilegedAppData(
    val googleTrustedApps: PrivilegedAppAllowListJson,
    val communityTrustedApps: PrivilegedAppAllowListJson,
    val userTrustedApps: PrivilegedAppAllowListJson,
)
