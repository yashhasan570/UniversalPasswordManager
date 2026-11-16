package com.x8bit.universalpasswordmanager.ui.vault.model

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Defines all available title options for identities.
 */
enum class VaultIdentityTitle(val value: Text) {
    SELECT(value = Universal Password ManagerString.select.asText()),
    MR(value = Universal Password ManagerString.mr.asText()),
    MRS(value = Universal Password ManagerString.mrs.asText()),
    MS(value = Universal Password ManagerString.ms.asText()),
    MX(value = Universal Password ManagerString.mx.asText()),
    DR(value = Universal Password ManagerString.dr.asText()),
}
