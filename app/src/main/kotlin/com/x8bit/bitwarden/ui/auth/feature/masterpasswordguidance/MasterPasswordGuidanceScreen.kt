package com.x8bit.universalpasswordmanager.ui.auth.feature.masterpasswordguidance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toAnnotatedString
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.card.Universal Password ManagerContentCard
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import kotlinx.collections.immutable.persistentListOf

/**
 * The top level composable for the Master Password Guidance screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterPasswordGuidanceScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGeneratePassword: () -> Unit,
    viewModel: MasterPasswordGuidanceViewModel = hiltViewModel(),
) {
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            MasterPasswordGuidanceEvent.NavigateBack -> onNavigateBack()
            MasterPasswordGuidanceEvent.NavigateToPasswordGenerator -> {
                onNavigateToGeneratePassword()
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
                title = stringResource(id = Universal Password ManagerString.master_password),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(MasterPasswordGuidanceAction.CloseAction)
                },
            )
        },
    ) {
        MasterPasswordGuidanceContent(
            onTryPasswordGeneratorAction = {
                viewModel.trySendAction(MasterPasswordGuidanceAction.TryPasswordGeneratorAction)
            },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun MasterPasswordGuidanceContent(
    onTryPasswordGeneratorAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(Universal Password ManagerString.a_secure_memorable_password),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(
                Universal Password ManagerString.one_of_the_best_ways_to_create_a_secure_and_memorable_password,
            ),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        MasterPasswordGuidanceContentBlocks()
        NeedSomeInspirationCard(
            onActionClicked = {
                onTryPasswordGeneratorAction()
            },
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Suppress("MaxLineLength")
@Composable
private fun MasterPasswordGuidanceContentBlocks(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Universal Password ManagerContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.choose_three_or_four_random_words)
                        .toAnnotatedString(),
                    subtitleText = annotatedStringResource(
                        id = Universal Password ManagerString.pick_three_or_four_random_unrelated_words,
                    ),
                    iconVectorResource = Universal Password ManagerDrawable.ic_number1,
                ),
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.combine_those_words_together)
                        .toAnnotatedString(),
                    subtitleText = annotatedStringResource(
                        id = Universal Password ManagerString.put_the_words_together_in_any_order_to_form_your_passphrase,
                    ),
                    iconVectorResource = Universal Password ManagerDrawable.ic_number2,
                ),
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.make_it_yours).toAnnotatedString(),
                    subtitleText = annotatedStringResource(
                        id = Universal Password ManagerString.add_a_number_or_symbol_to_make_it_even_stronger,
                    ),
                    iconVectorResource = Universal Password ManagerDrawable.ic_number3,
                ),
            ),
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun NeedSomeInspirationCard(
    onActionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerActionCard(
        cardTitle = stringResource(Universal Password ManagerString.need_some_inspiration),
        actionButton = Universal Password ManagerButtonData(
            label = Universal Password ManagerString.check_out_the_passphrase_generator.asText(),
            onClick = onActionClicked,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun MasterPasswordGuidanceScreenPreview() {
    Universal Password ManagerTheme {
        MasterPasswordGuidanceScreen(
            onNavigateBack = {},
            onNavigateToGeneratePassword = {},
        )
    }
}
