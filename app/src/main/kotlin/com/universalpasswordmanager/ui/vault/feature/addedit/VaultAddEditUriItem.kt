package com.universalpasswordmanager.ui.vault.feature.addedit

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
import com.universalpasswordmanager.core.util.persistentListOfNotNull
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalSelectionDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.dialog.row.UniversalBasicDialogRow
import com.universalpasswordmanager.ui.platform.components.dropdown.UniversalMultiSelectDialogContent
import com.universalpasswordmanager.ui.platform.components.dropdown.model.MultiSelectOption
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.data.platform.repository.model.UriMatchType
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.util.displayLabel
import com.universalpasswordmanager.ui.vault.feature.addedit.model.UriItem
import com.universalpasswordmanager.ui.vault.feature.addedit.model.UriMatchDisplayType
import com.universalpasswordmanager.ui.vault.feature.addedit.util.displayLabel
import com.universalpasswordmanager.ui.vault.feature.addedit.util.isAdvancedMatching
import com.universalpasswordmanager.ui.vault.feature.addedit.util.toDisplayMatchType
import com.universalpasswordmanager.ui.vault.feature.addedit.util.toUriMatchType
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

    UniversalTextField(
        label = stringResource(id = UniversalString.website_uri),
        value = uriItem.uri.orEmpty(),
        onValueChange = { onUriValueChange(uriItem.copy(uri = it)) },
        singleLine = false,
        keyboardType = KeyboardType.Uri,
        actions = {
            UniversalStandardIconButton(
                vectorIconRes = UniversalDrawable.ic_cog,
                contentDescription = stringResource(id = UniversalString.more_options),
                onClick = { shouldShowOptionsDialog = true },
                modifier = Modifier.testTag(tag = "LoginUriOptionsButton"),
            )
        },
        textFieldTestTag = "LoginUriEntry",
        cardStyle = cardStyle,
        modifier = modifier,
    )

    if (shouldShowOptionsDialog) {
        UniversalSelectionDialog(
            title = stringResource(id = UniversalString.options),
            onDismissRequest = { shouldShowOptionsDialog = false },
        ) {
            UniversalBasicDialogRow(
                text = stringResource(id = UniversalString.match_detection),
                onClick = {
                    shouldShowOptionsDialog = false
                    shouldShowMatchDialog = true
                },
            )
            UniversalBasicDialogRow(
                text = stringResource(id = UniversalString.remove),
                onClick = {
                    shouldShowOptionsDialog = false
                    onUriItemRemoved(uriItem)
                },
            )
        }
    }

    if (shouldShowMatchDialog) {
        UniversalSelectionDialog(
            title = stringResource(id = UniversalString.uri_match_detection),
            onDismissRequest = { shouldShowMatchDialog = false },
        ) {
            UniversalMultiSelectDialogContent(
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
            UniversalString.advanced_option_with_increased_risk_of_exposing_credentials
        }

        UriMatchDisplayType.REGULAR_EXPRESSION -> {
            UniversalString.advanced_option_increased_risk_exposing_credentials_used_incorrectly
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

    UniversalTwoButtonDialog(
        title = stringResource(
            id = UniversalString.are_you_sure_you_want_to_use,
            formatArgs = arrayOf(nameOfSelectedMatchDisplayType),
        ),
        message = stringResource(
            id = descriptionStringResId,
            formatArgs = arrayOf(nameOfSelectedMatchDisplayType),
        ),
        confirmButtonText = stringResource(id = UniversalString.yes),
        dismissButtonText = stringResource(id = UniversalString.cancel),
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
    UniversalTwoButtonDialog(
        title = stringResource(id = UniversalString.keep_your_credential_secure),
        message = stringResource(
            id = UniversalString.learn_more_about_how_to_keep_credentirals_secure,
            formatArgs = arrayOf(
                uriMatchDisplayType
                    .displayLabel(
                        defaultUriOption = defaultUriOption,
                    )
                    .invoke(),
            ),
        ),
        confirmButtonText = stringResource(id = UniversalString.learn_more),
        dismissButtonText = stringResource(id = UniversalString.close),
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
                title = stringResource(id = UniversalString.advanced_options),
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
