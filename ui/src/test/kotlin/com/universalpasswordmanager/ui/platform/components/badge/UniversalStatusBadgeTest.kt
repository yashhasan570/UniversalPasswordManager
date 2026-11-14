package com.universalpasswordmanager.ui.platform.components.badge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import com.universalpasswordmanager.ui.platform.base.BaseComposeTest
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import org.junit.Test

class UniversalStatusBadgeTest : BaseComposeTest() {

    @Test
    fun `success variant renders with label`() {
        setTestContent {
            UniversalTheme {
                UniversalStatusBadge(
                    label = "Active",
                    colors = UniversalTheme.colorScheme.statusBadge.success,
                )
            }
        }
        composeTestRule.onNodeWithText("Active").assertIsDisplayed()
    }

    @Test
    fun `error variant renders with label`() {
        setTestContent {
            UniversalTheme {
                UniversalStatusBadge(
                    label = "Canceled",
                    colors = UniversalTheme.colorScheme.statusBadge.error,
                )
            }
        }
        composeTestRule.onNodeWithText("Canceled").assertIsDisplayed()
    }

    @Test
    fun `warning variant renders with label`() {
        setTestContent {
            UniversalTheme {
                UniversalStatusBadge(
                    label = "Overdue payment",
                    colors = UniversalTheme.colorScheme.statusBadge.warning,
                )
            }
        }
        composeTestRule.onNodeWithText("Overdue payment").assertIsDisplayed()
    }
}
