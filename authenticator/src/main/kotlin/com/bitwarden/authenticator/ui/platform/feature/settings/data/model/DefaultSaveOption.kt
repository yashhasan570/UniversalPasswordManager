package .authenticator.ui.platform.feature.settings.data.model

/**
 * Represents the default save location the user has set.
 *
 * The [value] is used for consistent storage purposes.
 */
enum class DefaultSaveOption(val value: String?) {
    UNIVERSAL PASSWORD MANAGER_APP(value = "universalpasswordmanager"),
    LOCAL(value = "local"),
    NONE(value = null),
}
