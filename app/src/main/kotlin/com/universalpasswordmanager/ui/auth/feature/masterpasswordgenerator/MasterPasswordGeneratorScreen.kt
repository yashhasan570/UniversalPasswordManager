package com.universalpasswordmanager.ui.auth.feature.masterpasswordgenerator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.components.util.nonLetterColorVisualTransformation
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Top level composable for the master password generator.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun MasterPasswordGeneratorScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPreventLockout: () -> Unit,
    onNavigateBackWithPassword: () -> Unit,
    viewModel: MasterPasswordGeneratorViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = rememberUniversalSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            MasterPasswordGeneratorEvent.NavigateBack -> onNavigateBack()
            MasterPasswordGeneratorEvent.NavigateToPreventLockout -> onNavigateToPreventLockout()
            is MasterPasswordGeneratorEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    snackbarData = UniversalSnackbarData(message = event.text),
                    duration = SnackbarDuration.Short,
                )
            }

            is MasterPasswordGeneratorEvent.NavigateBackToRegistration -> {
                onNavigateBackWithPassword()
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MasterPasswordGeneratorTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    viewModel.trySendAction(MasterPasswordGeneratorAction.BackClickAction)
                },
                onSaveClick = {
                    viewModel.trySendAction(MasterPasswordGeneratorAction.SavePasswordClickAction)
                },
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            MasterPasswordGeneratorContent(
                generatedPassword = state.generatedPassword,
                onGenerateNewPassword = {
                    viewModel.trySendAction(
                        MasterPasswordGeneratorAction.GeneratePasswordClickAction,
                    )
                },
                onLearnToPreventLockout = {
                    viewModel.trySendAction(MasterPasswordGeneratorAction.PreventLockoutClickAction)
                },
                modifier = Modifier.standardHorizontalMargin(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun MasterPasswordGeneratorContent(
    generatedPassword: String,
    onGenerateNewPassword: () -> Unit,
    onLearnToPreventLockout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalTextField(
            label = null,
            value = generatedPassword,
            onValueChange = {},
            readOnly = true,
            shouldAddCustomLineBreaks = true,
            textStyle = UniversalTheme.typography.sensitiveInfoSmall,
            visualTransformation = nonLetterColorVisualTransformation(),
            cardStyle = CardStyle.Full,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))
        UniversalFilledButton(
            label = stringResource(UniversalString.generate_button_label),
            onClick = onGenerateNewPassword,
            icon = rememberVectorPainter(id = UniversalDrawable.ic_generate),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(
                UniversalString.write_this_password_down_and_keep_it_somewhere_safe,
            ),
            style = UniversalTheme.typography.bodySmall,
            color = UniversalTheme.colorScheme.text.primary,
        )
        UniversalClickableText(
            label = stringResource(
                UniversalString.learn_about_other_ways_to_prevent_account_lockout,
            ),
            style = UniversalTheme.typography.labelMedium,
            onClick = onLearnToPreventLockout,
            innerPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MasterPasswordGeneratorTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    UniversalTopAppBar(
        title = stringResource(UniversalString.generate_master_password),
        scrollBehavior = scrollBehavior,
        navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
        navigationIconContentDescription = stringResource(id = UniversalString.back),
        onNavigationIconClick = onBackClick,
        actions = {
            UniversalTextButton(
                label = stringResource(id = UniversalString.save),
                onClick = onSaveClick,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MasterPasswordGeneratorTopBarPreview() {
    UniversalTheme {
        MasterPasswordGeneratorTopBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onBackClick = { },
            onSaveClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MasterPasswordGeneratorContentPreview() {
    UniversalTheme {
        MasterPasswordGeneratorContent(
            generatedPassword = "really-secure-password",
            onGenerateNewPassword = { },
            onLearnToPreventLockout = { },
            modifier = Modifier.padding(16.dp),
        )
    }
}
