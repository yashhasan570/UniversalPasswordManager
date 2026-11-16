package .authenticator.ui.authenticator.feature.qrcodescan

import androidx.navigation3.runtime.EntryProviderScope
import .authenticator.ui.authenticator.feature.authenticator.AuthenticatorGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry

/**
 * Add the QR code scan screen to the navigation entries.
 */
fun EntryProviderScope<AuthenticatorGraph>.qrCodeScanDestination(
    onNavigateBack: () -> Unit,
    onNavigateToManualCodeEntryScreen: () -> Unit,
) {
    slideUpEntry<AuthenticatorGraph.QrCodeScanRoute> {
        QrCodeScanScreen(
            onNavigateToManualCodeEntryScreen = onNavigateToManualCodeEntryScreen,
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the QR code scan screen.
 */
fun NavigationController<AuthenticatorGraph>.navigateToQrCodeScanScreen() {
    this.navigate(route = AuthenticatorGraph.QrCodeScanRoute)
}
