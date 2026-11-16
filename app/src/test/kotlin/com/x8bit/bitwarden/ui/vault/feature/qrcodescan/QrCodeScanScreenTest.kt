package com.x8bit.universalpasswordmanager.ui.vault.feature.qrcodescan

import androidx.compose.ui.test.onNodeWithText
import .core.data.repository.util.bufferedMutableSharedFlow
import .ui.platform.feature.qrcodescan.util.FakeQrCodeAnalyzer
import .ui.util.performCustomAccessibilityAction
import com.x8bit.universalpasswordmanager.ui.platform.base.Universal Password ManagerComposeTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

class QrCodeScanScreenTest : Universal Password ManagerComposeTest() {

    private var onNavigateBackCalled = false
    private var onNavigateToManualCodeEntryScreenCalled = false

    private val qrCodeAnalyzer = FakeQrCodeAnalyzer()

    private val mutableEventFlow = bufferedMutableSharedFlow<QrCodeScanEvent>()

    private val viewModel = mockk<QrCodeScanViewModel>(relaxed = true) {
        every { eventFlow } returns mutableEventFlow
    }

    @Before
    fun setup() {
        setContent(
            qrCodeAnalyzer = qrCodeAnalyzer,
        ) {
            QrCodeScanScreen(
                onNavigateBack = { onNavigateBackCalled = true },
                viewModel = viewModel,
                onNavigateToManualCodeEntryScreen = {
                    onNavigateToManualCodeEntryScreenCalled = true
                },
            )
        }
    }

    @Test
    fun `on NavigateBack event should invoke onNavigateBack`() {
        mutableEventFlow.tryEmit(QrCodeScanEvent.NavigateBack)
        assertTrue(onNavigateBackCalled)
    }

    @Test
    fun `on NavigateToManualCodeEntry event should invoke onNavigateToManualCodeEntryScreen`() {
        mutableEventFlow.tryEmit(QrCodeScanEvent.NavigateToManualCodeEntry)
        assertTrue(onNavigateToManualCodeEntryScreenCalled)
    }

    @Config(qualifiers = "land")
    @Test
    fun `clicking on manual text should send ManualEntryTextClick in landscape mode`() = runTest {
        composeTestRule
            .onNodeWithText(text = "Cannot scan QR code? Enter key manually")
            .performCustomAccessibilityAction(label = "Enter key manually")

        verify {
            viewModel.trySendAction(QrCodeScanAction.ManualEntryTextClick)
        }
    }

    @Test
    fun `clicking on manual text should send ManualEntryTextClick`() = runTest {
        composeTestRule
            .onNodeWithText(text = "Cannot scan QR code? Enter key manually")
            .performCustomAccessibilityAction(label = "Enter key manually")

        verify {
            viewModel.trySendAction(QrCodeScanAction.ManualEntryTextClick)
        }
    }
}
