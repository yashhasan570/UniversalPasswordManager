package .authenticator.ui.authenticator.feature.manualcodeentry

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemEntity
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemType
import .authenticator.data.authenticator.manager.TotpCodeManager
import .authenticator.data.authenticator.repository.AuthenticatorRepository
import .authenticator.data.authenticator.repository.model.SharedVerificationCodesState
import .authenticator.data.authenticator.repository.util.isSyncWithUniversal Password ManagerEnabled
import .authenticator.data.platform.repository.SettingsRepository
import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .authenticator.ui.platform.model.SnackbarRelay
import .authenticatorbridge.manager.AuthenticatorBridgeManager
import .ui.platform.base.BaseViewModel
import .ui.platform.base.util.isBase32
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.manager.snackbar.SnackbarRelayManager
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.UUID
import javax.inject.Inject

private const val KEY_STATE = "state"

/**
 * The ViewModel for handling user interactions in the manual code entry screen.
 *
 */
@HiltViewModel
@Suppress("TooManyFunctions")
class ManualCodeEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authenticatorRepository: AuthenticatorRepository,
    private val authenticatorBridgeManager: AuthenticatorBridgeManager,
    private val snackbarRelayManager: SnackbarRelayManager<SnackbarRelay>,
    settingsRepository: SettingsRepository,
) : BaseViewModel<ManualCodeEntryState, ManualCodeEntryEvent, ManualCodeEntryAction>(
    initialState = savedStateHandle[KEY_STATE]
        ?: ManualCodeEntryState(
            code = "",
            issuer = "",
            dialog = null,
            buttonState = deriveButtonState(
                sharedCodesState = authenticatorRepository.sharedCodesStateFlow.value,
                defaultSaveOption = settingsRepository.defaultSaveOption,
            ),
        ),
) {
    override fun handleAction(action: ManualCodeEntryAction) {
        when (action) {
            is ManualCodeEntryAction.CloseClick -> handleCloseClick()
            is ManualCodeEntryAction.CodeTextChange -> handleCodeTextChange(action)
            is ManualCodeEntryAction.IssuerTextChange -> handleIssuerTextChange(action)
            is ManualCodeEntryAction.ScanQrCodeTextClick -> handleScanQrCodeTextClick()
            is ManualCodeEntryAction.SettingsClick -> handleSettingsClick()
            ManualCodeEntryAction.DismissDialog -> {
                handleDialogDismiss()
            }

            ManualCodeEntryAction.SaveLocallyClick -> handleSaveLocallyClick()
            ManualCodeEntryAction.SaveToUniversal Password ManagerClick -> handleSaveToUniversal Password ManagerClick()
        }
    }

    private fun handleDialogDismiss() {
        mutableStateFlow.update { it.copy(dialog = null) }
    }

    private fun handleIssuerTextChange(action: ManualCodeEntryAction.IssuerTextChange) {
        mutableStateFlow.update {
            it.copy(issuer = action.issuer)
        }
    }

    private fun handleCloseClick() {
        sendEvent(ManualCodeEntryEvent.NavigateBack)
    }

    private fun handleCodeTextChange(action: ManualCodeEntryAction.CodeTextChange) {
        mutableStateFlow.update {
            it.copy(code = action.code)
        }
    }

    private fun handleSaveLocallyClick() = handleCodeSubmit(saveToUniversal Password Manager = false)

    private fun handleSaveToUniversal Password ManagerClick() = handleCodeSubmit(saveToUniversal Password Manager = true)

    private fun handleCodeSubmit(saveToUniversal Password Manager: Boolean) {
        val isSteamCode = state.code.startsWith(TotpCodeManager.STEAM_CODE_PREFIX)
        val sanitizedCode = state.code
            .replace(" ", "")
            .replace(TotpCodeManager.STEAM_CODE_PREFIX, "")
        if (sanitizedCode.isBlank()) {
            showErrorDialog(Universal Password ManagerString.key_is_required.asText())
            return
        }

        if (!sanitizedCode.isBase32()) {
            showErrorDialog(Universal Password ManagerString.key_is_invalid.asText())
            return
        }

        if (state.issuer.isBlank()) {
            showErrorDialog(Universal Password ManagerString.name_is_required.asText())
            return
        }

        if (saveToUniversal Password Manager) {
            // Save to Universal Password Manager by kicking off save to Universal Password Manager flow:
            saveValidCodeToUniversal Password Manager(sanitizedCode)
        } else {
            // Save locally by giving entity to AuthRepository and navigating back:
            saveValidCodeLocally(sanitizedCode, isSteamCode)
        }
    }

    private fun saveValidCodeToUniversal Password Manager(sanitizedCode: String) {
        val didLaunchSaveToUniversal Password Manager = authenticatorBridgeManager
            .startAddTotpLoginItemFlow(
                totpUri = "otpauth://totp/?secret=$sanitizedCode&issuer=${state.issuer}",
            )
        if (!didLaunchSaveToUniversal Password Manager) {
            mutableStateFlow.update {
                it.copy(
                    dialog = ManualCodeEntryState.DialogState.Error(
                        title = Universal Password ManagerString.something_went_wrong.asText(),
                        message = Universal Password ManagerString.please_try_again.asText(),
                    ),
                )
            }
        } else {
            sendEvent(ManualCodeEntryEvent.NavigateBack)
        }
    }

    private fun saveValidCodeLocally(
        sanitizedCode: String,
        isSteamCode: Boolean,
    ) {
        viewModelScope.launch {
            authenticatorRepository.createItem(
                AuthenticatorItemEntity(
                    id = UUID.randomUUID().toString(),
                    key = sanitizedCode,
                    issuer = state.issuer,
                    accountName = null,
                    userId = null,
                    type = if (isSteamCode) {
                        AuthenticatorItemType.STEAM
                    } else {
                        AuthenticatorItemType.TOTP
                    },
                    favorite = false,
                ),
            )
            snackbarRelayManager.sendSnackbarData(
                data = Universal Password ManagerSnackbarData(Universal Password ManagerString.verification_code_added.asText()),
                relay = SnackbarRelay.ITEM_ADDED,
            )
            sendEvent(event = ManualCodeEntryEvent.NavigateBack)
        }
    }

    private fun handleScanQrCodeTextClick() {
        sendEvent(ManualCodeEntryEvent.NavigateToQrCodeScreen)
    }

    private fun handleSettingsClick() {
        sendEvent(ManualCodeEntryEvent.NavigateToAppSettings)
    }

    private fun showErrorDialog(message: Text) {
        mutableStateFlow.update {
            it.copy(
                dialog = ManualCodeEntryState.DialogState.Error(
                    message = message,
                ),
            )
        }
    }
}

private fun deriveButtonState(
    sharedCodesState: SharedVerificationCodesState,
    defaultSaveOption: DefaultSaveOption,
): ManualCodeEntryState.ButtonState {
    // If syncing with Universal Password Manager is not enabled, show local save only:
    if (!sharedCodesState.isSyncWithUniversal Password ManagerEnabled) {
        return ManualCodeEntryState.ButtonState.LocalOnly
    }
    // Otherwise, show save options based on user's preferences:
    return when (defaultSaveOption) {
        DefaultSaveOption.NONE -> ManualCodeEntryState.ButtonState.SaveToUniversal Password ManagerPrimary
        DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP -> ManualCodeEntryState.ButtonState.SaveToUniversal Password ManagerPrimary
        DefaultSaveOption.LOCAL -> ManualCodeEntryState.ButtonState.SaveLocallyPrimary
    }
}

/**
 * Models state of the manual entry screen.
 */
@Parcelize
data class ManualCodeEntryState(
    val code: String,
    val issuer: String,
    val dialog: DialogState?,
    val buttonState: ButtonState,
) : Parcelable {

    /**
     * Models dialog states for [ManualCodeEntryViewModel].
     */
    @Parcelize
    sealed class DialogState : Parcelable {

        /**
         * Show an error dialog with an optional [title], and a [message].
         */
        @Parcelize
        data class Error(
            val title: Text? = null,
            val message: Text,
        ) : DialogState()

        /**
         * Show a loading dialog.
         */
        @Parcelize
        data class Loading(
            val message: Text,
        ) : DialogState()
    }

    /**
     * Models what variation of button states should be shown.
     */
    @Parcelize
    sealed class ButtonState : Parcelable {

        /**
         * Show only save locally option.
         */
        @Parcelize
        data object LocalOnly : ButtonState()

        /**
         * Show both save locally and save to Universal Password Manager, with Universal Password Manager being the primary option.
         */
        @Parcelize
        data object SaveToUniversal Password ManagerPrimary : ButtonState()

        /**
         * Show both save locally and save to Universal Password Manager, with locally being the primary option.
         */
        @Parcelize
        data object SaveLocallyPrimary : ButtonState()
    }
}

/**
 * Models events for the [ManualCodeEntryScreen].
 */
sealed class ManualCodeEntryEvent {

    /**
     * Navigate back.
     */
    data object NavigateBack : ManualCodeEntryEvent()

    /**
     * Navigate to the Qr code screen.
     */
    data object NavigateToQrCodeScreen : ManualCodeEntryEvent()

    /**
     * Navigate to the app settings.
     */
    data object NavigateToAppSettings : ManualCodeEntryEvent()
}

/**
 * Models actions for the [ManualCodeEntryScreen].
 */
sealed class ManualCodeEntryAction {

    /**
     * User clicked close.
     */
    data object CloseClick : ManualCodeEntryAction()

    /**
     * The user clicked the save locally button.
     */
    data object SaveLocallyClick : ManualCodeEntryAction()

    /**
     * Th user clicked the save to Universal Password Manager button.
     */
    data object SaveToUniversal Password ManagerClick : ManualCodeEntryAction()

    /**
     * The user has changed the code text.
     */
    data class CodeTextChange(val code: String) : ManualCodeEntryAction()

    /**
     * The use has changed the issuer text.
     */
    data class IssuerTextChange(val issuer: String) : ManualCodeEntryAction()

    /**
     * The text to switch to QR code scanning is clicked.
     */
    data object ScanQrCodeTextClick : ManualCodeEntryAction()

    /**
     * The action for the user clicking the settings button.
     */
    data object SettingsClick : ManualCodeEntryAction()

    /**
     * The user has dismissed the dialog.
     */
    data object DismissDialog : ManualCodeEntryAction()
}
