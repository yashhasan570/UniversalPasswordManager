package com.universalpasswordmanager.ui.platform.feature.settings.flightrecorder.recordedLogs

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.settings.PreAuthSettingsGraph
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the recorded logs screen to the vault unlocked navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.recordedLogsDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.RecordedLogsRoute> {
        RecordedLogsScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Add the recorded logs screen to the pre-authentication settings navigation entries.
 */
fun EntryProviderScope<PreAuthSettingsGraph>.preAuthRecordedLogsDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<PreAuthSettingsGraph.RecordedLogsRoute> {
        RecordedLogsScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Navigate to the recorded logs screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToRecordedLogs() {
    this.navigate(route = VaultUnlockedGraph.RecordedLogsRoute)
}

/**
 * Navigate to the pre-authentication recorded logs screen.
 */
fun NavigationController<PreAuthSettingsGraph>.navigateToPreAuthRecordedLogs() {
    this.navigate(route = PreAuthSettingsGraph.RecordedLogsRoute)
}
