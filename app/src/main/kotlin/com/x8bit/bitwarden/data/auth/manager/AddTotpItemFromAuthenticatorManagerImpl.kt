package com.x8bit.universalpasswordmanager.data.auth.manager

import .ui.platform.model.TotpData

/**
 * Default in memory implementation for [AddTotpItemFromAuthenticatorManager].
 */
class AddTotpItemFromAuthenticatorManagerImpl : AddTotpItemFromAuthenticatorManager {

    override var pendingAddTotpLoginItemData: TotpData? = null
}
