package com.universalpasswordmanager.ui.vault.feature.item.util

import androidx.annotation.DrawableRes
import com.universalpasswordmanager.core.data.util.toFormattedDateStyle
import com.universalpasswordmanager.core.data.util.toFormattedDateTimeStyle
import com.universalpasswordmanager.ui.platform.base.util.nullIfAllEqual
import com.universalpasswordmanager.ui.platform.base.util.orNullIfBlank
import com.universalpasswordmanager.ui.platform.base.util.orZeroWidthSpace
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.vault.CardView
import com.universalpasswordmanager.vault.CipherType
import com.universalpasswordmanager.vault.CipherView
import com.universalpasswordmanager.vault.Fido2CredentialView
import com.universalpasswordmanager.vault.FieldType
import com.universalpasswordmanager.vault.FieldView
import com.universalpasswordmanager.vault.IdentityView
import com.universalpasswordmanager.vault.LoginUriView
import com.universalpasswordmanager.data.vault.repository.model.VaultData
import com.universalpasswordmanager.ui.vault.feature.attachments.util.isLargeFile
import com.universalpasswordmanager.ui.vault.feature.item.VaultItemState
import com.universalpasswordmanager.ui.vault.feature.item.model.TotpCodeItemData
import com.universalpasswordmanager.ui.vault.feature.item.model.VaultItemLocation
import com.universalpasswordmanager.ui.vault.feature.vault.util.toLoginIconData
import com.universalpasswordmanager.ui.vault.model.VaultBankAccountType
import com.universalpasswordmanager.ui.vault.model.VaultCardBrand
import com.universalpasswordmanager.ui.vault.model.VaultLinkedFieldType
import com.universalpasswordmanager.ui.vault.model.findVaultCardBrandWithNameOrNull
import com.universalpasswordmanager.ui.vault.util.formatCardNumber
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.Clock
import java.time.LocalDate
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Transforms [VaultData] into [VaultItemState.ViewState].
 */
@Suppress("CyclomaticComplexMethod", "LongMethod", "LongParameterList")
fun CipherView.toViewState(
    previousState: VaultItemState.ViewState.Content?,
    isPremiumUser: Boolean,
    totpCodeItemData: TotpCodeItemData?,
    clock: Clock = Clock.systemDefaultZone(),
    canDelete: Boolean,
    canRestore: Boolean,
    canAssignToCollections: Boolean,
    canEdit: Boolean,
    baseIconUrl: String,
    isIconLoadingDisabled: Boolean,
    relatedLocations: ImmutableList<VaultItemLocation>,
    hasOrganizations: Boolean,
    isInAppUpgradeAvailable: Boolean,
): VaultItemState.ViewState =
    VaultItemState.ViewState.Content(
        common = VaultItemState.ViewState.Content.Common(
            currentCipher = this,
            name = name,
            customFields = fields
                .orEmpty()
                .map { fieldView ->
                    fieldView.toCustomField(
                        previousState = previousState
                            ?.common
                            ?.customFields
                            ?.find { it.id == fieldView.hashCode().toString() },
                    )
                }
                .toImmutableList(),
            created = UniversalString.created.asText(
                creationDate.toFormattedDateTimeStyle(
                    dateStyle = FormatStyle.MEDIUM,
                    timeStyle = FormatStyle.SHORT,
                    clock = clock,
                ),
            ),
            lastUpdated = UniversalString.last_edited.asText(
                revisionDate.toFormattedDateTimeStyle(
                    dateStyle = FormatStyle.MEDIUM,
                    timeStyle = FormatStyle.SHORT,
                    clock = clock,
                ),
            ),
            notes = notes,
            requiresCloneConfirmation = login?.fido2Credentials?.any() ?: false,
            attachments = attachments
                ?.mapNotNull {
                    @Suppress("ComplexCondition")
                    if (it.id == null ||
                        it.fileName == null ||
                        it.size == null ||
                        it.sizeName == null ||
                        it.url == null
                    ) {
                        null
                    } else {
                        VaultItemState.ViewState.Content.Common.AttachmentItem(
                            id = requireNotNull(it.id),
                            title = requireNotNull(it.fileName),
                            displaySize = requireNotNull(it.sizeName),
                            url = requireNotNull(it.url),
                            isLargeFile = it.isLargeFile(),
                            isDownloadAllowed = isPremiumUser || this.organizationId != null,
                        )
                    }
                }
                .orEmpty()
                .toImmutableList(),
            canDelete = canDelete,
            canRestore = canRestore,
            canAssignToCollections = canAssignToCollections,
            canEdit = canEdit,
            favorite = this.favorite,
            archived = this.archivedDate != null,
            passwordHistoryCount = passwordHistory?.count(),
            iconData = this.toIconData(
                baseIconUrl = baseIconUrl,
                isIconLoadingDisabled = isIconLoadingDisabled,
            ),
            relatedLocations = relatedLocations,
            hasOrganizations = hasOrganizations,
            isInAppUpgradeAvailable = isInAppUpgradeAvailable,
        ),
        type = when (type) {
            CipherType.LOGIN -> {
                val loginValues = requireNotNull(login)
                VaultItemState.ViewState.Content.ItemType.Login(
                    username = loginValues.username,
                    passwordData = loginValues.password?.let {
                        VaultItemState.ViewState.Content.ItemType.Login.PasswordData(
                            password = it,
                            isVisible = (previousState?.type as?
                                VaultItemState.ViewState.Content.ItemType.Login)
                                ?.passwordData
                                ?.isVisible == true,
                            canViewPassword = viewPassword,
                        )
                    },
                    uris = loginValues.uris.orEmpty().map { it.toUriData() },
                    passwordRevisionDate = loginValues
                        .passwordRevisionDate
                        ?.toFormattedDateTimeStyle(
                            dateStyle = FormatStyle.MEDIUM,
                            timeStyle = FormatStyle.SHORT,
                            clock = clock,
                        )
                        ?.let { UniversalString.password_updated.asText(it) },
                    isPremiumUser = isPremiumUser,
                    canViewTotpCode = isPremiumUser || this.organizationUseTotp,
                    totpCodeItemData = totpCodeItemData,
                    fido2CredentialCreationDateText = loginValues
                        .fido2Credentials
                        ?.firstOrNull()
                        ?.getCreationDateText(clock),
                )
            }

            CipherType.SECURE_NOTE -> {
                VaultItemState.ViewState.Content.ItemType.SecureNote
            }

            CipherType.CARD -> {
                VaultItemState.ViewState.Content.ItemType.Card(
                    cardholderName = card?.cardholderName,
                    number = card?.number?.let {
                        VaultItemState.ViewState.Content.ItemType.Card.NumberData(
                            number = it.formatCardNumber(),
                            isVisible = (previousState?.type
                                as? VaultItemState.ViewState.Content.ItemType.Card)
                                ?.number
                                ?.isVisible == true,
                        )
                    },
                    brand = card?.cardBrand,
                    expiration = card?.expiration,
                    securityCode = card?.code?.let {
                        VaultItemState.ViewState.Content.ItemType.Card.CodeData(
                            code = it,
                            isVisible = (previousState?.type
                                as? VaultItemState.ViewState.Content.ItemType.Card)
                                ?.securityCode
                                ?.isVisible == true,
                        )
                    },
                    paymentCardBrandIconData = card?.paymentCardBrandIconRes?.let {
                        IconData.Local(iconRes = it)
                    },
                )
            }

            CipherType.IDENTITY -> {
                VaultItemState.ViewState.Content.ItemType.Identity(
                    username = identity?.username,
                    identityName = identity?.identityName,
                    company = identity?.company,
                    ssn = identity?.ssn,
                    passportNumber = identity?.passportNumber,
                    licenseNumber = identity?.licenseNumber,
                    email = identity?.email,
                    phone = identity?.phone,
                    address = identity?.identityAddress,
                )
            }

            CipherType.SSH_KEY -> {
                val sshKeyValues = requireNotNull(sshKey)
                VaultItemState.ViewState.Content.ItemType.SshKey(
                    name = name,
                    publicKey = sshKeyValues.publicKey,
                    privateKey = sshKeyValues.privateKey,
                    fingerprint = sshKeyValues.fingerprint,
                    showPrivateKey = (previousState?.type as?
                        VaultItemState.ViewState.Content.ItemType.SshKey)
                        ?.showPrivateKey == true,
                )
            }

            CipherType.BANK_ACCOUNT -> {
                VaultItemState.ViewState.Content.ItemType.BankAccount(
                    bankName = bankAccount?.bankName,
                    nameOnAccount = bankAccount?.nameOnAccount,
                    accountType = bankAccount?.accountType?.let(VaultBankAccountType::parse),
                    accountNumber = bankAccount?.accountNumber,
                    routingNumber = bankAccount?.routingNumber,
                    branchNumber = bankAccount?.branchNumber,
                    pin = bankAccount?.pin,
                    swiftCode = bankAccount?.swiftCode,
                    iban = bankAccount?.iban,
                    bankContactPhone = bankAccount?.bankContactPhone,
                )
            }

            CipherType.DRIVERS_LICENSE -> {
                VaultItemState.ViewState.Content.ItemType.DriversLicense(
                    firstName = driversLicense?.firstName,
                    middleName = driversLicense?.middleName,
                    lastName = driversLicense?.lastName,
                    licenseNumber = driversLicense?.licenseNumber,
                    dateOfBirth = driversLicense?.dateOfBirth?.toFormattedDate(clock = clock),
                    issuingCountry = driversLicense?.issuingCountry,
                    issuingState = driversLicense?.issuingState,
                    issuingAuthority = driversLicense?.issuingAuthority,
                    issueDate = driversLicense?.issueDate?.toFormattedDate(clock = clock),
                    expirationDate = driversLicense?.expirationDate?.toFormattedDate(clock = clock),
                    licenseClass = driversLicense?.licenseClass,
                )
            }

            CipherType.PASSPORT -> VaultItemState.ViewState.Content.ItemType.Passport(
                givenName = passport?.givenName,
                surname = passport?.surname,
                dateOfBirth = passport?.dateOfBirth?.toFormattedDate(clock = clock),
                sex = passport?.sex,
                birthPlace = passport?.birthPlace,
                nationality = passport?.nationality,
                passportNumber = passport?.passportNumber,
                passportType = passport?.passportType,
                nationalIdentificationNumber = passport?.nationalIdentificationNumber,
                issuingCountry = passport?.issuingCountry,
                issuingAuthority = passport?.issuingAuthority,
                issueDate = passport?.issueDate?.toFormattedDate(clock = clock),
                expirationDate = passport?.expirationDate?.toFormattedDate(clock = clock),
            )
        },
    )

/**
 * Transforms [FieldView] into [VaultItemState.ViewState.Content.Common.Custom].
 */
fun FieldView.toCustomField(
    previousState: VaultItemState.ViewState.Content.Common.Custom?,
): VaultItemState.ViewState.Content.Common.Custom =
    when (type) {
        FieldType.TEXT -> VaultItemState.ViewState.Content.Common.Custom.TextField(
            id = this.hashCode().toString(),
            name = name.orEmpty(),
            value = value.orZeroWidthSpace(),
            isCopyable = !value.isNullOrBlank(),
        )

        FieldType.HIDDEN -> VaultItemState.ViewState.Content.Common.Custom.HiddenField(
            id = this.hashCode().toString(),
            name = name.orEmpty(),
            value = value.orZeroWidthSpace(),
            isCopyable = !value.isNullOrBlank(),
            isVisible = (previousState as?
                VaultItemState.ViewState.Content.Common.Custom.HiddenField)
                ?.isVisible
                ?: false,
        )

        FieldType.BOOLEAN -> VaultItemState.ViewState.Content.Common.Custom.BooleanField(
            id = this.hashCode().toString(),
            name = name.orEmpty(),
            value = value?.toBoolean() ?: false,
        )

        FieldType.LINKED -> VaultItemState.ViewState.Content.Common.Custom.LinkedField(
            id = this.hashCode().toString(),
            vaultLinkedFieldType = VaultLinkedFieldType.fromId(requireNotNull(linkedId)),
            name = name.orEmpty(),
        )
    }

/**
 * Takes a [LocalDate] and converts it to appropriate human-readable format.
 */
private fun LocalDate.toFormattedDate(
    clock: Clock,
): String = this.toFormattedDateStyle(dateStyle = FormatStyle.LONG, clock = clock)

private fun LoginUriView.toUriData() =
    VaultItemState.ViewState.Content.ItemType.Login.UriData(
        uri = uri.orZeroWidthSpace(),
        isCopyable = !uri.isNullOrBlank(),
        isLaunchable = !uri.isNullOrBlank(),
    )

private fun Fido2CredentialView.getCreationDateText(clock: Clock): Text =
    UniversalString.created_x.asText(
        this.creationDate.toFormattedDateTimeStyle(
            dateStyle = FormatStyle.MEDIUM,
            timeStyle = FormatStyle.SHORT,
            clock = clock,
        ),
    )

private fun CipherView.toIconData(
    baseIconUrl: String,
    isIconLoadingDisabled: Boolean,
): IconData {
    return when (this.type) {
        CipherType.LOGIN -> {
            login?.uris.toLoginIconData(
                baseIconUrl = baseIconUrl,
                isIconLoadingDisabled = isIconLoadingDisabled,
                usePasskeyDefaultIcon = false,
            )
        }

        CipherType.CARD -> {
            card?.paymentCardBrandIconRes
                ?.let { IconData.Local(iconRes = it) }
                ?: IconData.Local(type.iconRes)
        }

        else -> {
            IconData.Local(iconRes = this.type.iconRes)
        }
    }
}

@get:DrawableRes
private val CipherType.iconRes: Int
    get() = when (this) {
        CipherType.SECURE_NOTE -> UniversalDrawable.ic_note
        CipherType.CARD -> UniversalDrawable.ic_payment_card
        CipherType.IDENTITY -> UniversalDrawable.ic_id_card
        CipherType.SSH_KEY -> UniversalDrawable.ic_ssh_key
        CipherType.LOGIN -> UniversalDrawable.ic_globe
        CipherType.BANK_ACCOUNT -> UniversalDrawable.ic_payment_card
        CipherType.DRIVERS_LICENSE -> UniversalDrawable.ic_note
        CipherType.PASSPORT -> UniversalDrawable.ic_passport
    }

@get:DrawableRes
private val CardView.paymentCardBrandIconRes: Int?
    get() = when (this.cardBrand) {
        VaultCardBrand.VISA -> UniversalDrawable.ic_payment_card_brand_visa
        VaultCardBrand.MASTERCARD -> UniversalDrawable.ic_payment_card_brand_mastercard
        VaultCardBrand.AMEX -> UniversalDrawable.ic_payment_card_brand_amex
        VaultCardBrand.DISCOVER -> UniversalDrawable.ic_payment_card_brand_discover
        VaultCardBrand.DINERS_CLUB -> UniversalDrawable.ic_payment_card_brand_diners_club
        VaultCardBrand.JCB -> UniversalDrawable.ic_payment_card_brand_jcb
        VaultCardBrand.MAESTRO -> UniversalDrawable.ic_payment_card_brand_maestro
        VaultCardBrand.UNIONPAY -> UniversalDrawable.ic_payment_card_brand_union_pay
        VaultCardBrand.RUPAY -> UniversalDrawable.ic_payment_card_brand_ru_pay
        VaultCardBrand.SELECT,
        VaultCardBrand.OTHER,
        null,
            -> null
    }

private val CardView.cardBrand: VaultCardBrand?
    get() = brand
        ?.findVaultCardBrandWithNameOrNull()
        .takeUnless { it == VaultCardBrand.SELECT }

private val CardView.expiration: String?
    get() = listOfNotNull(
        expMonth?.padStart(length = 2, padChar = '0'),
        expYear,
    )
        .joinToString("/")
        .orNullIfBlank()

/**
 * The full postal address for an [IdentityView], joining its address parts.
 */
private val IdentityView.identityAddress: String?
    get() = listOfNotNull(
        address1,
        address2,
        address3,
        listOf(city ?: "-", state ?: "-", postalCode ?: "-")
            .nullIfAllEqual("-")
            ?.joinToString(", "),
        country,
    )
        .joinToString("\n")
        .orNullIfBlank()

/**
 * The full name for an [IdentityView], joining its name parts.
 */
private val IdentityView.identityName: String?
    get() = listOfNotNull(
        title
            ?.lowercase()
            ?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            },
        firstName,
        middleName,
        lastName,
    )
        .joinToString(" ")
        .orNullIfBlank()
