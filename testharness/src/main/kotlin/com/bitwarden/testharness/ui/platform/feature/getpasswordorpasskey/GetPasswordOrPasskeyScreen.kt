package .testharness.ui.platform.feature.getpasswordorpasskey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .testharness.R
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString

/**
 * Get Password or Passkey test screen.
 *
 * This screen tests the combined credential retrieval use case where both GetPasswordOption
 * and GetPublicKeyCredentialOption are included in a single GetCredentialRequest. The system
 * credential picker will show both passwords and passkeys, allowing the user to select either
 * credential type.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetPasswordOrPasskeyScreen(
    onNavigateBack: () -> Unit,
    viewModel: GetPasswordOrPasskeyViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            GetPasswordOrPasskeyEvent.NavigateBack -> onNavigateBack()
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Universal Password ManagerScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = R.string.get_password_or_passkey_title),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(Universal Password ManagerString.back),
                    onNavigationIconClick = {
                        viewModel.trySendAction(GetPasswordOrPasskeyAction.BackClick)
                    },
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(state = rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Universal Password ManagerTextField(
                label = stringResource(R.string.relying_party_id),
                value = state.rpId,
                onValueChange = {
                    viewModel.trySendAction(GetPasswordOrPasskeyAction.RpIdChanged(it))
                },
                placeholder = stringResource(R.string.rp_id_hint),
                cardStyle = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("GetPasswordOrPasskeyRelyingPartyIdField"),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Universal Password ManagerTextField(
                label = stringResource(R.string.origin_optional),
                value = state.origin,
                onValueChange = {
                    viewModel.trySendAction(GetPasswordOrPasskeyAction.OriginChanged(it))
                },
                placeholder = stringResource(R.string.origin_hint),
                cardStyle = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("GetPasswordOrPasskeyOriginField"),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Universal Password ManagerFilledButton(
                label = stringResource(R.string.execute),
                onClick = { viewModel.trySendAction(GetPasswordOrPasskeyAction.ExecuteClick) },
                isEnabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("GetPasswordOrPasskeyExecuteButton"),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Universal Password ManagerTextButton(
                label = stringResource(R.string.clear),
                onClick = { viewModel.trySendAction(GetPasswordOrPasskeyAction.ClearResultClick) },
                isEnabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("GetPasswordOrPasskeyClearButton"),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Universal Password ManagerTextField(
                label = stringResource(R.string.result),
                value = state.resultText,
                onValueChange = { },
                cardStyle = null,
                readOnly = true,
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("GetPasswordOrPasskeyResultField"),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
