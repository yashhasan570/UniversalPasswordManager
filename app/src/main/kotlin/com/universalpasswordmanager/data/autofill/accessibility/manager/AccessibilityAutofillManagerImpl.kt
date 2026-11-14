package com.universalpasswordmanager.data.autofill.accessibility.manager

import com.universalpasswordmanager.data.autofill.accessibility.model.AccessibilityAction

/**
 * The default implementation for the [AccessibilityAutofillManager].
 */
class AccessibilityAutofillManagerImpl : AccessibilityAutofillManager {
    override var accessibilityAction: AccessibilityAction? = null
}
