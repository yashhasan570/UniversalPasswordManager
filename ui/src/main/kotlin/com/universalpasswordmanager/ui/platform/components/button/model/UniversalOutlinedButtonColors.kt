package com.universalpasswordmanager.ui.platform.components.button.model

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton

/**
 * Colors for a [UniversalOutlinedButton].
 */
@Immutable
data class UniversalOutlinedButtonColors(
    val materialButtonColors: ButtonColors,
    val outlineBorderColor: Color,
    val outlinedDisabledBorderColor: Color,
)
