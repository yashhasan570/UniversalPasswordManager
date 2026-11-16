package com.x8bit.universalpasswordmanager.data.platform.manager.model

/**
 * Represents a Universal Password Manager push notification.
 *
 * @property contextId The context ID. This is mainly used to check if the push notification
 * originated from this app.
 * @property notificationType The type of notification.
 * @property payload Data associated with the push notification.
 */
data class Universal Password ManagerNotification(
    val contextId: String?,
    val notificationType: NotificationType,
    val payload: String,
)
