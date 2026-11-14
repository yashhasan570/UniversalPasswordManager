package com.universalpasswordmanager.ui.auth.feature.startregistration

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.data.repository.model.Environment
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.auth.feature.startregistration.StartRegistrationAction.ErrorDialogDismiss
import com.universalpasswordmanager.ui.auth.feature.startregistration.StartRegistrationEvent.NavigateToPrivacyPolicy
import com.universalpasswordmanager.ui.auth.feature.startregistration.StartRegistrationEvent.NavigateToTerms
import com.universalpasswordmanager.ui.auth.feature.startregistration.handlers.StartRegistrationHandler
import com.universalpasswordmanager.ui.auth.feature.startregistration.handlers.rememberStartRegistrationHandler
import com.universalpasswordmanager.ui.platform.components.dropdown.EnvironmentSelector

/**
 * Top level composable for the start registration screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartRegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCompleteRegistration: (emailAddress: String, verificationToken: String) -> Unit,
    onNavigateToCheckEmail: (email: String) -> Unit,
    onNavigateToEnvironment: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: StartRegistrationViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberStartRegistrationHandler(viewModel = viewModel)
    val snackbarHostState = rememberUniversalSnackbarHostState()
    EventsEffect(viewModel) { event ->
        when (event) {
            is NavigateToPrivacyPolicy -> {
                intentManager.launchUri("https:///privacy/".toUri())
            }

            is NavigateToTerms -> intentManager.launchUri("https:///terms/".toUri())
            is StartRegistrationEvent.NavigateToUnsubscribe -> {
                intentManager.launchUri("https:///email-preferences/".toUri())
            }

            is StartRegistrationEvent.NavigateToServerSelectionInfo -> {
                intentManager.launchUri("https:///help/server-geographies/".toUri())
            }

            is StartRegistrationEvent.NavigateBack -> onNavigateBack.invoke()
            is StartRegistrationEvent.NavigateToCompleteRegistration -> {
                onNavigateToCompleteRegistration(event.email, event.verificationToken)
            }

            is StartRegistrationEvent.NavigateToCheckEmail -> onNavigateToCheckEmail(event.email)
            StartRegistrationEvent.NavigateToEnvironment -> onNavigateToEnvironment()
            is StartRegistrationEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    StartRegistrationDialogs(
        dialog = state.dialog,
        onDismissRequest = { viewModel.trySendAction(ErrorDialogDismiss) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.create_account),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = handler.onCloseClick,
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        StartRegistrationContent(
            state = state,
            handler = handler,
        )
    }
}

@Composable
private fun StartRegistrationDialogs(
    dialog: StartRegistrationDialog?,
    onDismissRequest: () -> Unit,
) {
    when (dialog) {
        is StartRegistrationDialog.Error -> {
            UniversalBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                throwable = dialog.error,
                onDismissRequest = onDismissRequest,
            )
        }

        StartRegistrationDialog.Loading -> {
            UniversalLoadingDialog(text = stringResource(id = UniversalString.create_account))
        }

        null -> Unit
    }
}

@Suppress("LongMethod")
@Composable
private fun StartRegistrationContent(
    state: StartRegistrationState,
    handler: StartRegistrationHandler,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = rememberVectorPainter(id = UniversalDrawable.logo_universalpasswordmanager),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(12.dp))

        UniversalTextField(
            label = stringResource(id = UniversalString.email_address_required),
            value = state.emailInput,
            onValueChange = handler.onEmailInputChange,
            keyboardType = KeyboardType.Email,
            textFieldTestTag = "EmailAddressEntry",
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                EnvironmentSelector(
                    labelText = stringResource(id = UniversalString.create_account_on_with_colon),
                    dialogTitle = stringResource(id = UniversalString.create_account_on),
                    options = state.environmentTypeOptions,
                    selectedOption = state.selectedEnvironmentType,
                    onOptionSelected = handler.onEnvironmentTypeSelect,
                    onHelpClick = handler.onServerGeologyHelpClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "RegionSelectorDropdown"),
                )
            },
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        UniversalTextField(
            label = stringResource(id = UniversalString.name),
            value = state.nameInput,
            onValueChange = handler.onNameInputChange,
            textFieldTestTag = "NameEntry",
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        if (state.selectedEnvironmentType != Environment.Type.SELF_HOSTED) {
            Spacer(modifier = Modifier.height(8.dp))
            ReceiveMarketingEmailsSwitch(
                isChecked = state.isReceiveMarketingEmailsToggled,
                onCheckedChange = handler.onReceiveMarketingEmailsToggle,
                onUnsubscribeClick = handler.onUnsubscribeMarketingEmailsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        UniversalFilledButton(
            label = stringResource(id = UniversalString.continue_text),
            onClick = handler.onContinueClick,
            isEnabled = state.isContinueButtonEnabled,
            modifier = Modifier
                .testTag("ContinueButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        TermsAndPrivacyText(
            onTermsClick = handler.onTermsClick,
            onPrivacyPolicyClick = handler.onPrivacyPolicyClick,
            modifier = Modifier.standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun TermsAndPrivacyText(
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strTerms = stringResource(
        id = UniversalString.external_link_format,
        formatArgs = arrayOf(stringResource(id = UniversalString.terms_of_service)),
    )
    val strPrivacy = stringResource(
        id = UniversalString.external_link_format,
        formatArgs = arrayOf(stringResource(id = UniversalString.privacy_policy)),
    )
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                testTag = "DisclaimerText"
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = strTerms,
                        action = {
                            onTermsClick()
                            true
                        },
                    ),
                    CustomAccessibilityAction(
                        label = strPrivacy,
                        action = {
                            onPrivacyPolicyClick()
                            true
                        },
                    ),
                )
            }
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = annotatedStringResource(
                id = UniversalString
                    .by_continuing_you_agree_to_the_terms_of_service_and_privacy_policy,
                onAnnotationClick = {
                    when (it) {
                        "termsOfService" -> onTermsClick()
                        "privacyPolicy" -> onPrivacyPolicyClick()
                    }
                },
            ),
            style = UniversalTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ReceiveMarketingEmailsSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onUnsubscribeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val unsubscribeString = stringResource(id = UniversalString.unsubscribe)
    @Suppress("MaxLineLength")
    UniversalSwitch(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = unsubscribeString,
                        action = {
                            onUnsubscribeClick()
                            true
                        },
                    ),
                )
            }
            .testTag(tag = "ReceiveMarketingEmailsToggle"),
        label = annotatedStringResource(
            id = UniversalString.get_emails_from_universalpasswordmanager_for_announcements_advices_and_research_opportunities_unsubscribe_any_time,
            onAnnotationClick = { onUnsubscribeClick() },
        ),
        isChecked = isChecked,
        onCheckedChange = onCheckedChange,
        cardStyle = CardStyle.Full,
    )
}

@PreviewScreenSizes
@Composable
private fun StartRegistrationContentFilledOut_preview() {
    UniversalTheme {
        StartRegistrationContent(
            state = StartRegistrationState(
                emailInput = "e@mail.com",
                selectedEnvironmentType = Environment.Type.US,
                nameInput = "Test User",
                isReceiveMarketingEmailsToggled = true,
                isContinueButtonEnabled = true,
                dialog = null,
            ),
            handler = StartRegistrationHandler(
                onEmailInputChange = {},
                onNameInputChange = {},
                onEnvironmentTypeSelect = {},
                onContinueClick = {},
                onTermsClick = {},
                onPrivacyPolicyClick = {},
                onReceiveMarketingEmailsToggle = {},
                onUnsubscribeMarketingEmailsClick = {},
                onServerGeologyHelpClick = {},
                onCloseClick = {},
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartRegistrationContentEmpty_preview() {
    UniversalTheme {
        StartRegistrationContent(
            state = StartRegistrationState(
                emailInput = "",
                selectedEnvironmentType = Environment.Type.US,
                nameInput = "",
                isReceiveMarketingEmailsToggled = false,
                isContinueButtonEnabled = false,
                dialog = null,
            ),
            handler = StartRegistrationHandler(
                onEmailInputChange = {},
                onNameInputChange = {},
                onEnvironmentTypeSelect = {},
                onContinueClick = {},
                onTermsClick = {},
                onPrivacyPolicyClick = {},
                onReceiveMarketingEmailsToggle = {},
                onUnsubscribeMarketingEmailsClick = {},
                onServerGeologyHelpClick = {},
                onCloseClick = {},
            ),
        )
    }
}
