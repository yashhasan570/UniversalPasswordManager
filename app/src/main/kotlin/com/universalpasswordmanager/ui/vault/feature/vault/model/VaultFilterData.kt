package com.universalpasswordmanager.ui.vault.feature.vault.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a currently [selectedVaultFilterType] and a list of possible [vaultFilterTypes].
 */
@Parcelize
data class VaultFilterData(
    val selectedVaultFilterType: VaultFilterType,
    val vaultFilterTypes: List<VaultFilterType>,
) : Parcelable
