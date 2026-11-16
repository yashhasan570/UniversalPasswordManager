package .authenticator.data.platform.manager.imports.model

/**
 * Represents the file formats a user can select to import their vault.
 */
enum class ImportFileFormat(
    val mimeType: String,
) {
    UNIVERSAL PASSWORD MANAGER_JSON("application/json"),
    TWO_FAS_JSON("*/*"),
    LAST_PASS_JSON("application/json"),
    AEGIS("application/json"),
}
