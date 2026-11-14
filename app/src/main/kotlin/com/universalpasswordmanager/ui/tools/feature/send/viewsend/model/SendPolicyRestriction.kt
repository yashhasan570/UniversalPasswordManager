package com.universalpasswordmanager.ui.tools.feature.send.viewsend.model

import android.os.Parcelable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import kotlinx.parcelize.Parcelize

/**
 * The reason a Send is restricted by its organization's SendControls policy, which determines the
 * warning shown on the view send screen and whether a compliant copy can be made.
 */
sealed class SendPolicyRestriction : Parcelable {
    /**
     * Whether a compliant copy of the Send can be made to replace it.
     */
    abstract val isCopyable: Boolean

    /**
     * The explanation shown to the user for this restriction.
     */
    abstract val message: Text

    /**
     * The Send is a file send, so it cannot be brought into compliance: its attachment is not
     * available to upload again.
     */
    @Parcelize
    data object FileNotCompliant : SendPolicyRestriction() {
        override val isCopyable: Boolean get() = false

        override val message: Text
            get() = UniversalString
                .this_send_is_not_compliant_with_your_organizations_send_policy
                .asText()
    }

    /**
     * The Send's type is no longer allowed by the policy, so a copy would be blocked by the same
     * restriction.
     */
    @Parcelize
    data object TypeNotAllowed : SendPolicyRestriction() {
        override val isCopyable: Boolean get() = false

        override val message: Text
            get() = UniversalString.text_sends_are_not_allowed_for_your_organization.asText()
    }

    /**
     * The Send can be replaced by a copy that satisfies the policy.
     */
    @Parcelize
    data object CopyRequired : SendPolicyRestriction() {
        override val isCopyable: Boolean get() = true

        override val message: Text
            get() = UniversalString.to_edit_this_send_make_a_copy.asText()
    }
}
