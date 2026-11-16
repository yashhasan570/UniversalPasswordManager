package .ui.platform.components.badge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import .ui.platform.base.BaseComposeTest
import .ui.platform.theme.Universal Password ManagerTheme
import org.junit.Test

class Universal Password ManagerStatusBadgeTest : BaseComposeTest() {

    @Test
    fun `success variant renders with label`() {
        setTestContent {
            Universal Password ManagerTheme {
                Universal Password ManagerStatusBadge(
                    label = "Active",
                    colors = Universal Password ManagerTheme.colorScheme.statusBadge.success,
                )
            }
        }
        composeTestRule.onNodeWithText("Active").assertIsDisplayed()
    }

    @Test
    fun `error variant renders with label`() {
        setTestContent {
            Universal Password ManagerTheme {
                Universal Password ManagerStatusBadge(
                    label = "Canceled",
                    colors = Universal Password ManagerTheme.colorScheme.statusBadge.error,
                )
            }
        }
        composeTestRule.onNodeWithText("Canceled").assertIsDisplayed()
    }

    @Test
    fun `warning variant renders with label`() {
        setTestContent {
            Universal Password ManagerTheme {
                Universal Password ManagerStatusBadge(
                    label = "Overdue payment",
                    colors = Universal Password ManagerTheme.colorScheme.statusBadge.warning,
                )
            }
        }
        composeTestRule.onNodeWithText("Overdue payment").assertIsDisplayed()
    }
}
