package com.universalpasswordmanager.data.platform.manager

import com.universalpasswordmanager.core.data.manager.dispatcher.FakeDispatcherManager
import com.universalpasswordmanager.core.util.isBuildVersionAtLeast
import com.universalpasswordmanager.data.manager.NativeLibraryManager
import com.universalpasswordmanager.sdk.LogLevel
import com.universalpasswordmanager.sdk.initLogger
import com.universalpasswordmanager.data.platform.manager.sdk.SdkPlatformApiFactory
import com.universalpasswordmanager.data.platform.manager.sdk.SdkRepositoryFactory
import com.universalpasswordmanager.data.platform.manager.sdk.log.SdkLoggerFactory
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SdkClientManagerTest {

    private val mockNativeLibraryManager = mockk<NativeLibraryManager> {
        every { loadLibrary(any()) } returns Result.success(Unit)
    }
    private val sdkRepoFactory: SdkRepositoryFactory = mockk {
        every { getRepositories(userId = any()) } returns mockk()
    }
    private val sdkPlatformApiFactory: SdkPlatformApiFactory = mockk {
        every { getServerCommunicationConfigPlatformApi() } returns mockk()
    }
    private val sdkLoggerFactory: SdkLoggerFactory = mockk {
        every { logLevel } returns LogLevel.INFO
        every { getLogCallback() } returns mockk()
    }

    @BeforeEach
    fun setUp() {
        mockkStatic(::isBuildVersionAtLeast, ::initLogger)
        every { isBuildVersionAtLeast(version = any()) } returns true
        every { initLogger(callback = any(), level = any(), platformLogger = any()) } just runs
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(::isBuildVersionAtLeast, ::initLogger)
    }

    @Test
    fun `init should load the universalpasswordmanager_uniffi library when build version is below 31`() = runTest {
        every { isBuildVersionAtLeast(31) } returns false
        createSdkClientManager()
        verify { mockNativeLibraryManager.loadLibrary("universalpasswordmanager_uniffi") }
    }

    @Test
    fun `init should not load the universalpasswordmanager_uniffi library when build version is 31 or above`() =
        runTest {
            every { isBuildVersionAtLeast(31) } returns true
            createSdkClientManager()
            verify(exactly = 0) { mockNativeLibraryManager.loadLibrary("universalpasswordmanager_uniffi") }
        }

    @Suppress("MaxLineLength")
    @Test
    fun `getOrCreateClient should create a new client for each userId and return a cached client for subsequent calls`() =
        runTest {
            val sdkClientManager = createSdkClientManager()
            val userId = "userId"
            val firstClient = sdkClientManager.getOrCreateClient(userId = userId)

            // Additional calls for the same userId return the same value
            val secondClient = sdkClientManager.getOrCreateClient(userId = userId)
            assertEquals(firstClient, secondClient)

            // Additional calls for different userIds should return different values
            val otherUserId = "otherUserId"
            val thirdClient = sdkClientManager.getOrCreateClient(userId = otherUserId)
            assertNotEquals(firstClient, thirdClient)
        }

    @Test
    fun `singleUseClient should create a new client everytime and run the lambda with it`() =
        runTest {
            val sdkClientManager = createSdkClientManager()
            val firstClient = sdkClientManager.singleUseClient { this }

            // Additional calls should always create a new client
            val secondClient = sdkClientManager.singleUseClient { this }
            assertNotEquals(firstClient, secondClient)
        }

    @Test
    fun `destroyClient should call close on the Client and remove it from the cache`() = runTest {
        val sdkClientManager = createSdkClientManager()
        val userId = "userId"
        val firstClient = sdkClientManager.getOrCreateClient(userId = userId)

        sdkClientManager.destroyClient(userId = userId)

        verify { firstClient.close() }

        // New calls for the same userId should return different values
        val secondClient = sdkClientManager.getOrCreateClient(userId = userId)
        assertNotEquals(firstClient, secondClient)
    }

    private fun createSdkClientManager(): SdkClientManagerImpl = SdkClientManagerImpl(
        dispatcherManager = FakeDispatcherManager(),
        clientProvider = { _, _ -> mockk(relaxed = true) },
        nativeLibraryManager = mockNativeLibraryManager,
        featureFlagManager = mockk(),
        sdkRepoFactory = sdkRepoFactory,
        sdkPlatformApiFactory = sdkPlatformApiFactory,
        sdkLoggerFactory = sdkLoggerFactory,
    )
}
