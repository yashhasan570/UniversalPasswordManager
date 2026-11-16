package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.dropdown.Universal Password ManagerDatePickerButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditLicenseTypeHandlers

/**
 * The UI for adding and editing a license cipher.
 */
@Suppress("LongMethod")
fun LazyListScope.vaultAddEditLicenseItems(
    licenseState: VaultAddEditState.ViewState.Content.ItemType.License,
    licenseHandlers: VaultAddEditLicenseTypeHandlers,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.license_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.first_name),
            value = licenseState.firstName,
            onValueChange = licenseHandlers.onFirstNameTextChange,
            textFieldTestTag = "LicenseFirstNameEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.middle_name),
            value = licenseState.middleName,
            onValueChange = licenseHandlers.onMiddleNameTextChange,
            textFieldTestTag = "LicenseMiddleNameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.last_name),
            value = licenseState.lastName,
            onValueChange = licenseHandlers.onLastNameTextChange,
            textFieldTestTag = "LicenseLastNameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.license_number),
            value = licenseState.licenseNumber,
            onValueChange = licenseHandlers.onLicenseNumberTextChange,
            passwordFieldTestTag = "LicenseLicenseNumberEntry",
            showPasswordTestTag = "LicenseShowLicenseNumberButton",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerDatePickerButton(
            label = stringResource(id = Universal Password ManagerString.date_of_birth),
            currentDate = licenseState.dateOfBirth,
            onDateSelect = licenseHandlers.onDateOfBirthChange,
            textFieldTestTag = "LicenseDateOfBirthEntry",
            allowFutureDates = false,
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.issuing_country),
            value = licenseState.issuingCountry,
            onValueChange = licenseHandlers.onIssuingCountryTextChange,
            textFieldTestTag = "LicenseIssuingCountryEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.issuing_state),
            value = licenseState.issuingState,
            onValueChange = licenseHandlers.onIssuingStateTextChange,
            textFieldTestTag = "LicenseIssuingStateEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.issuing_authority),
            value = licenseState.issuingAuthority,
            onValueChange = licenseHandlers.onIssuingAuthorityTextChange,
            textFieldTestTag = "LicenseIssuingAuthorityEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerDatePickerButton(
            label = stringResource(id = Universal Password ManagerString.issue_date),
            currentDate = licenseState.issueDate,
            onDateSelect = licenseHandlers.onIssueDateChange,
            textFieldTestTag = "LicenseIssueDateEntry",
            allowFutureDates = false,
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerDatePickerButton(
            label = stringResource(id = Universal Password ManagerString.expiration_date),
            currentDate = licenseState.expirationDate,
            onDateSelect = licenseHandlers.onExpirationDateChange,
            textFieldTestTag = "LicenseExpirationDateEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.license_class),
            value = licenseState.licenseClass,
            onValueChange = licenseHandlers.onLicenseClassTextChange,
            textFieldTestTag = "LicenseLicenseClassEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}
