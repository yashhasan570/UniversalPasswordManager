package com.universalpasswordmanager.ui.platform.components.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import com.universalpasswordmanager.ui.platform.base.util.bottomDivider
import com.universalpasswordmanager.ui.platform.base.util.interactiveBorder
import com.universalpasswordmanager.ui.platform.base.util.mirrorIfRtl
import com.universalpasswordmanager.ui.platform.base.util.tabNavigation
import com.universalpasswordmanager.ui.platform.components.appbar.color.bitwardenTopAppBarColors
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.field.color.bitwardenTextFieldColors
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Represents a Universal styled [TopAppBar] that assumes the following components:
 *
 * - an optional single navigation control in the upper-left defined by [navigationIcon].
 * - an editable [TextField] populated by a [searchTerm] in the middle.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun UniversalSearchTopAppBar(
    searchTerm: String,
    placeholder: String,
    onSearchTermChange: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    navigationIcon: NavigationIcon?,
    clearIconContentDescription: String,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets
        .union(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    autoFocus: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    TopAppBar(
        modifier = modifier
            .testTag(tag = "HeaderBarComponent")
            .bottomDivider(),
        windowInsets = windowInsets,
        colors = bitwardenTopAppBarColors(),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            navigationIcon?.let {
                UniversalStandardIconButton(
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
            val interactionSource = remember { MutableInteractionSource() }
            TextField(
                colors = bitwardenTextFieldColors(),
                textStyle = UniversalTheme.typography.bodyLarge,
                placeholder = { Text(text = placeholder) },
                value = searchTerm,
                singleLine = true,
                onValueChange = onSearchTermChange,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchTerm.isNotEmpty(),
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut(),
                    ) {
                        UniversalStandardIconButton(
                            vectorIconRes = UniversalDrawable.ic_clear,
                            contentDescription = clearIconContentDescription,
                            onClick = { onSearchTermChange("") },
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                interactionSource = interactionSource,
                modifier = Modifier
                    .tabNavigation()
                    .interactiveBorder(
                        interactionSource = interactionSource,
                        shape = UniversalTheme.shapes.content,
                    )
                    .testTag("SearchFieldEntry")
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
            )
        },
    )
    if (autoFocus) {
        LaunchedEffect(Unit) { focusRequester.requestFocus() }
    }
}
