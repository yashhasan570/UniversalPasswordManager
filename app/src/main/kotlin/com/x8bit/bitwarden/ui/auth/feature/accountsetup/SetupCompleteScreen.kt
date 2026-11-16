package com.x8bit.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Top level composable for the setup complete screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupCompleteScreen(
    viewModel: SetupCompleteViewModel = hiltViewModel(),
) {
    val setupCompleteAction: () -> Unit = {
        viewModel.trySendAction(SetupCompleteAction.CompleteSetup)
    }

    // Handle system back action to complete the setup.
    BackHandler(onBack = setupCompleteAction)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Universal Password ManagerScaffold(
        topBar = {
            Universal Password ManagerTopAppBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(Universal Password ManagerString.account_setup),
                navigationIcon = null,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        SetupCompleteContent(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onContinue = setupCompleteAction,
        )
    }
}

@Composable
private fun SetupCompleteContent(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(Modifier.height(32.dp))
        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_autofill),
            contentDescription = null,
            modifier = Modifier
                .align(CenterHorizontally)
                .standardHorizontalMargin()
                .size(size = 100.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Universal Password ManagerString.youre_all_set),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(CenterHorizontally)
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Universal Password ManagerString.what_universalpasswordmanager_has_to_offer),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(CenterHorizontally)
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.height(24.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(Universal Password ManagerString.continue_text),
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SetupCompleteContent_preview() {
    Universal Password ManagerTheme {
        Surface {
            SetupCompleteContent(
                modifier = Modifier.fillMaxSize(),
                onContinue = {},
            )
        }
    }
}
