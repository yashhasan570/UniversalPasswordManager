package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditBankAccountTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultBankAccountType
import com.x8bit.universalpasswordmanager.ui.vault.util.longName
import kotlinx.collections.immutable.toImmutableList

/**
 * The UI for adding and editing a bank account cipher.
 */
@Suppress("LongMethod")
fun LazyListScope.vaultAddEditBankAccountItems(
    bankAccountState: VaultAddEditState.ViewState.Content.ItemType.BankAccount,
    bankAccountHandlers: VaultAddEditBankAccountTypeHandlers,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.account_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.bank_name),
            value = bankAccountState.bankName,
            onValueChange = bankAccountHandlers.onBankNameTextChange,
            textFieldTestTag = "BankNameEntry",
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.name_on_account),
            value = bankAccountState.nameOnAccount,
            onValueChange = bankAccountHandlers.onNameOnAccountTextChange,
            textFieldTestTag = "NameOnAccountEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        val resources = LocalResources.current
        Universal Password ManagerMultiSelectButton(
            label = stringResource(id = Universal Password ManagerString.account_type),
            options = VaultBankAccountType
                .entries
                .map { it.longName() }
                .toImmutableList(),
            selectedOption = bankAccountState.accountType.longName(),
            onOptionSelected = { selectedString ->
                bankAccountHandlers.onAccountTypeSelect(
                    VaultBankAccountType
                        .entries
                        .first { it.longName.toString(resources) == selectedString },
                )
            },
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .testTag("AccountTypePicker")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.account_number),
            value = bankAccountState.accountNumber,
            onValueChange = bankAccountHandlers.onAccountNumberTextChange,
            showPasswordTestTag = "ShowAccountNumberButton",
            passwordFieldTestTag = "AccountNumberEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.routing_number),
            value = bankAccountState.routingNumber,
            onValueChange = bankAccountHandlers.onRoutingNumberTextChange,
            textFieldTestTag = "RoutingNumberEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.branch_number),
            value = bankAccountState.branchNumber,
            onValueChange = bankAccountHandlers.onBranchNumberTextChange,
            textFieldTestTag = "BranchNumberEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.pin),
            value = bankAccountState.pin,
            onValueChange = bankAccountHandlers.onPinTextChange,
            keyboardType = KeyboardType.NumberPassword,
            showPasswordTestTag = "ShowPinButton",
            passwordFieldTestTag = "PinEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.swift_code),
            value = bankAccountState.swiftCode,
            onValueChange = bankAccountHandlers.onSwiftCodeTextChange,
            textFieldTestTag = "SwiftCodeEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.iban),
            value = bankAccountState.iban,
            onValueChange = bankAccountHandlers.onIbanTextChange,
            showPasswordTestTag = "ShowIbanButton",
            passwordFieldTestTag = "IbanEntry",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    item {
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.bank_contact_phone),
            value = bankAccountState.bankContactPhone,
            onValueChange = bankAccountHandlers.onBankContactPhoneTextChange,
            textFieldTestTag = "BankContactPhoneEntry",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}
