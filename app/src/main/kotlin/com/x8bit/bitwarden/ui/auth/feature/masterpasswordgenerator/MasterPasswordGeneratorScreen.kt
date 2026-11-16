package com.x8bit.universalpasswordmanager.ui.auth.feature.masterpasswordgenerator

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
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.components.util.nonLetterColorVisualTransformation
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

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
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            MasterPasswordGeneratorEvent.NavigateBack -> onNavigateBack()
            MasterPasswordGeneratorEvent.NavigateToPreventLockout -> onNavigateToPreventLockout()
            is MasterPasswordGeneratorEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    snackbarData = Universal Password ManagerSnackbarData(message = event.text),
                    duration = SnackbarDuration.Short,
                )
            }

            is MasterPasswordGeneratorEvent.NavigateBackToRegistration -> {
                onNavigateBackWithPassword()
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
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
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
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
        Universal Password ManagerTextField(
            label = null,
            value = generatedPassword,
            onValueChange = {},
            readOnly = true,
            shouldAddCustomLineBreaks = true,
            textStyle = Universal Password ManagerTheme.typography.sensitiveInfoSmall,
            visualTransformation = nonLetterColorVisualTransformation(),
            cardStyle = CardStyle.Full,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(Universal Password ManagerString.generate_button_label),
            onClick = onGenerateNewPassword,
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_generate),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(
                Universal Password ManagerString.write_this_password_down_and_keep_it_somewhere_safe,
            ),
            style = Universal Password ManagerTheme.typography.bodySmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
        )
        Universal Password ManagerClickableText(
            label = stringResource(
                Universal Password ManagerString.learn_about_other_ways_to_prevent_account_lockout,
            ),
            style = Universal Password ManagerTheme.typography.labelMedium,
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
    Universal Password ManagerTopAppBar(
        title = stringResource(Universal Password ManagerString.generate_master_password),
        scrollBehavior = scrollBehavior,
        navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
        navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
        onNavigationIconClick = onBackClick,
        actions = {
            Universal Password ManagerTextButton(
                label = stringResource(id = Universal Password ManagerString.save),
                onClick = onSaveClick,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MasterPasswordGeneratorTopBarPreview() {
    Universal Password ManagerTheme {
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
    Universal Password ManagerTheme {
        MasterPasswordGeneratorContent(
            generatedPassword = "really-secure-password",
            onGenerateNewPassword = { },
            onLearnToPreventLockout = { },
            modifier = Modifier.padding(16.dp),
        )
    }
}
