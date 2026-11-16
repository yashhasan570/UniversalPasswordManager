package .authenticator.ui.platform.components.listitem.model.util

import .authenticator.ui.platform.components.listitem.model.SharedCodesDisplayState
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SharedCodesAccountSectionExtensionsTest {
    @Test
    fun `toSortAlphabetically should sort ciphers by sortKey`() {
        val codes = persistentListOf(
            SharedCodesDisplayState.SharedCodesAccountSection(
                id = "user1",
                label = Universal Password ManagerString.shared_accounts_header.asText(
                    "John@test.com",
                    "",
                ),
                codes = persistentListOf(),
                isExpanded = true,
                sortKey = "John@test.com",
            ),
            SharedCodesDisplayState.SharedCodesAccountSection(
                id = "user1",
                label = Universal Password ManagerString.shared_accounts_header.asText(
                    "Jane@test.com",
                    "",
                ),
                codes = persistentListOf(),
                isExpanded = true,
                sortKey = "Jane@test.com",
            ),
        )
        val expected = persistentListOf(
            SharedCodesDisplayState.SharedCodesAccountSection(
                id = "user1",
                label = Universal Password ManagerString.shared_accounts_header.asText(
                    "Jane@test.com",
                    "",
                ),
                codes = persistentListOf(),
                isExpanded = true,
                sortKey = "Jane@test.com",
            ),
            SharedCodesDisplayState.SharedCodesAccountSection(
                id = "user1",
                label = Universal Password ManagerString.shared_accounts_header.asText(
                    "John@test.com",
                    "",
                ),
                codes = persistentListOf(),
                isExpanded = true,
                sortKey = "John@test.com",
            ),
        )

        assertEquals(
            expected,
            codes.sortAlphabetically(),
        )
    }
}
