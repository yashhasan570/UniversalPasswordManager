package .authenticator.ui.platform.base

import androidx.compose.runtime.Composable
import .authenticator.ui.platform.composition.LocalManagerProvider
import .authenticator.ui.platform.manager.permissions.PermissionsManager
import .ui.platform.base.BaseComposeTest
import .ui.platform.feature.qrcodescan.util.QrCodeAnalyzer
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.manager.exit.ExitManager
import .ui.platform.theme.Universal Password ManagerTheme
import io.mockk.mockk

/**
 * A base class that can be used for performing Compose-layer testing using Robolectric, Compose
 * Testing, and JUnit 4.
 */
abstract class AuthenticatorComposeTest : BaseComposeTest() {

    /**
     * Helper for testing a basic Composable function that only requires a Composable environment
     * with the [Universal Password ManagerTheme].
     */
    @Suppress("LongParameterList")
    protected fun setContent(
        theme: AppTheme = AppTheme.DEFAULT,
        permissionsManager: PermissionsManager = mockk(),
        intentManager: IntentManager = mockk(),
        exitManager: ExitManager = mockk(),
        biometricsManager: BiometricsManager = mockk(),
        qrCodeAnalyzer: QrCodeAnalyzer = mockk(),
        test: @Composable () -> Unit,
    ) {
        setTestContent {
            Universal Password ManagerTheme(theme = theme) {
                LocalManagerProvider(
                    permissionsManager = permissionsManager,
                    intentManager = intentManager,
                    exitManager = exitManager,
                    biometricsManager = biometricsManager,
                    qrCodeAnalyzer = qrCodeAnalyzer,
                    content = test,
                )
            }
        }
    }
}
