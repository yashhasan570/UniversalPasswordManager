package com.universalpasswordmanager.data.tiles

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.Keep
import androidx.core.net.toUri
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.core.util.isBuildVersionAtLeast
import com.universalpasswordmanager.MainActivity

/**
 * A service for handling the My Vault quick settings tile.
 */
@Keep
@OmitFromCoverage
class UniversalVaultTileService : TileService() {

    override fun onClick() {
        if (isLocked) {
            unlockAndRun { launchVault() }
        } else {
            launchVault()
        }
    }

    private fun launchVault() {
        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setData("my_vault".toUri())
        if (!isBuildVersionAtLeast(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)) {
            @Suppress("DEPRECATION")
            @SuppressLint("StartActivityAndCollapseDeprecated")
            startActivityAndCollapse(intent)
        } else {
            startActivityAndCollapse(
                PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE,
                ),
            )
        }
    }
}
