package com.universalpasswordmanager.data.autofill.accessibility.manager

import com.universalpasswordmanager.data.autofill.accessibility.model.AccessibilityAction

/**
 * A relay manager used to notify the accessibility service to attempt an autofill.
 */
interface AccessibilityAutofillManager {
    /**
     * Indicates that the Autofill tile has been clicked and we attempt an accessibility-based
     * autofill.
     */
    var accessibilityAction: AccessibilityAction?
}
