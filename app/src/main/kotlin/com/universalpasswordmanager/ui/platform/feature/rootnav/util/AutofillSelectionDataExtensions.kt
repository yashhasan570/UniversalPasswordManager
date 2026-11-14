package com.universalpasswordmanager.ui.platform.feature.rootnav.util

import com.universalpasswordmanager.data.autofill.model.AutofillSelectionData
import com.universalpasswordmanager.ui.vault.model.VaultItemListingType

/**
 * Derives a [VaultItemListingType] from the given [AutofillSelectionData.Type].
 */
fun AutofillSelectionData.Type.toVaultItemListingType(): VaultItemListingType =
    when (this) {
        AutofillSelectionData.Type.CARD -> VaultItemListingType.Card
        AutofillSelectionData.Type.LOGIN -> VaultItemListingType.Login
        AutofillSelectionData.Type.IDENTITY -> VaultItemListingType.Identity
    }
