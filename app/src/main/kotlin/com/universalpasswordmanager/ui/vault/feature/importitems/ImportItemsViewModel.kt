package com.universalpasswordmanager.ui.vault.feature.importitems

import android.os.Parcelable
import androidx.credentials.providerevents.transfer.CredentialTypes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.universalpasswordmanager.cxf.importer.model.ImportCredentialsSelectionResult
import com.universalpasswordmanager.ui.platform.base.BackgroundEvent
import com.universalpasswordmanager.ui.platform.base.BaseViewModel
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import com.universalpasswordmanager.ui.platform.resource.UniversalPlurals
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asPluralsText
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.manager.PolicyManager
import com.universalpasswordmanager.data.platform.manager.util.hasRestrictItemTypes
import com.universalpasswordmanager.data.vault.manager.model.SyncVaultDataResult
import com.universalpasswordmanager.data.vault.repository.VaultRepository
import com.universalpasswordmanager.data.vault.repository.model.ImportCredentialsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

private const val KEY_STATE = "state"

/**
 * View model for the [ImportItemsScreen].
 */
@Suppress("TooManyFunctions")
@HiltViewModel
class ImportItemsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val vaultRepository: VaultRepository,
    private val policyManager: PolicyManager,
) : BaseViewModel<ImportItemsState, ImportItemsEvent, ImportItemsAction>(
    initialState = savedStateHandle[KEY_STATE] ?: ImportItemsState(),
) {

    override fun handleAction(action: ImportItemsAction) {
        when (action) {
            ImportItemsAction.BackClick -> {
                handleBackClick()
            }

            is ImportItemsAction.ImportFromAnotherAppClick -> {
                handleImportFromAnotherAppClick()
            }

            is ImportItemsAction.ImportCredentialSelectionReceive -> {
                handleImportCredentialSelectionReceive(action)
            }

            ImportItemsAction.ImportFromComputerClick -> {
                handleImportFromComputerClick()
            }

            ImportItemsAction.DismissDialog -> {
                handleDismissDialog()
            }

            ImportItemsAction.SyncFailedTryAgainClick -> {
                handleSyncFailedTryAgainClick()
            }

            is ImportItemsAction.Internal -> {
                handleInternalAction(action)
            }
        }
    }

    private fun handleInternalAction(action: ImportItemsAction.Internal) {
        when (action) {
            is ImportItemsAction.Internal.ImportCredentialsResultReceive -> {
                handleImportCredentialsResultReceive(action)
            }

            is ImportItemsAction.Internal.RetrySyncResultReceive -> {
                handleRetrySyncResultReceive(action)
            }
        }
    }

    private fun handleRetrySyncResultReceive(
        action: ImportItemsAction.Internal.RetrySyncResultReceive,
    ) {
        clearDialogs()
        when (action.result) {
            is SyncVaultDataResult.Success -> {
                sendEvent(
                    ImportItemsEvent.ShowBasicSnackbar(
                        data = UniversalSnackbarData(
                            message = UniversalString.syncing_complete.asText(),
                        ),
                    ),
                )
            }

            is SyncVaultDataResult.Error -> {
                showSyncFailedSnackbar()
            }
        }
    }

    private fun handleSyncFailedTryAgainClick() {
        showLoadingDialog(message = UniversalString.syncing.asText())
        viewModelScope.launch {
            sendAction(
                ImportItemsAction.Internal.RetrySyncResultReceive(
                    result = vaultRepository.syncForResult(),
                ),
            )
        }
    }

    private fun handleBackClick() {
        sendEvent(ImportItemsEvent.NavigateBack)
    }

    private fun handleImportFromAnotherAppClick() {
        val credentialTypes = buildList {
            add(CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH)
            add(CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY)
            add(CredentialTypes.CREDENTIAL_TYPE_ADDRESS)
            add(CredentialTypes.CREDENTIAL_TYPE_API_KEY)
            // Only include credit card type if policy doesn't restrict it
            if (!policyManager.hasRestrictItemTypes()) {
                add(CredentialTypes.CREDENTIAL_TYPE_CREDIT_CARD)
            }
            add(CredentialTypes.CREDENTIAL_TYPE_CUSTOM_FIELDS)
            add(CredentialTypes.CREDENTIAL_TYPE_DRIVERS_LICENSE)
            add(CredentialTypes.CREDENTIAL_TYPE_IDENTITY_DOCUMENT)
            add(CredentialTypes.CREDENTIAL_TYPE_NOTE)
            add(CredentialTypes.CREDENTIAL_TYPE_PASSPORT)
            add(CredentialTypes.CREDENTIAL_TYPE_PERSON_NAME)
            add(CredentialTypes.CREDENTIAL_TYPE_SSH_KEY)
            add(CredentialTypes.CREDENTIAL_TYPE_TOTP)
            add(CredentialTypes.CREDENTIAL_TYPE_WIFI)
        }

        sendEvent(
            ImportItemsEvent.ShowRegisteredImportSources(
                credentialTypes = credentialTypes,
            ),
        )
    }

    private fun handleImportFromComputerClick() {
        sendEvent(ImportItemsEvent.NavigateToImportFromComputer)
    }

    private fun handleImportCredentialSelectionReceive(
        action: ImportItemsAction.ImportCredentialSelectionReceive,
    ) {
        when (val result = action.selectionResult) {
            ImportCredentialsSelectionResult.Cancelled -> {
                showGeneralDialog(
                    title = UniversalString.import_cancelled.asText(),
                    message = UniversalString.import_was_cancelled_in_the_selected_app.asText(),
                    throwable = null,
                )
            }

            is ImportCredentialsSelectionResult.Failure -> {
                showGeneralDialog(
                    title = UniversalString.unable_to_import_your_items.asText(),
                    message = UniversalString.there_was_a_problem_importing_your_items.asText(),
                    throwable = result.error,
                )
            }

            is ImportCredentialsSelectionResult.Success -> {
                showLoadingDialog(UniversalString.saving_items.asText())
                viewModelScope.launch {
                    sendAction(
                        ImportItemsAction.Internal.ImportCredentialsResultReceive(
                            vaultRepository.importCxfPayload(
                                payload = result.response,
                            ),
                        ),
                    )
                }
            }
        }
    }

    private fun handleDismissDialog() {
        clearDialogs()
    }

    private fun handleImportCredentialsResultReceive(
        action: ImportItemsAction.Internal.ImportCredentialsResultReceive,
    ) {
        when (action.result) {
            is ImportCredentialsResult.Error -> {
                showGeneralDialog(
                    title = UniversalString.unable_to_import_your_items.asText(),
                    message = UniversalString.there_was_a_problem_importing_your_items.asText(),
                    throwable = action.result.error,
                )
            }

            is ImportCredentialsResult.Success -> {
                clearDialogs()
                sendEvent(
                    ImportItemsEvent.ShowBasicSnackbar(
                        data = UniversalSnackbarData(
                            messageHeader = UniversalString.import_successful.asText(),
                            message = UniversalPlurals
                                .x_items_have_been_imported_to_your_vault
                                .asPluralsText(
                                    quantity = action.result.itemCount,
                                    args = arrayOf(action.result.itemCount),
                                ),
                        ),
                    ),
                )
            }

            ImportCredentialsResult.NoItems -> {
                showGeneralDialog(
                    title = UniversalString.no_items_imported.asText(),
                    message = UniversalString.no_items_received_from_the_selected_app.asText(),
                    throwable = null,
                )
            }

            is ImportCredentialsResult.SyncFailed -> {
                clearDialogs()
                showSyncFailedSnackbar()
            }
        }
    }

    private fun showSyncFailedSnackbar() {
        sendEvent(
            ImportItemsEvent.ShowSyncFailedSnackbar(
                data = UniversalSnackbarData(
                    messageHeader = UniversalString.vault_sync_failed.asText(),
                    message = UniversalString
                        .your_items_have_been_successfully_imported_but_could_not_sync_vault
                        .asText(),
                    actionLabel = UniversalString.try_again.asText(),
                ),
            ),
        )
    }

    private fun clearDialogs() {
        mutableStateFlow.update { it.copy(dialog = null) }
    }

    private fun showGeneralDialog(
        title: Text,
        message: Text,
        throwable: Throwable?,
    ) {
        mutableStateFlow.update {
            it.copy(
                dialog = ImportItemsState.DialogState.General(
                    title = title,
                    message = message,
                    throwable = throwable,
                ),
            )
        }
    }

    private fun showLoadingDialog(message: Text) {
        mutableStateFlow.update {
            it.copy(
                dialog = ImportItemsState.DialogState.Loading(message = message),
            )
        }
    }
}

/**
 * State for the [ImportItemsScreen].
 */
@Parcelize
data class ImportItemsState(
    val dialog: DialogState? = null,
) : Parcelable {

    /**
     * Dialog state for the [ImportItemsScreen].
     */
    @Parcelize
    sealed class DialogState : Parcelable {

        /**
         * Show the loading dialog.
         */
        data class Loading(val message: Text) : DialogState()

        /**
         * Show a general dialog with the given title and message.
         */
        data class General(
            val title: Text,
            val message: Text,
            val throwable: Throwable?,
        ) : DialogState()
    }
}

/**
 * Actions for the [ImportItemsViewModel].
 */
sealed class ImportItemsAction {

    /**
     * User clicked the Import from computer option.
     */
    data object ImportFromComputerClick : ImportItemsAction()

    /**
     * User clicked the Import from another app option.
     */
    data object ImportFromAnotherAppClick : ImportItemsAction()

    /**
     * Result of credential selection from the selected credential manager.
     *
     * @property selectionResult The result of the credential selection.
     */
    data class ImportCredentialSelectionReceive(
        val selectionResult: ImportCredentialsSelectionResult,
    ) : ImportItemsAction()

    /**
     * User clicked the back button.
     */
    data object BackClick : ImportItemsAction()

    /**
     * User dismissed the dialog.
     */
    data object DismissDialog : ImportItemsAction()

    /**
     * User clicked the try again button.
     */
    data object SyncFailedTryAgainClick : ImportItemsAction()

    /**
     * Internal actions that the [ImportItemsViewModel] may itself send.
     */
    sealed class Internal : ImportItemsAction() {
        /**
         * Import CXF result received.
         */
        data class ImportCredentialsResultReceive(
            val result: ImportCredentialsResult,
        ) : Internal()

        /**
         * Retry sync result received.
         */
        data class RetrySyncResultReceive(
            val result: SyncVaultDataResult,
        ) : Internal()
    }
}

/**
 * Events for the [ImportItemsViewModel].
 */
sealed class ImportItemsEvent {

    /**
     * Navigate back.
     */
    data object NavigateBack : ImportItemsEvent()

    /**
     * Navigate to the import from computer screen.
     */
    data object NavigateToImportFromComputer : ImportItemsEvent()

    /**
     * Show registered import sources.
     *
     * @property credentialTypes The credential types to request.
     */
    data class ShowRegisteredImportSources(
        val credentialTypes: List<String>,
    ) : ImportItemsEvent(), BackgroundEvent

    /**
     * Show a basic snackbar.
     */
    data class ShowBasicSnackbar(
        val data: UniversalSnackbarData,
    ) : ImportItemsEvent(), BackgroundEvent

    /**
     * Show a snackbar indicating that the sync failed, with an option to retry.
     */
    data class ShowSyncFailedSnackbar(
        val data: UniversalSnackbarData,
    ) : ImportItemsEvent(), BackgroundEvent
}
