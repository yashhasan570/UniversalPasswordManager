package .authenticator

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import .authenticator.data.platform.repository.SettingsRepository
import .authenticator.ui.main.MainGraph
import .authenticator.ui.platform.composition.LocalManagerProvider
import .authenticator.ui.platform.feature.debugmenu.debugMenuDestination
import .authenticator.ui.platform.feature.debugmenu.manager.DebugMenuLaunchManager
import .authenticator.ui.platform.feature.debugmenu.navigateToDebugMenuScreen
import .authenticator.ui.platform.feature.rootnav.rootNavDestination
import .ui.platform.base.util.EventsEffect
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.util.setupEdgeToEdge
import .ui.platform.util.validate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Primary entry point for the application.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var debugLaunchManager: DebugMenuLaunchManager

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        intent = intent.validate()
        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition { shouldShowSplashScreen }
        super.onCreate(savedInstanceState)
        window.decorView.filterTouchesWhenObscured = true
        if (savedInstanceState == null) {
            mainViewModel.trySendAction(
                MainAction.ReceiveFirstIntent(
                    intent = intent,
                ),
            )
        }

        AppCompatDelegate.setDefaultNightMode(settingsRepository.appTheme.osValue)
        setupEdgeToEdge(appThemeFlow = mainViewModel.stateFlow.map { it.theme })
        setContent {
            val navigationController = rememberNavigationController<MainGraph>(
                routes = arrayOf(MainGraph.RootNavigationRoute),
            )
            SetupEventsEffect(navigationController = navigationController)
            val state by mainViewModel.stateFlow.collectAsStateWithLifecycle()
            updateScreenCapture(isScreenCaptureAllowed = state.isScreenCaptureAllowed)
            LocalManagerProvider {
                Universal Password ManagerTheme(
                    theme = state.theme,
                    dynamicColor = state.isDynamicColorsEnabled,
                ) {
                    Universal Password ManagerNavDisplay(
                        navigationController = navigationController,
                        entryProvider = entryProvider {
                            // Both root navigation and debug menu exist at this top level.
                            // The debug menu can appear on top of the rest of the app without
                            // interacting with the state-based navigation used by RootNavScreen.
                            rootNavDestination(
                                onSplashScreenRemoved = { shouldShowSplashScreen = false },
                            )
                            debugMenuDestination(
                                onNavigateBack = { navigationController.navigateBack() },
                                onSplashScreenRemoved = { shouldShowSplashScreen = false },
                            )
                        },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        val newIntent = intent.validate()
        super.onNewIntent(newIntent)
        mainViewModel.trySendAction(MainAction.ReceiveNewIntent(intent = newIntent))
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        val newIntent = intent.validate()
        super.onNewIntent(newIntent, caller)
        mainViewModel.trySendAction(MainAction.ReceiveNewIntent(intent = newIntent))
    }

    @Composable
    private fun SetupEventsEffect(navigationController: NavigationController<MainGraph>) {
        EventsEffect(viewModel = mainViewModel) { event ->
            when (event) {
                MainEvent.NavigateToDebugMenu -> navigationController.navigateToDebugMenuScreen()
                is MainEvent.UpdateAppTheme -> {
                    AppCompatDelegate.setDefaultNightMode(event.osTheme)
                }
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean = debugLaunchManager
        .actionOnInputEvent(event = event, action = ::sendOpenDebugMenuEvent)
        .takeIf { it }
        ?: super.dispatchTouchEvent(event)

    override fun dispatchKeyEvent(event: KeyEvent): Boolean = debugLaunchManager
        .actionOnInputEvent(event = event, action = ::sendOpenDebugMenuEvent)
        .takeIf { it }
        ?: super.dispatchKeyEvent(event)

    private fun sendOpenDebugMenuEvent() {
        mainViewModel.trySendAction(MainAction.OpenDebugMenu)
    }

    private fun updateScreenCapture(isScreenCaptureAllowed: Boolean) {
        if (isScreenCaptureAllowed) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
