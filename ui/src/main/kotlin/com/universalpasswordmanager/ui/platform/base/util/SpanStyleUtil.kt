package com.universalpasswordmanager.ui.platform.base.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Creates a new [SpanStyle] from the specified [color] and [textStyle].
 */
fun spanStyleOf(
    color: Color,
    textStyle: TextStyle,
): SpanStyle =
    SpanStyle(
        color = color,
        fontSize = textStyle.fontSize,
        fontWeight = textStyle.fontWeight,
        fontStyle = textStyle.fontStyle,
        fontSynthesis = textStyle.fontSynthesis,
        fontFamily = textStyle.fontFamily,
        fontFeatureSettings = textStyle.fontFeatureSettings,
        letterSpacing = textStyle.letterSpacing,
        baselineShift = textStyle.baselineShift,
        textGeometricTransform = textStyle.textGeometricTransform,
        localeList = textStyle.localeList,
        background = textStyle.background,
        textDecoration = textStyle.textDecoration,
        shadow = textStyle.shadow,
        platformStyle = textStyle.platformStyle?.spanStyle,
        drawStyle = textStyle.drawStyle,
    )

/**
 * A [SpanStyle] representing "regular" text using the text primary color and label medium font.
 */
val universalpasswordmanagerDefaultSpanStyle: SpanStyle
    @Composable
    @ReadOnlyComposable
    get() = spanStyleOf(
        color = UniversalTheme.colorScheme.text.primary,
        textStyle = UniversalTheme.typography.labelMedium,
    )

/**
 * A [SpanStyle] representing "bold" text using the text primary color and body medium emphasis
 * font.
 */
val universalpasswordmanagerBoldSpanStyle: SpanStyle
    @Composable
    @ReadOnlyComposable
    get() = spanStyleOf(
        color = UniversalTheme.colorScheme.text.primary,
        textStyle = UniversalTheme.typography.bodyMediumEmphasis,
    )

/**
 * A [SpanStyle] representing "clickable" text using the text interaction color and label medium
 * font.
 */
val universalpasswordmanagerClickableTextSpanStyle: SpanStyle
    @Composable
    @ReadOnlyComposable
    get() = spanStyleOf(
        color = UniversalTheme.colorScheme.text.interaction,
        textStyle = UniversalTheme.typography.labelMedium,
    )

/**
 * A [SpanStyle] representing "clickable" text in the focused state using the background alert
 * color and the underlined label medium font.
 */
val universalpasswordmanagerClickableTextFocusSpanStyle: SpanStyle
    @Composable
    @ReadOnlyComposable
    get() = spanStyleOf(
        color = UniversalTheme.colorScheme.background.alert,
        textStyle = UniversalTheme.typography.labelMedium.copy(
            textDecoration = TextDecoration.Underline,
        ),
    )
