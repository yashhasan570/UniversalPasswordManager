package com.universalpasswordmanager.ui.auth.feature.twofactorlogin.util

import androidx.annotation.DrawableRes
import com.universalpasswordmanager.network.model.TwoFactorAuthMethod
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.util.concat

/**
 * Get the title for the given auth method.
 */
val TwoFactorAuthMethod.title: Text
    get() = when (this) {
        TwoFactorAuthMethod.AUTHENTICATOR_APP -> UniversalString.authenticator_app_title.asText()
        TwoFactorAuthMethod.DUO -> UniversalString.duo_title.asText()
        TwoFactorAuthMethod.DUO_ORGANIZATION -> UniversalString.duo_org_title.asText(
            UniversalString.organization.asText(),
        )

        TwoFactorAuthMethod.EMAIL -> UniversalString.email.asText()
        TwoFactorAuthMethod.RECOVERY_CODE -> UniversalString.recovery_code_title.asText()
        TwoFactorAuthMethod.WEB_AUTH -> UniversalString.fido2_authenticate_web_authn.asText()
        TwoFactorAuthMethod.YUBI_KEY -> UniversalString.yubi_key_title.asText()
        else -> "".asText()
    }

/**
 * Get the description for the given auth method.
 */
fun TwoFactorAuthMethod.description(email: String): Text? = when (this) {
    TwoFactorAuthMethod.AUTHENTICATOR_APP -> UniversalString.enter_verification_code_app.asText()
    TwoFactorAuthMethod.DUO -> {
        UniversalString.follow_the_steps_from_duo_to_finish_logging_in.asText()
    }

    TwoFactorAuthMethod.DUO_ORGANIZATION -> {
        UniversalString.duo_two_step_login_is_required_for_your_account
            .asText()
            .concat(" ".asText())
            .concat(UniversalString.follow_the_steps_from_duo_to_finish_logging_in.asText())
    }

    TwoFactorAuthMethod.EMAIL -> UniversalString.enter_verification_code_email.asText(email)
    TwoFactorAuthMethod.WEB_AUTH -> {
        UniversalString.continue_to_complete_web_authn_verification.asText()
    }

    TwoFactorAuthMethod.YUBI_KEY -> UniversalString.yubi_key_instruction.asText()
    TwoFactorAuthMethod.U2F,
    TwoFactorAuthMethod.REMEMBER,
    TwoFactorAuthMethod.RECOVERY_CODE,
        -> null
}

/**
 * Get the button label for the given auth method.
 */
val TwoFactorAuthMethod.button: Text
    get() = when (this) {
        TwoFactorAuthMethod.DUO,
        TwoFactorAuthMethod.DUO_ORGANIZATION,
            -> UniversalString.launch_duo.asText()

        TwoFactorAuthMethod.AUTHENTICATOR_APP,
        TwoFactorAuthMethod.EMAIL,
        TwoFactorAuthMethod.YUBI_KEY,
        TwoFactorAuthMethod.U2F,
        TwoFactorAuthMethod.REMEMBER,
        TwoFactorAuthMethod.RECOVERY_CODE,
            -> UniversalString.continue_text.asText()

        TwoFactorAuthMethod.WEB_AUTH -> UniversalString.launch_web_authn.asText()
    }

/**
 * Gets a boolean indicating if the given auth method has the continue button enabled by default.
 */
val TwoFactorAuthMethod.isContinueButtonEnabled: Boolean
    get() = when (this) {
        TwoFactorAuthMethod.DUO,
        TwoFactorAuthMethod.DUO_ORGANIZATION,
        TwoFactorAuthMethod.WEB_AUTH,
            -> true

        TwoFactorAuthMethod.AUTHENTICATOR_APP,
        TwoFactorAuthMethod.EMAIL,
        TwoFactorAuthMethod.YUBI_KEY,
        TwoFactorAuthMethod.U2F,
        TwoFactorAuthMethod.REMEMBER,
        TwoFactorAuthMethod.RECOVERY_CODE,
            -> false
    }

/**
 * Gets a boolean indicating if the given auth method should display the password input field.
 */
val TwoFactorAuthMethod.showPasswordInput: Boolean
    get() = when (this) {
        TwoFactorAuthMethod.DUO,
        TwoFactorAuthMethod.DUO_ORGANIZATION,
        TwoFactorAuthMethod.WEB_AUTH,
            -> false

        TwoFactorAuthMethod.AUTHENTICATOR_APP,
        TwoFactorAuthMethod.EMAIL,
        TwoFactorAuthMethod.YUBI_KEY,
        TwoFactorAuthMethod.U2F,
        TwoFactorAuthMethod.REMEMBER,
        TwoFactorAuthMethod.RECOVERY_CODE,
            -> true
    }

/**
 * Gets a boolean indicating if the given auth method uses NFC.
 */
val TwoFactorAuthMethod.shouldUseNfc: Boolean
    get() = when (this) {
        TwoFactorAuthMethod.YUBI_KEY -> true
        else -> false
    }

/**
 * Gets a drawable resource for the image to be displayed or `null` if nothing should be displayed.
 */
@get:DrawableRes
val TwoFactorAuthMethod.imageRes: Int?
    get() = when (this) {
        TwoFactorAuthMethod.YUBI_KEY -> UniversalDrawable.ill_yubi_key
        TwoFactorAuthMethod.EMAIL -> UniversalDrawable.ill_email_code
        TwoFactorAuthMethod.AUTHENTICATOR_APP -> UniversalDrawable.ill_authenticator
        else -> null
    }
