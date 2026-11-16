package com.x8bit.universalpasswordmanager.data.autofill.manager

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import .vault.CipherView
import com.x8bit.universalpasswordmanager.data.auth.repository.AuthRepository
import com.x8bit.universalpasswordmanager.data.platform.manager.clipboard.Universal Password ManagerClipboardManager
import com.x8bit.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.x8bit.universalpasswordmanager.data.vault.repository.VaultRepository
import com.x8bit.universalpasswordmanager.data.vault.repository.model.GenerateTotpResult
import java.time.Clock

/**
 * Default implementation of the [AutofillTotpManager].
 */
class AutofillTotpManagerImpl(
    private val clock: Clock,
    private val clipboardManager: Universal Password ManagerClipboardManager,
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
                toastDescriptorOverride = Universal Password ManagerString.verification_code_totp.asText(),
            )
        }
    }
}
