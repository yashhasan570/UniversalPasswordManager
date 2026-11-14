package com.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.dropdown.UniversalDatePickerButton
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditLicenseTypeHandlers

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
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.license_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
        UniversalTextField(
            label = stringResource(id = UniversalString.first_name),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.middle_name),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.last_name),
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
        UniversalPasswordField(
            label = stringResource(id = UniversalString.license_number),
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
        UniversalDatePickerButton(
            label = stringResource(id = UniversalString.date_of_birth),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.issuing_country),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.issuing_state),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.issuing_authority),
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
        UniversalDatePickerButton(
            label = stringResource(id = UniversalString.issue_date),
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
        UniversalDatePickerButton(
            label = stringResource(id = UniversalString.expiration_date),
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
        UniversalTextField(
            label = stringResource(id = UniversalString.license_class),
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
