package com.universalpasswordmanager.ui.tools.feature.send

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toAnnotatedString
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalInfoCalloutCard
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.tools.feature.send.model.UpgradedToPremiumCardData

/**
 * Content for the empty state of the [SendScreen].
 */
@Suppress("LongMethod")
@Composable
fun SendEmpty(
    policyDisablesSend: Boolean,
    onAddItemClick: () -> Unit,
    upgradedToPremiumCardData: UpgradedToPremiumCardData?,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        upgradedToPremiumCardData?.let {
            Spacer(modifier = Modifier.height(height = 12.dp))
            UpgradedToPremiumActionCard(
                onActionClick = it.onCardClick,
                onDismissClick = it.onCardDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }
        if (policyDisablesSend) {
            Spacer(modifier = Modifier.height(12.dp))
            UniversalInfoCalloutCard(
                text = stringResource(id = UniversalString.send_disabled_warning),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1F))
        Image(
            painter = rememberVectorPainter(UniversalDrawable.ill_send),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(100.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(UniversalString.send_sensitive_information_safely),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(
                UniversalString.share_files_and_data_securely_with_anyone_on_any_platform,
            ),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .testTag("EmptySendListText"),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // This button is hidden from accessibility to avoid duplicate voice over with the FAB
        val newSendLabel = stringResource(id = UniversalString.add_a_send)
        UniversalFilledButton(
            onClick = onAddItemClick,
            label = stringResource(id = UniversalString.add_a_send),
            icon = rememberVectorPainter(UniversalDrawable.ic_plus_small),
            modifier = Modifier
                .clearAndSetSemantics {
                    text = newSendLabel.toAnnotatedString()
                    hideFromAccessibility()
                }
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.weight(1F))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SendEmpty_preview() {
    UniversalTheme {
        Column(
            modifier = Modifier.background(UniversalTheme.colorScheme.background.primary),
        ) {
            SendEmpty(
                policyDisablesSend = false,
                onAddItemClick = {},
                upgradedToPremiumCardData = null,
            )
        }
    }
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SendEmptyPolicyDisabled_preview() {
    UniversalTheme {
        Column(
            modifier = Modifier.background(UniversalTheme.colorScheme.background.primary),
        ) {
            SendEmpty(
                policyDisablesSend = true,
                onAddItemClick = {},
                upgradedToPremiumCardData = null,
            )
        }
    }
}
