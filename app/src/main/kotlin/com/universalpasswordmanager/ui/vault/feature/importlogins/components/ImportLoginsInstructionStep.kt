package com.universalpasswordmanager.ui.vault.feature.importlogins.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toAnnotatedString
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalContentCard
import com.universalpasswordmanager.ui.platform.components.content.model.ContentBlockData
import com.universalpasswordmanager.ui.platform.components.text.UniversalHyperTextLink
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Reusable component for each step of the import logins flow.
 */
@Suppress("LongMethod")
@Composable
fun ImportLoginsInstructionStep(
    stepText: String,
    stepTitle: String,
    instructions: ImmutableList<ContentBlockData>,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
    ctaText: String = stringResource(UniversalString.continue_text),
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = stepText,
            style = UniversalTheme.typography.titleSmall,
        )
        Spacer(Modifier.height(12.dp))
        Text(text = stepTitle, style = UniversalTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))
        UniversalContentCard(
            contentItems = instructions,
            modifier = Modifier
                .standardHorizontalMargin(),
            contentHeaderTextStyle = UniversalTheme.typography.bodyMedium,
            contentSubtitleTextStyle = UniversalTheme.typography.labelSmall,
        )
        Spacer(Modifier.height(24.dp))
        UniversalHyperTextLink(
            annotatedResId = UniversalString.need_help_check_out_import_help,
            annotationKey = "importHelp",
            accessibilityString = stringResource(id = UniversalString.import_help),
            onClick = onHelpClick,
            style = UniversalTheme.typography.bodySmall,
            isExternalLink = true,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(Modifier.height(24.dp))
        UniversalFilledButton(
            label = ctaText,
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.height(12.dp))
        UniversalOutlinedButton(
            label = stringResource(UniversalString.back),
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.navigationBarsPadding())
    }
}

@Preview
@Composable
private fun ImportLoginsInstructionStep_preview() {
    UniversalTheme {
        Column(modifier = Modifier.background(UniversalTheme.colorScheme.background.primary)) {
            ImportLoginsInstructionStep(
                stepText = "Step text",
                stepTitle = "Step title",
                instructions = persistentListOf(
                    ContentBlockData(
                        iconVectorResource = UniversalDrawable.ic_number1,
                        headerText = buildAnnotatedString {
                            append("Step text 1")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = UniversalTheme.typography.bodyMedium.fontFamily,
                                ),
                            ) {
                                append(" with bold text")
                            }
                        },
                        subtitleText = null,
                    ),
                    ContentBlockData(
                        iconVectorResource = UniversalDrawable.ic_number2,
                        headerText = buildAnnotatedString {
                            append("Step text 2")
                        },
                        subtitleText = "Added deets".toAnnotatedString(),
                    ),
                    ContentBlockData(
                        iconVectorResource = UniversalDrawable.ic_number3,
                        headerText = buildAnnotatedString {
                            append("Step text 3")
                        },
                    ),
                ),
                onBackClick = {},
                onContinueClick = {},
                onHelpClick = {},
            )
        }
    }
}
