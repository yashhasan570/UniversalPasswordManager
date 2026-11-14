@file:OmitFromCoverage

package com.universalpasswordmanager.cxf.ui.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.cxf.importer.CredentialExchangeImporter

/**
 * Provides access to the Credential Exchange importer throughout the app.
 */
val LocalCredentialExchangeImporter: ProvidableCompositionLocal<CredentialExchangeImporter> =
    compositionLocalOf {
        error("CompositionLocal LocalPermissionsManager not present")
    }
