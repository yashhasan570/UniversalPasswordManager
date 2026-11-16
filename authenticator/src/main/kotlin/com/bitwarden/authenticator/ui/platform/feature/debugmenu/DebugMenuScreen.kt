package .authenticator.ui.platform.feature.debugmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .core.data.manager.model.FlagKey
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.debug.ListItemContent
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Top level screen for the debug menu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun DebugMenuScreen(
    onNavigateBack: () -> Unit,
    viewModel: DebugMenuViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            DebugMenuEvent.NavigateBack -> onNavigateBack()
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(Universal Password ManagerString.debug_menu),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                    onNavigationIconClick = {
                        viewModel.trySendAction(DebugMenuAction.NavigateBack)
                    },
                ),
            )
        },
    ) {
        if (state.featureFlags.isEmpty()) {
            Universal Password ManagerErrorContent(
                message = stringResource(id = Universal Password ManagerString.empty_item_list),
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            FeatureFlagContent(
                featureFlagMap = state.featureFlags,
                onValueChange = { key, value ->
                    viewModel.trySendAction(DebugMenuAction.UpdateFeatureFlag(key, value))
                },
                onResetValues = { viewModel.trySendAction(DebugMenuAction.ResetFeatureFlagValues) },
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Composable
private fun FeatureFlagContent(
    featureFlagMap: Map<FlagKey<Any>, Any>,
    onValueChange: (key: FlagKey<Any>, value: Any) -> Unit,
    onResetValues: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(Universal Password ManagerString.feature_flags),
            modifier = Modifier
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        featureFlagMap.onEach { featureFlag ->
            featureFlag.key.ListItemContent(
                currentValue = featureFlag.value,
                onValueChange = onValueChange,
                cardStyle = featureFlagMap.keys.toListItemCardStyle(
                    index = featureFlagMap.keys.indexOf(element = featureFlag.key),
                ),
                modifier = Modifier.standardHorizontalMargin(),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(Universal Password ManagerString.reset_values),
            onClick = onResetValues,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureFlagContent_preview() {
    Universal Password ManagerTheme {
        FeatureFlagContent(
            featureFlagMap = mapOf(
                FlagKey.Universal Password ManagerAuthenticationEnabled to true,
            ),
            onValueChange = { _, _ -> },
            onResetValues = { },
        )
    }
}
