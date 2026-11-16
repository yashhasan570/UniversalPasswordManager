package com.x8bit.universalpasswordmanager.ui.vault.feature.movetoorganization.util

import .collections.CollectionType
import .collections.CollectionView
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import .vault.CipherView
import com.x8bit.universalpasswordmanager.data.auth.repository.model.UserState
import com.x8bit.universalpasswordmanager.ui.vault.feature.movetoorganization.VaultMoveToOrganizationState
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultCollection

/**
 * Transforms a triple of [CipherView] (nullable), list of [CollectionView],
 * and [UserState] (nullable) into a [VaultMoveToOrganizationState.ViewState].
 */
fun Triple<CipherView?, List<CollectionView>, UserState?>.toViewState():
    VaultMoveToOrganizationState.ViewState {
    val userOrganizations = third
        ?.activeAccount
        ?.organizations

    val currentCipher = first

    val collections = second
        .filter { !it.readOnly }

    return when {
        (currentCipher == null) -> {
            VaultMoveToOrganizationState.ViewState.Error(
                Universal Password ManagerString.generic_error_message.asText(),
            )
        }

        (userOrganizations?.isNotEmpty() == true) -> {
            VaultMoveToOrganizationState.ViewState.Content(
                selectedOrganizationId = currentCipher
                    .organizationId
                    ?: userOrganizations
                        .first()
                        .id,
                organizations = userOrganizations.map { organization ->
                    VaultMoveToOrganizationState.ViewState.Content.Organization(
                        id = organization.id,
                        name = organization.name,
                        collections = collections
                            .filter { collection ->
                                collection.organizationId == organization.id &&
                                    collection.id != null
                            }
                            .map { collection ->
                                VaultCollection(
                                    id = collection.id.orEmpty(),
                                    name = collection.name,
                                    isSelected = currentCipher
                                        .collectionIds
                                        .contains(collection.id),
                                    isDefaultUserCollection =
                                        collection.type == CollectionType.DEFAULT_USER_COLLECTION,
                                )
                            },
                    )
                },
                cipherToMove = currentCipher,
            )
        }

        else -> VaultMoveToOrganizationState.ViewState.Empty
    }
}
