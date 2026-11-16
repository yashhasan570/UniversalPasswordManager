package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.folders

import app.cash.turbine.test
import .core.data.repository.model.DataState
import .core.data.repository.util.bufferedMutableSharedFlow
import .ui.platform.base.BaseViewModelTest
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.manager.snackbar.SnackbarRelayManager
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import .ui.util.concat
import .vault.FolderView
import com.x8bit.universalpasswordmanager.data.vault.repository.VaultRepository
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.folders.model.FolderDisplayItem
import com.x8bit.universalpasswordmanager.ui.platform.model.SnackbarRelay
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class FoldersViewModelTest : BaseViewModelTest() {

    private val mutableFoldersStateFlow =
        MutableStateFlow<DataState<List<FolderView>>>(DataState.Loaded(listOf()))

    private val vaultRepository: VaultRepository = mockk {
        every { foldersStateFlow } returns mutableFoldersStateFlow
    }
    private val mutableSnackbarDataFlow = bufferedMutableSharedFlow<Universal Password ManagerSnackbarData>()
    private val snackbarRelayManager: SnackbarRelayManager<SnackbarRelay> = mockk {
        every {
            getSnackbarDataFlow(relay = any(), relays = anyVararg())
        } returns mutableSnackbarDataFlow
    }

    @Test
    fun `on snackbar data received should emit ShowSnackbar`() = runTest {
        val viewModel = createViewModel()

        val data = Universal Password ManagerSnackbarData(message = "Snackbar!".asText())
        viewModel.eventFlow.test {
            mutableSnackbarDataFlow.emit(data)
            assertEquals(
                FoldersEvent.ShowSnackbar(data = data),
                awaitItem(),
            )
        }
    }

    @Test
    fun `BackClick should emit NavigateBack`() = runTest {
        val viewModel = createViewModel()
        viewModel.eventFlow.test {
            viewModel.trySendAction(FoldersAction.CloseButtonClick)
            assertEquals(FoldersEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `AddFolderButtonClick should emit NavigateToAddFolderScreen`() = runTest {
        val viewModel = createViewModel()
        viewModel.eventFlow.test {
            viewModel.trySendAction(FoldersAction.AddFolderButtonClick)
            assertEquals(
                FoldersEvent.NavigateToAddFolderScreen,
                awaitItem(),
            )
        }
    }

    @Test
    fun `FolderClick should emit NavigateToAddFolderScreen`() = runTest {
        val testId = "TestId"
        val viewModel = createViewModel()
        viewModel.eventFlow.test {
            viewModel.trySendAction(FoldersAction.FolderClick(testId))
            assertEquals(
                FoldersEvent.NavigateToEditFolderScreen(testId),
                awaitItem(),
            )
        }
    }

    @Test
    fun `folderStateFlow Error should update state to error`() {
        val viewModel = createViewModel()

        mutableFoldersStateFlow.tryEmit(
            value = DataState.Error(
                data = listOf(),
                error = IllegalStateException(),
            ),
        )

        assertEquals(
            createFolderState(
                viewState = FoldersState.ViewState.Error(
                    Universal Password ManagerString.generic_error_message.asText(),
                ),
            ),
            viewModel.stateFlow.value,
        )
    }

    @Test
    fun `folderStateFlow Loaded with data should update state to content`() {
        val viewModel = createViewModel()

        mutableFoldersStateFlow.tryEmit(
            DataState.Loaded(
                listOf(DEFAULT_FOLDER_VIEW),
            ),
        )
        assertEquals(
            createFolderState(
                viewState = FoldersState.ViewState.Content(
                    folderList = listOf(DEFAULT__DISPLAY_FOLDER),
                ),
            ),
            viewModel.stateFlow.value,
        )
    }

    @Test
    fun `folderStateFlow Loading should update the state to Loading`() {
        val viewModel = createViewModel()

        mutableFoldersStateFlow.tryEmit(
            DataState.Loading,
        )

        assertEquals(
            createFolderState(),
            viewModel.stateFlow.value,
        )
    }

    @Test
    fun `folderStateFlow NoNetwork should update the state to Error`() {
        val viewModel = createViewModel()

        mutableFoldersStateFlow.tryEmit(
            value = DataState.NoNetwork(
                data = listOf(),
            ),
        )

        assertEquals(
            createFolderState(
                viewState = FoldersState.ViewState.Error(
                    Universal Password ManagerString.internet_connection_required_title
                        .asText()
                        .concat(
                            " ".asText(),
                            Universal Password ManagerString.internet_connection_required_message.asText(),
                        ),
                ),
            ),
            viewModel.stateFlow.value,
        )
    }

    @Test
    fun `FolderStateFlow Pending should update the state to Content`() {
        val viewModel = createViewModel()

        mutableFoldersStateFlow.tryEmit(
            value = DataState.Pending(
                listOf(DEFAULT_FOLDER_VIEW),
            ),
        )

        assertEquals(
            createFolderState(
                viewState = FoldersState.ViewState.Content(
                    folderList = listOf(DEFAULT__DISPLAY_FOLDER),
                ),
            ),
            viewModel.stateFlow.value,
        )
    }

    private fun createViewModel(): FoldersViewModel = FoldersViewModel(
        vaultRepository = vaultRepository,
        snackbarRelayManager = snackbarRelayManager,
    )

    private fun createFolderState(
        viewState: FoldersState.ViewState = FoldersState.ViewState.Loading,
    ) = FoldersState(
        viewState = viewState,
    )
}

private val DEFAULT_FOLDER_VIEW = FolderView(
    id = "1",
    name = "test",
    revisionDate = Instant.parse("2025-04-11T10:15:30.00Z"),
)
private val DEFAULT__DISPLAY_FOLDER = FolderDisplayItem("1", "test")
