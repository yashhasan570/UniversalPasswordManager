package com.x8bit.universalpasswordmanager.ui.auth.feature.resetPassword.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.auth.repository.model.createMockMasterPasswordPolicy
import com.x8bit.universalpasswordmanager.ui.auth.feature.resetpassword.util.toDisplayLabels
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
            persistentListOf(Universal Password ManagerString.policy_in_effect_min_length.asText(10)),
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
            persistentListOf(Universal Password ManagerString.policy_in_effect_min_complexity.asText(2)),
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
                Universal Password ManagerString.policy_in_effect_uppercase.asText(),
                Universal Password ManagerString.policy_in_effect_lowercase.asText(),
                Universal Password ManagerString.policy_in_effect_numbers.asText(),
                Universal Password ManagerString.policy_in_effect_special.asText(),
            ),
            policyList.toDisplayLabels(),
        )
    }
}
