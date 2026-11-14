package com.universalpasswordmanager.data.auth.manager

import com.universalpasswordmanager.ui.platform.model.TotpData

/**
 * Default in memory implementation for [AddTotpItemFromAuthenticatorManager].
 */
class AddTotpItemFromAuthenticatorManagerImpl : AddTotpItemFromAuthenticatorManager {

    override var pendingAddTotpLoginItemData: TotpData? = null
}
