package .authenticator.ui.authenticator.feature.qrcodescan

import android.os.Parcelable
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import .authenticator.data.authenticator.manager.TotpCodeManager
import .authenticator.data.authenticator.repository.AuthenticatorRepository
import .authenticator.data.authenticator.repository.model.TotpCodeResult
import .authenticator.data.authenticator.repository.util.isSyncWithUniversal Password ManagerEnabled
import .authenticator.data.platform.repository.SettingsRepository
import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .authenticatorbridge.manager.AuthenticatorBridgeManager
import .ui.platform.base.BaseViewModel
import .ui.platform.base.DeferredBackgroundEvent
import .ui.platform.util.getTotpDataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

private const val KEY_STATE = "state"

/**
 * Handles [QrCodeScanAction] and launches [QrCodeScanEvent] for the [QrCodeScanScreen].
 */
@HiltViewModel
@Suppress("TooManyFunctions")
class QrCodeScanViewModel @Inject constructor(
    private val authenticatorBridgeManager: AuthenticatorBridgeManager,
    private val authenticatorRepository: AuthenticatorRepository,
    private val settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<QrCodeScanState, QrCodeScanEvent, QrCodeScanAction>(
    initialState = savedStateHandle[KEY_STATE]
        ?: QrCodeScanState(
            hasHandledScan = false,
            dialog = null,
        ),
) {

    /**
     * Keeps track of a pending successful scan to support the case where the user is choosing
     * default save location.
     */
    private var pendingSuccessfulScan: TotpCodeResult.TotpCodeScan? = null

    override fun handleAction(action: QrCodeScanAction) {
        when (action) {
            is QrCodeScanAction.CloseClick -> handleCloseClick()
            is QrCodeScanAction.ManualEntryTextClick -> handleManualEntryTextClick()
            is QrCodeScanAction.CameraSetupErrorReceive -> handleCameraErrorReceive()
            is QrCodeScanAction.QrCodeScanReceive -> handleQrCodeScanReceive(action)
            QrCodeScanAction.SaveToUniversal Password ManagerErrorDismiss -> handleSaveToUniversal Password ManagerDismiss()
            is QrCodeScanAction.SaveLocallyClick -> handleSaveLocallyClick(action)
            is QrCodeScanAction.SaveToUniversal Password ManagerClick -> handleSaveToUniversal Password ManagerClick(action)
        }
    }

    private fun handleSaveToUniversal Password ManagerClick(action: QrCodeScanAction.SaveToUniversal Password ManagerClick) {
        if (action.saveAsDefault) {
            settingsRepository.defaultSaveOption = DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP
        }
        pendingSuccessfulScan?.let {
            saveCodeToUniversal Password ManagerAndNavigateBack(it)
        }
        pendingSuccessfulScan = null
    }

    private fun handleSaveLocallyClick(action: QrCodeScanAction.SaveLocallyClick) {
        if (action.saveAsDefault) {
            settingsRepository.defaultSaveOption = DefaultSaveOption.LOCAL
        }
        pendingSuccessfulScan?.let {
            saveCodeLocallyAndNavigateBack(it)
        }
        pendingSuccessfulScan = null
    }

    private fun handleSaveToUniversal Password ManagerDismiss() {
        mutableStateFlow.update {
            it.copy(
                hasHandledScan = false,
                dialog = null,
            )
        }
    }

    private fun handleCloseClick() {
        sendEvent(QrCodeScanEvent.NavigateBack)
    }

    private fun handleManualEntryTextClick() {
        sendEvent(QrCodeScanEvent.NavigateToManualCodeEntry)
    }

    private fun handleQrCodeScanReceive(action: QrCodeScanAction.QrCodeScanReceive) {
        if (state.hasHandledScan) {
            return
        }
        mutableStateFlow.update {
            it.copy(hasHandledScan = true)
        }
        val scannedCode = action.qrCode
        if (scannedCode.startsWith(TotpCodeManager.TOTP_CODE_PREFIX)) {
            handleTotpUriReceive(scannedCode)
        } else if (scannedCode.startsWith(TotpCodeManager.GOOGLE_EXPORT_PREFIX)) {
            handleGoogleExportUriReceive(scannedCode)
        } else {
            authenticatorRepository.emitTotpCodeResult(TotpCodeResult.CodeScanningError)
            sendEvent(QrCodeScanEvent.NavigateBack)
        }
    }

    // For more information: https:///help/authenticator-keys/#support-for-more-parameters
    private fun handleTotpUriReceive(scannedCode: String) {
        scannedCode
            .getTotpDataOrNull()
            ?.let {
                val result = TotpCodeResult.TotpCodeScan(code = scannedCode)
                if (authenticatorRepository.sharedCodesStateFlow.value.isSyncWithUniversal Password ManagerEnabled) {
                    when (settingsRepository.defaultSaveOption) {
                        DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP -> {
                            saveCodeToUniversal Password ManagerAndNavigateBack(result = result)
                        }

                        DefaultSaveOption.LOCAL -> saveCodeLocallyAndNavigateBack(result = result)
                        DefaultSaveOption.NONE -> {
                            pendingSuccessfulScan = result
                            mutableStateFlow.update {
                                it.copy(dialog = QrCodeScanState.DialogState.ChooseSaveLocation)
                            }
                        }
                    }
                } else {
                    // Syncing with Universal Password Manager not enabled, save code locally:
                    saveCodeLocallyAndNavigateBack(result = result)
                }
            }
            ?: run {
                authenticatorRepository.emitTotpCodeResult(TotpCodeResult.CodeScanningError)
                sendEvent(QrCodeScanEvent.NavigateBack)
            }
    }

    private fun handleGoogleExportUriReceive(scannedCode: String) {
        val uri = scannedCode.toUri()
        val encodedData = uri.getQueryParameter(TotpCodeManager.DATA_PARAM)
        if (encodedData.isNullOrEmpty()) {
            authenticatorRepository.emitTotpCodeResult(TotpCodeResult.CodeScanningError)
        } else {
            authenticatorRepository.emitTotpCodeResult(TotpCodeResult.GoogleExportScan(encodedData))
        }
        sendEvent(QrCodeScanEvent.NavigateBack)
    }

    private fun handleCameraErrorReceive() {
        sendEvent(QrCodeScanEvent.NavigateToManualCodeEntry)
    }

    private fun saveCodeToUniversal Password ManagerAndNavigateBack(result: TotpCodeResult.TotpCodeScan) {
        val didLaunchAddToUniversal Password Manager =
            authenticatorBridgeManager.startAddTotpLoginItemFlow(result.code)
        if (didLaunchAddToUniversal Password Manager) {
            sendEvent(QrCodeScanEvent.NavigateBack)
        } else {
            mutableStateFlow.update {
                it.copy(dialog = QrCodeScanState.DialogState.SaveToUniversal Password ManagerError)
            }
        }
    }

    private fun saveCodeLocallyAndNavigateBack(result: TotpCodeResult.TotpCodeScan) {
        authenticatorRepository.emitTotpCodeResult(result)
        sendEvent(QrCodeScanEvent.NavigateBack)
    }
}

/**
 * Models state for [QrCodeScanViewModel].
 *
 * @param dialog Dialog to be shown, or `null` if no dialog should be shown.
 */
@Parcelize
data class QrCodeScanState(
    val dialog: DialogState?,
    val hasHandledScan: Boolean,
) : Parcelable {

    /**
     * Models dialogs that can be shown on the QR Scan screen.
     */
    sealed class DialogState : Parcelable {
        /**
         * Displays a prompt to choose save location for a newly scanned code.
         */
        @Parcelize
        data object ChooseSaveLocation : DialogState()

        /**
         * Displays an error letting the user know that saving to universalpasswordmanager failed.
         */
        @Parcelize
        data object SaveToUniversal Password ManagerError : DialogState()
    }
}

/**
 * Models events for the [QrCodeScanScreen].
 */
sealed class QrCodeScanEvent {

    /**
     * Navigate back.
     * Added DeferredBackgroundEvent as QrCodeScan might be fired before events are consumed
     */
    data object NavigateBack : QrCodeScanEvent(), DeferredBackgroundEvent

    /**
     * Navigate to manual code entry screen.
     */
    data object NavigateToManualCodeEntry : QrCodeScanEvent()
}

/**
 * Models actions for the [QrCodeScanScreen].
 */
sealed class QrCodeScanAction {

    /**
     * User clicked close.
     */
    data object CloseClick : QrCodeScanAction()

    /**
     * The user has scanned a QR code.
     */
    data class QrCodeScanReceive(val qrCode: String) : QrCodeScanAction()

    /**
     * The text to switch to manual entry is clicked.
     */
    data object ManualEntryTextClick : QrCodeScanAction()

    /**
     * The Camera is unable to be setup.
     */
    data object CameraSetupErrorReceive : QrCodeScanAction()

    /**
     * The user dismissed the Save to Universal Password Manager error dialog.
     */
    data object SaveToUniversal Password ManagerErrorDismiss : QrCodeScanAction()

    /**
     * User clicked save to Universal Password Manager on the choose save location dialog.
     *
     * @param saveAsDefault Whether the user checked "Save as default".
     */
    data class SaveToUniversal Password ManagerClick(val saveAsDefault: Boolean) : QrCodeScanAction()

    /**
     * User clicked save locally on the save to Universal Password Manager dialog.
     *
     * @param saveAsDefault Whether the user checked "Save as default".
     */
    data class SaveLocallyClick(val saveAsDefault: Boolean) : QrCodeScanAction()
}
