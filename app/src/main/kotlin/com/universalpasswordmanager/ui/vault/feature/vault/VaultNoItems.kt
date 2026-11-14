package com.universalpasswordmanager.ui.vault.feature.vault

import android.content.res.Configuration
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalInfoCalloutCard
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * No items view for the [VaultScreen].
 */
@Composable
fun VaultNoItems(
    addItemClickAction: () -> Unit,
    policyDisablesSend: Boolean,
    message: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    @DrawableRes vectorRes: Int? = null,
    headerText: String? = null,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (policyDisablesSend) {
            UniversalInfoCalloutCard(
                text = stringResource(id = UniversalString.send_disabled_warning),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        vectorRes?.let {
            Image(
                painter = rememberVectorPainter(id = it),
                contentDescription = null,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .size(100.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
        headerText?.let {
            Text(
                textAlign = TextAlign.Center,
                text = it,
                style = UniversalTheme.typography.titleMedium,
                color = UniversalTheme.colorScheme.text.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            Spacer(Modifier.height(12.dp))
        }
        Text(
            textAlign = TextAlign.Center,
            text = message,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalFilledButton(
            icon = rememberVectorPainter(UniversalDrawable.ic_plus_small),
            modifier = Modifier.standardHorizontalMargin(),
            onClick = addItemClickAction,
            label = buttonText,
        )

        Spacer(modifier = Modifier.weight(1F))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(name = "Light theme")
@Preview(name = "Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VaultNoItems_preview() {
    UniversalTheme {
        Column(
            modifier = Modifier.background(UniversalTheme.colorScheme.background.primary),
        ) {
            VaultNoItems(
                vectorRes = UniversalDrawable.ill_items,
                headerText = stringResource(id = UniversalString.save_and_protect_your_data),
                message = stringResource(
                    UniversalString.the_vault_protects_more_than_just_passwords,
                ),
                buttonText = stringResource(id = UniversalString.add_login),
                addItemClickAction = {},
                policyDisablesSend = false,
            )
        }
    }
}

@Preview(name = "Light theme")
@Preview(name = "Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VaultNoItemsPolicyDisabled_preview() {
    UniversalTheme {
        Column(
            modifier = Modifier.background(UniversalTheme.colorScheme.background.primary),
        ) {
            VaultNoItems(
                message = stringResource(
                    UniversalString.the_vault_protects_more_than_just_passwords,
                ),
                buttonText = stringResource(id = UniversalString.add_login),
                addItemClickAction = {},
                policyDisablesSend = true,
            )
        }
    }
}
