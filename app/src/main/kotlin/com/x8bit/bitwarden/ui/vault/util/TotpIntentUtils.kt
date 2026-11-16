package com.x8bit.universalpasswordmanager.ui.vault.util

import android.content.Intent
import .ui.platform.model.TotpData
import .ui.platform.util.getTotpDataOrNull

/**
 * Checks if the given [Intent] contains data for a TOTP. The [TotpData] will be returned when the
 * correct data is present or `null` if data is invalid or missing.
 */
fun Intent.getTotpDataOrNull(): TotpData? = this.data?.getTotpDataOrNull()
