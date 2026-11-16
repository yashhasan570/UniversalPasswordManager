package .authenticatorbridge.model

/**
 * Domain level model for a TOTP item to be added to the Universal Password Manager app.
 *
 * @param totpUri A TOTP code URI to be added to the Universal Password Manager app.
 */
data class AddTotpLoginItemData(
    val totpUri: String,
)
