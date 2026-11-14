package com.universalpasswordmanager.ui.platform.util

/**
 * Extension to be applied to a nullable [Int] type where if the value is null, a default
 * value of "0" is returned.
 */
fun Int?.orZero() = this ?: 0
