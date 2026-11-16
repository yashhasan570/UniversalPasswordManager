package com.x8bit.universalpasswordmanager.ui.auth.feature.completeregistration

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Creates a mock [CompleteRegistrationDialog.HaveIBeenPwned].
 */
fun createHaveIBeenPwned(
    title: Text = Universal Password ManagerString.weak_and_exposed_master_password.asText(),
    message: Text = Universal Password ManagerString
        .weak_password_identified_and_found_in_a_data_breach_alert_description
        .asText(),
): CompleteRegistrationDialog.HaveIBeenPwned =
    CompleteRegistrationDialog.HaveIBeenPwned(
        title = title,
        message = message,
    )
