package com.universalpasswordmanager

import android.app.ComponentCaller
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.auth.AuthTabIntent
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppLanguage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.components.UniversalNavDisplay
import com.universalpasswordmanager.ui.platform.navigation.util.rememberNavigationController
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.util.setHorizonOSAppLayout
import com.universalpasswordmanager.ui.platform.util.setupEdgeToEdge
import com.universalpasswordmanager.ui.platform.util.validate
import com.universalpasswordmanager.data.autofill.accessibility.manager.AccessibilityCompletionManager
import com.universalpasswordmanager.data.autofill.manager.AutofillActivityManager
import com.universalpasswordmanager.data.autofill.manager.AutofillCompletionManager
import com.universalpasswordmanager.data.platform.manager.util.ObserveScreenDataEffect
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.universalpasswordmanager.ui.platform.composition.LocalManagerProvider
import com.universalpasswordmanager.ui.platform.feature.debugmenu.debugMenuDestination
import com.universalpasswordmanager.ui.platform.feature.debugmenu.manager.DebugMenuLaunchManager
import com.universalpasswordmanager.ui.platform.feature.debugmenu.navigateToDebugMenuScreen
import com.universalpasswordmanager.ui.platform.feature.main.MainGraph
import com.universalpasswordmanager.ui.platform.feature.overlaynav.overlayNavDestination
import com.universalpasswordmanager.ui.platform.model.AuthTabLaunchers
import com.universalpasswordmanager.ui.platform.util.appLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Primary entry point for the application.
 */
@Suppress("TooManyFunctions")
@OmitFromCoverage
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var autofillActivityManager: AutofillActivityManager

    @Inject
    lateinit var autofillCompletionManager: AutofillCompletionManager

    @Inject
    lateinit var accessibilityCompletionManager: AccessibilityCompletionManager

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var debugLaunchManager: DebugMenuLaunchManager

    private val duoLauncher = AuthTabIntent.registerActivityResultLauncher(this) {
        mainViewModel.trySendAction(MainAction.DuoResult(it))
    }
    private val ssoLauncher = AuthTabIntent.registerActivityResultLauncher(this) {
        mainViewModel.trySendAction(MainAction.SsoResult(it))
    }
    private val webAuthnLauncher = AuthTabIntent.registerActivityResultLauncher(this) {
        mainViewModel.trySendAction(MainAction.WebAuthnResult(it))
    }

    private val cookieLauncher = AuthTabIntent.registerActivityResultLauncher(this) {
        mainViewModel.trySendAction(MainAction.CookieAcquisitionResult(it))
    }

    private val premiumCheckoutLauncher = AuthTabIntent.registerActivityResultLauncher(this) {
        mainViewModel.trySendAction(MainAction.PremiumCheckoutResult(it))
    }

    private val stripePortalLauncher = AuthTabIntent.registerActivityResultLauncher(this) {
        mainViewModel.trySendAction(MainAction.StripePortalResult(it))
    }

    private val authTabLaunchers by lazy {
        AuthTabLaunchers(
            duo = duoLauncher,
            sso = ssoLauncher,
            webAuthn = webAuthnLauncher,
            cookie = cookieLauncher,
            premiumCheckout = premiumCheckoutLauncher,
            stripePortal = stripePortalLauncher,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        intent = intent.validate()
        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition { shouldShowSplashScreen }
        super.onCreate(savedInstanceState)
        window.decorView.filterTouchesWhenObscured = true
        if (savedInstanceState == null) {
            mainViewModel.trySendAction(MainAction.ReceiveFirstIntent(intent = intent))
        }

        // Within the app the theme will change dynamically and will be managed by the
        // OS, but we need to ensure we properly set the values when upgrading from older versions
        // that handle this differently or when the activity restarts.
        AppCompatDelegate.setDefaultNightMode(settingsRepository.appTheme.osValue)
        setupEdgeToEdge(appThemeFlow = mainViewModel.stateFlow.map { it.theme })
        setContent {
            val navigationController = rememberNavigationController<MainGraph>(
                routes = arrayOf(MainGraph.OverlayNavRoute),
            )
            SetupEventsEffect(navigationController = navigationController)
            val state by mainViewModel.stateFlow.collectAsStateWithLifecycle()
            updateScreenCapture(isScreenCaptureAllowed = state.isScreenCaptureAllowed)
            MainActivityContent(
                state = state,
                authTabLaunchers = authTabLaunchers,
                navigationController = navigationController,
                sendAction = mainViewModel::trySendAction,
                onSplashScreenRemoved = { shouldShowSplashScreen = false },
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        val newIntent = intent.validate()
        super.onNewIntent(newIntent)
        mainViewModel.trySendAction(action = MainAction.ReceiveNewIntent(intent = newIntent))
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        val newIntent = intent.validate()
        super.onNewIntent(newIntent, caller)
        mainViewModel.trySendAction(action = MainAction.ReceiveNewIntent(intent = newIntent))
    }

    override fun onResume() {
        super.onResume()
        // When the app resumes check for any app specific language which may have been
        // set via the device settings. Similar to the theme setting in onCreate this
        // ensures we properly set the values when upgrading from older versions
        // that handle this differently or when the activity restarts.
        val appSpecificLanguage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val locales: LocaleListCompat = AppCompatDelegate.getApplicationLocales()
            if (locales.isEmpty) {
                // App is using the system language
                null
            } else {
                // App has specific language settings
                locales.get(0)?.appLanguage
            }
        } else {
            // For older versions, use whatever language is available from the repository.
            settingsRepository.appLanguage
        }

        mainViewModel.trySendAction(
            action = MainAction.AppSpecificLanguageUpdate(
                appLanguage = appSpecificLanguage ?: AppLanguage.DEFAULT,
            ),
        )
    }

    override fun onStop() {
        super.onStop()
        // In some scenarios on an emulator the Activity can leak when recreated
        // if we don't first clear focus anytime we exit and return to the app.
        currentFocus?.clearFocus()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean = debugLaunchManager
        .actionOnInputEvent(event = event, action = ::sendOpenDebugMenuEvent)
        .takeIf { it }
        ?: super.dispatchTouchEvent(event)

    override fun dispatchKeyEvent(event: KeyEvent): Boolean = debugLaunchManager
        .actionOnInputEvent(event = event, action = ::sendOpenDebugMenuEvent)
        .takeIf { it }
        ?: super.dispatchKeyEvent(event)

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // resize only one time at the start
        if (!mainViewModel.stateFlow.value.hasResizeBeenRequested) {
            setHorizonOSAppLayout {
                mainViewModel.trySendAction(MainAction.Internal.ResizeHasBeenRequested)
            }
        }
    }

    @Composable
    private fun SetupEventsEffect(navigationController: NavigationController<MainGraph>) {
        EventsEffect(viewModel = mainViewModel) { event ->
            when (event) {
                is MainEvent.CompleteAccessibilityAutofill -> {
                    handleCompleteAccessibilityAutofill(event)
                }

                is MainEvent.CompleteAutofill -> handleCompleteAutofill(event)
                MainEvent.Recreate -> handleRecreate()
                MainEvent.NavigateToDebugMenu -> navigationController.navigateToDebugMenuScreen()
                is MainEvent.UpdateAppLocale -> {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(event.localeName),
                    )
                }

                is MainEvent.UpdateAppTheme -> AppCompatDelegate.setDefaultNightMode(event.osTheme)
            }
        }
    }

    private fun sendOpenDebugMenuEvent() {
        mainViewModel.trySendAction(MainAction.OpenDebugMenu)
    }

    private fun handleCompleteAccessibilityAutofill(
        event: MainEvent.CompleteAccessibilityAutofill,
    ) {
        accessibilityCompletionManager.completeAccessibilityAutofill(
            activity = this,
            cipherView = event.cipherView,
        )
    }

    private fun handleCompleteAutofill(event: MainEvent.CompleteAutofill) {
        autofillCompletionManager.completeAutofill(
            activity = this,
            cipherView = event.cipherView,
        )
    }

    private fun handleRecreate() {
        ActivityCompat.recreate(this)
    }

    private fun updateScreenCapture(isScreenCaptureAllowed: Boolean) {
        if (isScreenCaptureAllowed) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

@OmitFromCoverage
@Composable
private fun MainActivityContent(
    state: MainState,
    authTabLaunchers: AuthTabLaunchers,
    navigationController: NavigationController<MainGraph>,
    sendAction: (MainAction) -> Unit,
    onSplashScreenRemoved: () -> Unit,
) {
    LocalManagerProvider(
        featureFlagsState = state.featureFlagsState,
        authTabLaunchers = authTabLaunchers,
    ) {
        ObserveScreenDataEffect { sendAction(MainAction.ResumeScreenDataReceived(it)) }
        UniversalTheme(
            theme = state.theme,
            dynamicColor = state.isDynamicColorsEnabled,
        ) {
            UniversalNavDisplay(
                modifier = Modifier.background(UniversalTheme.colorScheme.background.primary),
                navigationController = navigationController,
                entryProvider = entryProvider {
                    // The OverlayNav and Debug destinations are the only UIs that can be
                    // displayed here, everything else should be inside the OverlayNav.
                    overlayNavDestination(onSplashScreenRemoved = onSplashScreenRemoved)
                    debugMenuDestination(
                        onNavigateBack = { navigationController.navigateBack() },
                        onSplashScreenRemoved = onSplashScreenRemoved,
                    )
                },
            )
        }
    }
}
