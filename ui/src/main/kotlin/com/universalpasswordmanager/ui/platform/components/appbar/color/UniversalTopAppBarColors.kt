package com.universalpasswordmanager.ui.platform.components.appbar.color

import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Provides a default set of Universal-styled colors for top app bars.
 */
@Composable
fun bitwardenTopAppBarColors(): TopAppBarColors = TopAppBarColors(
    containerColor = UniversalTheme.colorScheme.background.secondary,
    scrolledContainerColor = UniversalTheme.colorScheme.background.secondary,
    navigationIconContentColor = UniversalTheme.colorScheme.icon.primary,
    titleContentColor = UniversalTheme.colorScheme.text.primary,
    actionIconContentColor = UniversalTheme.colorScheme.icon.primary,
    subtitleContentColor = UniversalTheme.colorScheme.text.primary,
)
