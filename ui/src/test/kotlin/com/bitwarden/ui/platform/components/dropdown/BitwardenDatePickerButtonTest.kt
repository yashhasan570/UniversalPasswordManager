package .ui.platform.components.dropdown

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import .ui.platform.base.BaseComposeTest
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class Universal Password ManagerDatePickerButtonTest : BaseComposeTest() {

    @Test
    fun `dialog should stay open when switching modes after the button scrolls away`() {
        // Match a device, where tapping does not move focus to the tapped button.
        InstrumentationRegistry.getInstrumentation().setInTouchMode(true)
        lateinit var lazyListState: LazyListState
        setTestContent {
            Universal Password ManagerTheme {
                lazyListState = rememberLazyListState()
                LazyColumn(state = lazyListState) {
                    item {
                        Universal Password ManagerDatePickerButton(
                            label = LABEL,
                            currentDate = null,
                            onDateSelect = {},
                            cardStyle = null,
                            clock = FIXED_CLOCK,
                        )
                    }
                    items(count = 50) {
                        Spacer(modifier = Modifier.height(height = 200.dp))
                    }
                }
            }
        }

        composeTestRule.onNodeWithText(text = LABEL).performClick()
        composeTestRule
            .onNodeWithContentDescription(label = "Switch to text input mode")
            .performClick()
        composeTestRule.onNodeWithText(text = "Entered date").assertIsDisplayed()

        composeTestRule.runOnUiThread {
            runBlocking { lazyListState.scrollToItem(index = 40) }
        }
        composeTestRule
            .onNodeWithContentDescription(label = "Switch to calendar input mode")
            .performClick()

        composeTestRule.onNodeWithTag(testTag = "DatePickerDialog").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Selected date").assertIsDisplayed()
    }
}

private const val LABEL: String = "Date of birth"

private val FIXED_CLOCK: Clock = Clock.fixed(
    Instant.parse("2026-10-26T12:00:00Z"),
    ZoneOffset.UTC,
)
