package com.x8bit.universalpasswordmanager.ui.vault.model

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

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
    USERNAME(id = 100.toUInt(), label = Universal Password ManagerString.username.asText()),
    PASSWORD(id = 101.toUInt(), label = Universal Password ManagerString.password.asText()),

    CARDHOLDER_NAME(id = 300.toUInt(), label = Universal Password ManagerString.cardholder_name.asText()),
    EXPIRATION_MONTH(id = 301.toUInt(), label = Universal Password ManagerString.expiration_month.asText()),
    EXPIRATION_YEAR(id = 302.toUInt(), label = Universal Password ManagerString.expiration_year.asText()),
    SECURITY_CODE(id = 303.toUInt(), label = Universal Password ManagerString.security_code.asText()),
    BRAND(id = 304.toUInt(), label = Universal Password ManagerString.brand.asText()),
    NUMBER(id = 305.toUInt(), label = Universal Password ManagerString.number.asText()),

    TITLE(id = 400.toUInt(), label = Universal Password ManagerString.title.asText()),
    MIDDLE_NAME(id = 401.toUInt(), label = Universal Password ManagerString.middle_name.asText()),
    ADDRESS_1(id = 402.toUInt(), label = Universal Password ManagerString.address1.asText()),
    ADDRESS_2(id = 403.toUInt(), label = Universal Password ManagerString.address2.asText()),
    ADDRESS_3(id = 404.toUInt(), label = Universal Password ManagerString.address3.asText()),
    CITY(id = 405.toUInt(), label = Universal Password ManagerString.city_town.asText()),
    STATE(id = 406.toUInt(), label = Universal Password ManagerString.state_province.asText()),
    POSTAL_CODE(id = 407.toUInt(), label = Universal Password ManagerString.zip_postal_code.asText()),
    COUNTRY(id = 408.toUInt(), label = Universal Password ManagerString.country.asText()),
    COMPANY(id = 409.toUInt(), label = Universal Password ManagerString.company.asText()),
    EMAIL(id = 410.toUInt(), label = Universal Password ManagerString.email.asText()),
    PHONE(id = 411.toUInt(), label = Universal Password ManagerString.phone.asText()),
    SSN(id = 412.toUInt(), label = Universal Password ManagerString.ssn.asText()),
    IDENTITY_USERNAME(id = 413.toUInt(), label = Universal Password ManagerString.username.asText()),
    PASSPORT_NUMBER(id = 414.toUInt(), label = Universal Password ManagerString.passport_number.asText()),
    LICENSE_NUMBER(id = 415.toUInt(), label = Universal Password ManagerString.license_number.asText()),
    FIRST_NAME(id = 416.toUInt(), label = Universal Password ManagerString.first_name.asText()),
    LAST_NAME(id = 417.toUInt(), label = Universal Password ManagerString.last_name.asText()),
    FULL_NAME(id = 418.toUInt(), label = Universal Password ManagerString.full_name.asText()),
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
