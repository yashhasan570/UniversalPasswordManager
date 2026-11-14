package com.universalpasswordmanager.ui.vault.model

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Defines all available title options for identities.
 */
enum class VaultIdentityTitle(val value: Text) {
    SELECT(value = UniversalString.select.asText()),
    MR(value = UniversalString.mr.asText()),
    MRS(value = UniversalString.mrs.asText()),
    MS(value = UniversalString.ms.asText()),
    MX(value = UniversalString.mx.asText()),
    DR(value = UniversalString.dr.asText()),
}
