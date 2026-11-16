package com.x8bit.universalpasswordmanager.ui.vault.feature.importlogins.components

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
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toAnnotatedString
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.card.Universal Password ManagerContentCard
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
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
    ctaText: String = stringResource(Universal Password ManagerString.continue_text),
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = stepText,
            style = Universal Password ManagerTheme.typography.titleSmall,
        )
        Spacer(Modifier.height(12.dp))
        Text(text = stepTitle, style = Universal Password ManagerTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))
        Universal Password ManagerContentCard(
            contentItems = instructions,
            modifier = Modifier
                .standardHorizontalMargin(),
            contentHeaderTextStyle = Universal Password ManagerTheme.typography.bodyMedium,
            contentSubtitleTextStyle = Universal Password ManagerTheme.typography.labelSmall,
        )
        Spacer(Modifier.height(24.dp))
        Universal Password ManagerHyperTextLink(
            annotatedResId = Universal Password ManagerString.need_help_check_out_import_help,
            annotationKey = "importHelp",
            accessibilityString = stringResource(id = Universal Password ManagerString.import_help),
            onClick = onHelpClick,
            style = Universal Password ManagerTheme.typography.bodySmall,
            isExternalLink = true,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(Modifier.height(24.dp))
        Universal Password ManagerFilledButton(
            label = ctaText,
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.height(12.dp))
        Universal Password ManagerOutlinedButton(
            label = stringResource(Universal Password ManagerString.back),
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
    Universal Password ManagerTheme {
        Column(modifier = Modifier.background(Universal Password ManagerTheme.colorScheme.background.primary)) {
            ImportLoginsInstructionStep(
                stepText = "Step text",
                stepTitle = "Step title",
                instructions = persistentListOf(
                    ContentBlockData(
                        iconVectorResource = Universal Password ManagerDrawable.ic_number1,
                        headerText = buildAnnotatedString {
                            append("Step text 1")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Universal Password ManagerTheme.typography.bodyMedium.fontFamily,
                                ),
                            ) {
                                append(" with bold text")
                            }
                        },
                        subtitleText = null,
                    ),
                    ContentBlockData(
                        iconVectorResource = Universal Password ManagerDrawable.ic_number2,
                        headerText = buildAnnotatedString {
                            append("Step text 2")
                        },
                        subtitleText = "Added deets".toAnnotatedString(),
                    ),
                    ContentBlockData(
                        iconVectorResource = Universal Password ManagerDrawable.ic_number3,
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
