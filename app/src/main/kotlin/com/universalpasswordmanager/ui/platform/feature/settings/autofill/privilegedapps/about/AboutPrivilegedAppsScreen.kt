package com.universalpasswordmanager.ui.platform.feature.settings.autofill.privilegedapps.about

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
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.card.UniversalContentCard
import com.universalpasswordmanager.ui.platform.components.content.model.ContentBlockData
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
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
    UniversalScaffold(
        topBar = {
            UniversalTopAppBar(
                title = stringResource(UniversalString.about_privileged_applications),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
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
            text = stringResource(UniversalString.privileged_apps_description),
            textAlign = TextAlign.Center,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        UniversalContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(UniversalString.trusted_by_you),
                    subtitleText = stringResource(UniversalString.trusted_by_you_learn_more),
                ),
                ContentBlockData(
                    headerText = stringResource(UniversalString.trusted_by_the_community),
                    subtitleText = stringResource(UniversalString.trusted_by_community_learn_more),
                ),
                ContentBlockData(
                    headerText = stringResource(UniversalString.trusted_by_google),
                    subtitleText = stringResource(UniversalString.trusted_by_google_learn_more),
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
    UniversalTheme {
        AboutPrivilegedAppsContent()
    }
}
