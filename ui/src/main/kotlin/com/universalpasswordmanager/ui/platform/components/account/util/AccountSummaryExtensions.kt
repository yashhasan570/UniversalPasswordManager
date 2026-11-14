package com.universalpasswordmanager.ui.platform.components.account.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.universalpasswordmanager.ui.platform.components.account.model.AccountSummary
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import java.util.Locale

/**
 * Given the [AccountSummary], returns the first two "initials" found when looking at the
 * [AccountSummary.name].
 *
 * Ex:
 * - "First Last" -> "FL"
 * - "First Second Last" -> "FS"
 * - "First" -> "FI"
 * - name is `null`, email is "test@" -> "TE"
 */
val AccountSummary.initials: String
    get() {
        val names = this.name.orEmpty().split(" ").filter { it.isNotBlank() }
        return if (names.size >= 2) {
            names
                .take(2)
                .joinToString(separator = "") { it.first().toString() }
        } else {
            (this.name ?: this.email).take(2)
        }
            .uppercase(Locale.getDefault())
    }

/**
 * Drawable resource to display for the given [AccountSummary].
 */
@get:DrawableRes
val AccountSummary.iconRes: Int
    get() = when (this.status) {
        AccountSummary.Status.ACTIVE -> UniversalDrawable.ic_check_mark
        AccountSummary.Status.LOCKED -> UniversalDrawable.ic_locked
        AccountSummary.Status.LOGGED_OUT -> UniversalDrawable.ic_locked
        AccountSummary.Status.UNLOCKED -> UniversalDrawable.ic_unlocked
    }

/**
 * Test tag to be used for the for the given [AccountSummary.iconRes].
 */
val AccountSummary.iconTestTag: String
    get() = when (this.status) {
        AccountSummary.Status.ACTIVE -> "ActiveVaultIcon"
        AccountSummary.Status.LOCKED,
        AccountSummary.Status.LOGGED_OUT,
        AccountSummary.Status.UNLOCKED,
            -> "InactiveVaultIcon"
    }

/**
 * String resource of a supporting text to display (or `null`) for the given [AccountSummary].
 */
@get:StringRes
val AccountSummary.supportingTextResOrNull: Int?
    get() = when (this.status) {
        AccountSummary.Status.ACTIVE -> null
        AccountSummary.Status.LOCKED -> UniversalString.account_locked
        AccountSummary.Status.LOGGED_OUT -> UniversalString.account_logged_out
        AccountSummary.Status.UNLOCKED -> UniversalString.account_unlocked
    }
