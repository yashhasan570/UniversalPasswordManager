package com.universalpasswordmanager.ui.vault.feature.vault.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.feature.vault.model.VaultFilterData
import com.universalpasswordmanager.ui.vault.feature.vault.model.VaultFilterType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultFilterDataExtensionsTest {
    @Test
    fun `toAppBarTitle for a null value should return My Vault`() {
        assertEquals(
            UniversalString.my_vault.asText(),
            (null as VaultFilterData?).toAppBarTitle(),
        )
    }

    @Test
    fun `toAppBarTitle for a non-null value should return Vaults`() {
        assertEquals(
            UniversalString.vaults.asText(),
            VaultFilterData(
                selectedVaultFilterType = VaultFilterType.MyVault,
                vaultFilterTypes = listOf(
                    VaultFilterType.AllVaults,
                    VaultFilterType.MyVault,
                ),
            )
                .toAppBarTitle(),
        )
    }
}
