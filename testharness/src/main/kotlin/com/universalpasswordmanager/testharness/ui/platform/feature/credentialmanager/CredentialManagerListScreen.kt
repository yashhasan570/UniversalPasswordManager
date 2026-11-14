package com.universalpasswordmanager.testharness.ui.platform.feature.credentialmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.universalpasswordmanager.testharness.R
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.row.UniversalPushRow
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString

/**
 * Screen displaying available Credential Manager test flows.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialManagerListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGetPassword: () -> Unit,
    onNavigateToCreatePassword: () -> Unit,
    onNavigateToGetPasskey: () -> Unit,
    onNavigateToCreatePasskey: () -> Unit,
    onNavigateToGetPasswordOrPasskey: () -> Unit,
    viewModel: CredentialManagerListViewModel = hiltViewModel(),
) {
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            CredentialManagerListEvent.NavigateToGetPassword -> {
                onNavigateToGetPassword()
            }

            CredentialManagerListEvent.NavigateToCreatePassword -> {
                onNavigateToCreatePassword()
            }

            CredentialManagerListEvent.NavigateToGetPasskey -> {
                onNavigateToGetPasskey()
            }

            CredentialManagerListEvent.NavigateToCreatePasskey -> {
                onNavigateToCreatePasskey()
            }

            CredentialManagerListEvent.NavigateToGetPasswordOrPasskey -> {
                onNavigateToGetPasswordOrPasskey()
            }

            CredentialManagerListEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    UniversalScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = R.string.credential_manager),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                    navigationIconContentDescription = stringResource(UniversalString.back),
                    onNavigationIconClick = {
                        viewModel.trySendAction(CredentialManagerListAction.BackClick)
                    },
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(8.dp))

            UniversalListHeaderText(
                label = stringResource(id = R.string.credential_manager_flows),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            UniversalPushRow(
                text = stringResource(id = R.string.get_password),
                onClick = { viewModel.trySendAction(CredentialManagerListAction.GetPasswordClick) },
                cardStyle = CardStyle.Top(),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .testTag("GetPasswordRow"),
            )

            UniversalPushRow(
                text = stringResource(id = R.string.create_password),
                onClick = {
                    viewModel.trySendAction(CredentialManagerListAction.CreatePasswordClick)
                },
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .testTag("CreatePasswordRow"),
            )

            UniversalPushRow(
                text = stringResource(id = R.string.get_passkey),
                onClick = { viewModel.trySendAction(CredentialManagerListAction.GetPasskeyClick) },
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .testTag("GetPasskeyRow"),
            )

            UniversalPushRow(
                text = stringResource(id = R.string.create_passkey),
                onClick = {
                    viewModel.trySendAction(CredentialManagerListAction.CreatePasskeyClick)
                },
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .testTag("CreatePasskeyRow"),
            )

            UniversalPushRow(
                text = stringResource(id = R.string.get_password_or_passkey),
                onClick = {
                    viewModel.trySendAction(CredentialManagerListAction.GetPasswordOrPasskeyClick)
                },
                cardStyle = CardStyle.Bottom,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .testTag("GetPasswordOrPasskeyRow"),
            )

            Spacer(Modifier.height(16.dp))
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}
