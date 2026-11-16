package com.x8bit.universalpasswordmanager.ui.vault.feature.item

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.icon.model.IconData
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.itemHeader
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemAttachments
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemCustomFields
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemHistory
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.component.vaultItemNotes
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.handlers.VaultBankAccountItemTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.item.handlers.VaultCommonItemTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.util.longName

/**
 * Renders the [VaultItemScreen] content when viewing a bank account cipher.
 */
@Suppress("LongMethod")
@Composable
fun VaultItemBankAccountContent(
    commonState: VaultItemState.ViewState.Content.Common,
    bankAccountState: VaultItemState.ViewState.Content.ItemType.BankAccount,
    vaultCommonItemTypeHandlers: VaultCommonItemTypeHandlers,
    vaultBankAccountItemTypeHandlers: VaultBankAccountItemTypeHandlers,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(value = false) }
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            Spacer(Modifier.height(height = 12.dp))
        }
        itemHeader(
            value = commonState.name,
            isFavorite = commonState.favorite,
            isArchived = commonState.archived,
            iconData = commonState.iconData,
            relatedLocations = commonState.relatedLocations,
            iconTestTag = "BankAccountItemNameIcon",
            textFieldTestTag = "BankAccountItemNameEntry",
            isExpanded = isExpanded,
            onExpandClick = { isExpanded = !isExpanded },
            applyIconBackground = commonState.iconData is IconData.Local,
        )
        item(key = "bankAccountDetailsHeader") {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.details),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp)
                    .animateItem(),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
        }

        bankAccountState.bankName?.let { bankName ->
            item(key = "bankName") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.bank_name),
                    value = bankName,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    textFieldTestTag = "BankAccountItemBankNameEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = bankName),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.nameOnAccount?.let { nameOnAccount ->
            item(key = "nameOnAccount") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.name_on_account),
                    value = nameOnAccount,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_name_on_account,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopyNameOnAccountClick,
                            modifier = Modifier.testTag(
                                tag = "BankAccountCopyNameOnAccountButton",
                            ),
                        )
                    },
                    textFieldTestTag = "BankAccountItemNameOnAccountEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = nameOnAccount),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.accountType?.let { accountType ->
            item(key = "accountType") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.account_type),
                    value = accountType.longName(),
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    textFieldTestTag = "BankAccountItemAccountTypeEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = accountType),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.accountNumber?.let { accountNumber ->
            item(key = "accountNumber") {
                var showAccountNumber by rememberSaveable { mutableStateOf(value = false) }
                Universal Password ManagerPasswordField(
                    label = stringResource(id = Universal Password ManagerString.account_number),
                    value = accountNumber,
                    onValueChange = {},
                    showPassword = showAccountNumber,
                    showPasswordChange = { showAccountNumber = it },
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_account_number,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopyAccountNumberClick,
                            modifier = Modifier.testTag(
                                tag = "BankAccountCopyAccountNumberButton",
                            ),
                        )
                    },
                    showPasswordTestTag = "ShowAccountNumberButton",
                    passwordFieldTestTag = "BankAccountItemAccountNumberEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = accountNumber),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.routingNumber?.let { routingNumber ->
            item(key = "routingNumber") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.routing_number),
                    value = routingNumber,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_routing_number,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopyRoutingNumberClick,
                            modifier = Modifier.testTag(
                                tag = "BankAccountCopyRoutingNumberButton",
                            ),
                        )
                    },
                    textFieldTestTag = "BankAccountItemRoutingNumberEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = routingNumber),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.branchNumber?.let { branchNumber ->
            item(key = "branchNumber") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.branch_number),
                    value = branchNumber,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_branch_number,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopyBranchNumberClick,
                            modifier = Modifier.testTag(
                                tag = "BankAccountCopyBranchNumberButton",
                            ),
                        )
                    },
                    textFieldTestTag = "BankAccountItemBranchNumberEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = branchNumber),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.pin?.let { pin ->
            item(key = "pin") {
                var showPin by rememberSaveable { mutableStateOf(value = false) }
                Universal Password ManagerPasswordField(
                    label = stringResource(id = Universal Password ManagerString.pin),
                    value = pin,
                    onValueChange = {},
                    showPassword = showPin,
                    showPasswordChange = { showPin = it },
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_pin,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopyPinClick,
                            modifier = Modifier.testTag(tag = "BankAccountCopyPinButton"),
                        )
                    },
                    showPasswordTestTag = "ShowPinButton",
                    passwordFieldTestTag = "BankAccountItemPinEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = pin),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.swiftCode?.let { swiftCode ->
            item(key = "swiftCode") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.swift_code),
                    value = swiftCode,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_swift_code,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopySwiftCodeClick,
                            modifier = Modifier.testTag(
                                tag = "BankAccountCopySwiftCodeButton",
                            ),
                        )
                    },
                    textFieldTestTag = "BankAccountItemSwiftCodeEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = swiftCode),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.iban?.let { iban ->
            item(key = "iban") {
                var showIban by rememberSaveable { mutableStateOf(value = false) }
                Universal Password ManagerPasswordField(
                    label = stringResource(id = Universal Password ManagerString.iban),
                    value = iban,
                    onValueChange = {},
                    showPassword = showIban,
                    showPasswordChange = { showIban = it },
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_iban,
                            ),
                            onClick = vaultBankAccountItemTypeHandlers.onCopyIbanClick,
                            modifier = Modifier.testTag(tag = "BankAccountCopyIbanButton"),
                        )
                    },
                    showPasswordTestTag = "ShowIbanButton",
                    passwordFieldTestTag = "BankAccountItemIbanEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = iban),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        bankAccountState.bankContactPhone?.let { phone ->
            item(key = "bankContactPhone") {
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.bank_contact_phone),
                    value = phone,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(
                                id = Universal Password ManagerString.copy_bank_contact_phone,
                            ),
                            onClick =
                                vaultBankAccountItemTypeHandlers.onCopyBankContactPhoneClick,
                            modifier = Modifier.testTag(
                                tag = "BankAccountCopyBankContactPhoneButton",
                            ),
                        )
                    },
                    textFieldTestTag = "BankAccountItemBankContactPhoneEntry",
                    cardStyle = bankAccountState
                        .propertyList
                        .toListItemCardStyle(
                            index = bankAccountState.propertyList.indexOf(element = phone),
                            dividerPadding = 0.dp,
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        vaultItemNotes(
            notes = commonState.notes,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
        )

        vaultItemCustomFields(
            customFields = commonState.customFields,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
        )

        vaultItemAttachments(
            isInAppUpgradeAvailable = commonState.isInAppUpgradeAvailable,
            attachments = commonState.attachments,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
        )

        vaultItemHistory(
            commonState = commonState,
            vaultCommonItemTypeHandlers = vaultCommonItemTypeHandlers,
            loginPasswordRevisionDate = null,
        )

        item {
            Spacer(modifier = Modifier.height(88.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
