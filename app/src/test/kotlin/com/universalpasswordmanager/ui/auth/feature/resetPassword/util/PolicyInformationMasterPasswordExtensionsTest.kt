package com.universalpasswordmanager.ui.auth.feature.resetPassword.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.auth.repository.model.createMockMasterPasswordPolicy
import com.universalpasswordmanager.ui.auth.feature.resetpassword.util.toDisplayLabels
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PolicyInformationMasterPasswordExtensionsTest {
    @Test
    fun `toDisplayLabels with multiple minLength values should choose highest value`() {
        val policyList = listOf(
            createMockMasterPasswordPolicy(minLength = null),
            createMockMasterPasswordPolicy(minLength = 10),
            createMockMasterPasswordPolicy(minLength = 2),
        )
        assertEquals(
            persistentListOf(UniversalString.policy_in_effect_min_length.asText(10)),
            policyList.toDisplayLabels(),
        )
    }

    @Test
    fun `toDisplayLabels with multiple minComplexity values should choose highest value`() {
        val policyList = listOf(
            createMockMasterPasswordPolicy(minComplexity = null),
            createMockMasterPasswordPolicy(minComplexity = 1),
            createMockMasterPasswordPolicy(minComplexity = 2),
        )
        assertEquals(
            persistentListOf(UniversalString.policy_in_effect_min_complexity.asText(2)),
            policyList.toDisplayLabels(),
        )
    }

    @Test
    fun `toDisplayLabels lists any nonNull requirements`() {
        val policyList = listOf(
            createMockMasterPasswordPolicy(requireUpper = true),
            createMockMasterPasswordPolicy(requireLower = true),
            createMockMasterPasswordPolicy(requireNumbers = true),
            createMockMasterPasswordPolicy(requireSpecial = true),
        )
        assertEquals(
            persistentListOf(
                UniversalString.policy_in_effect_uppercase.asText(),
                UniversalString.policy_in_effect_lowercase.asText(),
                UniversalString.policy_in_effect_numbers.asText(),
                UniversalString.policy_in_effect_special.asText(),
            ),
            policyList.toDisplayLabels(),
        )
    }
}
