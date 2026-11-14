package com.universalpasswordmanager.ui.vault.feature.importlogins

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.universalpasswordmanagerBoldSpanStyle
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toAnnotatedString
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.bottomsheet.UniversalModalBottomSheet
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalContentCard
import com.universalpasswordmanager.ui.platform.components.content.model.ContentBlockData
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.vault.feature.importlogins.components.ImportLoginsInstructionStep
import com.universalpasswordmanager.ui.vault.feature.importlogins.handlers.ImportLoginHandler
import com.universalpasswordmanager.ui.vault.feature.importlogins.handlers.rememberImportLoginHandler
import kotlinx.collections.immutable.persistentListOf

private const val IMPORT_HELP_URL = "https:///help/import-data/"

/**
 * Top level component for the import logins screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportLoginsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ImportLoginsViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberImportLoginHandler(viewModel = viewModel)

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            ImportLoginsEvent.NavigateBack -> onNavigateBack()
            ImportLoginsEvent.OpenHelpLink -> {
                intentManager.startCustomTabsActivity(IMPORT_HELP_URL.toUri())
            }
        }
    }

    ImportLoginsDialogContent(state = state, handler = handler)

    BackHandler(enabled = true) {
        state.viewState.backAction?.let {
            viewModel.trySendAction(it)
        }
    }

    UniversalModalBottomSheet(
        showBottomSheet = state.showBottomSheet,
        sheetTitle = stringResource(UniversalString.universalpasswordmanager_tools),
        onDismiss = handler.onSuccessfulSyncAcknowledged,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier.statusBarsPadding(),
    ) { animatedOnDismiss ->
        ImportLoginsSuccessBottomSheetContent(
            onCompleteImportLogins = animatedOnDismiss,
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(UniversalString.import_logins),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(UniversalDrawable.ic_close),
                    onNavigationIconClick = handler.onCloseClick,
                    navigationIconContentDescription = stringResource(UniversalString.close),
                ),
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Crossfade(
            targetState = state.viewState,
            label = "CrossfadeBetweenViewStates",
            modifier = Modifier.fillMaxSize(),
        ) { viewState ->
            when (viewState) {
                ImportLoginsState.ViewState.InitialContent -> {
                    InitialImportLoginsContent(
                        onGetStartedClick = handler.onGetStartedClick,
                        onImportLaterClick = handler.onImportLaterClick,
                    )
                }

                ImportLoginsState.ViewState.ImportStepOne -> {
                    ImportLoginsStepOneContent(
                        onBackClick = handler.onMoveToInitialContent,
                        onContinueClick = handler.onMoveToStepTwo,
                        onHelpClick = handler.onHelpClick,
                    )
                }

                ImportLoginsState.ViewState.ImportStepTwo -> {
                    ImportLoginsStepTwoContent(
                        vaultUrl = state.currentWebVaultUrl,
                        onBackClick = handler.onMoveToStepOne,
                        onContinueClick = handler.onMoveToStepThree,
                        onHelpClick = handler.onHelpClick,
                    )
                }

                ImportLoginsState.ViewState.ImportStepThree -> {
                    ImportLoginsStepThreeContent(
                        onBackClick = handler.onMoveToStepTwo,
                        onContinueClick = handler.onMoveToSyncInProgress,
                        onHelpClick = handler.onHelpClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun ImportLoginsDialogContent(
    state: ImportLoginsState,
    handler: ImportLoginHandler,
) {
    val confirmButtonText = stringResource(UniversalString.confirm)
    val dismissButtonText = stringResource(UniversalString.cancel)
    when (val dialogState = state.dialogState) {
        ImportLoginsState.DialogState.GetStarted -> {
            UniversalTwoButtonDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = handler.onDismissDialog,
                confirmButtonText = confirmButtonText,
                dismissButtonText = dismissButtonText,
                onConfirmClick = handler.onConfirmGetStarted,
                onDismissClick = handler.onDismissDialog,
            )
        }

        ImportLoginsState.DialogState.ImportLater -> {
            UniversalTwoButtonDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = handler.onDismissDialog,
                confirmButtonText = confirmButtonText,
                dismissButtonText = dismissButtonText,
                onConfirmClick = handler.onConfirmImportLater,
                onDismissClick = handler.onDismissDialog,
            )
        }

        is ImportLoginsState.DialogState.Error -> {
            UniversalTwoButtonDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = handler.onDismissDialog,
                confirmButtonText = stringResource(UniversalString.try_again),
                dismissButtonText = stringResource(UniversalString.import_logins_later),
                onConfirmClick = handler.onRetrySync,
                onDismissClick = handler.onFailedSyncAcknowledged,
            )
        }

        is ImportLoginsState.DialogState.Syncing -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}

@Composable
private fun InitialImportLoginsContent(
    onGetStartedClick: () -> Unit,
    onImportLaterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberVectorPainter(id = UniversalDrawable.ill_import),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(124.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(UniversalString.give_your_vault_a_head_start),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(
                UniversalString
                    .from_your_computer_follow_these_instructions_to_export_saved_passwords,
            ),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(Modifier.height(24.dp))
        UniversalFilledButton(
            label = stringResource(UniversalString.get_started),
            onClick = onGetStartedClick,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        UniversalOutlinedButton(
            label = stringResource(UniversalString.import_logins_later),
            onClick = onImportLaterClick,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(Modifier.navigationBarsPadding())
    }
}

@Suppress("MaxLineLength")
@Composable
private fun ImportLoginsStepOneContent(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val instruction1 = annotatedStringResource(
        id = UniversalString.on_your_computer_log_in_to_your_current_browser_or_password_manager,
    )
    val instruction2 = annotatedStringResource(
        id = UniversalString.export_your_passwords_this_option_is_usually_found_in_your_settings,
    )
    val instruction3 = annotatedStringResource(
        id = UniversalString.save_the_exported_file_somewhere_on_your_computer_you_can_find_easily,
    )
    ImportLoginsInstructionStep(
        stepText = stringResource(UniversalString.step_1_of_3),
        stepTitle = stringResource(UniversalString.export_your_saved_logins),
        instructions = persistentListOf(
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number1,
                headerText = instruction1,
                subtitleText = null,
            ),
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number2,
                headerText = instruction2,
                subtitleText = null,
            ),
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number3,
                headerText = instruction3,
                subtitleText = stringResource(UniversalString.delete_this_file_after_import_is_complete)
                    .toAnnotatedString(),
            ),
        ),
        onBackClick = onBackClick,
        onContinueClick = onContinueClick,
        onHelpClick = onHelpClick,
        modifier = modifier,
    )
}

@Composable
private fun ImportLoginsStepTwoContent(
    vaultUrl: String,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val instruction1 = annotatedStringResource(
        id = UniversalString.on_your_computer_open_a_new_browser_tab_and_go_to_vault_universalpasswordmanager_com,
        args = arrayOf(vaultUrl),
    )
    val instruction2Text = stringResource(UniversalString.log_in_to_the_universalpasswordmanager_web_app)
    val instruction2 = buildAnnotatedString {
        withStyle(universalpasswordmanagerBoldSpanStyle) {
            append(instruction2Text)
        }
    }
    ImportLoginsInstructionStep(
        stepText = stringResource(UniversalString.step_2_of_3),
        stepTitle = stringResource(UniversalString.log_in_to_universalpasswordmanager),
        instructions = persistentListOf(
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number1,
                headerText = instruction1,
                subtitleText = null,
            ),
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number2,
                headerText = instruction2,
                subtitleText = null,
            ),
        ),
        onBackClick = onBackClick,
        onContinueClick = onContinueClick,
        onHelpClick = onHelpClick,
        modifier = modifier,
    )
}

@Suppress("LongMethod", "MaxLineLength")
@Composable
private fun ImportLoginsStepThreeContent(
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val instruction1 = annotatedStringResource(
        id = UniversalString.in_the_universalpasswordmanager_navigation_find_the_tools_option_and_select_import_data,
    )
    val instruction2 = annotatedStringResource(
        id = UniversalString.fill_out_the_form_and_import_your_saved_password_file,
    )
    val instruction3 = annotatedStringResource(
        id = UniversalString.select_import_data_in_the_web_app_then_done_to_finish_syncing,
    )
    val instruction4 = annotatedStringResource(
        id = UniversalString.for_your_security_be_sure_to_delete_your_saved_password_file,
    )
    ImportLoginsInstructionStep(
        stepText = stringResource(UniversalString.step_3_of_3),
        stepTitle = stringResource(UniversalString.import_logins_to_universalpasswordmanager),
        instructions = persistentListOf(
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number1,
                headerText = instruction1,
                subtitleText = null,
            ),
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number2,
                headerText = instruction2,
                subtitleText = null,
            ),
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number3,
                headerText = instruction3,
                subtitleText = null,
            ),
            ContentBlockData(
                iconVectorResource = UniversalDrawable.ic_number4,
                headerText = instruction4,
                subtitleText = null,
            ),
        ),
        onBackClick = onBackClick,
        ctaText = stringResource(UniversalString.done_text),
        onContinueClick = onContinueClick,
        onHelpClick = onHelpClick,
        modifier = modifier,
    )
}

@Suppress("LongMethod")
@Composable
private fun ImportLoginsSuccessBottomSheetContent(
    onCompleteImportLogins: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Image(
            painter = rememberVectorPainter(UniversalDrawable.ill_secure_devices),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(124.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(UniversalString.import_successful),
            style = UniversalTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(
                UniversalString.manage_your_logins_from_anywhere_with_universalpasswordmanager_tools,
            ),
            style = UniversalTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(Modifier.height(24.dp))
        UniversalContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(UniversalString.download_the_browser_extension),
                    subtitleText = stringResource(
                        UniversalString
                            .go_to_universalpasswordmanager_com_download_to_integrate_universalpasswordmanager_into_browser,
                    ),
                    iconVectorResource = UniversalDrawable.ic_puzzle,
                ),
                ContentBlockData(
                    headerText = stringResource(UniversalString.use_the_web_app),
                    subtitleText = stringResource(
                        UniversalString.log_in_at_universalpasswordmanager_com_to_easily_manage_your_account,
                    ),
                    iconVectorResource = UniversalDrawable.ic_desktop,
                ),
                ContentBlockData(
                    headerText = stringResource(UniversalString.autofill_passwords),
                    subtitleText = stringResource(
                        UniversalString.set_up_autofill_on_all_your_devices,
                    ),
                    iconVectorResource = UniversalDrawable.ic_shield,
                ),
            ),
            modifier = Modifier.standardHorizontalMargin(),
            contentSubtitleTextStyle = UniversalTheme.typography.bodySmall,
        )
        Spacer(Modifier.height(24.dp))
        UniversalFilledButton(
            label = stringResource(UniversalString.got_it),
            onClick = onCompleteImportLogins,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(Modifier.navigationBarsPadding())
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ImportLoginsInitialContent_preview() {
    UniversalTheme {
        Column(
            modifier = Modifier.background(
                UniversalTheme.colorScheme.background.primary,
            ),
        ) {
            InitialImportLoginsContent(
                onGetStartedClick = {},
                onImportLaterClick = {},
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ImportLoginsScreenDialog_preview(
    @PreviewParameter(ImportLoginsDialogContentPreviewProvider::class) state: ImportLoginsState,
) {
    UniversalTheme {
        Column(
            modifier = Modifier.background(
                UniversalTheme.colorScheme.background.primary,
            ),
        ) {
            ImportLoginsDialogContent(
                state = state,
                handler = ImportLoginHandler(
                    onDismissDialog = {},
                    onConfirmGetStarted = {},
                    onConfirmImportLater = {},
                    onCloseClick = {},
                    onGetStartedClick = {},
                    onImportLaterClick = {},
                    onHelpClick = {},
                    onMoveToInitialContent = {},
                    onMoveToStepOne = {},
                    onMoveToStepTwo = {},
                    onMoveToStepThree = {},
                    onMoveToSyncInProgress = {},
                    onRetrySync = {},
                    onFailedSyncAcknowledged = {},
                    onSuccessfulSyncAcknowledged = {},
                ),
            )
            InitialImportLoginsContent(
                onGetStartedClick = {},
                onImportLaterClick = {},
            )
        }
    }
}

@OmitFromCoverage
private class ImportLoginsDialogContentPreviewProvider :
    PreviewParameterProvider<ImportLoginsState> {
    override val values: Sequence<ImportLoginsState>
        get() = sequenceOf(
            ImportLoginsState(
                dialogState = ImportLoginsState.DialogState.GetStarted,
                viewState = ImportLoginsState.ViewState.InitialContent,
                showBottomSheet = false,
                currentWebVaultUrl = "vault.",
            ),
            ImportLoginsState(
                dialogState = ImportLoginsState.DialogState.ImportLater,
                viewState = ImportLoginsState.ViewState.InitialContent,
                showBottomSheet = false,
                currentWebVaultUrl = "vault.",
            ),
        )
}
