package .authenticator.ui.authenticator.feature.util

import .authenticator.data.authenticator.manager.model.VerificationCodeItem
import .authenticator.data.authenticator.repository.model.AuthenticatorItem
import .authenticator.data.authenticator.repository.model.SharedVerificationCodesState
import .authenticator.ui.platform.components.listitem.model.SharedCodesDisplayState
import .authenticator.ui.platform.components.listitem.model.VerificationCodeDisplayItem
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SharedVerificationCodesStateTest {

    @Test
    fun `toSharedCodesDisplayState on empty list should return empty list`() {
        val state = SharedVerificationCodesState.Success(emptyList())
        val expected = SharedCodesDisplayState.Codes(persistentListOf())
        assertEquals(
            expected,
            state.toSharedCodesDisplayState(
                alertThresholdSeconds = ALERT_THRESHOLD,
                isShowNextCodeEnabled = false,
            ),
        )
    }

    @Test
    fun `toSharedCodesDisplayState should return list of sections grouped by account`() {
        val state = SharedVerificationCodesState.Success(
            items = listOf(
                VerificationCodeItem(
                    code = "123456",
                    nextCode = null,
                    periodSeconds = 30,
                    timeLeftSeconds = 10,
                    issueTime = 100L,
                    id = "123",
                    issuer = null,
                    label = null,
                    source = AuthenticatorItem.Source.Shared(
                        userId = "user1",
                        nameOfUser = "John Appleseed",
                        email = "John@test.com",
                        environmentLabel = "",
                    ),
                ),
                VerificationCodeItem(
                    code = "987654",
                    nextCode = null,
                    periodSeconds = 30,
                    timeLeftSeconds = 10,
                    issueTime = 100L,
                    id = "987",
                    issuer = "issuer",
                    label = "accountName",
                    source = AuthenticatorItem.Source.Shared(
                        userId = "user1",
                        nameOfUser = "Jane Doe",
                        email = "Jane@test.com",
                        environmentLabel = "",
                    ),
                ),
            ),
        )
        val expected = SharedCodesDisplayState.Codes(
            sections = persistentListOf(
                SharedCodesDisplayState.SharedCodesAccountSection(
                    id = "user1",
                    label = Universal Password ManagerString.shared_accounts_header.asText(
                        "Jane@test.com",
                        "",
                    ),
                    codes = persistentListOf(
                        VerificationCodeDisplayItem(
                            authCode = "987654",
                            periodSeconds = 30,
                            timeLeftSeconds = 10,
                            id = "987",
                            title = "issuer",
                            subtitle = "accountName",
                            favorite = false,
                            showOverflow = false,
                            alertThresholdSeconds = ALERT_THRESHOLD,
                            showMoveToUniversal Password Manager = false,
                        ),
                    ),
                    isExpanded = true,
                    sortKey = "Jane@test.com",
                ),
                SharedCodesDisplayState.SharedCodesAccountSection(
                    id = "user1",
                    label = Universal Password ManagerString.shared_accounts_header.asText(
                        "John@test.com",
                        "",
                    ),
                    codes = persistentListOf(
                        VerificationCodeDisplayItem(
                            authCode = "123456",
                            periodSeconds = 30,
                            timeLeftSeconds = 10,
                            id = "123",
                            title = "--",
                            subtitle = null,
                            favorite = false,
                            showOverflow = false,
                            alertThresholdSeconds = ALERT_THRESHOLD,
                            showMoveToUniversal Password Manager = false,
                        ),
                    ),
                    isExpanded = true,
                    sortKey = "John@test.com",
                ),
            ),
        )
        assertEquals(
            expected,
            state.toSharedCodesDisplayState(
                alertThresholdSeconds = ALERT_THRESHOLD,
                isShowNextCodeEnabled = false,
            ),
        )
    }

    @Test
    fun `toSharedCodesDisplayState should return list of sections maintaining expanded state`() {
        val state = SharedVerificationCodesState.Success(
            items = listOf(
                VerificationCodeItem(
                    code = "123456",
                    nextCode = null,
                    periodSeconds = 30,
                    timeLeftSeconds = 10,
                    issueTime = 100L,
                    id = "123",
                    issuer = null,
                    label = null,
                    source = AuthenticatorItem.Source.Shared(
                        userId = "user1",
                        nameOfUser = "John Appleseed",
                        email = "John@test.com",
                        environmentLabel = "",
                    ),
                ),
                VerificationCodeItem(
                    code = "987654",
                    nextCode = null,
                    periodSeconds = 30,
                    timeLeftSeconds = 10,
                    issueTime = 100L,
                    id = "987",
                    issuer = "issuer",
                    label = "accountName",
                    source = AuthenticatorItem.Source.Shared(
                        userId = "user1",
                        nameOfUser = "Jane Doe",
                        email = "Jane@test.com",
                        environmentLabel = "",
                    ),
                ),
            ),
        )
        val expected = SharedCodesDisplayState.Codes(
            sections = persistentListOf(
                SharedCodesDisplayState.SharedCodesAccountSection(
                    id = "user1",
                    label = Universal Password ManagerString.shared_accounts_header.asText(
                        "Jane@test.com",
                        "",
                    ),
                    codes = persistentListOf(
                        VerificationCodeDisplayItem(
                            authCode = "987654",
                            periodSeconds = 30,
                            timeLeftSeconds = 10,
                            id = "987",
                            title = "issuer",
                            subtitle = "accountName",
                            favorite = false,
                            showOverflow = false,
                            alertThresholdSeconds = ALERT_THRESHOLD,
                            showMoveToUniversal Password Manager = false,
                        ),
                    ),
                    isExpanded = false,
                    sortKey = "Jane@test.com",
                ),
                SharedCodesDisplayState.SharedCodesAccountSection(
                    id = "user1",
                    label = Universal Password ManagerString.shared_accounts_header.asText(
                        "John@test.com",
                        "",
                    ),
                    codes = persistentListOf(
                        VerificationCodeDisplayItem(
                            authCode = "123456",
                            periodSeconds = 30,
                            timeLeftSeconds = 10,
                            id = "123",
                            title = "--",
                            subtitle = null,
                            favorite = false,
                            showOverflow = false,
                            alertThresholdSeconds = ALERT_THRESHOLD,
                            showMoveToUniversal Password Manager = false,
                        ),
                    ),
                    isExpanded = false,
                    sortKey = "John@test.com",
                ),
            ),
        )
        assertEquals(
            expected,
            state.toSharedCodesDisplayState(
                alertThresholdSeconds = ALERT_THRESHOLD,
                currentSections = expected.sections.map {
                    it.copy(label = "junk to show that it does update the other values".asText())
                },
                isShowNextCodeEnabled = false,
            ),
        )
    }
}

private const val ALERT_THRESHOLD = 7
