package .authenticator.data.platform.repository

import app.cash.turbine.test
import .authenticator.data.platform.datasource.disk.SettingsDiskSource
import .authenticator.data.platform.manager.lock.model.AppTimeout
import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .core.data.manager.dispatcher.FakeDispatcherManager
import .core.data.repository.util.bufferedMutableSharedFlow
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SettingsRepositoryTest {

    private val mutableAppTimeoutInMinutesFlow = MutableStateFlow<Int?>(null)
    private val settingsDiskSource: SettingsDiskSource = mockk {
        every { getAlertThresholdSeconds() } returns 7
        every { appTimeoutInMinutesFlow } returns mutableAppTimeoutInMinutesFlow
        every { appTimeoutInMinutes } answers { mutableAppTimeoutInMinutesFlow.value }
    }

    private val settingsRepository: SettingsRepository = SettingsRepositoryImpl(
        settingsDiskSource = settingsDiskSource,
        flightRecorderManager = mockk(),
        dispatcherManager = FakeDispatcherManager(),
    )

    @Test
    fun `hasUserDismissedDownloadUniversal Password ManagerCard should return false when disk source is null`() {
        every { settingsDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard } returns null
        assertFalse(settingsRepository.hasUserDismissedDownloadUniversal Password ManagerCard)
    }

    @Test
    fun `hasUserDismissedDownloadUniversal Password ManagerCard should return false when disk source is false`() {
        every { settingsDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard } returns false
        assertFalse(settingsRepository.hasUserDismissedDownloadUniversal Password ManagerCard)
    }

    @Test
    fun `hasUserDismissedDownloadUniversal Password ManagerCard should return true when disk source is true`() {
        every { settingsDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard } returns true
        assertTrue(settingsRepository.hasUserDismissedDownloadUniversal Password ManagerCard)
    }

    @Test
    fun `hasUserDismissedSyncWithUniversal Password ManagerCard should return false when disk source is null`() {
        every { settingsDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard } returns null
        assertFalse(settingsRepository.hasUserDismissedSyncWithUniversal Password ManagerCard)
    }

    @Test
    fun `hasUserDismissedSyncWithUniversal Password ManagerCard should return false when disk source is false`() {
        every { settingsDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard } returns false
        assertFalse(settingsRepository.hasUserDismissedSyncWithUniversal Password ManagerCard)
    }

    @Test
    fun `hasUserDismissedSyncWithUniversal Password ManagerCard should return true when disk source is true`() {
        every { settingsDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard } returns true
        assertTrue(settingsRepository.hasUserDismissedSyncWithUniversal Password ManagerCard)
    }

    @Test
    fun `hasUserDismissedSyncWithUniversal Password ManagerCard set should set disk source`() {
        every { settingsDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard = true } just runs
        settingsRepository.hasUserDismissedSyncWithUniversal Password ManagerCard = true
        verify { settingsRepository.hasUserDismissedSyncWithUniversal Password ManagerCard = true }
    }

    @Test
    fun `defaultSaveOption should pull from and update SettingsDiskSource`() {
        // Reading from repository should read from disk source:
        every { settingsDiskSource.defaultSaveOption } returns DefaultSaveOption.NONE
        assertEquals(
            DefaultSaveOption.NONE,
            settingsRepository.defaultSaveOption,
        )
        verify { settingsDiskSource.defaultSaveOption }

        // Writing to repository should write to disk source:
        every { settingsDiskSource.defaultSaveOption = DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP } just runs
        settingsRepository.defaultSaveOption = DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP
        verify { settingsDiskSource.defaultSaveOption = DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP }
    }

    @Test
    fun `defaultSaveOptionFlow should match SettingsDiskSource`() = runTest {
        // Reading from repository should read from disk source:
        val expectedOptions = listOf(
            DefaultSaveOption.NONE,
            DefaultSaveOption.LOCAL,
            DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP,
            DefaultSaveOption.NONE,
        )
        every { settingsDiskSource.defaultSaveOptionFlow } returns flow {
            expectedOptions.forEach { emit(it) }
        }

        settingsRepository.defaultSaveOptionFlow.test {
            expectedOptions.forEach {
                assertEquals(it, awaitItem())
            }
            awaitComplete()
        }
    }

    @Test
    fun `isDynamicColorsEnabled should pull from and update SettingsDiskSource`() {
        // Reading from repository should read from disk source:
        every { settingsDiskSource.isDynamicColorsEnabled } returns null
        assertFalse(settingsRepository.isDynamicColorsEnabled)
        verify { settingsDiskSource.isDynamicColorsEnabled }

        // Writing to repository should write to disk source:
        every { settingsDiskSource.isDynamicColorsEnabled = true } just runs
        settingsRepository.isDynamicColorsEnabled = true
        verify { settingsDiskSource.isDynamicColorsEnabled = true }
    }

    @Test
    fun `isDynamicColorsEnabledFlow should match SettingsDiskSource`() = runTest {
        // Reading from repository should read from disk source:
        val mutableDynamicColorsFlow = bufferedMutableSharedFlow<Boolean?>()
        every { settingsDiskSource.isDynamicColorsEnabledFlow } returns mutableDynamicColorsFlow
        every { settingsDiskSource.isDynamicColorsEnabled } returns null

        settingsRepository.isDynamicColorsEnabledFlow.test {
            assertFalse(awaitItem())
            mutableDynamicColorsFlow.emit(true)
            assertTrue(awaitItem())
            mutableDynamicColorsFlow.emit(false)
            assertFalse(awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `appTimeoutState should pull from and update SettingsDiskSource`() {
        // Reading from repository should read from disk source:
        assertEquals(AppTimeout.Never, settingsRepository.appTimeoutState)
        verify { settingsDiskSource.appTimeoutInMinutes }

        // Writing to repository should write to disk source:
        every { settingsDiskSource.appTimeoutInMinutes = 5 } just runs
        settingsRepository.appTimeoutState = AppTimeout.FiveMinutes
        verify { settingsDiskSource.appTimeoutInMinutes = 5 }
    }

    @Test
    fun `appTimeoutStateFlow should match SettingsDiskSource`() = runTest {
        settingsRepository.appTimeoutStateFlow.test {
            assertEquals(AppTimeout.Never, awaitItem())
            mutableAppTimeoutInMinutesFlow.emit(1)
            assertEquals(AppTimeout.OneMinute, awaitItem())
            mutableAppTimeoutInMinutesFlow.emit(240)
            assertEquals(AppTimeout.FourHours, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `isShowNextCodeEnabled should default to false when disk source returns null`() {
        every { settingsDiskSource.isShowNextCodeEnabled } returns null
        assertFalse(settingsRepository.isShowNextCodeEnabled)
    }

    @Test
    fun `isShowNextCodeEnabled should return disk source value when set`() {
        every { settingsDiskSource.isShowNextCodeEnabled } returns true
        assertTrue(settingsRepository.isShowNextCodeEnabled)
    }

    @Test
    fun `isShowNextCodeEnabled setter should write to disk source`() {
        every { settingsDiskSource.isShowNextCodeEnabled = true } just runs
        settingsRepository.isShowNextCodeEnabled = true
        verify { settingsDiskSource.isShowNextCodeEnabled = true }
    }

    @Test
    fun `isShowNextCodeEnabledFlow should map null to false and emit updates`() = runTest {
        val mutableShowNextCodeFlow = bufferedMutableSharedFlow<Boolean?>()
        every { settingsDiskSource.isShowNextCodeEnabledFlow } returns mutableShowNextCodeFlow
        every { settingsDiskSource.isShowNextCodeEnabled } returns null

        settingsRepository.isShowNextCodeEnabledFlow.test {
            assertFalse(awaitItem())
            mutableShowNextCodeFlow.emit(true)
            assertTrue(awaitItem())
            mutableShowNextCodeFlow.emit(false)
            assertFalse(awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `previouslySyncedUniversal Password ManagerAccountIds should pull from and update SettingsDiskSource`() {
        // Reading from repository should read from disk source:
        every { settingsDiskSource.previouslySyncedUniversal Password ManagerAccountIds } returns emptySet()
        assertEquals(
            emptySet<String>(),
            settingsRepository.previouslySyncedUniversal Password ManagerAccountIds,
        )
        verify { settingsDiskSource.previouslySyncedUniversal Password ManagerAccountIds }

        // Writing to repository should write to disk source:
        every {
            settingsDiskSource.previouslySyncedUniversal Password ManagerAccountIds = setOf("1", "2", "3")
        } just runs
        settingsRepository.previouslySyncedUniversal Password ManagerAccountIds = setOf("1", "2", "3")
        verify { settingsDiskSource.previouslySyncedUniversal Password ManagerAccountIds = setOf("1", "2", "3") }
    }
}
