package .testharness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import .testharness.ui.platform.feature.main.MainGraph
import .testharness.ui.platform.feature.rootnav.RootNavScreen
import .testharness.ui.platform.feature.rootnav.rootNavDestination
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.util.setupEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

/**
 * Primary entry point for the Credential Manager test harness application.
 *
 * Delegates navigation to [RootNavScreen] following the same pattern as @app and @authenticator
 * modules. Handles Activity-level concerns like theme and splash screen.
 *
 * The root navigation is managed by [RootNavScreen] which orchestrates all test screen flows:
 * - Landing screen with test category selection (Autofill, Credential Manager)
 * - Individual test screens for each Credential Manager API operation
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition { shouldShowSplashScreen }
        super.onCreate(savedInstanceState)

        setupEdgeToEdge(appThemeFlow = mainViewModel.stateFlow.map { it.theme })

        setContent {
            val navigationController = rememberNavigationController<MainGraph>(
                routes = arrayOf(MainGraph.RootNavigationRoute),
            )
            val state by mainViewModel.stateFlow.collectAsStateWithLifecycle()

            Universal Password ManagerTheme(
                theme = state.theme,
            ) {
                Universal Password ManagerNavDisplay(
                    navigationController = navigationController,
                    entryProvider = entryProvider {
                        rootNavDestination { shouldShowSplashScreen = false }
                    },
                )
            }
        }
    }
}
