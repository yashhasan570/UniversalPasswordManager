package com.universalpasswordmanager

import android.content.Intent
import com.universalpasswordmanager.ui.platform.base.BaseViewModel
import com.universalpasswordmanager.data.auth.repository.AuthRepository
import com.universalpasswordmanager.data.auth.repository.util.getCookieCallbackResultOrNull
import com.universalpasswordmanager.data.auth.repository.util.getDuoCallbackTokenResult
import com.universalpasswordmanager.data.auth.repository.util.getSsoCallbackResult
import com.universalpasswordmanager.data.auth.repository.util.getWebAuthResultOrNull
import com.universalpasswordmanager.data.auth.util.getYubiKeyResultOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * A view model that handles logic for the [AuthCallbackActivity].
 */
@HiltViewModel
class AuthCallbackViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<Unit, Unit, AuthCallbackAction>(Unit) {
    override fun handleAction(action: AuthCallbackAction) {
        when (action) {
            is AuthCallbackAction.IntentReceive -> handleIntentReceived(action)
        }
    }

    private fun handleIntentReceived(action: AuthCallbackAction.IntentReceive) {
        val yubiKeyResult = action.intent.getYubiKeyResultOrNull()
        val webAuthResult = action.intent.getWebAuthResultOrNull()
        val duoCallbackTokenResult = action.intent.getDuoCallbackTokenResult()
        val ssoCallbackResult = action.intent.getSsoCallbackResult()
        val cookieCallbackResult = action.intent.getCookieCallbackResultOrNull()
        when {
            yubiKeyResult != null -> {
                authRepository.setYubiKeyResult(yubiKeyResult = yubiKeyResult)
            }

            duoCallbackTokenResult != null -> {
                authRepository.setDuoCallbackTokenResult(
                    tokenResult = duoCallbackTokenResult,
                )
            }

            ssoCallbackResult != null -> {
                authRepository.setSsoCallbackResult(
                    result = ssoCallbackResult,
                )
            }

            cookieCallbackResult != null -> {
                authRepository.setCookieCallbackResult(
                    result = cookieCallbackResult,
                )
            }

            webAuthResult != null -> {
                authRepository.setWebAuthResult(webAuthResult = webAuthResult)
            }

            else -> Unit
        }
    }
}

/**
 * Actions for the [AuthCallbackViewModel].
 */
sealed class AuthCallbackAction {
    /**
     * Receive Intent by the application.
     */
    data class IntentReceive(val intent: Intent) : AuthCallbackAction()
}
