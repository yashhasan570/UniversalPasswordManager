package com.universalpasswordmanager.ui.vault.model

import androidx.annotation.StringRes
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.util.concat

/**
 * Defines all available expiration month options for cards.
 */
enum class VaultCardExpirationMonth(
    val value: Text,
    val number: String,
) {
    SELECT(
        value = UniversalString.select.asText(),
        number = "0",
    ),
    JANUARY(
        value = UniversalString.january.dateText("01 - "),
        number = "1",
    ),
    FEBRUARY(
        value = UniversalString.february.dateText("02 - "),
        number = "2",
    ),
    MARCH(
        value = UniversalString.march.dateText("03 - "),
        number = "3",
    ),
    APRIL(
        value = UniversalString.april.dateText("04 - "),
        number = "4",
    ),
    MAY(
        value = UniversalString.may.dateText("05 - "),
        number = "5",
    ),
    JUNE(
        value = UniversalString.june.dateText("06 - "),
        number = "6",
    ),
    JULY(
        value = UniversalString.july.dateText("07 - "),
        number = "7",
    ),
    AUGUST(
        value = UniversalString.august.dateText("08 - "),
        number = "8",
    ),
    SEPTEMBER(
        value = UniversalString.september.dateText("09 - "),
        number = "9",
    ),
    OCTOBER(
        value = UniversalString.october.dateText("10 - "),
        number = "10",
    ),
    NOVEMBER(
        value = UniversalString.november.dateText("11 - "),
        number = "11",
    ),
    DECEMBER(
        value = UniversalString.december.dateText("12 - "),
        number = "12",
    ),
}

private fun @receiver:StringRes Int.dateText(prefix: String): Text =
    prefix
        .asText()
        .concat(asText())
