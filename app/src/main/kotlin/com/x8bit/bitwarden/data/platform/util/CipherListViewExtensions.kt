package com.x8bit.universalpasswordmanager.data.platform.util

import .vault.CipherListView

/**
 * Indicates if this [CipherListView] is active based on its deleted or archived status.
 */
val CipherListView.isActive: Boolean
    get() = this.archivedDate == null && this.deletedDate == null
