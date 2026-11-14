package com.universalpasswordmanager.ui.platform.components.appbar.action

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable

/**
 * Represents the Universal search action item.
 *
 * This is an [Icon] composable tailored specifically for the search functionality
 * in the Universal app.
 * It presents the search icon and offers an `onClick` callback for when the icon is tapped.
 *
 * @param contentDescription A description of the UI element, used for accessibility purposes.
 * @param isDisplayed Whether this action item should be displayed.
 * @param onClick A callback to be invoked when this action item is clicked.
 */
@Composable
fun UniversalSearchActionItem(
    contentDescription: String,
    isDisplayed: Boolean = true,
    onClick: () -> Unit,
) {
    if (!isDisplayed) return
    UniversalStandardIconButton(
        vectorIconRes = UniversalDrawable.ic_search,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = Modifier.testTag(tag = "SearchButton"),
    )
}

@Preview(showBackground = true)
@Composable
private fun UniversalSearchActionItem_preview() {
    UniversalSearchActionItem(
        contentDescription = "Search",
        onClick = {},
    )
}
