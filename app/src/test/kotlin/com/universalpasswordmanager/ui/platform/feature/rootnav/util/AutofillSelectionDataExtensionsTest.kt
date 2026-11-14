package com.universalpasswordmanager.ui.platform.feature.rootnav.util

import com.universalpasswordmanager.data.autofill.model.AutofillSelectionData
import com.universalpasswordmanager.ui.vault.model.VaultItemListingType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AutofillSelectionDataExtensionsTest {
    @Test
    fun `toVaultItemListingType should return the correct result for each type`() {
        mapOf(
            AutofillSelectionData.Type.CARD to VaultItemListingType.Card,
            AutofillSelectionData.Type.LOGIN to VaultItemListingType.Login,
            AutofillSelectionData.Type.IDENTITY to VaultItemListingType.Identity,
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.toVaultItemListingType(),
                )
            }
    }
}
