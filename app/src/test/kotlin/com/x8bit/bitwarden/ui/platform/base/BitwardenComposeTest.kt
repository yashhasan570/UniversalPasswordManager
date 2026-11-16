package com.x8bit.universalpasswordmanager.ui.platform.base

import androidx.compose.runtime.Composable
import .cxf.importer.CredentialExchangeImporter
import .cxf.manager.CredentialExchangeCompletionManager
import .cxf.validator.CredentialExchangeRequestValidator
import .ui.platform.base.BaseComposeTest
import .ui.platform.feature.cardscanner.util.CardTextAnalyzer
import .ui.platform.feature.qrcodescan.util.QrCodeAnalyzer
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.manager.exit.ExitManager
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.data.platform.manager.util.AppResumeStateManager
import com.x8bit.universalpasswordmanager.ui.credentials.manager.CredentialProviderCompletionManager
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalManagerProvider
import com.x8bit.universalpasswordmanager.ui.platform.manager.keychain.KeyChainManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.nfc.NfcManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.review.AppReviewManager
import com.x8bit.universalpasswordmanager.ui.platform.model.AuthTabLaunchers
import com.x8bit.universalpasswordmanager.ui.platform.model.FeatureFlagsState
import io.mockk.mockk
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

abstract class Universal Password ManagerComposeTest : BaseComposeTest() {

    /**
     * Helper for testing a basic Composable function that only requires a [Composable]. The
     * [AppTheme] is overridable and the [backDispatcher] is configured automatically.
     */
    @Suppress("LongParameterList")
    protected fun setContent(
        theme: AppTheme = AppTheme.DEFAULT,
        featureFlagsState: FeatureFlagsState = FeatureFlagsState,
        authTabLaunchers: AuthTabLaunchers = mockk(),
        appResumeStateManager: AppResumeStateManager = mockk(),
        appReviewManager: AppReviewManager = mockk(),
        biometricsManager: BiometricsManager = mockk(),
        clock: Clock = Clock.fixed(Instant.parse("2023-10-27T12:00:00Z"), ZoneOffset.UTC),
        exitManager: ExitManager = mockk(),
        intentManager: IntentManager = mockk(),
        credentialProviderCompletionManager: CredentialProviderCompletionManager = mockk(),
        keyChainManager: KeyChainManager = mockk(),
        nfcManager: NfcManager = mockk(),
        permissionsManager: PermissionsManager = mockk(),
        credentialExchangeImporter: CredentialExchangeImporter = mockk(),
        credentialExchangeCompletionManager: CredentialExchangeCompletionManager = mockk(),
        credentialExchangeRequestValidator: CredentialExchangeRequestValidator = mockk(),
        cardTextAnalyzer: CardTextAnalyzer = mockk(),
        qrCodeAnalyzer: QrCodeAnalyzer = mockk(),
        test: @Composable () -> Unit,
    ) {
        setTestContent {
            LocalManagerProvider(
                featureFlagsState = featureFlagsState,
                authTabLaunchers = authTabLaunchers,
                appResumeStateManager = appResumeStateManager,
                appReviewManager = appReviewManager,
                biometricsManager = biometricsManager,
                clock = clock,
                exitManager = exitManager,
                intentManager = intentManager,
                credentialProviderCompletionManager = credentialProviderCompletionManager,
                keyChainManager = keyChainManager,
                nfcManager = nfcManager,
                permissionsManager = permissionsManager,
                credentialExchangeImporter = credentialExchangeImporter,
                credentialExchangeCompletionManager = credentialExchangeCompletionManager,
                credentialExchangeRequestValidator = credentialExchangeRequestValidator,
                cardTextAnalyzer = cardTextAnalyzer,
                qrCodeAnalyzer = qrCodeAnalyzer,
            ) {
                Universal Password ManagerTheme(
                    theme = theme,
                    content = test,
                )
            }
        }
    }
}
