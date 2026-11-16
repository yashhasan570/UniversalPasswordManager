package com.x8bit.universalpasswordmanager.ui.vault.feature.importitems

import androidx.credentials.providerevents.exception.ImportCredentialsInvalidJsonException
import androidx.credentials.providerevents.transfer.CredentialTypes
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import .cxf.importer.model.ImportCredentialsSelectionResult
import .policies.PolicyType
import .ui.platform.base.BaseViewModelTest
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.resource.Universal Password ManagerPlurals
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asPluralsText
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.manager.PolicyManager
import com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.model.createMockSdkPolicy
import com.x8bit.universalpasswordmanager.data.vault.manager.model.SyncVaultDataResult
import com.x8bit.universalpasswordmanager.data.vault.repository.VaultRepository
import com.x8bit.universalpasswordmanager.data.vault.repository.model.ImportCredentialsResult
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ImportItemsViewModelTest : BaseViewModelTest() {

    private val vaultRepository = mockk<VaultRepository>()
    private val policyManager = mockk<PolicyManager>()

    @Test
    fun `BackClick sends NavigateBack event`() = runTest {
        val viewModel = createViewModel()
        viewModel.eventFlow.test {
            viewModel.trySendAction(ImportItemsAction.BackClick)
            assertEquals(ImportItemsEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `ImportFromComputerClick sends NavigateToImportFromComputer event`() = runTest {
        val viewModel = createViewModel()
        viewModel.eventFlow.test {
            viewModel.trySendAction(ImportItemsAction.ImportFromComputerClick)
            assertEquals(ImportItemsEvent.NavigateToImportFromComputer, awaitItem())
        }
    }

    @Test
    fun `DismissDialog updates state`() = runTest {
        val viewModel = createViewModel()
        viewModel.trySendAction(ImportItemsAction.DismissDialog)
        assertEquals(ImportItemsState(), viewModel.stateFlow.value)
    }

    @Test
    fun `ImportFromAnotherAppClick sends ShowRegisteredImportSources event`() {
        runTest {
            every {
                policyManager.getActivePolicies(PolicyType.RESTRICTED_ITEM_TYPES)
            } returns emptyList()

            val viewModel = createViewModel()
            viewModel.trySendAction(ImportItemsAction.ImportFromAnotherAppClick)
            viewModel.eventFlow.test {
                assertEquals(
                    ImportItemsEvent.ShowRegisteredImportSources(
                        listOf(
                            CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH,
                            CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY,
                            CredentialTypes.CREDENTIAL_TYPE_ADDRESS,
                            CredentialTypes.CREDENTIAL_TYPE_API_KEY,
                            CredentialTypes.CREDENTIAL_TYPE_CREDIT_CARD,
                            CredentialTypes.CREDENTIAL_TYPE_CUSTOM_FIELDS,
                            CredentialTypes.CREDENTIAL_TYPE_DRIVERS_LICENSE,
                            CredentialTypes.CREDENTIAL_TYPE_IDENTITY_DOCUMENT,
                            CredentialTypes.CREDENTIAL_TYPE_NOTE,
                            CredentialTypes.CREDENTIAL_TYPE_PASSPORT,
                            CredentialTypes.CREDENTIAL_TYPE_PERSON_NAME,
                            CredentialTypes.CREDENTIAL_TYPE_SSH_KEY,
                            CredentialTypes.CREDENTIAL_TYPE_TOTP,
                            CredentialTypes.CREDENTIAL_TYPE_WIFI,
                        ),
                    ),
                    awaitItem(),
                )
            }
        }
    }

    @Suppress("MaxLineLength")
    @Test
    fun `ImportFromAnotherAppClick sends ShowRegisteredImportSources event without CREDIT_CARD when policy enabled`() {
        runTest {
            // Policy is active and enabled
            every {
                policyManager.getActivePolicies(PolicyType.RESTRICTED_ITEM_TYPES)
            } returns listOf(
                createMockSdkPolicy(
                    organizationId = "org-id",
                    id = "policy-id",
                    type = PolicyType.RESTRICTED_ITEM_TYPES,
                    enabled = true,
                    data = null,
                ),
            )

            val viewModel = createViewModel()
            viewModel.trySendAction(ImportItemsAction.ImportFromAnotherAppClick)
            viewModel.eventFlow.test {
                assertEquals(
                    ImportItemsEvent.ShowRegisteredImportSources(
                        listOf(
                            CredentialTypes.CREDENTIAL_TYPE_BASIC_AUTH,
                            CredentialTypes.CREDENTIAL_TYPE_PUBLIC_KEY,
                            CredentialTypes.CREDENTIAL_TYPE_ADDRESS,
                            CredentialTypes.CREDENTIAL_TYPE_API_KEY,
                            // CREDENTIAL_TYPE_CREDIT_CARD is excluded
                            CredentialTypes.CREDENTIAL_TYPE_CUSTOM_FIELDS,
                            CredentialTypes.CREDENTIAL_TYPE_DRIVERS_LICENSE,
                            CredentialTypes.CREDENTIAL_TYPE_IDENTITY_DOCUMENT,
                            CredentialTypes.CREDENTIAL_TYPE_NOTE,
                            CredentialTypes.CREDENTIAL_TYPE_PASSPORT,
                            CredentialTypes.CREDENTIAL_TYPE_PERSON_NAME,
                            CredentialTypes.CREDENTIAL_TYPE_SSH_KEY,
                            CredentialTypes.CREDENTIAL_TYPE_TOTP,
                            CredentialTypes.CREDENTIAL_TYPE_WIFI,
                        ),
                    ),
                    awaitItem(),
                )
            }
        }
    }

    @Test
    fun `ImportCredentialSelectionReceive and Cancelled result updates state`() = runTest {
        val viewModel = createViewModel()

        viewModel.trySendAction(
            ImportItemsAction.ImportCredentialSelectionReceive(
                selectionResult = ImportCredentialsSelectionResult.Cancelled,
            ),
        )

        val expectedState = ImportItemsState(
            dialog = ImportItemsState.DialogState.General(
                title = Universal Password ManagerString.import_cancelled.asText(),
                message = Universal Password ManagerString.import_was_cancelled_in_the_selected_app.asText(),
                throwable = null,
            ),
        )
        assertEquals(expectedState, viewModel.stateFlow.value)
    }

    @Test
    fun `ImportCredentialSelectionReceive and Failure result updates state`() = runTest {
        val viewModel = createViewModel()
        val exception = ImportCredentialsInvalidJsonException("Error")

        viewModel.trySendAction(
            ImportItemsAction.ImportCredentialSelectionReceive(
                selectionResult = ImportCredentialsSelectionResult.Failure(
                    error = exception,
                ),
            ),
        )

        val expectedState = ImportItemsState(
            dialog = ImportItemsState.DialogState.General(
                title = Universal Password ManagerString.unable_to_import_your_items.asText(),
                message = Universal Password ManagerString.there_was_a_problem_importing_your_items.asText(),
                throwable = exception,
            ),
        )
        assertEquals(expectedState, viewModel.stateFlow.value)
    }

    @Test
    fun `ImportCredentialSelectionReceive and Success result updates state and triggers import`() =
        runTest {
            val cxfPayload = "{\"credentials\":[]}"
            val selectionResult = ImportCredentialsSelectionResult.Success(
                response = cxfPayload,
                callingAppInfo = mockk(),
            )
            coEvery {
                vaultRepository.importCxfPayload(cxfPayload)
            } just awaits

            val viewModel = createViewModel()

            viewModel.trySendAction(
                ImportItemsAction.ImportCredentialSelectionReceive(
                    selectionResult,
                ),
            )

            assertEquals(
                ImportItemsState(
                    dialog = ImportItemsState.DialogState.Loading(
                        message = Universal Password ManagerString.saving_items.asText(),
                    ),
                ),
                viewModel.stateFlow.value,
            )

            // Verify that the repository method was called
            coVerify { vaultRepository.importCxfPayload(cxfPayload) }
        }

    @Test
    fun `SyncFailedTryAgainClick should update state and trigger sync`() = runTest {
        val viewModel = createViewModel()
        coEvery {
            vaultRepository.syncForResult()
        } returns SyncVaultDataResult.Success(itemsAvailable = true)

        viewModel.trySendAction(ImportItemsAction.SyncFailedTryAgainClick)

        coVerify(exactly = 1) {
            vaultRepository.syncForResult()
        }
    }

    @Test
    fun `Internal ImportCxfResultReceive and Error result updates state`() = runTest {
        val viewModel = createViewModel()
        val exception = ImportCredentialsInvalidJsonException("Error")
        viewModel.trySendAction(
            ImportItemsAction.Internal.ImportCredentialsResultReceive(
                ImportCredentialsResult.Error(
                    error = exception,
                ),
            ),
        )

        val expectedState = ImportItemsState(
            dialog = ImportItemsState.DialogState.General(
                title = Universal Password ManagerString.unable_to_import_your_items.asText(),
                message = Universal Password ManagerString.there_was_a_problem_importing_your_items.asText(),
                throwable = exception,
            ),
        )
        assertEquals(expectedState, viewModel.stateFlow.value)
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Internal ImportCredentialsResultReceive with Success result should clear dialogs, and show snackbar`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.trySendAction(
                ImportItemsAction.Internal.ImportCredentialsResultReceive(
                    ImportCredentialsResult.Success(itemCount = 2),
                ),
            )

            val expectedState = ImportItemsState()
            assertEquals(expectedState, viewModel.stateFlow.value)
            viewModel.eventFlow.test {
                assertEquals(
                    ImportItemsEvent.ShowBasicSnackbar(
                        Universal Password ManagerSnackbarData(
                            messageHeader = Universal Password ManagerString.import_successful.asText(),
                            message = Universal Password ManagerPlurals
                                .x_items_have_been_imported_to_your_vault
                                .asPluralsText(
                                    quantity = 2,
                                    args = arrayOf(2),
                                ),
                        ),
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `Internal ImportCxfResultReceive and NoItems result updates state`() = runTest {
        val viewModel = createViewModel()

        viewModel.trySendAction(
            ImportItemsAction.Internal.ImportCredentialsResultReceive(
                ImportCredentialsResult.NoItems,
            ),
        )

        val expectedState = ImportItemsState(
            dialog = ImportItemsState.DialogState.General(
                title = Universal Password ManagerString.no_items_imported.asText(),
                message = Universal Password ManagerString.no_items_received_from_the_selected_app.asText(),
                throwable = null,
            ),
        )
        assertEquals(expectedState, viewModel.stateFlow.value)
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Internal ImportCxfResultReceive and SyncFailed should clear dialogs and show snackbar`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.trySendAction(
                ImportItemsAction.Internal.ImportCredentialsResultReceive(
                    ImportCredentialsResult.SyncFailed(
                        error = RuntimeException("Error"),
                    ),
                ),
            )

            val expectedState = ImportItemsState()
            assertEquals(expectedState, viewModel.stateFlow.value)
            viewModel.eventFlow.test {
                assertEquals(
                    ImportItemsEvent.ShowSyncFailedSnackbar(
                        data = Universal Password ManagerSnackbarData(
                            messageHeader = Universal Password ManagerString.vault_sync_failed.asText(),
                            message = Universal Password ManagerString
                                .your_items_have_been_successfully_imported_but_could_not_sync_vault
                                .asText(),
                            actionLabel = Universal Password ManagerString.try_again.asText(),
                        ),
                    ),
                    awaitItem(),
                )
            }
        }

    @Suppress("MaxLineLength")
    @Test
    fun `Internal RetrySyncResultReceive with Success should clear dialogs and display success snackbar`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.trySendAction(
                ImportItemsAction.Internal.RetrySyncResultReceive(
                    result = SyncVaultDataResult.Success(itemsAvailable = true),
                ),
            )

            assertEquals(
                ImportItemsState(),
                viewModel.stateFlow.value,
            )
            viewModel.eventFlow.test {
                assertEquals(
                    ImportItemsEvent.ShowBasicSnackbar(
                        data = Universal Password ManagerSnackbarData(
                            message = Universal Password ManagerString.syncing_complete.asText(),
                        ),
                    ),
                    awaitItem(),
                )
            }
        }

    @Suppress("MaxLineLength")
    @Test
    fun `Internal RetrySyncResultReceive with Error should clear dialogs and show SyncFailed snackbar`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.trySendAction(
                ImportItemsAction.Internal.RetrySyncResultReceive(
                    result = SyncVaultDataResult.Error(throwable = RuntimeException("Error")),
                ),
            )

            assertEquals(
                ImportItemsState(),
                viewModel.stateFlow.value,
            )
            viewModel.eventFlow.test {
                assertEquals(
                    ImportItemsEvent.ShowSyncFailedSnackbar(
                        data = Universal Password ManagerSnackbarData(
                            messageHeader = Universal Password ManagerString.vault_sync_failed.asText(),
                            message = Universal Password ManagerString
                                .your_items_have_been_successfully_imported_but_could_not_sync_vault
                                .asText(),
                            actionLabel = Universal Password ManagerString.try_again.asText(),
                        ),
                    ),
                    awaitItem(),
                )
            }
        }

    private fun createViewModel(): ImportItemsViewModel = ImportItemsViewModel(
        vaultRepository = vaultRepository,
        savedStateHandle = SavedStateHandle(),
        policyManager = policyManager,
    )
}
