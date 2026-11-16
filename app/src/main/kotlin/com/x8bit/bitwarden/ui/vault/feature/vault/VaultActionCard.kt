package com.x8bit.universalpasswordmanager.ui.vault.feature.vault

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.handlers.VaultHandlers

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
            Universal Password ManagerActionCard(
                cardTitle = stringResource(id = Universal Password ManagerString.upgraded_to_premium),
                cardSubtitle = stringResource(
                    id = Universal Password ManagerString.you_now_have_access_to_all_advanced_security_features,
                ),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.learn_more.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                    isExternalLink = true,
                ),
                leadingContent = {
                    Icon(
                        painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_star),
                        contentDescription = null,
                        tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                    )
                },
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.UpgradePremium -> {
            Universal Password ManagerActionCard(
                cardTitle = stringResource(
                    id = Universal Password ManagerString.unlock_advanced_security_features,
                ),
                cardSubtitle = stringResource(
                    id = Universal Password ManagerString
                        .a_premium_plan_gives_you_more_tools_to_stay_secure_and_in_control,
                ),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.learn_more.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.PremiumNeedsAttention -> {
            Universal Password ManagerActionCard(
                cardTitle = stringResource(id = Universal Password ManagerString.your_subscription_needs_attention),
                cardSubtitle = stringResource(id = Universal Password ManagerString.check_your_plan_for_details),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.view_plan.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.IntroducingArchive -> {
            Universal Password ManagerActionCard(
                cardTitle = stringResource(id = Universal Password ManagerString.introducing_archive),
                cardSubtitle = stringResource(
                    id = Universal Password ManagerString.keep_items_you_dont_need_right_now_safe_but_out_sight,
                ),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.go_to_archive.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                leadingContent = {
                    Icon(
                        painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_archive),
                        contentDescription = null,
                        tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                    )
                },
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        VaultState.ActionCardState.ImportItems -> {
            Universal Password ManagerActionCard(
                cardTitle = stringResource(id = Universal Password ManagerString.import_saved_logins),
                cardSubtitle = stringResource(id = Universal Password ManagerString.use_a_computer_to_import_logins),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.get_started.asText(),
                    onClick = { vaultHandlers.actionCardClick(actionCardState) },
                ),
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) },
                modifier = modifier,
            )
        }

        is VaultState.ActionCardState.VaultPolicyBanner -> {
            Universal Password ManagerActionCard(
                cardTitle = actionCardState.title?.invoke(),
                cardSubtitle = actionCardState.message(),
                actionButton = actionCardState.button?.let {
                    Universal Password ManagerButtonData(
                        label = it,
                        onClick = { vaultHandlers.actionCardClick(actionCardState) },
                    )
                },
                onDismissClick = { vaultHandlers.dismissActionCardClick(actionCardState) }
                    .takeIf { actionCardState.button == null },
                leadingContent = {
                    Universal Password ManagerIcon(
                        iconData = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_info_circle),
                        tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                    )
                },
                modifier = modifier,
            )
        }
    }
}
