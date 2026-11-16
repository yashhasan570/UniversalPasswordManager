package com.x8bit.universalpasswordmanager.data.platform.manager.network

import .core.data.manager.dispatcher.DispatcherManager
import .core.data.manager.dispatcher.FakeDispatcherManager
import .core.data.util.advanceTimeByAndRunCurrent
import .data.repository.ServerConfigRepository
import .data.repository.model.Environment
import com.work.Universal Password ManagerServiceClient
import com.x8bit.universalpasswordmanager.data.auth.repository.AuthRepository
import com.x8bit.universalpasswordmanager.data.auth.repository.model.AuthState
import com.x8bit.universalpasswordmanager.data.auth.repository.model.UserState
import com.x8bit.universalpasswordmanager.data.platform.repository.EnvironmentRepository
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
    private val mockUniversal Password ManagerServiceClient: Universal Password ManagerServiceClient = mockk {
        every { setRefreshTokenProvider(any()) } just runs
    }

    private lateinit var networkConfigManager: NetworkConfigManager

    @BeforeEach
    fun setUp() {
        networkConfigManager = NetworkConfigManagerImpl(
            authRepository = authRepository,
            environmentRepository = environmentRepository,
            serverConfigRepository = serverConfigRepository,
            universalpasswordmanagerServiceClient = mockUniversal Password ManagerServiceClient,
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
