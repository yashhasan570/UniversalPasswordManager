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
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditPassportTypeHandlers

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
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.passport_details),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.last_name),
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
        Universal Password ManagerDatePickerButton(
            label = stringResource(id = Universal Password ManagerString.date_of_birth),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.sex),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.birth_place),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.nationality),
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
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.passport_number),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.passport_type),
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
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.national_identification_number),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.issuing_country),
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
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.issuing_authority),
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
        Universal Password ManagerDatePickerButton(
            label = stringResource(id = Universal Password ManagerString.issue_date),
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
        Universal Password ManagerDatePickerButton(
            label = stringResource(id = Universal Password ManagerString.expiration_date),
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
