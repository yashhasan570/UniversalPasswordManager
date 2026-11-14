package com.universalpasswordmanager.data.autofill.manager

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.vault.CipherView
import com.universalpasswordmanager.data.auth.repository.AuthRepository
import com.universalpasswordmanager.data.platform.manager.clipboard.UniversalClipboardManager
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.universalpasswordmanager.data.vault.repository.VaultRepository
import com.universalpasswordmanager.data.vault.repository.model.GenerateTotpResult
import java.time.Clock

/**
 * Default implementation of the [AutofillTotpManager].
 */
class AutofillTotpManagerImpl(
    private val clock: Clock,
    private val clipboardManager: UniversalClipboardManager,
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val vaultRepository: VaultRepository,
) : AutofillTotpManager {
    override suspend fun tryCopyTotpToClipboard(cipherView: CipherView) {
        if (settingsRepository.isAutoCopyTotpDisabled) return
        val isPremium = authRepository.userStateFlow.value?.activeAccount?.isPremium == true
        if (!isPremium && !cipherView.organizationUseTotp) return
        cipherView.login?.totp ?: return
        val cipherId = cipherView.id ?: return

        val totpResult = vaultRepository.generateTotp(
            time = clock.instant(),
            cipherId = cipherId,
        )

        if (totpResult is GenerateTotpResult.Success) {
            clipboardManager.setText(
                text = totpResult.code,
                toastDescriptorOverride = UniversalString.verification_code_totp.asText(),
            )
        }
    }
}
