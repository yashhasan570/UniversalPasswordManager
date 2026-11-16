package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import android.content.res.Resources
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditIdentityTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultIdentityTitle
import kotlinx.collections.immutable.toImmutableList

/**
 * The UI for adding and editing an identity cipher.
 */
@Suppress("LongMethod")
fun LazyListScope.vaultAddEditIdentityItems(
    identityState: VaultAddEditState.ViewState.Content.ItemType.Identity,
    identityItemTypeHandlers: VaultAddEditIdentityTypeHandlers,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.personal_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
        TitleMultiSelectButton(
            selectedTitle = identityState.selectedTitle,
            onTitleSelected = identityItemTypeHandlers.onTitleSelected,
            modifier = Modifier
                .testTag("IdentityTitlePicker")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.first_name),
            value = identityState.firstName,
            onValueChange = identityItemTypeHandlers.onFirstNameTextChange,
            textFieldTestTag = "IdentityFirstNameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.middle_name),
            value = identityState.middleName,
            onValueChange = identityItemTypeHandlers.onMiddleNameTextChange,
            textFieldTestTag = "IdentityMiddleNameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.last_name),
            value = identityState.lastName,
            onValueChange = identityItemTypeHandlers.onLastNameTextChange,
            textFieldTestTag = "IdentityLastNameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.username),
            value = identityState.username,
            onValueChange = identityItemTypeHandlers.onUsernameTextChange,
            textFieldTestTag = "IdentityUsernameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.company),
            value = identityState.company,
            onValueChange = identityItemTypeHandlers.onCompanyTextChange,
            textFieldTestTag = "IdentityCompanyEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.identification),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
    }
    item {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.ssn),
            value = identityState.ssn,
            onValueChange = identityItemTypeHandlers.onSsnTextChange,
            showPasswordTestTag = "IdentityShowSsnNumberButton",
            passwordFieldTestTag = "IdentitySsnEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.passport_number),
            value = identityState.passportNumber,
            onValueChange = identityItemTypeHandlers.onPassportNumberTextChange,
            showPasswordTestTag = "IdentityShowPassportNumberButton",
            passwordFieldTestTag = "IdentityPassportNumberEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.license_number),
            value = identityState.licenseNumber,
            onValueChange = identityItemTypeHandlers.onLicenseNumberTextChange,
            textFieldTestTag = "IdentityLicenseNumberEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.contact_info),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.email),
            value = identityState.email,
            onValueChange = identityItemTypeHandlers.onEmailTextChange,
            textFieldTestTag = "IdentityEmailEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.phone),
            value = identityState.phone,
            onValueChange = identityItemTypeHandlers.onPhoneTextChange,
            textFieldTestTag = "IdentityPhoneEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.address),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.address1),
            value = identityState.address1,
            onValueChange = identityItemTypeHandlers.onAddress1TextChange,
            textFieldTestTag = "IdentityAddressOneEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.address2),
            value = identityState.address2,
            onValueChange = identityItemTypeHandlers.onAddress2TextChange,
            textFieldTestTag = "IdentityAddressTwoEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.address3),
            value = identityState.address3,
            onValueChange = identityItemTypeHandlers.onAddress3TextChange,
            textFieldTestTag = "IdentityAddressThreeEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.city_town),
            value = identityState.city,
            onValueChange = identityItemTypeHandlers.onCityTextChange,
            textFieldTestTag = "IdentityCityEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.state_province),
            value = identityState.state,
            onValueChange = identityItemTypeHandlers.onStateTextChange,
            textFieldTestTag = "IdentityStateEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.zip_postal_code),
            value = identityState.zip,
            onValueChange = identityItemTypeHandlers.onZipTextChange,
            textFieldTestTag = "IdentityPostalCodeEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.country),
            value = identityState.country,
            onValueChange = identityItemTypeHandlers.onCountryTextChange,
            textFieldTestTag = "IdentityCountryEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun TitleMultiSelectButton(
    selectedTitle: VaultIdentityTitle,
    onTitleSelected: (VaultIdentityTitle) -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.title),
        options = VaultIdentityTitle
            .entries
            .map { it.value() }
            .toImmutableList(),
        selectedOption = selectedTitle.value(),
        onOptionSelected = { selectedString ->
            onTitleSelected(
                VaultIdentityTitle
                    .entries
                    .first { it.value.toString(resources) == selectedString },
            )
        },
        cardStyle = CardStyle.Top(),
        modifier = modifier,
    )
}
