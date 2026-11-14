package com.universalpasswordmanager.ui.vault.feature.itemlisting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.universalpasswordmanager.ui.platform.components.content.UniversalEmptyContent
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.vault.feature.vault.VaultNoItems

/**
 * Empty view for the [VaultItemListingScreen].
 */
@Composable
fun VaultItemListingEmpty(
    state: VaultItemListingState.ViewState.NoItems,
    policyDisablesSend: Boolean,
    addItemClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.shouldShowAddButton) {
        VaultNoItems(
            policyDisablesSend = policyDisablesSend,
            vectorRes = state.vectorRes,
            headerText = state.header?.invoke(),
            message = state.message(),
            buttonText = state.buttonText(),
            modifier = modifier,
            addItemClickAction = addItemClickAction,
        )
    } else {
        UniversalEmptyContent(
            title = state.header?.invoke(),
            text = state.message(),
            illustrationData = state.vectorRes?.let { IconData.Local(iconRes = it) },
            modifier = modifier,
        )
    }
}
