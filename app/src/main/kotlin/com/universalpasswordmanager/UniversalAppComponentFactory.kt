package com.universalpasswordmanager

import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.annotation.Keep
import androidx.core.app.AppComponentFactory
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.data.autofill.UniversalAutofillService
import com.universalpasswordmanager.data.autofill.accessibility.UniversalAccessibilityService
import com.universalpasswordmanager.data.credentials.UniversalCredentialProviderService
import com.universalpasswordmanager.data.tiles.UniversalAutofillTileService
import com.universalpasswordmanager.data.tiles.UniversalGeneratorTileService
import com.universalpasswordmanager.data.tiles.UniversalVaultTileService

/**
 * A factory class that allows us to intercept when a manifest element is being instantiated
 * and modify various characteristics before initialization.
 */
@Suppress("unused")
@Keep
@OmitFromCoverage
class UniversalAppComponentFactory : AppComponentFactory() {
    /**
     * Used to intercept when certain legacy services are being instantiated and modify which
     * service is created. This is required because the [className] used in the manifest must match
     * the legacy Xamarin app service name but the service name in this app is different.
     *
     * Services currently being managed:
     * * [UniversalAccessibilityService]
     * * [UniversalAutofillService]
     * * [UniversalAutofillTileService]
     * * [UniversalCredentialProviderService]
     * * [UniversalVaultTileService]
     * * [UniversalGeneratorTileService]
     */
    override fun instantiateServiceCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?,
    ): Service = when (className) {
        LEGACY_ACCESSIBILITY_SERVICE_NAME -> {
            super.instantiateServiceCompat(
                cl,
                UniversalAccessibilityService::class.java.name,
                intent,
            )
        }

        LEGACY_AUTOFILL_SERVICE_NAME -> {
            super.instantiateServiceCompat(cl, UniversalAutofillService::class.java.name, intent)
        }

        LEGACY_AUTOFILL_TILE_SERVICE_NAME -> {
            super.instantiateServiceCompat(
                cl,
                UniversalAutofillTileService::class.java.name,
                intent,
            )
        }

        LEGACY_CREDENTIAL_SERVICE_NAME -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                super.instantiateServiceCompat(
                    cl,
                    UniversalCredentialProviderService::class.java.name,
                    intent,
                )
            } else {
                throw UnsupportedOperationException(
                    "The CredentialProviderService requires API 34 or higher.",
                )
            }
        }

        LEGACY_VAULT_TILE_SERVICE_NAME -> {
            super.instantiateServiceCompat(cl, UniversalVaultTileService::class.java.name, intent)
        }

        LEGACY_GENERATOR_TILE_SERVICE_NAME -> {
            super.instantiateServiceCompat(
                cl,
                UniversalGeneratorTileService::class.java.name,
                intent,
            )
        }

        else -> super.instantiateServiceCompat(cl, className, intent)
    }
}
