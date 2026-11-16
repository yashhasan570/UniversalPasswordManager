package .ui.platform.components.appbar.action

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.interactiveBorder
import .ui.platform.base.util.nullableTestTag
import .ui.platform.components.appbar.color.universalpasswordmanagerMenuItemColors
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents a composable overflow item specifically tailored for Universal Password Manager's UI.
 *
 * This composable wraps an [IconButton] with an "overflow" icon, typically used to
 * indicate more actions available that are not immediately visible on the interface.
 * The item is centrally aligned within a predefined [Box] of size 24.dp.
 *
 * @param menuItemDataList The list of [OverflowMenuItemData] that will populate the overflow
 * dropdown menu.
 * @param modifier The [Modifier] for this composable.
 * @param isVisible Indicates if this composable should be shown.
 * @param contentDescription The content description for the icon button.
 * @param vectorIconRes The resource indicating what icon to display.
 * @param testTag The test tag applied to this composable.
 */
@Composable
fun Universal Password ManagerOverflowActionItem(
    menuItemDataList: ImmutableList<OverflowMenuItemData>,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    contentDescription: String = stringResource(id = Universal Password ManagerString.more_options),
    @DrawableRes vectorIconRes: Int = Universal Password ManagerDrawable.ic_ellipsis_vertical,
    testTag: String? = "HeaderBarOptionsButton",
) {
    if (menuItemDataList.isEmpty() || !isVisible) return
    var isOverflowMenuVisible by rememberSaveable { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Universal Password ManagerStandardIconButton(
            vectorIconRes = vectorIconRes,
            contentDescription = contentDescription,
            onClick = { isOverflowMenuVisible = !isOverflowMenuVisible },
            modifier = Modifier.nullableTestTag(tag = testTag),
        )
        DropdownMenu(
            shape = Universal Password ManagerTheme.shapes.menu,
            expanded = isOverflowMenuVisible,
            onDismissRequest = { isOverflowMenuVisible = false },
            offset = DpOffset(x = (-12).dp, y = 0.dp),
            containerColor = Universal Password ManagerTheme.colorScheme.background.primary,
            modifier = Modifier
                .semantics { testTagsAsResourceId = true }
                .testTag("FloatingOptionsContent")
                .widthIn(
                    min = 112.dp,
                    max = 280.dp,
                ),
            content = {
                menuItemDataList.forEach { dropdownMenuItemData ->
                    Universal Password ManagerDropdownMenuItem(
                        data = dropdownMenuItemData,
                        onHideOverflowMenu = { isOverflowMenuVisible = false },
                    )
                }
            },
        )
    }
}

@Composable
private fun Universal Password ManagerDropdownMenuItem(
    data: OverflowMenuItemData,
    onHideOverflowMenu: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val contentDescriptionText = if (data.isExternalLink) {
        stringResource(id = Universal Password ManagerString.external_link_format, formatArgs = arrayOf(data.text))
    } else {
        data.text
    }
    DropdownMenuItem(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescriptionText
                this.role = Role.Button
            }
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = Universal Password ManagerTheme.shapes.row,
                color = data
                    .color
                    .takeUnless { it.isUnspecified }
                    ?: Universal Password ManagerTheme.colorScheme.stroke.border,
            )
            .testTag(tag = data.testTag),
        interactionSource = interactionSource,
        colors = universalpasswordmanagerMenuItemColors(
            textColor = data
                .color
                .takeUnless { it.isUnspecified }
                ?: Universal Password ManagerTheme.colorScheme.text.primary,
        ),
        enabled = data.isEnabled,
        text = {
            Text(
                text = data.text,
                style = Universal Password ManagerTheme.typography.bodyLarge,
                modifier = Modifier.semantics { hideFromAccessibility() },
            )
        },
        onClick = {
            onHideOverflowMenu()
            data.onClick()
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerOverflowActionItem_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerOverflowActionItem(
            menuItemDataList = persistentListOf(
                OverflowMenuItemData(
                    text = "Test",
                    onClick = {},
                ),
            ),
        )
    }
}
