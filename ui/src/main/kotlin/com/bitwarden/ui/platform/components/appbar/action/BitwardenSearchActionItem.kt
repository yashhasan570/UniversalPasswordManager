package .ui.platform.components.appbar.action

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.resource.Universal Password ManagerDrawable

/**
 * Represents the Universal Password Manager search action item.
 *
 * This is an [Icon] composable tailored specifically for the search functionality
 * in the Universal Password Manager app.
 * It presents the search icon and offers an `onClick` callback for when the icon is tapped.
 *
 * @param contentDescription A description of the UI element, used for accessibility purposes.
 * @param isDisplayed Whether this action item should be displayed.
 * @param onClick A callback to be invoked when this action item is clicked.
 */
@Composable
fun Universal Password ManagerSearchActionItem(
    contentDescription: String,
    isDisplayed: Boolean = true,
    onClick: () -> Unit,
) {
    if (!isDisplayed) return
    Universal Password ManagerStandardIconButton(
        vectorIconRes = Universal Password ManagerDrawable.ic_search,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = Modifier.testTag(tag = "SearchButton"),
    )
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerSearchActionItem_preview() {
    Universal Password ManagerSearchActionItem(
        contentDescription = "Search",
        onClick = {},
    )
}
