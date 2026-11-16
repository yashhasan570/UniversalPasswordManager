package .authenticator.data.platform.datasource.disk

import androidx.core.content.edit
import app.cash.turbine.test
import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .data.datasource.disk.base.FakeSharedPreferences
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SettingDiskSourceTest {

    private val sharedPreferences: FakeSharedPreferences = FakeSharedPreferences()

    private val settingDiskSource: SettingsDiskSource = SettingsDiskSourceImpl(
        sharedPreferences = sharedPreferences,
        flightRecorderDiskSource = mockk(),
    )

    @Test
    fun `hasUserDismissedDownloadUniversal Password ManagerCard should read and write from shared preferences`() {
        val sharedPrefsKey = "bwPreferencesStorage:hasUserDismissedDownloadUniversal Password ManagerCard"

        // Shared preferences and the disk source start with the same value:
        assertNull(settingDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard)
        assertNull(sharedPreferences.getString(sharedPrefsKey, null))

        // Updating the disk source updates shared preferences:
        settingDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard = false
        assertFalse(sharedPreferences.getBoolean(sharedPrefsKey, true))

        sharedPreferences.edit {
            putBoolean(sharedPrefsKey, true)
        }
        assertTrue(settingDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard!!)
    }

    @Test
    fun `hasUserDismissedSyncWithUniversal Password ManagerCard should read and write from shared preferences`() {
        val sharedPrefsKey = "bwPreferencesStorage:hasUserDismissedSyncWithUniversal Password ManagerCard"

        // Shared preferences and the disk source start with the same value:
        assertNull(settingDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard)
        assertNull(sharedPreferences.getString(sharedPrefsKey, null))

        // Updating the disk source updates shared preferences:
        settingDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard = false
        assertFalse(sharedPreferences.getBoolean(sharedPrefsKey, true))

        sharedPreferences.edit {
            putBoolean(sharedPrefsKey, true)
        }
        assertTrue(settingDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard!!)
    }

    @Test
    fun `defaultSaveOption should read and write from shared preferences`() = runTest {
        val sharedPrefsKey = "bwPreferencesStorage:defaultSaveOption"

        settingDiskSource.defaultSaveOptionFlow.test {
            // Verify initial value is null and disk source should default to NONE
            assertNull(sharedPreferences.getString(sharedPrefsKey, null))
            assertEquals(
                DefaultSaveOption.NONE,
                settingDiskSource.defaultSaveOption,
            )
            assertEquals(
                DefaultSaveOption.NONE,
                awaitItem(),
            )

            // Updating the shared preferences should update disk source
            sharedPreferences.edit {
                putString(
                    sharedPrefsKey,
                    DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP.value,
                )
            }
            assertEquals(
                DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP,
                settingDiskSource.defaultSaveOption,
            )

            // Updating the disk source should update shared preferences
            settingDiskSource.defaultSaveOption = DefaultSaveOption.LOCAL
            assertEquals(
                DefaultSaveOption.LOCAL.value,
                sharedPreferences.getString(sharedPrefsKey, null),
            )
            assertEquals(
                DefaultSaveOption.LOCAL,
                awaitItem(),
            )

            // Incorrect value should default to DefaultSaveOption.NONE
            sharedPreferences.edit {
                putString(
                    sharedPrefsKey,
                    "invalid",
                )
            }
            assertEquals(
                DefaultSaveOption.NONE,
                settingDiskSource.defaultSaveOption,
            )
        }
    }

    @Test
    fun `previouslySyncedUniversal Password ManagerAccountIds should read and write from shared preferences`() {
        val sharedPrefsKey = "bwPreferencesStorage:previouslySyncedUniversal Password ManagerAccountIds"

        // Disk source should read value from shared preferences:
        sharedPreferences.edit {
            putStringSet(sharedPrefsKey, setOf("a"))
        }
        assertEquals(
            setOf("a"),
            settingDiskSource.previouslySyncedUniversal Password ManagerAccountIds,
        )

        // Updating the disk source should update shared preferences:
        settingDiskSource.previouslySyncedUniversal Password ManagerAccountIds = setOf("1", "2")
        assertEquals(
            setOf("1", "2"),
            settingDiskSource.previouslySyncedUniversal Password ManagerAccountIds,
        )
    }

    @Test
    fun `isShowNextCodeEnabled should read and write from shared preferences`() = runTest {
        val sharedPrefsKey = "bwPreferencesStorage:showNextCodeEnabled"

        settingDiskSource.isShowNextCodeEnabledFlow.test {
            // Shared preferences and the disk source start with the same value:
            assertNull(settingDiskSource.isShowNextCodeEnabled)
            assertNull(awaitItem())

            // Updating the disk source updates shared preferences:
            settingDiskSource.isShowNextCodeEnabled = true
            assertTrue(sharedPreferences.getBoolean(sharedPrefsKey, false))
            assertTrue(awaitItem()!!)

            // Updating the disk source to false:
            settingDiskSource.isShowNextCodeEnabled = false
            assertFalse(sharedPreferences.getBoolean(sharedPrefsKey, true))
            assertFalse(awaitItem()!!)

            // Updating shared preferences updates the disk source:
            sharedPreferences.edit {
                putBoolean(sharedPrefsKey, true)
            }
            assertTrue(settingDiskSource.isShowNextCodeEnabled!!)
        }
    }

    @Test
    fun `appTimeoutInMinutes should read and write from shared preferences`() {
        val sharedPrefsKey = "bwPreferencesStorage:appTimeoutInMinutes"

        // Shared preferences and the disk source start with the same value:
        assertNull(settingDiskSource.appTimeoutInMinutes)

        // Updating the disk source updates shared preferences:
        settingDiskSource.appTimeoutInMinutes = 60
        assertEquals(sharedPreferences.getInt(sharedPrefsKey, 0), 60)

        sharedPreferences.edit { putInt(sharedPrefsKey, 240) }
        assertEquals(settingDiskSource.appTimeoutInMinutes, 240)
    }
}
