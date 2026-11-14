package com.universalpasswordmanager.data.auth.util

import android.content.Context
import android.content.Intent
import com.universalpasswordmanager.ui.platform.util.getSafeParcelableExtra
import com.universalpasswordmanager.MainActivity
import com.universalpasswordmanager.data.platform.manager.model.PasswordlessRequestData

private const val NOTIFICATION_DATA: String = "notificationData"

/**
 * Creates an [Intent] that can be used to navigate the pending auth approval screen.
 */
fun createPasswordlessRequestDataIntent(
    context: Context,
    data: PasswordlessRequestData,
): Intent =
    Intent(context, MainActivity::class.java)
        .putExtra(NOTIFICATION_DATA, data)

/**
 * Checks if the given [Intent] contains data for passwordless authorization.
 * The [PasswordlessRequestData] will be returned when present.
 */
fun Intent.getPasswordlessRequestDataIntentOrNull(): PasswordlessRequestData? =
    this.getSafeParcelableExtra(NOTIFICATION_DATA)
