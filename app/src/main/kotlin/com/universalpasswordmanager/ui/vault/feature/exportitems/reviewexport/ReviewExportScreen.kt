package com.universalpasswordmanager.ui.vault.feature.exportitems.reviewexport

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.cxf.manager.CredentialExchangeCompletionManager
import com.universalpasswordmanager.cxf.ui.composition.LocalCredentialExchangeCompletionManager
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.nullableTestTag
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.content.UniversalEmptyContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.feature.exportitems.component.ExportItemsScaffold
import com.universalpasswordmanager.ui.vault.feature.exportitems.reviewexport.handlers.ReviewExportHandlers
import com.universalpasswordmanager.ui.vault.feature.exportitems.reviewexport.handlers.rememberReviewExportHandler

/**
 * The main composable for the Review Export screen.
 *
 * This screen allows the user to review a summary of items that will be exported
 * before confirming the operation. It displays a list of item types and their counts,
 * an illustrative image, and provides options to proceed with the export or cancel.
 * The screen adheres to the MVI pattern by observing state from [ReviewExportViewModel]
 * and dispatching actions via [ReviewExportHandlers]. Upon completion of the export operation,
 * it utilizes the [CredentialExchangeCompletionManager] to finalize the credential exchange
 * process.
 *
 * @param onNavigateBack Callback invoked when the user chooses to navigate back (e.g., via cancel
 * or back button).
 * @param viewModel The [ReviewExportViewModel] instance for this screen.
 * @param credentialExchangeCompletionManager Manager responsible for completing the credential
 * export process.
 * Defaults to the manager provided by [LocalCredentialExchangeCompletionManager].
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewExportScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAccountSelection: () -> Unit,
    viewModel: ReviewExportViewModel = hiltViewModel(),
    credentialExchangeCompletionManager: CredentialExchangeCompletionManager =
        LocalCredentialExchangeCompletionManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberReviewExportHandler(viewModel)

    EventsEffect(viewModel) {
        when (it) {
            is ReviewExportEvent.NavigateBack -> onNavigateBack()
            is ReviewExportEvent.NavigateToAccountSelection -> onNavigateToAccountSelection()
            is ReviewExportEvent.CompleteExport -> {
                credentialExchangeCompletionManager.completeCredentialExport(it.result)
            }
        }
    }

    ReviewExportDialogs(
        dialog = state.dialog,
        onDismiss = handler.onDismissDialog,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    ExportItemsScaffold(
        navIcon = rememberVectorPainter(UniversalDrawable.ic_back),
        onNavigationIconClick = handler.onNavigateBackClick,
        navigationIconContentDescription = stringResource(UniversalString.back),
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
    ) {
        when (val viewState = state.viewState) {
            ReviewExportState.ViewState.NoItems -> {
                UniversalEmptyContent(
                    title = stringResource(UniversalString.no_items_available_to_import),
                    text = stringResource(
                        UniversalString
                            .your_vault_may_be_empty_or_import_some_item_types_isnt_supported,
                    ),
                    primaryButton = if (state.hasOtherAccounts) {
                        UniversalButtonData(
                            label = UniversalString.select_a_different_account.asText(),
                            testTag = "SelectADifferentAccountButton",
                            onClick = handler.onSelectAnotherAccountClick,
                        )
                    } else {
                        null
                    },
                    secondaryButton = UniversalButtonData(
                        label = UniversalString.cancel.asText(),
                        testTag = "NoItemsCancelButton",
                        onClick = handler.onCancelClick,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is ReviewExportState.ViewState.Content -> {
                ReviewExportContent(
                    content = viewState,
                    onImportItemsClick = handler.onImportItemsClick,
                    onCancelClick = handler.onCancelClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .standardHorizontalMargin(),
                )
            }
        }
    }
}

/**
 * Displays dialogs based on the [ReviewExportState.DialogState].
 *
 * @param dialog The current dialog state from [ReviewExportState].
 * @param onDismiss Callback invoked when a dialog is dismissed.
 */
@Composable
private fun ReviewExportDialogs(
    dialog: ReviewExportState.DialogState?,
    onDismiss: () -> Unit,
) {
    when (dialog) {
        is ReviewExportState.DialogState.General -> {
            UniversalBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                throwable = dialog.error,
                onDismissRequest = onDismiss,
            )
        }

        is ReviewExportState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }
}

/**
 * The main content area of the Review Export screen.
 *
 * This composable lays out the illustrative image, titles, list of items to export,
 * and action buttons.
 *
 * @param content The current [ReviewExportState] to render.
 * @param onImportItemsClick Callback invoked when the "Import Items" button is clicked.
 * @param onCancelClick Callback invoked when the "Cancel" button is clicked.
 * @param modifier The modifier to be applied to the content root.
 */
@Suppress("LongMethod")
@Composable
private fun ReviewExportContent(
    content: ReviewExportState.ViewState.Content,
    onImportItemsClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, bottom = 16.dp),
    ) {
        Image(
            painter = painterResource(id = UniversalDrawable.ill_import),
            contentDescription = null,
            modifier = Modifier.height(160.dp),
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(UniversalString.import_items),
            style = UniversalTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = UniversalTheme.colorScheme.text.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(
                UniversalString.import_passwords_passkeys_and_other_item_types_from_your_vault,
            ),
            style = UniversalTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = UniversalTheme.colorScheme.text.secondary,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalListHeaderText(
            label = stringResource(UniversalString.items_to_import),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )

        ItemCountRow(
            label = stringResource(UniversalString.passwords).asText(),
            itemCount = content.itemTypeCounts.passwordCount,
            cardStyle = CardStyle.Top(),
        )
        ItemCountRow(
            label = stringResource(UniversalString.passkeys).asText(),
            itemCount = content.itemTypeCounts.passkeyCount,
            cardStyle = CardStyle.Middle(),
        )
        ItemCountRow(
            label = stringResource(UniversalString.identities).asText(),
            itemCount = content.itemTypeCounts.identityCount,
            cardStyle = CardStyle.Middle(),
        )
        ItemCountRow(
            label = stringResource(UniversalString.cards).asText(),
            itemCount = content.itemTypeCounts.cardCount,
            cardStyle = CardStyle.Middle(),
        )
        ItemCountRow(
            label = stringResource(UniversalString.secure_notes).asText(),
            itemCount = content.itemTypeCounts.secureNoteCount,
            cardStyle = CardStyle.Bottom,
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalFilledButton(
            label = stringResource(UniversalString.import_items),
            onClick = onImportItemsClick,
            isExternalLink = true,
            modifier = Modifier
                .fillMaxWidth()
                .nullableTestTag("ImportItemsButton"),
        )

        Spacer(modifier = Modifier.height(8.dp))

        UniversalOutlinedButton(
            label = stringResource(UniversalString.cancel),
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .nullableTestTag("CancelButton"),
        )

        Spacer(Modifier.height(12.dp))
        Spacer(Modifier.navigationBarsPadding())
    }
}

/**
 * Displays a single row in the list of items to be exported.
 *
 * @param label The display name of the item type.
 * @param itemCount The number of items of this type that are staged for export.
 */
@Composable
private fun ItemCountRow(
    label: Text,
    itemCount: Int,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                paddingHorizontal = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = label(),
            style = UniversalTheme.typography.bodyLarge,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = itemCount.toString(),
            style = UniversalTheme.typography.labelSmall,
            color = UniversalTheme.colorScheme.text.primary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Review Export Content")
@Composable
private fun ReviewExportContent_preview() {
    ExportItemsScaffold(
        navIcon = rememberVectorPainter(UniversalDrawable.ic_close),
        navigationIconContentDescription = stringResource(UniversalString.close),
        onNavigationIconClick = { },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    ) {
        ReviewExportContent(
            content = ReviewExportState.ViewState.Content(
                itemTypeCounts = ReviewExportState.ItemTypeCounts(
                    passwordCount = 14,
                    passkeyCount = 14,
                    identityCount = 3,
                    secureNoteCount = 5,
                ),
            ),
            onImportItemsClick = {},
            onCancelClick = {},
            modifier = Modifier
                .fillMaxSize()
                .standardHorizontalMargin(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Review Export Empty Content")
@Composable
private fun ReviewExportContent_NoItems_preview() {
    ExportItemsScaffold(
        navIcon = rememberVectorPainter(UniversalDrawable.ic_close),
        navigationIconContentDescription = stringResource(UniversalString.close),
        onNavigationIconClick = { },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    ) {
        UniversalEmptyContent(
            title = stringResource(UniversalString.no_items_available_to_import),
            text = stringResource(
                UniversalString
                    .your_vault_may_be_empty_or_import_some_item_types_isnt_supported,
            ),
            primaryButton = UniversalButtonData(
                label = UniversalString.select_a_different_account.asText(),
                testTag = "SelectADifferentAccountButton",
                onClick = { },
            ),
            secondaryButton = UniversalButtonData(
                label = UniversalString.cancel.asText(),
                testTag = "NoItemsCancelButton",
                onClick = { },
            ),
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}
