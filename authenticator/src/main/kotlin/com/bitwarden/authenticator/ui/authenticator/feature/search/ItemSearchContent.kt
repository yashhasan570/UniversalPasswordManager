package .authenticator.ui.authenticator.feature.search

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .authenticator.ui.authenticator.feature.search.handlers.SearchHandlers
import .authenticator.ui.platform.components.listitem.VaultVerificationCodeItem
import .authenticator.ui.platform.components.listitem.model.SharedCodesDisplayState
import .authenticator.ui.platform.components.listitem.model.VaultDropdownMenuAction
import .authenticator.ui.platform.components.listitem.model.VerificationCodeDisplayItem
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * The content state for the item search screen.
 */
@Composable
fun ItemSearchContent(
    viewState: ItemSearchState.ViewState.Content,
    searchHandlers: SearchHandlers,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(Modifier.height(height = 12.dp))
        }

        if (viewState.hasLocalAndSharedItems) {
            item {
                Universal Password ManagerListHeaderText(
                    label = viewState.localListHeader(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(height = 8.dp))
            }
        }

        itemsIndexed(viewState.itemList) { index, item ->
            VaultVerificationCodeItem(
                displayItem = item,
                onItemClick = { searchHandlers.onItemClick(item.authCode) },
                onDropdownMenuClick = { searchHandlers.onDropdownMenuClick(it, item) },
                cardStyle = viewState.itemList.toListItemCardStyle(index = index),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        if (viewState.hasLocalAndSharedItems) {
            item {
                Spacer(Modifier.height(height = 12.dp))
            }
        }

        sharedCodes(
            sharedItems = viewState.sharedItems,
            onCopyClick = searchHandlers.onItemClick,
            onDropdownMenuClick = searchHandlers.onDropdownMenuClick,
        )

        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

private fun LazyListScope.sharedCodes(
    sharedItems: SharedCodesDisplayState,
    onCopyClick: (authCode: String) -> Unit,
    onDropdownMenuClick: (VaultDropdownMenuAction, VerificationCodeDisplayItem) -> Unit,
) {
    when (sharedItems) {
        is SharedCodesDisplayState.Codes -> {
            sharedItems.sections.forEach { section ->
                item {
                    Universal Password ManagerListHeaderText(
                        label = section.label(),
                        supportingLabel = section.codes.size.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .standardHorizontalMargin()
                            .padding(horizontal = 16.dp),
                    )
                    Spacer(Modifier.height(height = 12.dp))
                }

                itemsIndexed(section.codes) { index, item ->
                    VaultVerificationCodeItem(
                        displayItem = item,
                        onItemClick = { onCopyClick(item.authCode) },
                        onDropdownMenuClick = { onDropdownMenuClick(it, item) },
                        cardStyle = section.codes.toListItemCardStyle(index = index),
                        modifier = Modifier
                            .fillMaxWidth()
                            .standardHorizontalMargin(),
                    )
                }
            }
        }

        SharedCodesDisplayState.Error -> {
            item {
                Text(
                    text = stringResource(Universal Password ManagerString.shared_codes_error),
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    style = Universal Password ManagerTheme.typography.bodySmall,
                    modifier = Modifier.standardHorizontalMargin(),
                )
            }
        }
    }
}
