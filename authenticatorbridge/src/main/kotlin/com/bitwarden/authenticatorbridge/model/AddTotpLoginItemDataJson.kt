package .authenticatorbridge.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable model for a TOTP item to be added to the Universal Password Manager app. For domain level
 * model, see [AddTotpLoginItemData].
 *
 * @param totpUri A TOTP code URI to be added to the Universal Password Manager app.
 */
@Serializable
internal data class AddTotpLoginItemDataJson(
    @SerialName("totpUri")
    val totpUri: String,
)
