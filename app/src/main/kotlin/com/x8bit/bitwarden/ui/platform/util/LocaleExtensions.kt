package com.x8bit.universalpasswordmanager.ui.platform.util

import .ui.platform.feature.settings.appearance.model.AppLanguage
import java.util.Locale

/**
 * If returns an associated [AppLanguage] with the [Locale]. If there is
 * none that are mapped to the locale's language then the value is null.
 */
val Locale.appLanguage: AppLanguage?
    get() = AppLanguage
        .entries
        .find { it.localeName?.lowercase(this) == this.toLanguageTag().lowercase(this) }
