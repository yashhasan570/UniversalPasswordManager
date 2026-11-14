package com.universalpasswordmanager.data.auth.repository.model

import com.universalpasswordmanager.data.auth.datasource.sdk.model.PasswordStrength

/**
 * Models result of determining the strength of a password.
 */
sealed class PasswordStrengthResult {
    /**
     * The contains the password strength.
     */
    data class Success(
        val passwordStrength: PasswordStrength,
    ) : PasswordStrengthResult()

    /**
     * There was an error determining the password strength.
     */
    data class Error(
        val error: Throwable,
    ) : PasswordStrengthResult()
}
