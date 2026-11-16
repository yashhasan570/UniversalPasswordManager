package .authenticator.ui.authenticator.feature.util

import .authenticator.data.authenticator.manager.util.createMockLocalAuthenticatorItemSource
import .authenticator.data.authenticator.manager.util.createMockSharedAuthenticatorItemSource
import .authenticator.data.authenticator.manager.util.createMockVerificationCodeItem
import .authenticator.data.authenticator.repository.model.AuthenticatorItem
import .authenticator.data.authenticator.repository.model.SharedVerificationCodesState
import .authenticator.ui.platform.components.listitem.model.VerificationCodeDisplayItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VerificationCodeItemExtensionsTest {

    @Test
    fun `toDisplayItem should map Local items correctly`() {
        val alertThresholdSeconds = 7
        val favoriteItem = createMockVerificationCodeItem(
            number = 1,
            source = createMockLocalAuthenticatorItemSource(isFavorite = true),
        )
        val nonFavoriteItem = createMockVerificationCodeItem(number = 2)

        val expectedFavoriteItem = VerificationCodeDisplayItem(
            id = favoriteItem.id,
            title = favoriteItem.issuer!!,
            subtitle = favoriteItem.label,
            timeLeftSeconds = favoriteItem.timeLeftSeconds,
            periodSeconds = favoriteItem.periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
            authCode = favoriteItem.code,
            nextAuthCode = null,
            favorite = (favoriteItem.source as AuthenticatorItem.Source.Local).isFavorite,
            showOverflow = true,
            showMoveToUniversal Password Manager = false,
        )

        val expectedNonFavoriteItem = VerificationCodeDisplayItem(
            id = nonFavoriteItem.id,
            title = nonFavoriteItem.issuer!!,
            subtitle = nonFavoriteItem.label,
            timeLeftSeconds = nonFavoriteItem.timeLeftSeconds,
            periodSeconds = nonFavoriteItem.periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
            authCode = nonFavoriteItem.code,
            nextAuthCode = null,
            favorite = (nonFavoriteItem.source as AuthenticatorItem.Source.Local).isFavorite,
            showOverflow = true,
            showMoveToUniversal Password Manager = false,
        )

        assertEquals(
            expectedFavoriteItem,
            favoriteItem.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = true,
            ),
        )
        assertEquals(
            expectedNonFavoriteItem,
            nonFavoriteItem.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = true,
            ),
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toDisplayItem should only showMoveToUniversal Password Manager when SharedVerificationCodesState is Success`() {
        val alertThresholdSeconds = 7
        val item = createMockVerificationCodeItem(1)
        val expectedDontShowMoveToUniversal Password ManagerItem =
            VerificationCodeDisplayItem(
                id = item.id,
                title = item.issuer!!,
                subtitle = item.label,
                timeLeftSeconds = item.timeLeftSeconds,
                periodSeconds = item.periodSeconds,
                alertThresholdSeconds = alertThresholdSeconds,
                authCode = item.code,
                nextAuthCode = null,
                favorite = false,
                showOverflow = true,
                showMoveToUniversal Password Manager = false,
            )

        assertEquals(
            expectedDontShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.AppNotInstalled,
                showOverflow = true,
            ),
        )
        assertEquals(
            expectedDontShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = true,
            ),
        )
        assertEquals(
            expectedDontShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.FeatureNotEnabled,
                showOverflow = true,
            ),
        )
        assertEquals(
            expectedDontShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Loading,
                showOverflow = true,
            ),
        )
        assertEquals(
            expectedDontShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.OsVersionNotSupported,
                showOverflow = true,
            ),
        )
        assertEquals(
            expectedDontShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.SyncNotEnabled,
                showOverflow = true,
            ),
        )

        val expectedShouldShowMoveToUniversal Password ManagerItem = expectedDontShowMoveToUniversal Password ManagerItem.copy(
            showMoveToUniversal Password Manager = true,
        )
        assertEquals(
            expectedShouldShowMoveToUniversal Password ManagerItem,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Success(emptyList()),
                showOverflow = true,
            ),
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toDisplayItem should populate nextAuthCode when setting enabled and time below threshold`() {
        val alertThresholdSeconds = 7
        val item = createMockVerificationCodeItem(
            number = 1,
            timeLeftSeconds = 5,
        )
        val expected = VerificationCodeDisplayItem(
            id = item.id,
            title = item.issuer!!,
            subtitle = item.label,
            timeLeftSeconds = item.timeLeftSeconds,
            periodSeconds = item.periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
            authCode = item.code,
            nextAuthCode = item.nextCode,
            favorite = false,
            showOverflow = true,
            showMoveToUniversal Password Manager = false,
        )
        assertEquals(
            expected,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = true,
            ),
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toDisplayItem should return null nextAuthCode when setting disabled even below threshold`() {
        val alertThresholdSeconds = 7
        val item = createMockVerificationCodeItem(
            number = 1,
            timeLeftSeconds = 5,
        )
        val expected = VerificationCodeDisplayItem(
            id = item.id,
            title = item.issuer!!,
            subtitle = item.label,
            timeLeftSeconds = item.timeLeftSeconds,
            periodSeconds = item.periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
            authCode = item.code,
            nextAuthCode = null,
            favorite = false,
            showOverflow = true,
            showMoveToUniversal Password Manager = false,
        )
        assertEquals(
            expected,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = false,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = true,
            ),
        )
    }

    @Test
    @Suppress("MaxLineLength")
    fun `toDisplayItem should return null nextAuthCode when time above threshold even with setting enabled`() {
        val alertThresholdSeconds = 7
        val item = createMockVerificationCodeItem(
            number = 1,
            timeLeftSeconds = 15,
        )
        val expected = VerificationCodeDisplayItem(
            id = item.id,
            title = item.issuer!!,
            subtitle = item.label,
            timeLeftSeconds = item.timeLeftSeconds,
            periodSeconds = item.periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
            authCode = item.code,
            nextAuthCode = null,
            favorite = false,
            showOverflow = true,
            showMoveToUniversal Password Manager = false,
        )
        assertEquals(
            expected,
            item.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = true,
            ),
        )
    }

    @Test
    fun `toDisplayItem should map Shared items correctly`() {
        val alertThresholdSeconds = 7
        val favoriteItem = createMockVerificationCodeItem(
            number = 1,
            source = createMockSharedAuthenticatorItemSource(
                number = 1,
                userId = "1",
                nameOfUser = "John Doe",
                email = "test@",
                environmentLabel = "",
            ),
        )

        val expectedFavoriteItem = VerificationCodeDisplayItem(
            id = favoriteItem.id,
            title = favoriteItem.issuer!!,
            subtitle = favoriteItem.label,
            timeLeftSeconds = favoriteItem.timeLeftSeconds,
            periodSeconds = favoriteItem.periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
            authCode = favoriteItem.code,
            nextAuthCode = null,
            favorite = false,
            showOverflow = false,
            showMoveToUniversal Password Manager = false,
        )

        assertEquals(
            expectedFavoriteItem,
            favoriteItem.toDisplayItem(
                alertThresholdSeconds = alertThresholdSeconds,
                isShowNextCodeEnabled = true,
                sharedVerificationCodesState = SharedVerificationCodesState.Error,
                showOverflow = false,
            ),
        )
    }
}
