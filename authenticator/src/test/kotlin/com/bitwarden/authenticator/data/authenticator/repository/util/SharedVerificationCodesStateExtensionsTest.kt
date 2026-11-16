package .authenticator.data.authenticator.repository.util

import .authenticator.data.authenticator.repository.model.SharedVerificationCodesState
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SharedVerificationCodesStateExtensionsTest {

    @Test
    @Suppress("MaxLineLength")
    fun `isSyncWithUniversal Password ManagerEnabled should return true only when SharedVerificationCodesState is Success `() {
        assertFalse(SharedVerificationCodesState.AppNotInstalled.isSyncWithUniversal Password ManagerEnabled)
        assertFalse(SharedVerificationCodesState.Error.isSyncWithUniversal Password ManagerEnabled)
        assertFalse(SharedVerificationCodesState.FeatureNotEnabled.isSyncWithUniversal Password ManagerEnabled)
        assertFalse(SharedVerificationCodesState.Loading.isSyncWithUniversal Password ManagerEnabled)
        assertFalse(SharedVerificationCodesState.OsVersionNotSupported.isSyncWithUniversal Password ManagerEnabled)
        assertFalse(SharedVerificationCodesState.SyncNotEnabled.isSyncWithUniversal Password ManagerEnabled)
        assertTrue(SharedVerificationCodesState.Success(emptyList()).isSyncWithUniversal Password ManagerEnabled)
    }

    @Test
    @Suppress("MaxLineLength")
    fun `itemsOrEmpty should return a non empty list only when state is Success `() {
        assertTrue(SharedVerificationCodesState.AppNotInstalled.itemsOrEmpty.isEmpty())
        assertTrue(SharedVerificationCodesState.Error.itemsOrEmpty.isEmpty())
        assertTrue(SharedVerificationCodesState.FeatureNotEnabled.itemsOrEmpty.isEmpty())
        assertTrue(SharedVerificationCodesState.Loading.itemsOrEmpty.isEmpty())
        assertTrue(SharedVerificationCodesState.OsVersionNotSupported.itemsOrEmpty.isEmpty())
        assertTrue(SharedVerificationCodesState.SyncNotEnabled.itemsOrEmpty.isEmpty())
        assertFalse(
            SharedVerificationCodesState.Success(
                listOf(mockk()),
            ).itemsOrEmpty.isEmpty(),
        )
    }
}
