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
import com.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditPassportTypeHandlers

/**
 * The UI for adding and editing a passport cipher.
 */
@Suppress("LongMethod")
fun LazyListScope.vaultAddEditPassportItems(
    passportState: VaultAddEditState.ViewState.Content.ItemType.Passport,
    passportHandlers: VaultAddEditPassportTypeHandlers,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.passport_details),
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
            value = passportState.givenName,
            onValueChange = passportHandlers.onGivenNameTextChange,
            textFieldTestTag = "PassportGivenNameEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.last_name),
            value = passportState.surname,
            onValueChange = passportHandlers.onSurnameTextChange,
            textFieldTestTag = "PassportSurnameEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalDatePickerButton(
            label = stringResource(id = UniversalString.date_of_birth),
            currentDate = passportState.dateOfBirth,
            onDateSelect = passportHandlers.onDateOfBirthChange,
            textFieldTestTag = "PassportDateOfBirthEntry",
            allowFutureDates = false,
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.sex),
            value = passportState.sex,
            onValueChange = passportHandlers.onSexTextChange,
            textFieldTestTag = "PassportSexEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.birth_place),
            value = passportState.birthPlace,
            onValueChange = passportHandlers.onBirthPlaceTextChange,
            textFieldTestTag = "PassportBirthPlaceEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.nationality),
            value = passportState.nationality,
            onValueChange = passportHandlers.onNationalityTextChange,
            textFieldTestTag = "PassportNationalityEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalPasswordField(
            label = stringResource(id = UniversalString.passport_number),
            value = passportState.passportNumber,
            onValueChange = passportHandlers.onPassportNumberTextChange,
            passwordFieldTestTag = "PassportPassportNumberEntry",
            showPasswordTestTag = "PassportShowPassportNumberButton",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.passport_type),
            value = passportState.passportType,
            onValueChange = passportHandlers.onPassportTypeTextChange,
            textFieldTestTag = "PassportPassportTypeEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalPasswordField(
            label = stringResource(id = UniversalString.national_identification_number),
            value = passportState.nationalIdentificationNumber,
            onValueChange = passportHandlers.onNationalIdentificationNumberTextChange,
            passwordFieldTestTag = "PassportNationalIdentificationNumberEntry",
            showPasswordTestTag = "PassportShowNationalIdentificationNumberButton",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.issuing_country),
            value = passportState.issuingCountry,
            onValueChange = passportHandlers.onIssuingCountryTextChange,
            textFieldTestTag = "PassportIssuingCountryEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalTextField(
            label = stringResource(id = UniversalString.issuing_authority),
            value = passportState.issuingAuthority,
            onValueChange = passportHandlers.onIssuingAuthorityTextChange,
            textFieldTestTag = "PassportIssuingAuthorityEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        UniversalDatePickerButton(
            label = stringResource(id = UniversalString.issue_date),
            currentDate = passportState.issueDate,
            onDateSelect = passportHandlers.onIssueDateChange,
            textFieldTestTag = "PassportIssueDateEntry",
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
            currentDate = passportState.expirationDate,
            onDateSelect = passportHandlers.onExpirationDateChange,
            textFieldTestTag = "PassportExpirationDateEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}
