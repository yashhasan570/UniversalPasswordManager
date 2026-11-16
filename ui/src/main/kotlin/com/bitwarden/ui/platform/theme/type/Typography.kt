package .ui.platform.theme.type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import .ui.R
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * The app's primary font family, backed by the Inter variable font. Each weight used by the app
 * is declared so that the matching [FontWeight] renders the real weight from the font's weight
 * axis instead of a synthesized one.
 */
val universalpasswordmanagerFontFamily: FontFamily = FontFamily(
    interVariableFont(weight = FontWeight.Normal),
    interVariableFont(weight = FontWeight.Medium),
    interVariableFont(weight = FontWeight.SemiBold),
    interVariableFont(weight = FontWeight.Bold),
)

/**
 * The default [Universal Password ManagerTypography] for the app.
 */
val universalpasswordmanagerTypography: Universal Password ManagerTypography = Universal Password ManagerTypography(
    displayLarge = TextStyle(
        fontSize = 56.sp,
        lineHeight = 64.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    displayMedium = TextStyle(
        fontSize = 44.sp,
        lineHeight = 52.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    displaySmall = TextStyle(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    headlineSmall = TextStyle(
        fontSize = 18.sp,
        lineHeight = 22.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    titleExtraLarge = TextStyle(
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    titleLarge = TextStyle(
        fontSize = 19.sp,
        lineHeight = 28.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    bodyLargeEmphasis = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    bodyMedium = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    bodyMediumEmphasis = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    labelLargeEmphasis = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    labelLargeRegular = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    sensitiveInfoSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular_mono)),
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    sensitiveInfoMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular_mono)),
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    eyebrowMedium = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontFamily = universalpasswordmanagerFontFamily,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.6.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
)

/**
 * Derives a Material [Typography] from the [Universal Password ManagerTypography].
 */
fun Universal Password ManagerTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerTypography_preview() {
    Universal Password ManagerTheme {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = "Display large", style = universalpasswordmanagerTypography.displayLarge)
            Text(text = "Display medium", style = universalpasswordmanagerTypography.displayMedium)
            Text(text = "Display small", style = universalpasswordmanagerTypography.displaySmall)
            Text(text = "Headline large", style = universalpasswordmanagerTypography.headlineLarge)
            Text(text = "Headline medium", style = universalpasswordmanagerTypography.headlineMedium)
            Text(text = "Headline small", style = universalpasswordmanagerTypography.headlineSmall)
            Text(text = "Title extra large", style = universalpasswordmanagerTypography.titleExtraLarge)
            Text(text = "Title large", style = universalpasswordmanagerTypography.titleLarge)
            Text(text = "Title medium", style = universalpasswordmanagerTypography.titleMedium)
            Text(text = "Title small", style = universalpasswordmanagerTypography.titleSmall)
            Text(text = "Body large", style = universalpasswordmanagerTypography.bodyLarge)
            Text(text = "Body large emphasis", style = universalpasswordmanagerTypography.bodyLargeEmphasis)
            Text(text = "Body medium", style = universalpasswordmanagerTypography.bodyMedium)
            Text(text = "Body medium emphasis", style = universalpasswordmanagerTypography.bodyMediumEmphasis)
            Text(text = "Body small", style = universalpasswordmanagerTypography.bodySmall)
            Text(text = "Label large", style = universalpasswordmanagerTypography.labelLarge)
            Text(text = "Label large emphasis", style = universalpasswordmanagerTypography.labelLargeEmphasis)
            Text(text = "Label large regular", style = universalpasswordmanagerTypography.labelLargeRegular)
            Text(text = "Label medium", style = universalpasswordmanagerTypography.labelMedium)
            Text(text = "Label small", style = universalpasswordmanagerTypography.labelSmall)
            Text(text = "Sensitive info small", style = universalpasswordmanagerTypography.sensitiveInfoSmall)
            Text(text = "Sensitive info medium", style = universalpasswordmanagerTypography.sensitiveInfoMedium)
            Text(text = "Eyebrow medium", style = universalpasswordmanagerTypography.eyebrowMedium)
        }
    }
}

private fun interVariableFont(weight: FontWeight): Font =
    Font(
        resId = R.font.inter_variable,
        weight = weight,
        variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
    )
