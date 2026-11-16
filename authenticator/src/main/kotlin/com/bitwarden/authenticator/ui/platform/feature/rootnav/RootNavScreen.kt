package .authenticator.ui.platform.feature.rootnav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import .authenticator.ui.auth.unlock.unlockDestination
import .authenticator.ui.authenticator.feature.authenticator.authenticatorGraph
import .authenticator.ui.platform.components.biometrics.BiometricChanges
import .authenticator.ui.platform.feature.splash.splashDestination
import .authenticator.ui.platform.feature.tutorial.tutorialDestination
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController

/**
 * Controls the root level [NavDisplay] for the app.
 *
 * The root back stack is driven entirely by the [RootNavState]: whenever the state changes, the
 * entire back stack is replaced with the back stack for the new state. Each route in the
 * [RootNavGraph] represents a top-level section of the app, which may host its own nested
 * navigation graph.
 */
@Composable
fun RootNavScreen(
    viewModel: RootNavViewModel = hiltViewModel(),
    navigationController: NavigationController<RootNavGraph> = rememberNavigationController(
        routes = arrayOf(RootNavGraph.SplashRoute),
    ),
    entryProvider: (RootNavGraph) -> NavEntry<RootNavGraph> = entryProvider(viewModel = viewModel),
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    onSplashScreenRemoved: () -> Unit = {},
) {
    val state by viewModel.stateFlow.collectAsState()

    val isNotSplashScreen = state.navState !is RootNavState.NavState.Splash
    LaunchedEffect(isNotSplashScreen) {
        if (isNotSplashScreen) {
            onSplashScreenRemoved()
        }
    }

    LaunchedEffect(key1 = navigationController) {
        navigationController.backStackSnapshot.collect {
            viewModel.trySendAction(RootNavAction.BackStackUpdate)
        }
    }

    BiometricChanges(
        biometricsManager = biometricsManager,
        onBiometricSupportChange = {
            viewModel.trySendAction(RootNavAction.BiometricSupportChanged(it))
        },
    )

    Universal Password ManagerNavDisplay(
        navigationController = navigationController,
        entryProvider = entryProvider,
    )

    // When the state changes, replace the entire root back stack with the back stack for the new
    // state.
    navigationController.replaceAll(routes = state.navState.toBackStack())
}

/**
 * Returns the root back stack that should be displayed for the given [RootNavState.NavState].
 */
private fun RootNavState.NavState.toBackStack(): List<RootNavGraph> = when (this) {
    RootNavState.NavState.Splash -> listOf(RootNavGraph.SplashRoute)
    RootNavState.NavState.Tutorial -> listOf(RootNavGraph.TutorialRoute)
    RootNavState.NavState.Locked -> listOf(RootNavGraph.UnlockRoute)
    RootNavState.NavState.Unlocked -> listOf(RootNavGraph.AuthenticatorRoute)
}

private fun entryProvider(
    viewModel: RootNavViewModel,
): (RootNavGraph) -> NavEntry<RootNavGraph> = entryProvider {
    splashDestination()
    tutorialDestination()
    unlockDestination()
    authenticatorGraph(
        onBackStackUpdate = { viewModel.trySendAction(RootNavAction.BackStackUpdate) },
    )
}
