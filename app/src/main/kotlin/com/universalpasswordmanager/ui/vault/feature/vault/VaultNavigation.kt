@file:OmitFromCoverage

package com.universalpasswordmanager.ui.vault.feature.vault

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.util.rootPushEntry
import com.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.universalpasswordmanager.ui.vault.model.VaultItemListingType

/**
 * Add vault destination to the navigation entries.
 */
@Suppress("LongParameterList")
fun EntryProviderScope<VaultGraph>.vaultDestination(
    onNavigateToVaultAddItemScreen: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToVerificationCodeScreen: () -> Unit,
    onNavigateToVaultItemScreen: (route: VaultUnlockedGraph.VaultItemRoute) -> Unit,
    onNavigateToVaultEditItemScreen: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToVaultItemListingScreen: (vaultItemType: VaultItemListingType) -> Unit,
    onNavigateToSearchVault: (searchType: SearchType.Vault) -> Unit,
    onDimBottomNavBarRequest: (shouldDim: Boolean) -> Unit,
    onNavigateToImportLogins: () -> Unit,
    onNavigateToAddFolderScreen: (selectedFolderId: String?) -> Unit,
    onNavigateToAboutScreen: () -> Unit,
    onNavigateToAutofillScreen: () -> Unit,
    onNavigateToPlan: () -> Unit,
) {
    rootPushEntry<VaultGraph.VaultRoute> {
        VaultScreen(
            onNavigateToVaultAddItemScreen = onNavigateToVaultAddItemScreen,
            onNavigateToVaultItemScreen = onNavigateToVaultItemScreen,
            onNavigateToVaultEditItemScreen = onNavigateToVaultEditItemScreen,
            onNavigateToVaultItemListingScreen = onNavigateToVaultItemListingScreen,
            onNavigateToVerificationCodeScreen = onNavigateToVerificationCodeScreen,
            onNavigateToSearchVault = onNavigateToSearchVault,
            onDimBottomNavBarRequest = onDimBottomNavBarRequest,
            onNavigateToImportLogins = onNavigateToImportLogins,
            onNavigateToAddFolderScreen = onNavigateToAddFolderScreen,
            onNavigateToAboutScreen = onNavigateToAboutScreen,
            onNavigateToAutofillScreen = onNavigateToAutofillScreen,
            onNavigateToPlan = onNavigateToPlan,
        )
    }
}
