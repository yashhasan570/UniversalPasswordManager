package com.x8bit.universalpasswordmanager.data.autofill.accessibility.model

import android.net.Uri
import .vault.CipherView

/**
 *Represents an action to be taken by the accessibility service.
 */
sealed class AccessibilityAction {
    /**
     * Indicates that the accessibility service should attempt to scan the currently foregrounded
     * application for a [Uri].
     */
    data object AttemptParseUri : AccessibilityAction()

    /**
     * Indicates that the accessibility service should attempt to scan the currently foregrounded
     * application for a fields to fill.
     */
    data class AttemptFill(
        val cipherView: CipherView,
        val uri: Uri,
    ) : AccessibilityAction()
}
