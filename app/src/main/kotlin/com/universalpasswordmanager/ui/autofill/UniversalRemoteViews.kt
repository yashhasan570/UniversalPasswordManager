package com.universalpasswordmanager.ui.autofill

import android.content.Context
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.color.UniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.darkUniversalColorScheme
import com.universalpasswordmanager.ui.platform.theme.color.lightUniversalColorScheme
import com.universalpasswordmanager.R
import com.universalpasswordmanager.data.autofill.model.AutofillAppInfo
import com.universalpasswordmanager.data.autofill.model.AutofillCipher
import com.universalpasswordmanager.ui.autofill.util.getAutofillSuggestionContentDescription
import com.universalpasswordmanager.ui.autofill.util.isSystemDarkMode

/**
 * Build [RemoteViews] for representing an autofill suggestion.
 */
fun buildAutofillRemoteViews(
    autofillAppInfo: AutofillAppInfo,
    autofillCipher: AutofillCipher,
): RemoteViews =
    buildAutofillRemoteViews(
        autofillAppInfo = autofillAppInfo,
        name = autofillCipher.name,
        subtitle = autofillCipher.subtitle,
        iconRes = autofillCipher.iconRes,
        shouldTintIcon = true,
        autofillContentDescription = getAutofillSuggestionContentDescription(
            autofillCipher = autofillCipher,
            autofillAppInfo = autofillAppInfo,
        ),
    )

/**
 * Build [RemoteViews] to represent the Vault Item suggestion (for opening or unlocking the vault).
 */
fun buildVaultItemAutofillRemoteViews(
    autofillAppInfo: AutofillAppInfo,
    isLocked: Boolean,
): RemoteViews =
    buildAutofillRemoteViews(
        autofillAppInfo = autofillAppInfo,
        name = autofillAppInfo.context.getString(R.string.app_name),
        subtitle = autofillAppInfo.context.run {
            if (isLocked) {
                getString(UniversalString.vault_is_locked)
            } else {
                getString(UniversalString.go_to_my_vault)
            }
        },
        iconRes = UniversalDrawable.logo_universalpasswordmanager_icon,
        shouldTintIcon = false,
        autofillContentDescription = null,
    )

@Suppress("LongParameterList")
private fun buildAutofillRemoteViews(
    autofillAppInfo: AutofillAppInfo,
    autofillContentDescription: String?,
    name: String,
    subtitle: String,
    @DrawableRes iconRes: Int,
    shouldTintIcon: Boolean,
): RemoteViews =
    RemoteViews(
        autofillAppInfo.packageName,
        R.layout.autofill_remote_view,
    )
        .apply {
            autofillContentDescription?.let {
                setContentDescription(
                    R.id.container,
                    it,
                )
            }
            setTextViewText(
                R.id.title,
                name,
            )
            setTextViewText(
                R.id.subtitle,
                subtitle,
            )
            setImageViewResource(
                R.id.icon,
                iconRes,
            )
            setInt(
                R.id.container,
                "setBackgroundColor",
                autofillAppInfo.context.background.toArgb(),
            )
            setInt(
                R.id.title,
                "setTextColor",
                autofillAppInfo.context.primaryText.toArgb(),
            )
            setInt(
                R.id.subtitle,
                "setTextColor",
                autofillAppInfo.context.secondaryText.toArgb(),
            )
            if (shouldTintIcon) {
                setInt(
                    R.id.icon,
                    "setColorFilter",
                    autofillAppInfo.context.iconTint.toArgb(),
                )
            }
        }

private val Context.universalpasswordmanagerColorScheme: UniversalColorScheme
    get() = if (isSystemDarkMode) darkUniversalColorScheme else lightUniversalColorScheme

val Context.iconTint: Color get() = universalpasswordmanagerColorScheme.icon.primary
private val Context.primaryText: Color get() = universalpasswordmanagerColorScheme.text.primary
private val Context.secondaryText: Color get() = universalpasswordmanagerColorScheme.text.secondary
private val Context.background: Color get() = universalpasswordmanagerColorScheme.background.primary
