@file:OmitFromCoverage

package .cxf.ui.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import .annotation.OmitFromCoverage
import .cxf.validator.CredentialExchangeRequestValidator

/**
 * Provides access to the Credential Exchange request validator throughout the app.
 */
@Suppress("MaxLineLength")
val LocalCredentialExchangeRequestValidator: ProvidableCompositionLocal<CredentialExchangeRequestValidator> =
    compositionLocalOf {
        error("CompositionLocal LocalPermissionsManager not present")
    }
