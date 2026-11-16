package com.x8bit.universalpasswordmanager.data.auth.manager

import .ui.platform.model.TotpData

/**
 * Manager for keeping track of requests from the Universal Password Manager Authenticator app to add a TOTP
 * item.
 */
interface AddTotpItemFromAuthenticatorManager {

    /**
     * Current pending [TotpData] to be added from the Authenticator app.
     */
    var pendingAddTotpLoginItemData: TotpData?
}
