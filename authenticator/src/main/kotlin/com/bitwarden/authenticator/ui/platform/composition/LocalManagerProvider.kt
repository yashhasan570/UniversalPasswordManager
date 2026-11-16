@file:OmitFromCoverage

package .authenticator.ui.platform.composition

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import .annotation.OmitFromCoverage
import .authenticator.ui.platform.manager.AuthenticatorBuildInfoManagerImpl
import .authenticator.ui.platform.manager.permissions.PermissionsManager
import .authenticator.ui.platform.manager.permissions.PermissionsManagerImpl
import .core.data.manager.BuildInfoManager
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.composition.LocalExitManager
import .ui.platform.composition.LocalIntentManager
import .ui.platform.composition.LocalQrCodeAnalyzer
import .ui.platform.feature.qrcodescan.util.QrCodeAnalyzer
import .ui.platform.feature.qrcodescan.util.QrCodeAnalyzerImpl
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.manager.biometrics.model.BiometricsConfig
import .ui.platform.manager.exit.ExitManager
import .ui.platform.manager.exit.ExitManagerImpl
import .ui.platform.resource.Universal Password ManagerString
import java.time.Clock

/**
 * Helper [Composable] that wraps a [content] and provides manager classes via [CompositionLocal].
 */
@Composable
fun LocalManagerProvider(
    activity: Activity = requireNotNull(LocalActivity.current),
    permissionsManager: PermissionsManager = PermissionsManagerImpl(activity),
    clock: Clock = Clock.systemDefaultZone(),
    buildInfoManager: BuildInfoManager = AuthenticatorBuildInfoManagerImpl(),
    intentManager: IntentManager = IntentManager.create(activity, clock, buildInfoManager),
    exitManager: ExitManager = ExitManagerImpl(activity),
    biometricsManager: BiometricsManager = BiometricsManager.create(
        activity = activity,
        config = BiometricsConfig(
            promptTitleRes = Universal Password ManagerString.universalpasswordmanager_authenticator,
            biometricsPromptDescriptionRes = Universal Password ManagerString.device_verification,
            isDeviceCredentialAllowedForBiometrics = true,
        ),
    ),
    qrCodeAnalyzer: QrCodeAnalyzer = QrCodeAnalyzerImpl(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPermissionsManager provides permissionsManager,
        LocalIntentManager provides intentManager,
        LocalExitManager provides exitManager,
        LocalBiometricsManager provides biometricsManager,
        LocalQrCodeAnalyzer provides qrCodeAnalyzer,
        content = content,
    )
}

/**
 * Provides access to the permission manager throughout the app.
 */
val LocalPermissionsManager: ProvidableCompositionLocal<PermissionsManager> = compositionLocalOf {
    error("CompositionLocal LocalPermissionsManager not present")
}
