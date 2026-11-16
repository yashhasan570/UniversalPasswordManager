package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.privilegedapps.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.card.Universal Password ManagerContentCard
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * Top level composable for the About Privileged Apps screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPrivilegedAppsScreen(
    onNavigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(Universal Password ManagerString.about_privileged_applications),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = remember { onNavigateBack },
            )
        },
    ) {
        AboutPrivilegedAppsContent(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun AboutPrivilegedAppsContent(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(Universal Password ManagerString.privileged_apps_description),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Universal Password ManagerContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.trusted_by_you),
                    subtitleText = stringResource(Universal Password ManagerString.trusted_by_you_learn_more),
                ),
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.trusted_by_the_community),
                    subtitleText = stringResource(Universal Password ManagerString.trusted_by_community_learn_more),
                ),
                ContentBlockData(
                    headerText = stringResource(Universal Password ManagerString.trusted_by_google),
                    subtitleText = stringResource(Universal Password ManagerString.trusted_by_google_learn_more),
                ),
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutPrivilegedAppsContent_Preview() {
    Universal Password ManagerTheme {
        AboutPrivilegedAppsContent()
    }
}
