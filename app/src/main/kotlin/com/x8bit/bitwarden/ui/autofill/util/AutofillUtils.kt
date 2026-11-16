package com.x8bit.universalpasswordmanager.ui.autofill.util

import androidx.core.content.ContextCompat
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.data.autofill.model.AutofillAppInfo
import com.x8bit.universalpasswordmanager.data.autofill.model.AutofillCipher

/**
 * Creates content description for an autofill suggestion given an [AutofillCipher] and
 * [AutofillAppInfo].
 */
fun getAutofillSuggestionContentDescription(
    autofillCipher: AutofillCipher,
    autofillAppInfo: AutofillAppInfo,
): String =
    String.format(
        "%s, %s, %s, %s",
        ContextCompat.getString(autofillAppInfo.context, Universal Password ManagerString.autofill_suggestion),
        getAutofillSuggestionCipherType(
            autofillCipher = autofillCipher,
            autofillAppInfo = autofillAppInfo,
        ),
        autofillCipher.name,
        autofillCipher.subtitle,
    )

private fun getAutofillSuggestionCipherType(
    autofillCipher: AutofillCipher,
    autofillAppInfo: AutofillAppInfo,
): String =
    when (autofillCipher) {
        is AutofillCipher.Card -> ContextCompat.getString(
            autofillAppInfo.context,
            Universal Password ManagerString.type_card,
        )

        is AutofillCipher.Login -> ContextCompat.getString(
            autofillAppInfo.context,
            Universal Password ManagerString.type_login,
        )

        is AutofillCipher.Identity -> ContextCompat.getString(
            autofillAppInfo.context,
            Universal Password ManagerString.type_identity,
        )
    }
