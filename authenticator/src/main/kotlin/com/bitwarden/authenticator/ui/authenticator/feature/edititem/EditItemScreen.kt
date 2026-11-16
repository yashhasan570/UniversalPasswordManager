package .authenticator.ui.authenticator.feature.edititem

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemAlgorithm
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemType
import .authenticator.ui.authenticator.feature.edititem.model.EditItemData
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerExpandingHeader
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.stepper.Universal Password ManagerStepper
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerPlurals
import .ui.platform.resource.Universal Password ManagerString
import kotlinx.collections.immutable.toImmutableList

/**
 * Displays the edit authenticator item screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    onNavigateBack: () -> Unit = { },
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            EditItemEvent.NavigateBack -> onNavigateBack()
        }
    }

    EditItemDialogs(
        dialogState = state.dialog,
        onDismissRequest = { viewModel.trySendAction(EditItemAction.DismissDialog) },
    )

    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.edit),
                scrollBehavior = scrollBehavior,
                navigationIcon = painterResource(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = { viewModel.trySendAction(EditItemAction.CancelClick) },
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.save),
                        onClick = { viewModel.trySendAction(EditItemAction.SaveClick) },
                        modifier = Modifier.semantics { testTag = "SaveButton" },
                    )
                },
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
    ) {
        when (val viewState = state.viewState) {
            is EditItemState.ViewState.Content -> {
                EditItemContent(
                    viewState = viewState,
                    onIssuerNameTextChange = {
                        viewModel.trySendAction(EditItemAction.IssuerNameTextChange(it))
                    },
                    onUsernameTextChange = {
                        viewModel.trySendAction(EditItemAction.UsernameTextChange(it))
                    },
                    onToggleFavorite = {
                        viewModel.trySendAction(EditItemAction.FavoriteToggleClick(it))
                    },
                    onTypeOptionClicked = {
                        viewModel.trySendAction(EditItemAction.TypeOptionClick(it))
                    },
                    onTotpCodeTextChange = {
                        viewModel.trySendAction(EditItemAction.TotpCodeTextChange(it))
                    },
                    onAlgorithmOptionClicked = {
                        viewModel.trySendAction(EditItemAction.AlgorithmOptionClick(it))
                    },
                    onRefreshPeriodOptionClicked = {
                        viewModel.trySendAction(EditItemAction.RefreshPeriodOptionClick(it))
                    },
                    onNumberOfDigitsChanged = {
                        viewModel.trySendAction(EditItemAction.NumberOfDigitsOptionClick(it))
                    },
                    onExpandAdvancedOptionsClicked = {
                        viewModel.trySendAction(EditItemAction.ExpandAdvancedOptionsClick)
                    },
                )
            }

            is EditItemState.ViewState.Error -> {
                Universal Password ManagerErrorContent(message = viewState.message())
            }

            EditItemState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent()
            }
        }
    }
}

/**
 * The top level content UI state for the [EditItemScreen].
 */
@Suppress("LongMethod")
@Composable
fun EditItemContent(
    modifier: Modifier = Modifier,
    viewState: EditItemState.ViewState.Content,
    onIssuerNameTextChange: (String) -> Unit = {},
    onUsernameTextChange: (String) -> Unit = {},
    onToggleFavorite: (Boolean) -> Unit = {},
    onTypeOptionClicked: (AuthenticatorItemType) -> Unit = {},
    onTotpCodeTextChange: (String) -> Unit = {},
    onAlgorithmOptionClicked: (AuthenticatorItemAlgorithm) -> Unit = {},
    onRefreshPeriodOptionClicked: (AuthenticatorRefreshPeriodOption) -> Unit = {},
    onNumberOfDigitsChanged: (Int) -> Unit = {},
    onExpandAdvancedOptionsClicked: () -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(height = 12.dp))
            Universal Password ManagerListHeaderText(
                modifier = Modifier
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                label = stringResource(id = Universal Password ManagerString.information),
            )
        }

        item {
            Spacer(Modifier.height(8.dp))
            Universal Password ManagerTextField(
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
                textFieldTestTag = "NameTextField",
                label = stringResource(id = Universal Password ManagerString.name),
                value = viewState.itemData.issuer,
                onValueChange = onIssuerNameTextChange,
                singleLine = true,
                cardStyle = CardStyle.Top(),
            )
        }

        item {
            Universal Password ManagerPasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
                passwordFieldTestTag = "KeyTextField",
                label = stringResource(id = Universal Password ManagerString.key),
                value = viewState.itemData.totpCode,
                onValueChange = onTotpCodeTextChange,
                singleLine = true,
                cardStyle = CardStyle.Middle(),
            )
        }

        item {
            Universal Password ManagerTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
                textFieldTestTag = "UsernameTextField",
                label = stringResource(id = Universal Password ManagerString.username),
                value = viewState.itemData.username.orEmpty(),
                onValueChange = onUsernameTextChange,
                singleLine = true,
                cardStyle = CardStyle.Middle(),
            )
        }

        item {
            Universal Password ManagerSwitch(
                label = stringResource(id = Universal Password ManagerString.favorite),
                isChecked = viewState.itemData.favorite,
                onCheckedChange = onToggleFavorite,
                cardStyle = CardStyle.Bottom,
                modifier = Modifier
                    .testTag(tag = "ItemFavoriteToggle")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        item(key = "AdvancedOptions") {
            Universal Password ManagerExpandingHeader(
                isExpanded = viewState.isAdvancedOptionsExpanded,
                onClick = onExpandAdvancedOptionsClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("AdditionalOptionsExpandingHeader"),
            )
        }

        if (viewState.isAdvancedOptionsExpanded) {
            advancedOptions(
                viewState = viewState,
                onAlgorithmOptionClicked = onAlgorithmOptionClicked,
                onTypeOptionClicked = onTypeOptionClicked,
                onRefreshPeriodOptionClicked = onRefreshPeriodOptionClicked,
                onNumberOfDigitsChanged = onNumberOfDigitsChanged,
            )
        }

        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Suppress("LongMethod")
private fun LazyListScope.advancedOptions(
    viewState: EditItemState.ViewState.Content,
    onAlgorithmOptionClicked: (AuthenticatorItemAlgorithm) -> Unit,
    onTypeOptionClicked: (AuthenticatorItemType) -> Unit,
    onRefreshPeriodOptionClicked: (AuthenticatorRefreshPeriodOption) -> Unit,
    onNumberOfDigitsChanged: (Int) -> Unit,
) {
    item(key = "OtpItemTypeSelector") {
        val possibleTypeOptions = AuthenticatorItemType.entries
        val typeOptionsWithStrings = possibleTypeOptions.associateWith { it.name }
        Universal Password ManagerMultiSelectButton(
            modifier = Modifier
                .testTag(tag = "OTPItemTypePicker")
                .standardHorizontalMargin()
                .fillMaxWidth()
                .animateItem(),
            label = stringResource(id = Universal Password ManagerString.otp_type),
            options = typeOptionsWithStrings.values.toImmutableList(),
            selectedOption = viewState.itemData.type.name,
            cardStyle = CardStyle.Top(),
            onOptionSelected = { selectedOption ->
                val selectedOptionName = typeOptionsWithStrings
                    .entries
                    .first { it.value == selectedOption }
                    .key
                onTypeOptionClicked(selectedOptionName)
            },
        )
    }

    item(key = "AlgorithmItemTypeSelector") {
        val possibleAlgorithmOptions = AuthenticatorItemAlgorithm.entries
        val algorithmOptionsWithStrings = possibleAlgorithmOptions.associateWith { it.name }
        Universal Password ManagerMultiSelectButton(
            modifier = Modifier
                .testTag(tag = "AlgorithmItemTypePicker")
                .standardHorizontalMargin()
                .fillMaxWidth()
                .animateItem(),
            label = stringResource(id = Universal Password ManagerString.algorithm),
            options = algorithmOptionsWithStrings.values.toImmutableList(),
            selectedOption = viewState.itemData.algorithm.name,
            cardStyle = CardStyle.Middle(),
            onOptionSelected = { selectedOption ->
                val selectedOptionName = algorithmOptionsWithStrings
                    .entries
                    .first { it.value == selectedOption }
                    .key
                onAlgorithmOptionClicked(selectedOptionName)
            },
        )
    }

    item(key = "RefreshPeriodItemTypePicker") {
        val possibleRefreshPeriodOptions = AuthenticatorRefreshPeriodOption.entries
        val refreshPeriodOptionsWithStrings = possibleRefreshPeriodOptions.associateWith {
            pluralStringResource(
                id = Universal Password ManagerPlurals.refresh_period_seconds,
                count = it.seconds,
                formatArgs = arrayOf(it.seconds),
            )
        }
        Universal Password ManagerMultiSelectButton(
            modifier = Modifier
                .testTag(tag = "RefreshPeriodItemTypePicker")
                .standardHorizontalMargin()
                .fillMaxWidth()
                .animateItem(),
            label = stringResource(id = Universal Password ManagerString.refresh_period),
            options = refreshPeriodOptionsWithStrings.values.toImmutableList(),
            selectedOption = refreshPeriodOptionsWithStrings.getValue(
                key = viewState.itemData.refreshPeriod,
            ),
            cardStyle = CardStyle.Middle(),
            onOptionSelected = remember(viewState) {
                { selectedOption ->
                    val selectedOptionName = refreshPeriodOptionsWithStrings
                        .entries
                        .first { it.value == selectedOption }
                        .key
                    onRefreshPeriodOptionClicked(selectedOptionName)
                }
            },
        )
    }

    item(key = "DigitsCounterItem") {
        DigitsCounterItem(
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .animateItem(),
            digits = viewState.itemData.digits,
            onDigitsCounterChange = onNumberOfDigitsChanged,
            minValue = viewState.minDigitsAllowed,
            maxValue = viewState.maxDigitsAllowed,
        )
    }
}

@Composable
private fun EditItemDialogs(
    dialogState: EditItemState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is EditItemState.DialogState.Generic -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        is EditItemState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(
                text = dialogState.message(),
            )
        }

        null -> Unit
    }
}

@Composable
private fun DigitsCounterItem(
    digits: Int,
    onDigitsCounterChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerStepper(
        label = stringResource(id = Universal Password ManagerString.number_of_digits),
        value = digits.coerceIn(minValue, maxValue),
        range = minValue..maxValue,
        onValueChange = onDigitsCounterChange,
        cardStyle = CardStyle.Bottom,
        modifier = modifier.testTag(tag = "DigitsValueLabel"),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditItemContentExpandedOptionsPreview() {
    EditItemContent(
        viewState = EditItemState.ViewState.Content(
            isAdvancedOptionsExpanded = true,
            itemData = EditItemData(
                refreshPeriod = AuthenticatorRefreshPeriodOption.THIRTY,
                totpCode = "123456",
                type = AuthenticatorItemType.TOTP,
                username = "account name",
                issuer = "issuer",
                algorithm = AuthenticatorItemAlgorithm.SHA1,
                digits = 6,
                favorite = true,
            ),
            minDigitsAllowed = 5,
            maxDigitsAllowed = 10,
        ),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditItemContentCollapsedOptionsPreview() {
    EditItemContent(
        viewState = EditItemState.ViewState.Content(
            isAdvancedOptionsExpanded = false,
            itemData = EditItemData(
                refreshPeriod = AuthenticatorRefreshPeriodOption.THIRTY,
                totpCode = "123456",
                type = AuthenticatorItemType.TOTP,
                username = "account name",
                issuer = "issuer",
                algorithm = AuthenticatorItemAlgorithm.SHA1,
                digits = 6,
                favorite = false,
            ),
            minDigitsAllowed = 5,
            maxDigitsAllowed = 10,
        ),
    )
}
