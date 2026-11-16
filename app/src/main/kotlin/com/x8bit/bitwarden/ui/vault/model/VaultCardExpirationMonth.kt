package com.x8bit.universalpasswordmanager.ui.vault.model

import androidx.annotation.StringRes
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import .ui.util.concat

/**
 * Defines all available expiration month options for cards.
 */
enum class VaultCardExpirationMonth(
    val value: Text,
    val number: String,
) {
    SELECT(
        value = Universal Password ManagerString.select.asText(),
        number = "0",
    ),
    JANUARY(
        value = Universal Password ManagerString.january.dateText("01 - "),
        number = "1",
    ),
    FEBRUARY(
        value = Universal Password ManagerString.february.dateText("02 - "),
        number = "2",
    ),
    MARCH(
        value = Universal Password ManagerString.march.dateText("03 - "),
        number = "3",
    ),
    APRIL(
        value = Universal Password ManagerString.april.dateText("04 - "),
        number = "4",
    ),
    MAY(
        value = Universal Password ManagerString.may.dateText("05 - "),
        number = "5",
    ),
    JUNE(
        value = Universal Password ManagerString.june.dateText("06 - "),
        number = "6",
    ),
    JULY(
        value = Universal Password ManagerString.july.dateText("07 - "),
        number = "7",
    ),
    AUGUST(
        value = Universal Password ManagerString.august.dateText("08 - "),
        number = "8",
    ),
    SEPTEMBER(
        value = Universal Password ManagerString.september.dateText("09 - "),
        number = "9",
    ),
    OCTOBER(
        value = Universal Password ManagerString.october.dateText("10 - "),
        number = "10",
    ),
    NOVEMBER(
        value = Universal Password ManagerString.november.dateText("11 - "),
        number = "11",
    ),
    DECEMBER(
        value = Universal Password ManagerString.december.dateText("12 - "),
        number = "12",
    ),
}

private fun @receiver:StringRes Int.dateText(prefix: String): Text =
    prefix
        .asText()
        .concat(asText())
