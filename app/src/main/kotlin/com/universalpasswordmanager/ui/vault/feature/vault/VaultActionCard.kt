package com.universalpasswordmanager.ui.vault.feature.vault

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCard
import com.universalpasswordmanager.ui.platform.components.icon.UniversalIcon
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.feature.vault.handlers.VaultHandlers

/**
 * The action card for the vault screen.
 */
@Suppress("LongMethod")
@Composable
fun VaultActionCard(
    actionCardState: VaultState.ActionCardState,
    vaultHandlers: VaultHandlers,
    modifier: Modifier = Modifier,
) {
    when (actionCardState) {
        VaultState.ActionCardState.UpgradedToPremium -> {
            UniversalActionCard(
                cardTitle = stringResource(id = UniversalString.upgraded_to_premium),
                cardSubtitle = stringResource(
                    id = UniversalString.you_now_have_access_to_all_advanced_security_features,
                ),
                actionButton = UniversalButtonData(
                    label = UniversalString.learn_more.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                    isExternalLink = true,
                ),
                leadingContent = {
                    Icon(
                        painter = rememberVectorPainter(id = UniversalDrawable.ic_star),
                        contentDescription = null,
                        tint = UniversalTheme.colorScheme.icon.secondary,
                    )
                },
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.UpgradePremium -> {
            UniversalActionCard(
                cardTitle = stringResource(
                    id = UniversalString.unlock_advanced_security_features,
                ),
                cardSubtitle = stringResource(
                    id = UniversalString
                        .a_premium_plan_gives_you_more_tools_to_stay_secure_and_in_control,
                ),
                actionButton = UniversalButtonData(
                    label = UniversalString.learn_more.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.PremiumNeedsAttention -> {
            UniversalActionCard(
                cardTitle = stringResource(id = UniversalString.your_subscription_needs_attention),
                cardSubtitle = stringResource(id = UniversalString.check_your_plan_for_details),
                actionButton = UniversalButtonData(
                    label = UniversalString.view_plan.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.IntroducingArchive -> {
            UniversalActionCard(
                cardTitle = stringResource(id = UniversalString.introducing_archive),
                cardSubtitle = stringResource(
                    id = UniversalString.keep_items_you_dont_need_right_now_safe_but_out_sight,
                ),
                actionButton = UniversalButtonData(
                    label = UniversalString.go_to_archive.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                leadingContent = {
                    Icon(
                        painter = rememberVectorPainter(id = UniversalDrawable.ic_archive),
                        contentDescription = null,
                        tint = UniversalTheme.colorScheme.icon.secondary,
                    )
                },
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.ImportItems -> {
            UniversalActionCard(
                cardTitle = stringResource(id = UniversalString.import_saved_logins),
                cardSubtitle = stringResource(id = UniversalString.use_a_computer_to_import_logins),
                actionButton = UniversalButtonData(
                    label = UniversalString.get_started.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        is VaultState.ActionCardState.VaultPolicyBanner -> {
            UniversalActionCard(
                cardTitle = actionCardState.title?.invoke(),
                cardSubtitle = actionCardState.message(),
                actionButton = actionCardState.button?.let {
                    UniversalButtonData(
                        label = it,
                        onClick = { vaultHandlers.actionCardClick(actionCardState) },
                    )
                },
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) }
                    .takeIf { actionCardState.button == null },
                leadingContent = {
                    UniversalIcon(
                        iconData = IconData.Local(iconRes = UniversalDrawable.ic_info_circle),
                        tint = UniversalTheme.colorScheme.icon.secondary,
                    )
                },
                modifier = modifier,
            )
        }
    }
}
