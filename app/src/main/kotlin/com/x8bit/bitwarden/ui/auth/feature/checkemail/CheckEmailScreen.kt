package com.x8bit.universalpasswordmanager.ui.auth.feature.checkemail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.util.startDefaultEmailApplication
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.auth.feature.checkemail.handlers.rememberCheckEmailHandler

/**
 * Top level composable for the check email screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun CheckEmailScreen(
    onNavigateBack: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: CheckEmailViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberCheckEmailHandler(viewModel = viewModel)
    EventsEffect(viewModel) { event ->
        when (event) {
            is CheckEmailEvent.NavigateBack -> {
                onNavigateBack()
            }

            is CheckEmailEvent.NavigateToEmailApp -> {
                intentManager.startDefaultEmailApplication()
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.create_account),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = handler.onBackClick,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            CheckEmailContent(
                email = state.email,
                onOpenEmailAppClick = handler.onOpenEmailAppClick,
                onChangeEmailClick = handler.onChangeEmailClick,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun CheckEmailContent(
    email: String,
    onOpenEmailAppClick: () -> Unit,
    onChangeEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_email),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(100.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.check_your_email),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = annotatedStringResource(
                id = Universal Password ManagerString.we_sent_an_email_to,
                args = arrayOf(email),
                emphasisHighlightStyle = SpanStyle(
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    fontSize = Universal Password ManagerTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                ),
            ),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        @Suppress("MaxLineLength")
        Text(
            text = stringResource(
                Universal Password ManagerString.select_the_link_in_the_email_to_verify_your_email_address_and_continue_creating_your_account,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.open_email_app),
            onClick = onOpenEmailAppClick,
            isExternalLink = true,
            modifier = Modifier
                .testTag("OpenEmailApp")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerOutlinedButton(
            label = stringResource(Universal Password ManagerString.change_email_address),
            onClick = onChangeEmailClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckEmailScreenNewUi_preview() {
    Universal Password ManagerTheme {
        CheckEmailContent(
            email = "email@fake.com",
            onOpenEmailAppClick = { },
            onChangeEmailClick = { },
        )
    }
}
