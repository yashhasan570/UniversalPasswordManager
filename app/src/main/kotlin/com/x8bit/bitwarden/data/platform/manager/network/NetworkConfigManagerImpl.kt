package com.x8bit.universalpasswordmanager.data.platform.manager.network

import .core.data.manager.dispatcher.DispatcherManager
import .data.repository.ServerConfigRepository
import com.work.Universal Password ManagerServiceClient
import com.x8bit.universalpasswordmanager.data.auth.repository.AuthRepository
import com.x8bit.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

private const val ENVIRONMENT_DEBOUNCE_TIMEOUT_MS: Long = 500L

/**
 * Primary implementation of [NetworkConfigManager].
 */
class NetworkConfigManagerImpl(
    authRepository: AuthRepository,
    environmentRepository: EnvironmentRepository,
    serverConfigRepository: ServerConfigRepository,
    universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    dispatcherManager: DispatcherManager,
) : NetworkConfigManager {

    private val collectionScope = CoroutineScope(dispatcherManager.unconfined)

    init {
        @OptIn(FlowPreview::class)
        combine(
            environmentRepository.environmentStateFlow,
            authRepository.userStateFlow.map { it?.activeUserId }.distinctUntilChanged(),
        ) { _, _ -> }
            .debounce(timeoutMillis = ENVIRONMENT_DEBOUNCE_TIMEOUT_MS)
            .onEach { _ ->
                // This updates the stored service configuration by performing a network request.
                // We debounce it to avoid rapid repeated requests.
                serverConfigRepository.getServerConfig(forceRefresh = true)
            }
            .launchIn(collectionScope)
        universalpasswordmanagerServiceClient.setRefreshTokenProvider(authRepository)
    }
}
