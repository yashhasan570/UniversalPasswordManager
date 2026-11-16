package com.x8bit.universalpasswordmanager.data.platform.util

import .data.datasource.disk.model.EnvironmentUrlDataJson
import .data.repository.model.Environment
import .ui.platform.manager.intent.model.AuthTabData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EnvironmentExtensionsTest {

    @Test
    fun `duoAuthTabData should return the correct AuthTabData for all environments`() {
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "duo-callback",
            ),
            Environment.Prod.Us.duoAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "duo-callback",
            ),
            Environment.Prod.Eu.duoAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "duo-callback",
            ),
            Environment.Prod.FedRamp.duoAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "duo-callback",
            ),
            Environment.SelfHosted(DEFAULT_INTERNAL_ENVIRONMENT_URL_DATA).duoAuthTabData,
        )
        assertEquals(
            AuthTabData.CustomScheme(
                callbackUrl = "duo-callback",
                callbackScheme = "universalpasswordmanager",
            ),
            Environment.SelfHosted(DEFAULT_CUSTOM_ENVIRONMENT_URL_DATA).duoAuthTabData,
        )
    }

    @Test
    fun `webAuthnAuthTabData should return the correct AuthTabData for all environments`() {
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "webauthn-callback",
            ),
            Environment.Prod.Us.webAuthnAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "webauthn-callback",
            ),
            Environment.Prod.Eu.webAuthnAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "webauthn-callback",
            ),
            Environment.Prod.FedRamp.webAuthnAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "webauthn-callback",
            ),
            Environment.SelfHosted(DEFAULT_INTERNAL_ENVIRONMENT_URL_DATA).webAuthnAuthTabData,
        )
        assertEquals(
            AuthTabData.CustomScheme(
                callbackUrl = "webauthn-callback",
                callbackScheme = "universalpasswordmanager",
            ),
            Environment.SelfHosted(DEFAULT_CUSTOM_ENVIRONMENT_URL_DATA).webAuthnAuthTabData,
        )
    }

    @Test
    fun `ssoAuthTabData should return the correct AuthTabData for all environments`() {
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "sso-callback",
            ),
            Environment.Prod.Us.ssoAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "sso-callback",
            ),
            Environment.Prod.Eu.ssoAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "sso-callback",
            ),
            Environment.Prod.FedRamp.ssoAuthTabData,
        )
        assertEquals(
            AuthTabData.HttpsScheme(
                host = "",
                path = "sso-callback",
            ),
            Environment.SelfHosted(DEFAULT_INTERNAL_ENVIRONMENT_URL_DATA).ssoAuthTabData,
        )
        assertEquals(
            AuthTabData.CustomScheme(
                callbackUrl = "sso-callback",
                callbackScheme = "universalpasswordmanager",
            ),
            Environment.SelfHosted(DEFAULT_CUSTOM_ENVIRONMENT_URL_DATA).ssoAuthTabData,
        )
    }

    @Test
    fun `appLinksScheme should return the correct scheme for US environment`() {
        val expectedScheme = "https"

        assertEquals(
            expectedScheme,
            Environment.Prod.Us.appLinksScheme,
        )
    }

    @Test
    fun `appLinksScheme should return the correct scheme for EU environment`() {
        val expectedScheme = "https"

        assertEquals(
            expectedScheme,
            Environment.Prod.Eu.appLinksScheme,
        )
    }

    @Test
    fun `appLinksScheme should return the correct scheme for FedRAMP environment`() {
        val expectedScheme = "https"

        assertEquals(
            expectedScheme,
            Environment.Prod.FedRamp.appLinksScheme,
        )
    }

    @Test
    fun `appLinksScheme should return the correct scheme for internal environment`() {
        val expectedScheme = "https"

        assertEquals(
            expectedScheme,
            Environment.SelfHosted(DEFAULT_INTERNAL_ENVIRONMENT_URL_DATA).appLinksScheme,
        )
    }

    @Test
    fun `appLinksScheme should return the correct scheme for custom environment`() {
        val expectedScheme = "universalpasswordmanager"

        assertEquals(
            expectedScheme,
            Environment.SelfHosted(DEFAULT_CUSTOM_ENVIRONMENT_URL_DATA).appLinksScheme,
        )
    }
}

private val DEFAULT_CUSTOM_ENVIRONMENT_URL_DATA = EnvironmentUrlDataJson(
    base = "base",
    api = "api",
    identity = "identity",
    icon = "icon",
    notifications = "notifications",
    webVault = "webVault",
    events = "events",
)

private val DEFAULT_INTERNAL_ENVIRONMENT_URL_DATA = DEFAULT_CUSTOM_ENVIRONMENT_URL_DATA.copy(
    base = "qa.vault.",
)
