@file:OmitFromCoverage

package .authenticator.ui.platform.util

import android.content.Intent
import androidx.core.net.toUri
import .annotation.OmitFromCoverage
import .ui.platform.manager.IntentManager

/**
 * Launches the Universal Password Manager account settings.
 */
fun IntentManager.startUniversal Password ManagerAccountSettings() {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            "settings/account_security".toUri(),
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        },
    )
}
