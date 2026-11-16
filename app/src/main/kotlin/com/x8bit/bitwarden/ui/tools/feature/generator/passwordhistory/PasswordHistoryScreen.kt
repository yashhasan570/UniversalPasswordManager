package com.x8bit.universalpasswordmanager.ui.tools.feature.generator.passwordhistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.indicator.Universal Password ManagerCircularProgressIndicator
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * Displays the password history screen
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordHistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: PasswordHistoryViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            PasswordHistoryEvent.NavigateBack -> onNavigateBack.invoke()
        }
    }

    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.password_history),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = {
                    viewModel.trySendAction(PasswordHistoryAction.CloseClick)
                },
                actions = {
                    Universal Password ManagerOverflowActionItem(
                        isVisible = state.menuEnabled,
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.clear),
                                onClick = {
                                    viewModel.trySendAction(
                                        PasswordHistoryAction.PasswordClearClick,
                                    )
                                },
                            ),
                        ),
                    )
                },
            )
        },
        content = {
            when (val viewState = state.viewState) {
                is PasswordHistoryState.ViewState.Loading -> {
                    PasswordHistoryLoading(
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is PasswordHistoryState.ViewState.Error -> {
                    PasswordHistoryError(
                        state = viewState,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is PasswordHistoryState.ViewState.Empty -> {
                    PasswordHistoryEmpty(
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is PasswordHistoryState.ViewState.Content -> {
                    PasswordHistoryContent(
                        state = viewState,
                        modifier = Modifier
                            .fillMaxSize(),
                        onPasswordCopyClick = { password ->
                            viewModel.trySendAction(
                                PasswordHistoryAction.PasswordCopyClick(password),
                            )
                        },
                    )
                }
            }
        },
    )
}

@Composable
private fun PasswordHistoryLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Universal Password ManagerCircularProgressIndicator()
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun PasswordHistoryContent(
    state: PasswordHistoryState.ViewState.Content,
    onPasswordCopyClick: (PasswordHistoryState.GeneratedPassword) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(height = 12.dp))
        }
        itemsIndexed(state.passwords) { index, password ->
            PasswordHistoryListItem(
                label = password.password,
                supportingLabel = password.date,
                onCopyClick = { onPasswordCopyClick(password) },
                cardStyle = state.passwords.toListItemCardStyle(index = index),
                modifier = Modifier
                    .testTag("GeneratedPasswordRow")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }
        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun PasswordHistoryError(
    state: PasswordHistoryState.ViewState.Error,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = state.message.invoke(),
            style = Universal Password ManagerTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun PasswordHistoryEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.testTag("NoPasswordsDisplayedLabel"),
            text = stringResource(id = Universal Password ManagerString.no_passwords_to_list),
            style = Universal Password ManagerTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
