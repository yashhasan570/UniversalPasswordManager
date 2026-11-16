package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import .core.util.persistentListOfNotNull
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.dialog.Universal Password ManagerSelectionDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dialog.row.Universal Password ManagerBasicDialogRow
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectDialogContent
import .ui.platform.components.dropdown.model.MultiSelectOption
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.data.platform.repository.model.UriMatchType
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.util.displayLabel
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.model.UriItem
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.model.UriMatchDisplayType
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.util.displayLabel
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.util.isAdvancedMatching
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.util.toDisplayMatchType
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.util.toUriMatchType
import kotlinx.collections.immutable.ImmutableList

/**
 * The URI item displayed to the user.
 */
@Suppress("LongMethod")
@Composable
fun VaultAddEditUriItem(
    uriItem: UriItem,
    onUriItemRemoved: (UriItem) -> Unit,
    onUriValueChange: (UriItem) -> Unit,
    cardStyle: CardStyle,
    defaultUriMatchType: UriMatchType,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    var shouldShowOptionsDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowMatchDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowAdvancedMatchDialog by rememberSaveable { mutableStateOf(false) }
    var optionPendingConfirmation by rememberSaveable { mutableStateOf<UriMatchDisplayType?>(null) }
    var shouldShowLearnMoreMatchDetectionDialog by rememberSaveable { mutableStateOf(false) }
    val defaultUriOption = remember(defaultUriMatchType) {
        defaultUriMatchType.displayLabel.toString(resources)
    }

    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.website_uri),
        value = uriItem.uri.orEmpty(),
        onValueChange = { onUriValueChange(uriItem.copy(uri = it)) },
        singleLine = false,
        keyboardType = KeyboardType.Uri,
        actions = {
            Universal Password ManagerStandardIconButton(
                vectorIconRes = Universal Password ManagerDrawable.ic_cog,
                contentDescription = stringResource(id = Universal Password ManagerString.more_options),
                onClick = { shouldShowOptionsDialog = true },
                modifier = Modifier.testTag(tag = "LoginUriOptionsButton"),
            )
        },
        textFieldTestTag = "LoginUriEntry",
        cardStyle = cardStyle,
        modifier = modifier,
    )

    if (shouldShowOptionsDialog) {
        Universal Password ManagerSelectionDialog(
            title = stringResource(id = Universal Password ManagerString.options),
            onDismissRequest = { shouldShowOptionsDialog = false },
        ) {
            Universal Password ManagerBasicDialogRow(
                text = stringResource(id = Universal Password ManagerString.match_detection),
                onClick = {
                    shouldShowOptionsDialog = false
                    shouldShowMatchDialog = true
                },
            )
            Universal Password ManagerBasicDialogRow(
                text = stringResource(id = Universal Password ManagerString.remove),
                onClick = {
                    shouldShowOptionsDialog = false
                    onUriItemRemoved(uriItem)
                },
            )
        }
    }

    if (shouldShowMatchDialog) {
        Universal Password ManagerSelectionDialog(
            title = stringResource(id = Universal Password ManagerString.uri_match_detection),
            onDismissRequest = { shouldShowMatchDialog = false },
        ) {
            Universal Password ManagerMultiSelectDialogContent(
                options = uriMatchingOptions(defaultUriOption = defaultUriOption),
                selectedOption = MultiSelectOption.Row(
                    uriItem
                        .match
                        .toDisplayMatchType()
                        .displayLabel(defaultUriOption = defaultUriOption)
                        .invoke(),
                ),
                onOptionSelected = { selectedOption ->
                    shouldShowMatchDialog = false

                    val newSelectedType =
                        UriMatchDisplayType
                            .entries
                            .first {
                                it.displayLabel(defaultUriOption)
                                    .invoke(resources) == selectedOption.title
                            }

                    if (newSelectedType.isAdvancedMatching()) {
                        optionPendingConfirmation = newSelectedType
                        shouldShowAdvancedMatchDialog = true
                    } else {
                        onUriValueChange(
                            uriItem.copy(match = newSelectedType.toUriMatchType()),
                        )
                        optionPendingConfirmation = null
                    }
                },
            )
        }
    }

    val currentOptionToConfirm = optionPendingConfirmation

    if (shouldShowAdvancedMatchDialog && currentOptionToConfirm != null) {
        AdvancedMatchDetectionWarning(
            pendingOption = currentOptionToConfirm,
            defaultUriMatchType = defaultUriMatchType,
            onDialogConfirm = {
                onUriValueChange(
                    uriItem.copy(match = currentOptionToConfirm.toUriMatchType()),
                )
                shouldShowAdvancedMatchDialog = false
                optionPendingConfirmation = null
                shouldShowLearnMoreMatchDetectionDialog = true
            },
            onDialogDismiss = {
                shouldShowAdvancedMatchDialog = false
                optionPendingConfirmation = null
            },
        )
    }

    if (shouldShowLearnMoreMatchDetectionDialog) {
        LearnMoreAboutMatchDetectionDialog(
            uriMatchDisplayType = uriItem.match.toDisplayMatchType(),
            defaultUriOption = defaultUriOption,
            onDialogConfirm = {
                onLearnMoreClick()
                shouldShowLearnMoreMatchDetectionDialog = false
            },
            onDialogDismiss = {
                shouldShowLearnMoreMatchDetectionDialog = false
            },
        )
    }
}

@Composable
private fun AdvancedMatchDetectionWarning(
    pendingOption: UriMatchDisplayType,
    defaultUriMatchType: UriMatchType,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
) {

    val descriptionStringResId = when (pendingOption) {
        UriMatchDisplayType.STARTS_WITH -> {
            Universal Password ManagerString.advanced_option_with_increased_risk_of_exposing_credentials
        }

        UriMatchDisplayType.REGULAR_EXPRESSION -> {
            Universal Password ManagerString.advanced_option_increased_risk_exposing_credentials_used_incorrectly
        }

        UriMatchDisplayType.DEFAULT,
        UriMatchDisplayType.HOST,
        UriMatchDisplayType.BASE_DOMAIN,
        UriMatchDisplayType.EXACT,
        UriMatchDisplayType.NEVER,
            ->
            error("Unexpected option on AdvancedMatchDetectionWarning")
    }

    val nameOfSelectedMatchDisplayType = pendingOption
        .displayLabel(defaultUriOption = defaultUriMatchType.displayLabel())
        .invoke()

    Universal Password ManagerTwoButtonDialog(
        title = stringResource(
            id = Universal Password ManagerString.are_you_sure_you_want_to_use,
            formatArgs = arrayOf(nameOfSelectedMatchDisplayType),
        ),
        message = stringResource(
            id = descriptionStringResId,
            formatArgs = arrayOf(nameOfSelectedMatchDisplayType),
        ),
        confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
        dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
        onConfirmClick = onDialogConfirm,
        onDismissClick = onDialogDismiss,
        onDismissRequest = onDialogDismiss,
    )
}

@Composable
private fun LearnMoreAboutMatchDetectionDialog(
    uriMatchDisplayType: UriMatchDisplayType,
    defaultUriOption: String,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
) {
    Universal Password ManagerTwoButtonDialog(
        title = stringResource(id = Universal Password ManagerString.keep_your_credential_secure),
        message = stringResource(
            id = Universal Password ManagerString.learn_more_about_how_to_keep_credentirals_secure,
            formatArgs = arrayOf(
                uriMatchDisplayType
                    .displayLabel(
                        defaultUriOption = defaultUriOption,
                    )
                    .invoke(),
            ),
        ),
        confirmButtonText = stringResource(id = Universal Password ManagerString.learn_more),
        dismissButtonText = stringResource(id = Universal Password ManagerString.close),
        onConfirmClick = onDialogConfirm,
        onDismissClick = onDialogDismiss,
        onDismissRequest = onDialogDismiss,
    )
}

@Composable
private fun uriMatchingOptions(defaultUriOption: String): ImmutableList<MultiSelectOption> {
    val advancedOptions = UriMatchDisplayType.entries.filter { it.isAdvancedMatching() }
    return persistentListOfNotNull(
        *UriMatchDisplayType
            .entries
            .filter { !it.isAdvancedMatching() }
            .map { MultiSelectOption.Row(it.displayLabel(defaultUriOption).invoke()) }
            .toTypedArray(),
        if (advancedOptions.isNotEmpty()) {
            MultiSelectOption.Header(
                title = stringResource(id = Universal Password ManagerString.advanced_options),
                testTag = "AdvancedOptionsSection",
            )
        } else {
            null
        },
        *advancedOptions
            .map { MultiSelectOption.Row(it.displayLabel(defaultUriOption).invoke()) }
            .toTypedArray(),
    )
}
