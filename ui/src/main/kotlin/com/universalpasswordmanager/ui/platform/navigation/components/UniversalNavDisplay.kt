package com.universalpasswordmanager.ui.platform.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.rememberUniversalNavEntryDecorators
import com.universalpasswordmanager.ui.platform.theme.ContentTransformProvider
import com.universalpasswordmanager.ui.platform.theme.NavDisplayTransitions
import com.universalpasswordmanager.ui.platform.theme.PredictiveContentTransformProvider

/**
 * A [NavDisplay] that displays the back stack of the given [navigationController], which is
 * popped whenever the user navigates back.
 */
@Composable
fun <T : NavKey> UniversalNavDisplay(
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
        entryDecorators = rememberUniversalNavEntryDecorators(),
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider,
    )
}
