@file:OmitFromCoverage

package com.universalpasswordmanager.cxf.ui.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.cxf.manager.CredentialExchangeCompletionManager

/**
 * Provides access to the Credential Exchange completion manager throughout the app.
 */
@Suppress("MaxLineLength")
val LocalCredentialExchangeCompletionManager: ProvidableCompositionLocal<CredentialExchangeCompletionManager> =
    compositionLocalOf {
        error("CompositionLocal LocalCredentialExchangeCompletionManager not present")
    }
