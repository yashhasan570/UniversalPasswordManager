package com.universalpasswordmanager.data.tiles

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.Keep
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.core.util.isBuildVersionAtLeast
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.AccessibilityActivity
import com.universalpasswordmanager.data.autofill.accessibility.manager.AccessibilityAutofillManager
import com.universalpasswordmanager.data.autofill.accessibility.model.AccessibilityAction
import com.universalpasswordmanager.data.autofill.accessibility.util.isAccessibilityServiceEnabled
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A service for handling the Autofill quick settings tile.
 */
@AndroidEntryPoint
@Keep
@OmitFromCoverage
class UniversalAutofillTileService : TileService() {
    @Inject
    lateinit var accessibilityAutofillManager: AccessibilityAutofillManager

    override fun onClick() {
        if (isLocked) {
            unlockAndRun { launchAutofill() }
        } else {
            launchAutofill()
        }
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun launchAutofill() {
        if (!applicationContext.isAccessibilityServiceEnabled) {
            showDialog(getAccessibilityServiceRequiredDialog())
            return
        }
        accessibilityAutofillManager.accessibilityAction = AccessibilityAction.AttemptParseUri
        val intent = Intent(applicationContext, AccessibilityActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        if (!isBuildVersionAtLeast(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)) {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        } else {
            startActivityAndCollapse(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE,
                ),
            )
        }
    }

    private fun getAccessibilityServiceRequiredDialog(): Dialog =
        AlertDialog.Builder(this)
            .setMessage(UniversalString.autofill_tile_accessibility_required)
            .setCancelable(true)
            .setPositiveButton(UniversalString.okay) { dialog, _ -> dialog.cancel() }
            .create()
}
