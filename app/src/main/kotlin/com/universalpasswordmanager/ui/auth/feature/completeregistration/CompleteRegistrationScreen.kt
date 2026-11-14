package com.universalpasswordmanager.ui.auth.feature.completeregistration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCardSmall
import com.universalpasswordmanager.ui.platform.components.card.color.bitwardenCardColors
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.model.WindowSize
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.util.rememberWindowSize
import com.universalpasswordmanager.ui.auth.feature.completeregistration.handlers.CompleteRegistrationHandler
import com.universalpasswordmanager.ui.auth.feature.completeregistration.handlers.rememberCompleteRegistrationHandler

/**
 * Top level composable for the complete registration screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun CompleteRegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordGuidance: () -> Unit,
    onNavigateToPreventAccountLockout: () -> Unit,
    onNavigateToLogin: (email: String) -> Unit,
    viewModel: CompleteRegistrationViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberCompleteRegistrationHandler(viewModel = viewModel)
    val snackbarHostState = rememberUniversalSnackbarHostState()
    // route OS back actions through the VM to clear the special circumstance
    BackHandler(onBack = handler.onBackClick)

    EventsEffect(viewModel) { event ->
        when (event) {
            is CompleteRegistrationEvent.NavigateBack -> onNavigateBack.invoke()
            is CompleteRegistrationEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(UniversalSnackbarData(message = event.message))
            }

            CompleteRegistrationEvent.NavigateToMakePasswordStrong -> onNavigateToPasswordGuidance()
            CompleteRegistrationEvent.NavigateToPreventAccountLockout -> {
                onNavigateToPreventAccountLockout()
            }

            is CompleteRegistrationEvent.NavigateToLogin -> {
                onNavigateToLogin(
                    event.email,
                )
            }
        }
    }

    // Show dialog if needed:
    when (val dialog = state.dialog) {
        is CompleteRegistrationDialog.Error -> {
            UniversalBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                throwable = dialog.error,
                onDismissRequest = handler.onDismissErrorDialog,
            )
        }

        is CompleteRegistrationDialog.HaveIBeenPwned -> {
            UniversalTwoButtonDialog(
                title = dialog.title(),
                message = dialog.message(),
                confirmButtonText = stringResource(id = UniversalString.yes),
                dismissButtonText = stringResource(id = UniversalString.no),
                onConfirmClick = handler.onContinueWithBreachedPasswordClick,
                onDismissClick = handler.onDismissErrorDialog,
                onDismissRequest = handler.onDismissErrorDialog,
            )
        }

        CompleteRegistrationDialog.Loading -> {
            UniversalLoadingDialog(text = stringResource(id = UniversalString.create_account))
        }

        null -> Unit
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.create_account),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
                onNavigationIconClick = handler.onBackClick,
            )
        },
        snackbarHost = { UniversalSnackbarHost(bitwardenHostState = snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            CompleteRegistrationContent(
                passwordInput = state.passwordInput,
                passwordStrengthState = state.passwordStrengthState,
                confirmPasswordInput = state.confirmPasswordInput,
                passwordHintInput = state.passwordHintInput,
                isCheckDataBreachesToggled = state.isCheckDataBreachesToggled,
                handler = handler,
                nextButtonEnabled = state.validSubmissionReady,
                callToActionText = stringResource(UniversalString.next),
                minimumPasswordLength = state.minimumPasswordLength,
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun CompleteRegistrationContent(
    passwordInput: String,
    passwordStrengthState: PasswordStrengthState,
    confirmPasswordInput: String,
    passwordHintInput: String,
    isCheckDataBreachesToggled: Boolean,
    nextButtonEnabled: Boolean,
    minimumPasswordLength: Int,
    callToActionText: String,
    handler: CompleteRegistrationHandler,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        CompleteRegistrationContentHeader(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        UniversalActionCardSmall(
            actionIcon = rememberVectorPainter(id = UniversalDrawable.ic_question_circle),
            actionText = stringResource(id = UniversalString.what_makes_a_password_strong),
            callToActionText = stringResource(id = UniversalString.learn_more),
            callToActionTextColor = UniversalTheme.colorScheme.text.interaction,
            colors = bitwardenCardColors(
                containerColor = UniversalTheme.colorScheme.background.primary,
            ),
            onCardClicked = handler.onMakeStrongPassword,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(24.dp))

        var showPassword by rememberSaveable { mutableStateOf(false) }
        UniversalPasswordField(
            label = stringResource(id = UniversalString.master_password_required),
            showPassword = showPassword,
            showPasswordChange = { showPassword = it },
            value = passwordInput,
            onValueChange = handler.onPasswordInputChange,
            showPasswordTestTag = "PasswordVisibilityToggle",
            imeAction = ImeAction.Next,
            supportingContent = {
                PasswordStrengthIndicator(
                    state = passwordStrengthState,
                    currentCharacterCount = passwordInput.length,
                    minimumCharacterCount = minimumPasswordLength,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            passwordFieldTestTag = "MasterPasswordEntry",
            cardStyle = CardStyle.Top(dividerPadding = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        UniversalPasswordField(
            label = stringResource(id = UniversalString.retype_master_password_required),
            value = confirmPasswordInput,
            showPassword = showPassword,
            showPasswordChange = { showPassword = it },
            onValueChange = handler.onConfirmPasswordInputChange,
            showPasswordTestTag = "ConfirmPasswordVisibilityToggle",
            passwordFieldTestTag = "ConfirmMasterPasswordEntry",
            cardStyle = CardStyle.Middle(dividerPadding = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        UniversalTextField(
            label = stringResource(
                id = UniversalString.master_password_hint_not_specified,
            ),
            value = passwordHintInput,
            onValueChange = handler.onPasswordHintChange,
            supportingContentPadding = PaddingValues(top = 12.dp),
            supportingContent = {
                Text(
                    text = stringResource(
                        id = UniversalString
                            .bitwarden_cannot_recover_a_lost_or_forgotten_master_password,
                    ),
                    style = UniversalTheme.typography.bodySmall,
                    color = UniversalTheme.colorScheme.text.secondary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                )
                UniversalClickableText(
                    label = stringResource(
                        id = UniversalString.learn_about_other_ways_to_prevent_account_lockout,
                    ),
                    onClick = handler.onLearnToPreventLockout,
                    style = UniversalTheme.typography.labelMedium,
                    shape = UniversalTheme.shapes.contentBottom,
                    innerPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            textFieldTestTag = "MasterPasswordHintLabel",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        UniversalSwitch(
            label = stringResource(
                id = UniversalString.check_known_data_breaches_for_this_password,
            ),
            isChecked = isCheckDataBreachesToggled,
            onCheckedChange = handler.onCheckDataBreachesToggle,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .testTag("CheckExposedMasterPasswordToggle")
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        UniversalFilledButton(
            label = callToActionText,
            isEnabled = nextButtonEnabled,
            onClick = handler.onCallToAction,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun CompleteRegistrationContentHeader(
    modifier: Modifier = Modifier,
) {
    when (rememberWindowSize()) {
        WindowSize.Compact -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OrderedHeaderContent()
            }
        }

        WindowSize.Medium -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OrderedHeaderContent()
            }
        }
    }
}

/**
 * Header content ordered with the image "first" and the text "second" which can be placed in a
 * [Column] or [Row].
 */
@Composable
private fun OrderedHeaderContent() {
    Image(
        painter = rememberVectorPainter(id = UniversalDrawable.ill_lock),
        contentDescription = null,
        modifier = Modifier.size(100.dp),
    )
    Spacer(modifier = Modifier.size(24.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(UniversalString.choose_your_master_password),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(
                UniversalString.choose_a_unique_and_strong_password_to_keep_your_information_safe,
            ),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewScreenSizes
@Composable
private fun CompleteRegistrationContentOldUI_preview() {
    UniversalTheme {
        CompleteRegistrationContent(
            passwordInput = "tortor",
            passwordStrengthState = PasswordStrengthState.WEAK_3,
            confirmPasswordInput = "consequat",
            passwordHintInput = "dissentiunt",
            isCheckDataBreachesToggled = false,
            handler = CompleteRegistrationHandler(
                onDismissErrorDialog = {},
                onContinueWithBreachedPasswordClick = {},
                onBackClick = {},
                onPasswordInputChange = {},
                onConfirmPasswordInputChange = {},
                onPasswordHintChange = {},
                onCheckDataBreachesToggle = {},
                onLearnToPreventLockout = {},
                onMakeStrongPassword = {},
                onCallToAction = {},
            ),
            callToActionText = "Next",
            nextButtonEnabled = true,
            modifier = Modifier.standardHorizontalMargin(),
            minimumPasswordLength = 12,
        )
    }
}
