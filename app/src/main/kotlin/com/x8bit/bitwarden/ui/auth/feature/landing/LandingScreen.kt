package com.x8bit.universalpasswordmanager.ui.auth.feature.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .data.repository.model.Environment
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.account.Universal Password ManagerAccountSwitcher
import .ui.platform.components.account.Universal Password ManagerPlaceholderAccountActionItem
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.components.dropdown.EnvironmentSelector

/**
 * The top level composable for the Landing screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun LandingScreen(
    onNavigateToLogin: (emailAddress: String) -> Unit,
    onNavigateToEnvironment: () -> Unit,
    onNavigateToStartRegistration: () -> Unit,
    onNavigateToPreAuthSettings: () -> Unit,
    viewModel: LandingViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is LandingEvent.NavigateToLogin -> onNavigateToLogin(event.emailAddress)
            LandingEvent.NavigateToEnvironment -> onNavigateToEnvironment()
            LandingEvent.NavigateToStartRegistration -> onNavigateToStartRegistration()
            LandingEvent.NavigateToSettings -> onNavigateToPreAuthSettings()
            is LandingEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    when (val dialog = state.dialog) {
        is LandingState.DialogState.AccountAlreadyAdded -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.account_already_added),
                message = stringResource(
                    id = Universal Password ManagerString.switch_to_already_added_account_confirmation,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = {
                    viewModel.trySendAction(
                        LandingAction.ConfirmSwitchToMatchingAccountClick(dialog.accountSummary),
                    )
                },
                onDismissClick = { viewModel.trySendAction(LandingAction.DialogDismiss) },
                onDismissRequest = { viewModel.trySendAction(LandingAction.DialogDismiss) },
            )
        }

        is LandingState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.an_error_has_occurred),
                message = dialog.message(),
                onDismissRequest = { viewModel.trySendAction(LandingAction.DialogDismiss) },
            )
        }

        null -> Unit
    }

    var isAccountMenuVisible by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { !isAccountMenuVisible },
    )

    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (state.isAppBarVisible) {
                Universal Password ManagerTopAppBar(
                    title = "",
                    scrollBehavior = scrollBehavior,
                    navigationIcon = null,
                    actions = {
                        Universal Password ManagerPlaceholderAccountActionItem(
                            onClick = { isAccountMenuVisible = !isAccountMenuVisible },
                        )
                    },
                )
            }
        },
        overlay = {
            Universal Password ManagerAccountSwitcher(
                isVisible = isAccountMenuVisible,
                accountSummaries = state.accountSummaries,
                onSwitchAccountClick = {
                    viewModel.trySendAction(LandingAction.SwitchAccountClick(it))
                },
                onLockAccountClick = {
                    viewModel.trySendAction(LandingAction.LockAccountClick(it))
                },
                onLogoutAccountClick = {
                    viewModel.trySendAction(LandingAction.LogoutAccountClick(it))
                },
                onAddAccountClick = {
                    // Not available
                },
                onDismissRequest = { isAccountMenuVisible = false },
                isAddAccountAvailable = false,
                topAppBarScrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxSize(),
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        LandingScreenContent(
            state = state,
            onEmailInputChange = { viewModel.trySendAction(LandingAction.EmailInputChanged(it)) },
            onEnvironmentTypeSelect = {
                viewModel.trySendAction(LandingAction.EnvironmentTypeSelect(it))
            },
            onRememberMeToggle = { viewModel.trySendAction(LandingAction.RememberMeToggle(it)) },
            onContinueClick = { viewModel.trySendAction(LandingAction.ContinueButtonClick) },
            onCreateAccountClick = { viewModel.trySendAction(LandingAction.CreateAccountClick) },
            onAppSettingsClick = { viewModel.trySendAction(LandingAction.AppSettingsClick) },
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun LandingScreenContent(
    state: LandingState,
    onEmailInputChange: (String) -> Unit,
    onEnvironmentTypeSelect: (Environment.Type) -> Unit,
    onRememberMeToggle: (Boolean) -> Unit,
    onContinueClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.logo_universalpasswordmanager),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.login_to_universalpasswordmanager),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.headlineSmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .wrapContentHeight(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerTextField(
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
            value = state.emailInput,
            onValueChange = onEmailInputChange,
            label = stringResource(id = Universal Password ManagerString.email_address),
            keyboardType = KeyboardType.Email,
            textFieldTestTag = "EmailAddressEntry",
            cardStyle = CardStyle.Full,
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                EnvironmentSelector(
                    labelText = stringResource(id = Universal Password ManagerString.logging_in_on_with_colon),
                    dialogTitle = stringResource(id = Universal Password ManagerString.logging_in_on),
                    options = state.environmentTypeOptions,
                    selectedOption = state.selectedEnvironmentType,
                    onOptionSelected = onEnvironmentTypeSelect,
                    isHelpEnabled = false,
                    onHelpClick = {},
                    modifier = Modifier
                        .testTag("RegionSelectorDropdown")
                        .fillMaxWidth(),
                )
            },
        )

        Spacer(modifier = Modifier.height(height = 8.dp))

        Universal Password ManagerSwitch(
            label = stringResource(id = Universal Password ManagerString.remember_email),
            isChecked = state.isRememberEmailEnabled,
            onCheckedChange = onRememberMeToggle,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .testTag("RememberMeSwitch")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.continue_text),
            onClick = onContinueClick,
            isEnabled = state.isContinueButtonEnabled,
            modifier = Modifier
                .testTag("ContinueButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        AnimatedVisibility(
            visible = state.allowCreateAccount,
            label = "CreateAccountAnimatedVisibility",
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Text(
                        text = stringResource(id = Universal Password ManagerString.new_to_universalpasswordmanager),
                        style = Universal Password ManagerTheme.typography.bodyMedium,
                        color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    )

                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.create_an_account),
                        onClick = onCreateAccountClick,
                        modifier = Modifier
                            .testTag("CreateAccountLabel"),
                    )
                }
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
        }
        Universal Password ManagerTextButton(
            label = stringResource(id = Universal Password ManagerString.app_settings),
            onClick = onAppSettingsClick,
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_cog),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
