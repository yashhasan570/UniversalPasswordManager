package com.universalpasswordmanager.data.platform.manager.network

import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.core.data.manager.dispatcher.FakeDispatcherManager
import com.universalpasswordmanager.core.data.util.advanceTimeByAndRunCurrent
import com.universalpasswordmanager.data.repository.ServerConfigRepository
import com.universalpasswordmanager.data.repository.model.Environment
import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.data.auth.repository.AuthRepository
import com.universalpasswordmanager.data.auth.repository.model.AuthState
import com.universalpasswordmanager.data.auth.repository.model.UserState
import com.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkConfigManagerTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val dispatcherManager: DispatcherManager = FakeDispatcherManager(
        unconfined = testDispatcher,
    )
    private val mutableAuthStateFlow = MutableStateFlow<AuthState>(AuthState.Uninitialized)
    private val mutableUserStateFlow = MutableStateFlow<UserState?>(null)
    private val mutableEnvironmentStateFlow = MutableStateFlow<Environment>(Environment.Prod.Us)

    private val authRepository: AuthRepository = mockk {
        every { authStateFlow } returns mutableAuthStateFlow
        every { userStateFlow } returns mutableUserStateFlow
    }
    private val environmentRepository: EnvironmentRepository = mockk {
        every { environmentStateFlow } returns mutableEnvironmentStateFlow
    }
    private val serverConfigRepository: ServerConfigRepository = mockk {
        coEvery { getServerConfig(forceRefresh = true) } returns null
    }
    private val mockUniversalServiceClient: UniversalServiceClient = mockk {
        every { setRefreshTokenProvider(any()) } just runs
    }

    private lateinit var networkConfigManager: NetworkConfigManager

    @BeforeEach
    fun setUp() {
        networkConfigManager = NetworkConfigManagerImpl(
            authRepository = authRepository,
            environmentRepository = environmentRepository,
            serverConfigRepository = serverConfigRepository,
            universalpasswordmanagerServiceClient = mockUniversalServiceClient,
            dispatcherManager = dispatcherManager,
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `changes in the Environment or active user ID should call getServerConfig after debounce period`() {
        mutableEnvironmentStateFlow.value = Environment.Prod.Eu
        mutableUserStateFlow.value = mockk { every { activeUserId } returns "userId" }
        mutableEnvironmentStateFlow.value = Environment.Prod.Us
        mutableUserStateFlow.value = null
        testDispatcher.advanceTimeByAndRunCurrent(delayTimeMillis = 500L)
        coVerify(exactly = 1) {
            serverConfigRepository.getServerConfig(forceRefresh = true)
        }
    }
}
