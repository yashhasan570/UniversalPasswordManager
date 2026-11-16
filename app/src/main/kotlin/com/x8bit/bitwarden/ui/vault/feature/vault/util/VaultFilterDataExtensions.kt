package com.x8bit.universalpasswordmanager.ui.vault.feature.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.model.VaultFilterData

/**
 * Derives an app bar title given the [VaultFilterData].
 */
fun VaultFilterData?.toAppBarTitle(): Text =
    if (this != null) {
        Universal Password ManagerString.vaults
    } else {
        Universal Password ManagerString.my_vault
    }
        .asText()
