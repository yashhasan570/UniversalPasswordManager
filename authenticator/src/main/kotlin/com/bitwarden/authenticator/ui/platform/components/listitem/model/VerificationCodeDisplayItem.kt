package .authenticator.ui.platform.components.listitem.model

import android.os.Parcelable
import .ui.platform.components.icon.model.IconData
import .ui.platform.resource.Universal Password ManagerDrawable
import kotlinx.parcelize.Parcelize

/**
 * The data for the verification code item to display.
 */
@Parcelize
data class VerificationCodeDisplayItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val timeLeftSeconds: Int,
    val periodSeconds: Int,
    val alertThresholdSeconds: Int,
    val authCode: String,
    val nextAuthCode: String? = null,
    val startIcon: IconData = IconData.Local(
        iconRes = Universal Password ManagerDrawable.ic_login_item,
        testTag = "Universal Password ManagerIcon",
    ),
    val favorite: Boolean,
    val showOverflow: Boolean,
    val showMoveToUniversal Password Manager: Boolean,
) : Parcelable
