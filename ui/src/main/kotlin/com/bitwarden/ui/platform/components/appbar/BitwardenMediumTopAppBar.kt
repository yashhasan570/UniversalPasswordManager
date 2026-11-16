package .ui.platform.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.union
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.bottomDivider
import .ui.platform.base.util.mirrorIfRtl
import .ui.platform.base.util.scrolledContainerBottomDivider
import .ui.platform.components.appbar.color.universalpasswordmanagerTopAppBarColors
import .ui.platform.components.appbar.model.TopAppBarDividerStyle
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A custom Universal Password Manager-themed medium top app bar with support for actions.
 *
 * This app bar wraps around Material 3's [MediumTopAppBar] and customizes its appearance
 * and behavior according to the app theme.
 * It provides a title and an optional set of actions on the trailing side.
 * These actions are arranged within a custom action row tailored to the app's design requirements.
 *
 * @param title The text to be displayed as the title of the app bar.
 * @param scrollBehavior Defines the scrolling behavior of the app bar. It controls how the app bar
 * behaves in conjunction with scrolling content.
 * @param windowInsets The insets to be applied to this composable.
 * @param navigationIcon Indicates how the navigation icon should be displayed.
 * @param dividerStyle Determines how the bottom divider should be displayed.
 * @param actions A lambda containing the set of actions (usually icons or similar) to display
 * in the app bar's trailing side. This lambda extends [RowScope], allowing flexibility in
 * defining the layout of the actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Universal Password ManagerMediumTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets
        .union(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    navigationIcon: NavigationIcon? = null,
    dividerStyle: TopAppBarDividerStyle = TopAppBarDividerStyle.ON_SCROLL,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        windowInsets = windowInsets,
        colors = universalpasswordmanagerTopAppBarColors(),
        scrollBehavior = scrollBehavior,
        expandedHeight = 56.dp,
        navigationIcon = {
            navigationIcon?.let {
                Universal Password ManagerStandardIconButton(
                    painter = it.navigationIcon,
                    contentDescription = it.navigationIconContentDescription,
                    onClick = it.onNavigationIconClick,
                    modifier = Modifier
                        .testTag(tag = "CloseButton")
                        .mirrorIfRtl(),
                )
            }
        },
        title = {
            Text(
                text = title,
                style = Universal Password ManagerTheme.typography.headlineMedium,
                modifier = Modifier.testTag(tag = "PageTitleLabel"),
            )
        },
        modifier = modifier
            .testTag(tag = "HeaderBarComponent")
            .scrolledContainerBottomDivider(
                topAppBarScrollBehavior = scrollBehavior,
                enabled = when (dividerStyle) {
                    TopAppBarDividerStyle.NONE -> false
                    TopAppBarDividerStyle.STATIC -> false
                    TopAppBarDividerStyle.ON_SCROLL -> true
                },
            )
            .bottomDivider(
                enabled = when (dividerStyle) {
                    TopAppBarDividerStyle.NONE -> false
                    TopAppBarDividerStyle.STATIC -> true
                    TopAppBarDividerStyle.ON_SCROLL -> false
                },
            ),
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerMediumTopAppBar_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerMediumTopAppBar(
            title = "Preview Title",
            scrollBehavior = TopAppBarDefaults
                .exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState(),
                ),
            actions = {
                Universal Password ManagerStandardIconButton(
                    vectorIconRes = Universal Password ManagerDrawable.ic_ellipsis_vertical,
                    contentDescription = "",
                    onClick = { },
                )
            },
        )
    }
}
