package com.universalpasswordmanager.ui.auth.feature.twofactorlogin.util

import com.universalpasswordmanager.network.model.TwoFactorAuthMethod
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.util.concat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwoFactorAuthMethodExtensionTest {
    @Test
    fun `title returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to UniversalString.authenticator_app_title
                .asText(),
            TwoFactorAuthMethod.EMAIL to UniversalString.email.asText(),
            TwoFactorAuthMethod.DUO to UniversalString.duo_title.asText(),
            TwoFactorAuthMethod.YUBI_KEY to UniversalString.yubi_key_title.asText(),
            TwoFactorAuthMethod.U2F to "".asText(),
            TwoFactorAuthMethod.REMEMBER to "".asText(),
            TwoFactorAuthMethod.DUO_ORGANIZATION to UniversalString.duo_org_title.asText(
                UniversalString.organization.asText(),
            ),
            TwoFactorAuthMethod.WEB_AUTH to UniversalString.fido2_authenticate_web_authn.asText(),
            TwoFactorAuthMethod.RECOVERY_CODE to UniversalString.recovery_code_title.asText(),
        )
            .forEach { (type, title) ->
                assertEquals(
                    title,
                    type.title,
                )
            }
    }

    @Test
    fun `description returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to
                UniversalString.enter_verification_code_app.asText(),
            TwoFactorAuthMethod.EMAIL to
                UniversalString.enter_verification_code_email.asText("ex***@email.com"),
            TwoFactorAuthMethod.DUO to
                UniversalString.follow_the_steps_from_duo_to_finish_logging_in.asText(),
            TwoFactorAuthMethod.YUBI_KEY to UniversalString.yubi_key_instruction.asText(),
            TwoFactorAuthMethod.U2F to null,
            TwoFactorAuthMethod.REMEMBER to null,
            TwoFactorAuthMethod.DUO_ORGANIZATION to
                UniversalString.duo_two_step_login_is_required_for_your_account
                    .asText()
                    .concat(" ".asText())
                    .concat(
                        UniversalString.follow_the_steps_from_duo_to_finish_logging_in.asText(),
                    ),
            TwoFactorAuthMethod.WEB_AUTH to
                UniversalString.continue_to_complete_web_authn_verification.asText(),
            TwoFactorAuthMethod.RECOVERY_CODE to null,
        )
            .forEach { (type, title) ->
                assertEquals(
                    title,
                    type.description("ex***@email.com"),
                )
            }
    }

    @Test
    fun `button returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to UniversalString.continue_text.asText(),
            TwoFactorAuthMethod.EMAIL to UniversalString.continue_text.asText(),
            TwoFactorAuthMethod.DUO to UniversalString.launch_duo.asText(),
            TwoFactorAuthMethod.YUBI_KEY to UniversalString.continue_text.asText(),
            TwoFactorAuthMethod.U2F to UniversalString.continue_text.asText(),
            TwoFactorAuthMethod.REMEMBER to UniversalString.continue_text.asText(),
            TwoFactorAuthMethod.DUO_ORGANIZATION to UniversalString.launch_duo.asText(),
            TwoFactorAuthMethod.WEB_AUTH to UniversalString.launch_web_authn.asText(),
            TwoFactorAuthMethod.RECOVERY_CODE to UniversalString.continue_text.asText(),
        )
            .forEach { (type, buttonLabel) ->
                assertEquals(buttonLabel, type.button)
            }
    }

    @Test
    fun `isContinueButtonEnabled returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to false,
            TwoFactorAuthMethod.EMAIL to false,
            TwoFactorAuthMethod.DUO to true,
            TwoFactorAuthMethod.YUBI_KEY to false,
            TwoFactorAuthMethod.U2F to false,
            TwoFactorAuthMethod.REMEMBER to false,
            TwoFactorAuthMethod.DUO_ORGANIZATION to true,
            TwoFactorAuthMethod.WEB_AUTH to true,
            TwoFactorAuthMethod.RECOVERY_CODE to false,
        )
            .forEach { (type, isContinueButtonEnabled) ->
                assertEquals(isContinueButtonEnabled, type.isContinueButtonEnabled)
            }
    }

    @Test
    fun `showPasswordInput returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to true,
            TwoFactorAuthMethod.EMAIL to true,
            TwoFactorAuthMethod.DUO to false,
            TwoFactorAuthMethod.YUBI_KEY to true,
            TwoFactorAuthMethod.U2F to true,
            TwoFactorAuthMethod.REMEMBER to true,
            TwoFactorAuthMethod.DUO_ORGANIZATION to false,
            TwoFactorAuthMethod.WEB_AUTH to false,
            TwoFactorAuthMethod.RECOVERY_CODE to true,
        )
            .forEach { (type, showPasswordInput) ->
                assertEquals(showPasswordInput, type.showPasswordInput)
            }
    }

    @Test
    fun `shouldUseNfc returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to false,
            TwoFactorAuthMethod.EMAIL to false,
            TwoFactorAuthMethod.DUO to false,
            TwoFactorAuthMethod.YUBI_KEY to true,
            TwoFactorAuthMethod.U2F to false,
            TwoFactorAuthMethod.REMEMBER to false,
            TwoFactorAuthMethod.DUO_ORGANIZATION to false,
            TwoFactorAuthMethod.WEB_AUTH to false,
            TwoFactorAuthMethod.RECOVERY_CODE to false,
        )
            .forEach { (type, shouldUseNfc) ->
                assertEquals(shouldUseNfc, type.shouldUseNfc)
            }
    }

    @Test
    fun `imageRes returns the expected value`() {
        mapOf(
            TwoFactorAuthMethod.AUTHENTICATOR_APP to UniversalDrawable.ill_authenticator,
            TwoFactorAuthMethod.EMAIL to UniversalDrawable.ill_email_code,
            TwoFactorAuthMethod.DUO to null,
            TwoFactorAuthMethod.YUBI_KEY to UniversalDrawable.ill_yubi_key,
            TwoFactorAuthMethod.U2F to null,
            TwoFactorAuthMethod.REMEMBER to null,
            TwoFactorAuthMethod.DUO_ORGANIZATION to null,
            TwoFactorAuthMethod.WEB_AUTH to null,
            TwoFactorAuthMethod.RECOVERY_CODE to null,
        )
            .forEach { (type, imageRes) ->
                assertEquals(imageRes, type.imageRes)
            }
    }
}
