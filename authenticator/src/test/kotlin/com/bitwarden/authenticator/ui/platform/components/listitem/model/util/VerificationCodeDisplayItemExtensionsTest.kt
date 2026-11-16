package .authenticator.ui.platform.components.listitem.model.util

import .authenticator.ui.platform.components.listitem.model.VerificationCodeDisplayItem
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VerificationCodeDisplayItemExtensionsTest {
    @Test
    fun `toSortAlphabetically should sort ciphers by title`() {
        val codes = persistentListOf(
            VerificationCodeDisplayItem(
                authCode = "123456",
                periodSeconds = 30,
                timeLeftSeconds = 10,
                id = "123",
                title = "Universal Password Manager",
                subtitle = null,
                favorite = false,
                showOverflow = false,
                alertThresholdSeconds = 7,
                showMoveToUniversal Password Manager = false,
            ),
            VerificationCodeDisplayItem(
                authCode = "123456",
                periodSeconds = 30,
                timeLeftSeconds = 10,
                id = "7643",
                title = "--",
                subtitle = null,
                favorite = false,
                showOverflow = false,
                alertThresholdSeconds = 7,
                showMoveToUniversal Password Manager = false,
            ),
            VerificationCodeDisplayItem(
                authCode = "123456",
                periodSeconds = 30,
                timeLeftSeconds = 10,
                id = "84345",
                title = "universalpasswordmanager",
                subtitle = null,
                favorite = false,
                showOverflow = false,
                alertThresholdSeconds = 7,
                showMoveToUniversal Password Manager = false,
            ),
        )
        val expected = persistentListOf(
            VerificationCodeDisplayItem(
                authCode = "123456",
                periodSeconds = 30,
                timeLeftSeconds = 10,
                id = "7643",
                title = "--",
                subtitle = null,
                favorite = false,
                showOverflow = false,
                alertThresholdSeconds = 7,
                showMoveToUniversal Password Manager = false,
            ),
            VerificationCodeDisplayItem(
                authCode = "123456",
                periodSeconds = 30,
                timeLeftSeconds = 10,
                id = "84345",
                title = "universalpasswordmanager",
                subtitle = null,
                favorite = false,
                showOverflow = false,
                alertThresholdSeconds = 7,
                showMoveToUniversal Password Manager = false,
            ),
            VerificationCodeDisplayItem(
                authCode = "123456",
                periodSeconds = 30,
                timeLeftSeconds = 10,
                id = "123",
                title = "Universal Password Manager",
                subtitle = null,
                favorite = false,
                showOverflow = false,
                alertThresholdSeconds = 7,
                showMoveToUniversal Password Manager = false,
            ),
        )

        assertEquals(
            expected,
            codes.sortAlphabetically(),
        )
    }
}
