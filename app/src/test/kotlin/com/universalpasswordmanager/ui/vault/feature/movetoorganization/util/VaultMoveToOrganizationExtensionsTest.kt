package com.universalpasswordmanager.ui.vault.feature.movetoorganization.util

import com.universalpasswordmanager.data.repository.model.Environment
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.auth.datasource.disk.model.OnboardingStatus
import com.universalpasswordmanager.data.auth.repository.model.UserState
import com.universalpasswordmanager.data.auth.repository.model.createMockOrganization
import com.universalpasswordmanager.data.platform.manager.model.FirstTimeState
import com.universalpasswordmanager.data.vault.datasource.sdk.model.createMockCipherView
import com.universalpasswordmanager.data.vault.datasource.sdk.model.createMockCollectionView
import com.universalpasswordmanager.ui.vault.feature.movetoorganization.VaultMoveToOrganizationState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultMoveToOrganizationExtensionsTest {

    @Test
    @Suppress("MaxLineLength")
    fun `toViewState should transform a valid triple of CipherView, CollectionView list, and UserState into Content ViewState`() {
        val triple = Triple(
            first = createMockCipherView(number = 1),
            second = listOf(
                createMockCollectionView(number = 1),
                createMockCollectionView(number = 2),
                createMockCollectionView(number = 3),
            ),
            third = createMockUserState(),
        )

        val result = triple.toViewState()

        assertEquals(
            VaultMoveToOrganizationState.ViewState.Content(
                selectedOrganizationId = "mockOrganizationId-1",
                organizations = createMockOrganizationList(),
                cipherToMove = createMockCipherView(number = 1),
            ),
            result,
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toViewState should transform a triple of null CipherView, CollectionView list, and UserState into Error ViewState`() {
        val triple = Triple(
            first = null,
            second = listOf(
                createMockCollectionView(number = 1),
                createMockCollectionView(number = 2),
                createMockCollectionView(number = 3),
            ),
            third = createMockUserState(),
        )

        val result = triple.toViewState()

        assertEquals(
            VaultMoveToOrganizationState.ViewState.Error(UniversalString.generic_error_message.asText()),
            result,
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toViewState should transform a triple of CipherView, CollectionView list, and UserState without organizations into Empty ViewState`() {
        val triple = Triple(
            first = createMockCipherView(number = 1),
            second = listOf(
                createMockCollectionView(number = 1),
                createMockCollectionView(number = 2),
                createMockCollectionView(number = 3),
            ),
            third = createMockUserState(hasOrganizations = false),
        )

        val result = triple.toViewState()

        assertEquals(
            VaultMoveToOrganizationState.ViewState.Empty,
            result,
        )
    }
}

private fun createMockUserState(hasOrganizations: Boolean = true): UserState =
    UserState(
        activeUserId = "activeUserId",
        accounts = listOf(
            UserState.Account(
                userId = "activeUserId",
                name = "Active User",
                email = "active@",
                avatarColorHex = "#aa00aa",
                environment = Environment.Prod.Us,
                isPremium = true,
                isPremiumFromSelf = true,
                isLoggedIn = true,
                isVaultUnlocked = true,
                needsPasswordReset = false,
                isBiometricsEnabled = false,
                needsMasterPassword = false,
                organizations = if (hasOrganizations) {
                    listOf(
                        createMockOrganization(
                            number = 1,
                            id = "mockOrganizationId-1",
                            name = "mockOrganizationName-1",
                            keyConnectorUrl = null,
                        ),
                        createMockOrganization(
                            number = 1,
                            id = "mockOrganizationId-2",
                            name = "mockOrganizationName-2",
                            keyConnectorUrl = null,
                        ),
                        createMockOrganization(
                            number = 1,
                            id = "mockOrganizationId-3",
                            name = "mockOrganizationName-3",
                            keyConnectorUrl = null,
                        ),
                    )
                } else {
                    emptyList()
                },
                trustedDevice = null,
                hasMasterPassword = true,
                isUsingKeyConnector = false,
                onboardingStatus = OnboardingStatus.COMPLETE,
                firstTimeState = FirstTimeState(showImportLoginsCard = true),
                isExportable = true,
                creationDate = null,
            ),
        ),
    )
