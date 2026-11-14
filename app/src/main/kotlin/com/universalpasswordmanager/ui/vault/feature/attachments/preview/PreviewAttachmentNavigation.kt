package com.universalpasswordmanager.ui.vault.feature.attachments.preview

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the preview attachment screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.previewAttachmentDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.PreviewAttachmentRoute> { route ->
        PreviewAttachmentScreen(
            viewModel = hiltViewModel(
                creationCallback = { factory: PreviewAttachmentViewModel.Factory ->
                    factory.create(route = route)
                },
            ),
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the preview attachment screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToPreviewAttachment(
    route: VaultUnlockedGraph.PreviewAttachmentRoute,
) {
    this.navigate(route = route)
}
