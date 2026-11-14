package com.universalpasswordmanager.ui.vault.model

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Represents the types for linked fields.
 *
 * @param id The ID for the linked field.
 * @param label A human-readable label for the linked field.
 */
enum class VaultLinkedFieldType(
    val id: UInt,
    val label: Text,
) {
    USERNAME(id = 100.toUInt(), label = UniversalString.username.asText()),
    PASSWORD(id = 101.toUInt(), label = UniversalString.password.asText()),

    CARDHOLDER_NAME(id = 300.toUInt(), label = UniversalString.cardholder_name.asText()),
    EXPIRATION_MONTH(id = 301.toUInt(), label = UniversalString.expiration_month.asText()),
    EXPIRATION_YEAR(id = 302.toUInt(), label = UniversalString.expiration_year.asText()),
    SECURITY_CODE(id = 303.toUInt(), label = UniversalString.security_code.asText()),
    BRAND(id = 304.toUInt(), label = UniversalString.brand.asText()),
    NUMBER(id = 305.toUInt(), label = UniversalString.number.asText()),

    TITLE(id = 400.toUInt(), label = UniversalString.title.asText()),
    MIDDLE_NAME(id = 401.toUInt(), label = UniversalString.middle_name.asText()),
    ADDRESS_1(id = 402.toUInt(), label = UniversalString.address1.asText()),
    ADDRESS_2(id = 403.toUInt(), label = UniversalString.address2.asText()),
    ADDRESS_3(id = 404.toUInt(), label = UniversalString.address3.asText()),
    CITY(id = 405.toUInt(), label = UniversalString.city_town.asText()),
    STATE(id = 406.toUInt(), label = UniversalString.state_province.asText()),
    POSTAL_CODE(id = 407.toUInt(), label = UniversalString.zip_postal_code.asText()),
    COUNTRY(id = 408.toUInt(), label = UniversalString.country.asText()),
    COMPANY(id = 409.toUInt(), label = UniversalString.company.asText()),
    EMAIL(id = 410.toUInt(), label = UniversalString.email.asText()),
    PHONE(id = 411.toUInt(), label = UniversalString.phone.asText()),
    SSN(id = 412.toUInt(), label = UniversalString.ssn.asText()),
    IDENTITY_USERNAME(id = 413.toUInt(), label = UniversalString.username.asText()),
    PASSPORT_NUMBER(id = 414.toUInt(), label = UniversalString.passport_number.asText()),
    LICENSE_NUMBER(id = 415.toUInt(), label = UniversalString.license_number.asText()),
    FIRST_NAME(id = 416.toUInt(), label = UniversalString.first_name.asText()),
    LAST_NAME(id = 417.toUInt(), label = UniversalString.last_name.asText()),
    FULL_NAME(id = 418.toUInt(), label = UniversalString.full_name.asText()),
    ;

    @Suppress("UndocumentedPublicClass")
    companion object {
        /**
         * Helper function to get the LinkedCustomFieldType from the id
         */
        fun fromId(id: UInt): VaultLinkedFieldType =
            VaultLinkedFieldType.entries.first { it.id == id }
    }
}
