package com.universalpasswordmanager.ui.platform.components.field

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.universalpasswordmanager.ui.platform.base.BaseComposeTest
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import org.junit.Assert.assertEquals
import org.junit.Test

class UniversalPasswordFieldTest : BaseComposeTest() {

    @Test
    fun `read only hidden password uses fixed length mask`() {
        val password = "correct horse battery staple"

        setTestContent {
            UniversalTheme {
                UniversalPasswordField(
                    label = "Password",
                    value = password,
                    showPassword = false,
                    showPasswordChange = { },
                    onValueChange = { },
                    readOnly = true,
                    cardStyle = CardStyle.Full,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Password")
            .assertTextEquals("Password", "••••••••")
    }

    @Test
    fun `read only visible password shows actual value`() {
        val password = "correct horse battery staple"

        setTestContent {
            UniversalTheme {
                UniversalPasswordField(
                    label = "Password",
                    value = password,
                    showPassword = true,
                    showPasswordChange = { },
                    onValueChange = { },
                    readOnly = true,
                    cardStyle = CardStyle.Full,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Password")
            .assertTextEquals("Password", password)
    }

    @Test
    fun `read only hidden password always uses fixed length mask`() {
        setTestContent {
            UniversalTheme {
                UniversalPasswordField(
                    label = "Password",
                    value = "12345",
                    showPassword = false,
                    showPasswordChange = { },
                    onValueChange = { },
                    readOnly = true,
                    cardStyle = CardStyle.Full,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Password")
            .assertTextEquals("Password", "••••••••")
    }

    @Test
    fun `editable hidden password preserves value length`() {
        setTestContent {
            UniversalTheme {
                UniversalPasswordField(
                    label = "Password",
                    value = "12345",
                    showPassword = false,
                    showPasswordChange = { },
                    onValueChange = { },
                    readOnly = false,
                    cardStyle = CardStyle.Full,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Password")
            .assertTextEquals("Password", "•••••")
    }

    @Test
    fun `tapping a masked read only field does not emit the mask`() {
        val emitted = mutableListOf<String>()

        setTestContent {
            UniversalTheme {
                UniversalPasswordField(
                    label = "Password",
                    value = "correct horse battery staple",
                    showPassword = false,
                    showPasswordChange = { },
                    onValueChange = { emitted += it },
                    readOnly = true,
                    cardStyle = CardStyle.Full,
                )
            }
        }

        composeTestRule.onNodeWithText("Password").performClick()
        composeTestRule.waitForIdle()

        assertEquals(emptyList<String>(), emitted)
    }

    @Test
    fun `non interactable hidden password uses fixed length mask`() {
        setTestContent {
            UniversalTheme {
                UniversalHiddenPasswordField(
                    label = "Password",
                    value = "correct horse battery staple",
                    cardStyle = CardStyle.Full,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Password")
            .assertTextEquals("Password", "••••••••")
    }
}
