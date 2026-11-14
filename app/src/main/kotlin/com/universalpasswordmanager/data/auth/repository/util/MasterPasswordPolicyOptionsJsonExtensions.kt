package com.universalpasswordmanager.data.auth.repository.util

import com.universalpasswordmanager.network.model.MasterPasswordPolicyOptionsJson
import com.universalpasswordmanager.data.auth.repository.model.PolicyInformation

/**
 * Converts the [MasterPasswordPolicyOptionsJson] to a [PolicyInformation.MasterPassword].
 */
fun MasterPasswordPolicyOptionsJson.toPolicyInformation(): PolicyInformation.MasterPassword =
    PolicyInformation.MasterPassword(
        minLength = this.minimumLength,
        minComplexity = this.minimumComplexity,
        requireUpper = this.shouldRequireUppercase,
        requireLower = this.shouldRequireLowercase,
        requireNumbers = this.shouldRequireNumbers,
        requireSpecial = this.shouldRequireSpecialCharacters,
        enforceOnLogin = this.shouldEnforceOnLogin,
    )
