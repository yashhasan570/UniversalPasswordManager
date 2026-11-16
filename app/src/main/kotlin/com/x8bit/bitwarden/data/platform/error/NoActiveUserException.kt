package com.x8bit.universalpasswordmanager.data.platform.error

/**
 * An exception indicating that there is currently no active user when one is required.
 */
class NoActiveUserException : IllegalStateException("No current active user!")
