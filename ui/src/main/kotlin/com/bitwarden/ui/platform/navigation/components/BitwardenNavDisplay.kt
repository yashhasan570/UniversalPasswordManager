package .ui.platform.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.rememberUniversal Password ManagerNavEntryDecorators
import .ui.platform.theme.ContentTransformProvider
import .ui.platform.theme.NavDisplayTransitions
import .ui.platform.theme.PredictiveContentTransformProvider

/**
 * A [NavDisplay] that displays the back stack of the given [navigationController], which is
 * popped whenever the user navigates back.
 */
@Composable
fun <T : NavKey> Universal Password ManagerNavDisplay(
    navigationController: NavigationController<T>,
    entryProvider: (key: T) -> NavEntry<T>,
    modifier: Modifier = Modifier,
    transitionSpec: ContentTransformProvider<T> = NavDisplayTransitions.root(),
    popTransitionSpec: ContentTransformProvider<T> = transitionSpec,
    predictivePopTransitionSpec: PredictiveContentTransformProvider<T> =
        NavDisplayTransitions.predictive(provider = popTransitionSpec),
) {
    NavDisplay(
        backStack = navigationController.backStack,
        modifier = modifier,
        onBack = navigationController::navigateBack,
        entryDecorators = rememberUniversal Password ManagerNavEntryDecorators(),
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider,
    )
}
