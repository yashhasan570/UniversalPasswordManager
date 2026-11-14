package com.universalpasswordmanager.ui.auth.feature.masterpasswordhint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString

/**
 * The top level composable for the Login screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterPasswordHintScreen(
    onNavigateBack: () -> Unit,
    viewModel: MasterPasswordHintViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            MasterPasswordHintEvent.NavigateBack -> onNavigateBack()
        }
    }

    when (val dialogState = state.dialog) {
        is MasterPasswordHintState.DialogState.PasswordHintSent -> {
            UniversalBasicDialog(
                title = stringResource(id = UniversalString.password_hint),
                message = stringResource(id = UniversalString.password_hint_alert),
                onDismissRequest = {
                    viewModel.trySendAction(MasterPasswordHintAction.DismissDialog)
                },
            )
        }

        is MasterPasswordHintState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialogState.message())
        }

        is MasterPasswordHintState.DialogState.Error -> {
            UniversalBasicDialog(
                title = dialogState
                    .title
                    ?.invoke()
                    ?: stringResource(id = UniversalString.an_error_has_occurred),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = {
                    viewModel.trySendAction(MasterPasswordHintAction.DismissDialog)
                },
            )
        }

        null -> Unit
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.password_hint),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(MasterPasswordHintAction.CloseClick)
                },
                actions = {
                    UniversalTextButton(
                        label = stringResource(id = UniversalString.submit),
                        onClick = { viewModel.trySendAction(MasterPasswordHintAction.SubmitClick) },
                        modifier = Modifier.testTag("SubmitButton"),
                    )
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            UniversalTextField(
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
                value = state.emailInput,
                onValueChange = {
                    viewModel.trySendAction(MasterPasswordHintAction.EmailInputChange(it))
                },
                label = stringResource(id = UniversalString.email_address),
                keyboardType = KeyboardType.Email,
                textFieldTestTag = "MasterPasswordHintEmailField",
                supportingText = stringResource(id = UniversalString.enter_email_for_hint),
                cardStyle = CardStyle.Full,
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
