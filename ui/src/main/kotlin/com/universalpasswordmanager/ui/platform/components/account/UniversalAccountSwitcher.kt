package com.universalpasswordmanager.ui.platform.components.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.interactiveBorder
import com.universalpasswordmanager.ui.platform.base.util.lowercaseWithCurrentLocal
import com.universalpasswordmanager.ui.platform.base.util.scrolledContainerBackground
import com.universalpasswordmanager.ui.platform.base.util.toSafeOverlayColor
import com.universalpasswordmanager.ui.platform.base.util.toUnscaledTextUnit
import com.universalpasswordmanager.ui.platform.components.account.dialog.UniversalLogoutConfirmationDialog
import com.universalpasswordmanager.ui.platform.components.account.dialog.UniversalRemovalConfirmationDialog
import com.universalpasswordmanager.ui.platform.components.account.model.AccountSummary
import com.universalpasswordmanager.ui.platform.components.account.util.iconRes
import com.universalpasswordmanager.ui.platform.components.account.util.iconTestTag
import com.universalpasswordmanager.ui.platform.components.account.util.initials
import com.universalpasswordmanager.ui.platform.components.account.util.supportingTextResOrNull
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalSelectionDialog
import com.universalpasswordmanager.ui.platform.components.dialog.row.UniversalBasicDialogRow
import com.universalpasswordmanager.ui.platform.components.divider.UniversalHorizontalDivider
import com.universalpasswordmanager.ui.platform.components.scrim.UniversalAnimatedScrim
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * The maximum number of accounts before the "Add account" button will be hidden to prevent the user
 * from adding any more.
 */
private const val MAXIMUM_ACCOUNT_LIMIT = 5

/**
 * An account switcher that will slide down inside whatever parent is it placed in and add a
 * a scrim via a [UniversalAnimatedScrim] to all content below it (but not above it). Additional
 * [UniversalAnimatedScrim] may be manually placed over other components that might not be covered
 * by the internal one.
 *
 * Note that this is intended to be used in conjunction with screens containing a top app bar but
 * should be placed with the screen's content and not with the bar itself.
 *
 * @param isVisible Whether this component is visible. Changing this value will animate the
 * component in or out of view.
 * @param accountSummaries The accounts to display in the switcher.
 * @param onSwitchAccountClick A callback when an account is clicked indicating that the account
 * should be switched to.
 * @param onLockAccountClick A callback when an account is clicked indicating that the account
 * should be locked.
 * @param onLogoutAccountClick A callback when an account is clicked indicating that the account
 * should be logged out.
 * @param onAddAccountClick A callback when the Add Account row is clicked.
 * @param onDismissRequest A callback when the component requests to be dismissed. This is triggered
 * whenever the user clicks on the scrim or any of the switcher items.
 * @param isAddAccountAvailable Whether the "Add account" button is available. Note that even
 * when `true`, this button may be hidden when there are more than [MAXIMUM_ACCOUNT_LIMIT] accounts
 * present.
 * @param modifier A [Modifier] for the composable.
 * @param topAppBarScrollBehavior Used to derive the background color of the content and keep it in
 * sync with the associated app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun UniversalAccountSwitcher(
    isVisible: Boolean,
    accountSummaries: ImmutableList<AccountSummary>,
    onSwitchAccountClick: (AccountSummary) -> Unit,
    onLockAccountClick: (AccountSummary) -> Unit,
    onLogoutAccountClick: (AccountSummary) -> Unit,
    onAddAccountClick: () -> Unit,
    onDismissRequest: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .union(WindowInsets.navigationBars)
        .only(WindowInsetsSides.Horizontal),
    isAddAccountAvailable: Boolean = true,
) {
    // Track the actual visibility (according to the internal transitions) so that we know when we
    // can safely show dialogs.
    var isVisibleActual by remember { mutableStateOf(isVisible) }

    var lockOrLogoutAccount by remember { mutableStateOf<AccountSummary?>(null) }
    var logoutConfirmationAccount by remember { mutableStateOf<AccountSummary?>(null) }
    var removeConfirmationAccount by remember { mutableStateOf<AccountSummary?>(null) }
    when {
        isVisibleActual -> {
            // Can not show dialogs when the switcher itself is visible
        }

        lockOrLogoutAccount != null -> {
            LockOrLogoutDialog(
                accountSummary = requireNotNull(lockOrLogoutAccount),
                onDismissRequest = { lockOrLogoutAccount = null },
                onLockAccountClick = {
                    onLockAccountClick(it)
                    lockOrLogoutAccount = null
                },
                onLogoutAccountClick = {
                    lockOrLogoutAccount = null
                    logoutConfirmationAccount = it
                },
                onRemoveAccountClick = {
                    lockOrLogoutAccount = null
                    removeConfirmationAccount = it
                },
            )
        }

        logoutConfirmationAccount != null -> {
            UniversalLogoutConfirmationDialog(
                accountSummary = requireNotNull(logoutConfirmationAccount),
                onDismissRequest = { logoutConfirmationAccount = null },
                onConfirmClick = {
                    onLogoutAccountClick(requireNotNull(logoutConfirmationAccount))
                    logoutConfirmationAccount = null
                },
            )
        }

        removeConfirmationAccount != null -> {
            UniversalRemovalConfirmationDialog(
                accountSummary = requireNotNull(removeConfirmationAccount),
                onDismissRequest = { removeConfirmationAccount = null },
                onConfirmClick = {
                    onLogoutAccountClick(requireNotNull(removeConfirmationAccount))
                    removeConfirmationAccount = null
                },
            )
        }
    }

    Box(modifier = modifier) {
        UniversalAnimatedScrim(
            isVisible = isVisible,
            onClick = onDismissRequest,
            modifier = Modifier
                .fillMaxSize(),
        )
        AnimatedAccountSwitcher(
            isVisible = isVisible,
            accountSummaries = accountSummaries,
            onSwitchAccountClick = {
                onDismissRequest()
                onSwitchAccountClick(it)
            },
            onSwitchAccountLongClick = {
                onDismissRequest()
                lockOrLogoutAccount = it
            },
            onAddAccountClick = {
                onDismissRequest()
                onAddAccountClick()
            },
            isAddAccountAvailable = isAddAccountAvailable,
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            currentAnimationState = { isVisibleActual = it },
            windowInsets = windowInsets,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
)
@Composable
private fun AnimatedAccountSwitcher(
    isVisible: Boolean,
    accountSummaries: ImmutableList<AccountSummary>,
    onSwitchAccountClick: (AccountSummary) -> Unit,
    onSwitchAccountLongClick: (AccountSummary) -> Unit,
    onAddAccountClick: () -> Unit,
    isAddAccountAvailable: Boolean,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    currentAnimationState: (isVisible: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .union(WindowInsets.navigationBars)
        .only(WindowInsetsSides.Horizontal),
) {
    val transition = updateTransition(
        targetState = isVisible,
        label = "AnimatedAccountSwitcher",
    )
        .also { currentAnimationState(it.currentState) }
    transition.AnimatedVisibility(
        visible = { it },
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it },
    ) {
        LazyColumn(
            modifier = modifier
                .testTag("AccountListView")
                // To prevent going all the way up to the bottom of the screen, we'll add some small
                // bottom padding.
                .padding(bottom = 24.dp)
                // Match the color of the switcher the different states of the app bar.
                .scrolledContainerBackground(topAppBarScrollBehavior)
                .windowInsetsPadding(windowInsets),
        ) {
            items(accountSummaries) { accountSummary ->
                AccountSummaryItem(
                    accountSummary = accountSummary,
                    onSwitchAccountClick = onSwitchAccountClick,
                    onSwitchAccountLongClick = onSwitchAccountLongClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                UniversalHorizontalDivider()
            }
            if (accountSummaries.size < MAXIMUM_ACCOUNT_LIMIT && isAddAccountAvailable) {
                item {
                    AddAccountItem(
                        onClick = onAddAccountClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("AddAccountButton")
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun AccountSummaryItem(
    accountSummary: AccountSummary,
    onSwitchAccountClick: (AccountSummary) -> Unit,
    onSwitchAccountLongClick: (AccountSummary) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .testTag("AccountCell")
            .requiredHeightIn(min = 60.dp)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = ripple(color = UniversalTheme.colorScheme.background.pressed),
                onClick = { onSwitchAccountClick(accountSummary) },
                onLongClick = { onSwitchAccountLongClick(accountSummary) },
                role = Role.Button,
            )
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = UniversalTheme.shapes.row,
            )
            .padding(vertical = 8.dp)
            .then(modifier),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = rememberVectorPainter(
                    id = UniversalDrawable.ic_account_initials_container,
                ),
                contentDescription = null,
                tint = accountSummary.avatarColor,
                modifier = Modifier.size(24.dp),
            )

            Text(
                text = accountSummary.initials,
                style = UniversalTheme.typography.titleMedium
                    // Do not allow scaling
                    .copy(fontSize = 12.dp.toUnscaledTextUnit()),
                color = accountSummary.avatarColor.toSafeOverlayColor(),
                modifier = Modifier.clearAndSetSemantics { },
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = accountSummary.email,
                style = UniversalTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("AccountEmailLabel"),
            )

            Text(
                text = accountSummary.environmentLabel,
                style = UniversalTheme.typography.bodyMedium,
                color = UniversalTheme.colorScheme.text.secondary,
                modifier = Modifier.testTag("AccountEnvironmentLabel"),
            )

            accountSummary.supportingTextResOrNull?.let { supportingTextResId ->
                Text(
                    text = stringResource(id = supportingTextResId).lowercaseWithCurrentLocal(),
                    style = UniversalTheme.typography.bodyMedium,
                    color = UniversalTheme.colorScheme.text.secondary,
                    modifier = Modifier.testTag("AccountStatusLabel"),
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = rememberVectorPainter(id = accountSummary.iconRes),
            contentDescription = null,
            tint = when (accountSummary.status) {
                AccountSummary.Status.ACTIVE -> UniversalTheme.colorScheme.icon.secondary
                else -> UniversalTheme.colorScheme.icon.primary
            },
            modifier = Modifier
                .testTag(accountSummary.iconTestTag)
                .size(24.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun LockOrLogoutDialog(
    accountSummary: AccountSummary,
    onDismissRequest: () -> Unit,
    onLockAccountClick: (AccountSummary) -> Unit,
    onLogoutAccountClick: (AccountSummary) -> Unit,
    onRemoveAccountClick: (AccountSummary) -> Unit,
) {
    UniversalSelectionDialog(
        title = "${accountSummary.email}\n${accountSummary.environmentLabel}",
        onDismissRequest = onDismissRequest,
        selectionItems = {
            if (accountSummary.isVaultUnlocked) {
                UniversalBasicDialogRow(
                    text = stringResource(id = UniversalString.lock),
                    onClick = {
                        onLockAccountClick(accountSummary)
                    },
                )
            }
            if (accountSummary.isLoggedIn) {
                UniversalBasicDialogRow(
                    text = stringResource(id = UniversalString.log_out),
                    onClick = {
                        onLogoutAccountClick(accountSummary)
                    },
                )
            } else {
                UniversalBasicDialogRow(
                    text = stringResource(id = UniversalString.remove_account),
                    onClick = {
                        onRemoveAccountClick(accountSummary)
                    },
                )
            }
        },
    )
}

@Composable
private fun AddAccountItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = UniversalTheme.colorScheme.background.pressed),
                onClick = onClick,
                role = Role.Button,
            )
            .interactiveBorder(
                interactionSource = interactionSource,
                shape = UniversalTheme.shapes.row,
            )
            .padding(vertical = 8.dp)
            .then(modifier),
    ) {
        Icon(
            painter = rememberVectorPainter(id = UniversalDrawable.ic_plus_small),
            contentDescription = null,
            tint = UniversalTheme.colorScheme.icon.secondary,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(24.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(id = UniversalString.add_account),
            style = UniversalTheme.typography.labelMedium,
            color = UniversalTheme.colorScheme.text.interaction,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun UniversalAccountSwitcher_preview() {
    UniversalAccountSwitcher(
        isVisible = true,
        accountSummaries = listOf(
            AccountSummary(
                userId = "123",
                name = "Cool Guy",
                email = "coolestguyeverthatlikestosurfandbeachvibes@gmail.com",
                avatarColorHex = "#EEEEEE",
                environmentLabel = "label",
                isActive = true,
                isLoggedIn = true,
                isVaultUnlocked = true,
            ),
        )
            .toImmutableList(),
        onSwitchAccountClick = {},
        onLockAccountClick = {},
        onLogoutAccountClick = {},
        onAddAccountClick = {},
        onDismissRequest = {},
        topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            state = rememberTopAppBarState(),
            canScroll = { false },
        ),
    )
}
