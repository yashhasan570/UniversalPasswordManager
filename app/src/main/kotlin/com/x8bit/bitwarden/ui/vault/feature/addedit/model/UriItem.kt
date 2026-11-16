package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.model

import android.os.Parcelable
import .vault.UriMatchType
import kotlinx.parcelize.Parcelize

/**
 * Represents the URI item being displayed to the user.
 */
@Parcelize
data class UriItem(
    val id: String,
    val uri: String?,
    val match: UriMatchType?,
    val checksum: String?,
) : Parcelable
