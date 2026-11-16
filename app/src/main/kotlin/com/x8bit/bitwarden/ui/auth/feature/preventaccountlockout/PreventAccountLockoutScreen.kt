package com.x8bit.universalpasswordmanager.ui.auth.feature.preventaccountlockout

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.card.Universal Password ManagerContentCard
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * Top level screen component for the prevent account lockout info screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun PreventAccountLockoutScreen(
    onNavigateBack: () -> Unit,
    viewModel: PreventAccountLockoutViewModel = hiltViewModel(),
) {
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            PreventAccountLockoutEvent.NavigateBack -> onNavigateBack()
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(Universal Password ManagerString.prevent_account_lockout),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(PreventAccountLockoutAction.CloseClickAction)
                },
            )
        },
    ) {
        PreventAccountLockoutContent(
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
private fun PreventAccountLockoutContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(Universal Password ManagerString.never_lose_access_to_your_vault),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(
                Universal Password ManagerString.the_best_way_to_make_sure_you_can_always_access_your_vault,
            ),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Universal Password ManagerContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.create_a_hint),
                    subtitleText = stringResource(
                        Universal Password ManagerString.your_hint_will_be_send_to_you_via_email_when_you_request_it,
                    ),
                    iconVectorResource = Universal Password ManagerDrawable.ic_light_bulb,
                ),
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.write_your_password_down),
                    subtitleText = stringResource(Universal Password ManagerString.keep_it_secret_keep_it_safe),
                    iconVectorResource = Universal Password ManagerDrawable.ic_pencil,
                ),
            ),
        )
    }
    Spacer(modifier = Modifier.navigationBarsPadding())
}

@Preview
@Composable
private fun PreventAccountLockoutScreenPreview() {
    Universal Password ManagerTheme {
        PreventAccountLockoutScreen(onNavigateBack = {})
    }
}
