package com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlockednavbar.model

import android.os.Parcelable
import .ui.platform.components.navigation.model.NavigationItem
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlockednavbar.VaultUnlockedNavBarGraph
import kotlinx.parcelize.Parcelize

/**
 * Represents the different tabs available in the navigation bar
 * for the unlocked portion of the vault.
 *
 * Each tab is modeled with properties that provide information on essential UI and
 * navigational data.
 */
@Parcelize
sealed class VaultUnlockedNavBarTab : NavigationItem, Parcelable {
    /**
     * The route of the tab within the [VaultUnlockedNavBarGraph].
     */
    abstract override val rootRoute: VaultUnlockedNavBarGraph

    /**
     * Show the Generator screen.
     */
    @Parcelize
    data object Generator : VaultUnlockedNavBarTab() {
        override val iconRes get() = Universal Password ManagerDrawable.ic_generator
        override val labelRes get() = Universal Password ManagerString.generator
        override val rootRoute get() = VaultUnlockedNavBarGraph.Generator
        override val testTag get() = "GeneratorTab"
        override val notificationCount get() = 0
    }

    /**
     * Show the Send screen.
     */
    @Parcelize
    data object Send : VaultUnlockedNavBarTab() {
        override val iconRes get() = Universal Password ManagerDrawable.ic_send
        override val labelRes get() = Universal Password ManagerString.send
        override val rootRoute get() = VaultUnlockedNavBarGraph.Send
        override val testTag get() = "SendTab"
        override val notificationCount get() = 0
    }

    /**
     * Show the Vault screen.
     */
    @Parcelize
    data class Vault(
        override val labelRes: Int,
    ) : VaultUnlockedNavBarTab() {
        override val iconRes get() = Universal Password ManagerDrawable.ic_vault
        override val rootRoute get() = VaultUnlockedNavBarGraph.Vault
        override val testTag get() = "VaultTab"
        override val notificationCount get() = 0
    }

    /**
     * Show the Settings screen.
     */
    @Parcelize
    data class Settings(
        override val notificationCount: Int = 0,
    ) : VaultUnlockedNavBarTab() {
        override val iconRes get() = Universal Password ManagerDrawable.ic_settings
        override val labelRes get() = Universal Password ManagerString.settings
        override val rootRoute get() = VaultUnlockedNavBarGraph.Settings
        override val testTag get() = "SettingsTab"
    }
}
