package com.x8bit.universalpasswordmanager.ui.vault.feature.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.model.VaultFilterData
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.model.VaultFilterType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultFilterDataExtensionsTest {
    @Test
    fun `toAppBarTitle for a null value should return My Vault`() {
        assertEquals(
            Universal Password ManagerString.my_vault.asText(),
            (null as VaultFilterData?).toAppBarTitle(),
        )
    }

    @Test
    fun `toAppBarTitle for a non-null value should return Vaults`() {
        assertEquals(
            Universal Password ManagerString.vaults.asText(),
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
