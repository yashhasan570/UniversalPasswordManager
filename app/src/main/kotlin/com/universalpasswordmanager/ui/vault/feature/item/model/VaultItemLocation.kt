package com.universalpasswordmanager.ui.vault.feature.item.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import kotlinx.parcelize.Parcelize

/**
 * Represents the location of a vault item.
 *
 * A vault item can be located in an [Organization], [Collection], or a [Folder].
 * Each location type provides specific details and an associated icon.
 */
sealed class VaultItemLocation : Parcelable {
    /**
     * The name of the location. This can be the organization name, collection name, or folder name
     * depending on the location type.
     */
    abstract val name: String

    /**
     * Icon for the location
     */
    @get:DrawableRes
    abstract val icon: Int

    /**
     * Represents an organization assignment.
     */
    @Parcelize
    data class Organization(
        override val name: String,
    ) : VaultItemLocation() {
        override val icon: Int
            get() = UniversalDrawable.ic_organization
    }

    /**
     * Represents a collection assignment.
     */
    @Parcelize
    data class Collection(
        override val name: String,
        @get:DrawableRes override val icon: Int = UniversalDrawable.ic_collections,
    ) : VaultItemLocation()

    /**
     * Represents a folder assignment.
     */
    @Parcelize
    data class Folder(
        override val name: String,
    ) : VaultItemLocation() {
        override val icon: Int
            get() = UniversalDrawable.ic_folder
    }
}
