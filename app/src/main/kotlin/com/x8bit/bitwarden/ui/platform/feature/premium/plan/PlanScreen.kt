@file:Suppress("TooManyFunctions")

package com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .annotation.OmitFromCoverage
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.annotatedPluralsResource
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.spanStyleOf
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.badge.Universal Password ManagerStatusBadge
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.content.Universal Password ManagerContentBlock
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.divider.Universal Password ManagerHorizontalDivider
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.intent.model.AuthTabData
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerPlurals
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.billing.repository.model.PremiumSubscriptionStatus
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAuthTabLaunchers
import com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.PlanState.ViewState.Error.Type.PRICING_UNAVAILABLE
import com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.PlanState.ViewState.Error.Type.SUBSCRIPTION
import com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.handlers.PlanHandlers
import com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.util.badgeColors
import com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.util.labelRes
import com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.util.showsFeatureList
import com.x8bit.universalpasswordmanager.ui.platform.model.AuthTabLaunchers
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val PLACEHOLDER_TEXT: String = "--"

/**
 * The screen for the plan — shows the upgrade flow for free users and the
 * subscription-management surface for premium users.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    onNavigateBack: () -> Unit,
    onNavigateToUpgradedToPremium: () -> Unit,
    viewModel: PlanViewModel,
    intentManager: IntentManager = LocalIntentManager.current,
    authTabLaunchers: AuthTabLaunchers = LocalAuthTabLaunchers.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handlers = remember(viewModel) { PlanHandlers.create(viewModel) }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is PlanEvent.LaunchBrowser -> {
                intentManager.startAuthTab(
                    uri = event.url.toUri(),
                    authTabData = event.authTabData,
                    launcher = authTabLaunchers.premiumCheckout,
                )
            }

            is PlanEvent.LaunchPortal -> {
                intentManager.startAuthTab(
                    uri = event.url.toUri(),
                    authTabData = AuthTabData.CustomScheme(
                        callbackUrl = PREMIUM_CHECKOUT_CALLBACK_URL,
                    ),
                    launcher = authTabLaunchers.stripePortal,
                )
            }

            is PlanEvent.LaunchUri -> intentManager.launchUri(event.url.toUri())
            PlanEvent.NavigateBack -> onNavigateBack()
            PlanEvent.NavigateToUpgradedToPremium -> onNavigateToUpgradedToPremium()
        }
    }

    PlanDialogs(
        dialogState = state.dialogState,
        handlers = handlers,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = state.title),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = state.navigationIcon),
                navigationIconContentDescription = stringResource(
                    id = state.navigationIconContentDescription,
                ),
                onNavigationIconClick = handlers.onBackClick,
            )
        },
    ) {
        when (val viewState = state.viewState) {
            is PlanState.ViewState.Content.Free.Cloud -> {
                FreeCloudContent(
                    viewState = viewState,
                    handlers = handlers,
                )
            }

            is PlanState.ViewState.Content.Free.SelfHosted -> {
                FreeSelfHostedContent()
            }

            is PlanState.ViewState.Content.Premium -> {
                PremiumContent(
                    viewState = viewState,
                    handlers = handlers,
                )
            }

            is PlanState.ViewState.Error -> {
                Universal Password ManagerErrorContent(
                    illustrationData = IconData.Local(iconRes = Universal Password ManagerDrawable.ill_data_breach),
                    message = viewState.message(),
                    buttonData = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.try_again.asText(),
                        onClick = {
                            when (viewState.type) {
                                PRICING_UNAVAILABLE -> handlers.onRetryPricingClick()
                                SUBSCRIPTION -> handlers.onRetrySubscriptionClick()
                            }
                        },
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is PlanState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(
                    text = viewState.message(),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun PlanDialogs(
    dialogState: PlanState.DialogState?,
    handlers: PlanHandlers,
) {
    when (dialogState) {
        is PlanState.DialogState.CheckoutError -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.secure_checkout_didnt_load),
                message = stringResource(id = Universal Password ManagerString.trouble_opening_payment_page),
                confirmButtonText = stringResource(id = Universal Password ManagerString.try_again),
                dismissButtonText = stringResource(id = Universal Password ManagerString.close),
                onConfirmClick = handlers.onRetryClick,
                onDismissClick = handlers.onDismissError,
                onDismissRequest = handlers.onDismissError,
            )
        }

        is PlanState.DialogState.WaitingForPayment -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.payment_not_received_yet),
                message = stringResource(id = Universal Password ManagerString.return_to_stripe_to_finish),
                confirmButtonText = stringResource(id = Universal Password ManagerString.go_back),
                dismissButtonText = stringResource(id = Universal Password ManagerString.close),
                onConfirmClick = handlers.onGoBackClick,
                onDismissClick = handlers.onCancelWaiting,
                onDismissRequest = handlers.onCancelWaiting,
            )
        }

        is PlanState.DialogState.PendingUpgrade -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.upgrade_pending),
                message = stringResource(
                    id = Universal Password ManagerString.upgrade_pending_message,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.sync_now),
                dismissButtonText = stringResource(id = Universal Password ManagerString.continue_text),
                onConfirmClick = handlers.onSyncClick,
                onDismissClick = handlers.onContinueClick,
                onDismissRequest = handlers.onContinueClick,
            )
        }

        is PlanState.DialogState.CancelConfirmation -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.continue_to_stripe),
                message = stringResource(
                    id = Universal Password ManagerString
                        .youll_be_taken_to_stripe_to_manage_your_subscription_cancellation,
                    dialogState.nextRenewalDate,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.continue_text),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = handlers.onConfirmCancelClick,
                onDismissClick = handlers.onDismissCancelConfirmation,
                onDismissRequest = handlers.onDismissCancelConfirmation,
            )
        }

        is PlanState.DialogState.PortalError -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.portal_error),
                message = stringResource(id = Universal Password ManagerString.trouble_loading_portal),
                confirmButtonText = stringResource(id = Universal Password ManagerString.try_again),
                dismissButtonText = stringResource(id = Universal Password ManagerString.close),
                onConfirmClick = handlers.onRetryPortalClick,
                onDismissClick = handlers.onDismissPortalError,
                onDismissRequest = handlers.onDismissPortalError,
            )
        }

        PlanState.DialogState.LoadingPortal -> {
            Universal Password ManagerLoadingDialog(
                text = stringResource(id = Universal Password ManagerString.loading_portal),
            )
        }

        is PlanState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.message())
        }

        null -> Unit
    }
}

@Composable
private fun FreeCloudContent(
    viewState: PlanState.ViewState.Content.Free.Cloud,
    handlers: PlanHandlers,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        PremiumDetailsCard(
            modifier = Modifier.standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        // Hide the Upgrade Now CTA (and its Stripe footer copy) while a Stripe upgrade is
        // already in flight for the active user. CTAs reappear once the server flips the
        // user to Premium.
        if (!viewState.isPremiumUpgradePending) {
            UpgradeNowCallToAction(
                viewState = viewState,
                onUpgradeNowClick = handlers.onUpgradeNowClick,
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun UpgradeNowCallToAction(
    viewState: PlanState.ViewState.Content.Free.Cloud,
    onUpgradeNowClick: () -> Unit,
) {
    Universal Password ManagerFilledButton(
        label = stringResource(id = Universal Password ManagerString.upgrade_to_premium),
        onClick = onUpgradeNowClick,
        icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_external_link),
        modifier = Modifier
            .standardHorizontalMargin()
            .fillMaxWidth()
            .testTag("UpgradeNowButton"),
    )
    Spacer(modifier = Modifier.height(height = 12.dp))

    PriceRow(
        rate = viewState.rate,
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
    Spacer(modifier = Modifier.height(height = 12.dp))

    Text(
        text = stringResource(
            id = Universal Password ManagerString.youll_complete_the_purchase_with_stripe_secure_checkout,
        ),
        style = Universal Password ManagerTheme.typography.bodyMedium,
        color = Universal Password ManagerTheme.colorScheme.text.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin()
            .testTag("StripeFooterText"),
    )
    Spacer(modifier = Modifier.height(height = 16.dp))
}

@Composable
private fun PriceRow(
    rate: String,
    modifier: Modifier = Modifier,
) {
    val formattedContentDescription = stringResource(
        id = Universal Password ManagerString.per_month_cancel_anytime_content_description,
        formatArgs = arrayOf(rate),
    )
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                contentDescription = formattedContentDescription
            },
    ) {
        Text(
            text = rate,
            style = Universal Password ManagerTheme.typography.labelMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .semantics { hideFromAccessibility() }
                .alignByBaseline(),
        )
        Spacer(modifier = Modifier.width(width = 4.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.per_month_cancel_anytime),
            style = Universal Password ManagerTheme.typography.labelSmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .semantics { hideFromAccessibility() }
                .alignByBaseline(),
        )
    }
}

@Suppress("MaxLineLength")
@Composable
private fun FreeSelfHostedContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerInfoCalloutCard(
            text = stringResource(
                id = Universal Password ManagerString
                    .to_manage_your_premium_subscription_youll_need_to_login_to_your_web_vault_on_a_computer,
            ),
            startIcon = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_info_circle),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag("SelfHostedManageOnWebVaultCallout"),
        )
        Spacer(modifier = Modifier.height(16.dp))
        PremiumFeaturesCard(
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun PremiumFeaturesCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .cardStyle(
                cardStyle = CardStyle.Full,
                // Override bottom padding to account for custom
                // `Universal Password ManagerContentBlock` vertical padding, below.
                paddingBottom = 0.dp,
            ),
    ) {
        Text(
            text = stringResource(id = Universal Password ManagerString.unlock_premium_features),
            style = Universal Password ManagerTheme.typography.labelLarge,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .standardHorizontalMargin(),
        )

        PremiumFeatureRows()
    }
}

@Composable
private fun ColumnScope.PremiumFeatureRows() {
    Universal Password ManagerHorizontalDivider()

    val features = listOf(
        Universal Password ManagerString.built_in_authenticator,
        Universal Password ManagerString.emergency_access,
        Universal Password ManagerString.secure_file_storage,
        Universal Password ManagerString.breach_monitoring,
    )
    features.forEachIndexed { index, featureStringRes ->
        Universal Password ManagerContentBlock(
            data = ContentBlockData(
                headerText = stringResource(id = featureStringRes),
                iconVectorResource = Universal Password ManagerDrawable.ic_check_mark,
            ),
            headerTextStyle = Universal Password ManagerTheme.typography.titleMedium,
            showDivider = index != features.lastIndex,
            modifier = Modifier
                .defaultMinSize(minHeight = 60.dp)
                .padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun PremiumDetailsCard(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .run {
                if (Universal Password ManagerTheme.colorScheme.isDynamicTheme) {
                    cardStyle(cardStyle = CardStyle.Full, paddingVertical = 0.dp)
                } else {
                    paint(
                        painter = painterResource(id = Universal Password ManagerDrawable.bg_card_gradient),
                        contentScale = ContentScale.FillBounds,
                    )
                }
            },
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        Image(
            painter = painterResource(id = Universal Password ManagerDrawable.img_premium),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.unlock_advanced_protection),
            style = Universal Password ManagerTheme.typography.headlineSmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        Benefits(modifier = Modifier.standardHorizontalMargin())
        Spacer(modifier = Modifier.height(height = 4.dp))
    }
}

@Composable
private fun Benefits(
    modifier: Modifier = Modifier,
    benefits: ImmutableList<Text> = persistentListOf(
        Universal Password ManagerString.breeze_through_2fa_with_built_in_codes.asText(),
        Universal Password ManagerString.run_reports_to_find_risky_passwords.asText(),
        Universal Password ManagerString.keep_documents_safe_and_encrypted.asText(),
        Universal Password ManagerString.add_a_trusted_emergency_contact.asText(),
        Universal Password ManagerString.identify_unsecure_websites.asText(),
        Universal Password ManagerString.flag_accounts_with_inactive_2fa.asText(),
        Universal Password ManagerString.share_files_securely_with_anyone_using_send.asText(),
        Universal Password ManagerString.receive_24_7_priority_support.asText(),
    ),
) {
    Column(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
        benefits.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier,
            ) {
                Icon(
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_checkmark_small),
                    contentDescription = null,
                    tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(
                    text = it(),
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
        }
    }
}

@Composable
private fun PremiumContent(
    viewState: PlanState.ViewState.Content.Premium,
    handlers: PlanHandlers,
    modifier: Modifier = Modifier,
) {
    var shouldShowManagePlanDialog by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        SubscriptionCard(
            viewState = viewState,
            modifier = Modifier.standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.manage_plan),
            onClick = { shouldShowManagePlanDialog = true },
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_external_link),
            isExternalLink = true,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag("ManagePlanButton"),
        )

        if (viewState.showCancelButton) {
            Spacer(modifier = Modifier.height(12.dp))
            Universal Password ManagerOutlinedButton(
                label = stringResource(id = Universal Password ManagerString.cancel_premium),
                onClick = handlers.onCancelPremiumClick,
                icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_external_link),
                isExternalLink = true,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth()
                    .testTag("CancelPremiumButton"),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }

    if (shouldShowManagePlanDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.continue_to_web_app),
            message = stringResource(
                id = Universal Password ManagerString.manage_your_subscription_plan_in_the_universalpasswordmanager_web_app,
            ),
            confirmButtonText = stringResource(id = Universal Password ManagerString.continue_text),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                shouldShowManagePlanDialog = false
                handlers.onManagePlanClick()
            },
            onDismissClick = { shouldShowManagePlanDialog = false },
            onDismissRequest = { shouldShowManagePlanDialog = false },
        )
    }
}

@Composable
private fun SubscriptionCard(
    viewState: PlanState.ViewState.Content.Premium,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .cardStyle(
                cardStyle = CardStyle.Full,
                // Override bottom padding; the final row (line item or feature) owns its
                // own spacing.
                paddingBottom = 0.dp,
            ),
    ) {
        SubscriptionHeader(
            status = viewState.status,
            nextChargeTotalText = viewState.nextChargeTotalText,
            nextChargeDateText = viewState.nextChargeDateText,
            cancelAtDateText = viewState.cancelAtDateText,
            canceledDateText = viewState.canceledDateText,
            suspensionDateText = viewState.suspensionDateText,
            gracePeriodDays = viewState.gracePeriodDays,
            modifier = Modifier.standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))

        if (viewState.status?.showsFeatureList() == true) {
            PremiumFeatureRows()
        } else {
            SubscriptionLineItems(viewState = viewState)
        }
    }
}

@Composable
private fun ColumnScope.SubscriptionLineItems(
    viewState: PlanState.ViewState.Content.Premium,
) {
    Universal Password ManagerHorizontalDivider()
    SubscriptionLineItem(
        label = stringResource(id = Universal Password ManagerString.billing_amount),
        value = viewState.billingAmountText(),
        testTag = "BillingAmountRow",
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )

    viewState.storageCostText?.let { storageCostText ->
        Universal Password ManagerHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
        SubscriptionLineItem(
            label = stringResource(id = Universal Password ManagerString.storage_cost),
            value = storageCostText,
            testTag = "StorageCostRow",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    viewState.discountAmountText?.let { discountAmountText ->
        Universal Password ManagerHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
        SubscriptionLineItem(
            label = stringResource(id = Universal Password ManagerString.discount),
            value = discountAmountText,
            testTag = "DiscountRow",
            valueColor = Universal Password ManagerTheme.colorScheme.statusBadge.success.text,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    Universal Password ManagerHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    SubscriptionLineItem(
        label = stringResource(id = Universal Password ManagerString.estimated_tax),
        value = viewState.estimatedTaxText,
        testTag = "EstimatedTaxRow",
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )

    Universal Password ManagerHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    SubscriptionLineItem(
        label = stringResource(id = Universal Password ManagerString.total),
        value = viewState.totalText(),
        testTag = "TotalRow",
        labelStyle = Universal Password ManagerTheme.typography.bodyLargeEmphasis,
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
}

@Composable
private fun SubscriptionHeader(
    status: PremiumSubscriptionStatus?,
    nextChargeTotalText: String?,
    nextChargeDateText: String?,
    cancelAtDateText: String?,
    canceledDateText: String?,
    suspensionDateText: String?,
    gracePeriodDays: Int?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = Universal Password ManagerString.premium_plan_name),
                style = Universal Password ManagerTheme.typography.titleExtraLarge,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
            )
            status?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Universal Password ManagerStatusBadge(
                    label = stringResource(id = it.labelRes()),
                    colors = it.badgeColors(),
                )
            }
        }

        val descriptionText = subscriptionDescriptionText(
            status = status,
            nextChargeTotalText = nextChargeTotalText,
            nextChargeDateText = nextChargeDateText,
            cancelAtDateText = cancelAtDateText,
            canceledDateText = canceledDateText,
            suspensionDateText = suspensionDateText,
            gracePeriodDays = gracePeriodDays,
        )

        descriptionText?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = Universal Password ManagerTheme.typography.labelLargeRegular,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
            )
        }
    }
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
private fun subscriptionDescriptionText(
    status: PremiumSubscriptionStatus?,
    nextChargeTotalText: String?,
    nextChargeDateText: String?,
    cancelAtDateText: String?,
    canceledDateText: String?,
    suspensionDateText: String?,
    gracePeriodDays: Int?,
): AnnotatedString? {
    val baseStyle = spanStyleOf(
        color = Universal Password ManagerTheme.colorScheme.text.secondary,
        textStyle = Universal Password ManagerTheme.typography.labelLargeRegular,
    )
    val emphasisStyle = spanStyleOf(
        color = Universal Password ManagerTheme.colorScheme.text.secondary,
        textStyle = Universal Password ManagerTheme.typography.labelLargeEmphasis,
    )
    return when (status) {
        PremiumSubscriptionStatus.ACTIVE -> annotatedStringResource(
            id = Universal Password ManagerString.premium_next_charge_summary,
            args = arrayOf(
                nextChargeTotalText ?: PLACEHOLDER_TEXT,
                nextChargeDateText ?: PLACEHOLDER_TEXT,
            ),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.CANCELED -> annotatedStringResource(
            id = Universal Password ManagerString.subscription_canceled_description,
            args = arrayOf(canceledDateText ?: suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.PENDING_CANCELLATION -> annotatedStringResource(
            id = Universal Password ManagerString.subscription_pending_cancellation_description,
            args = arrayOf(cancelAtDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.UNPAID -> annotatedStringResource(
            id = Universal Password ManagerString.subscription_unpaid_description,
            args = arrayOf(suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.UPDATE_PAYMENT -> annotatedStringResource(
            id = Universal Password ManagerString.subscription_update_payment_description,
            args = arrayOf(suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.PAST_DUE -> {
            val days = gracePeriodDays ?: 0
            annotatedPluralsResource(
                id = Universal Password ManagerPlurals.subscription_past_due_description,
                quantity = days,
                days.toString(),
                suspensionDateText ?: PLACEHOLDER_TEXT,
                style = baseStyle,
                emphasisHighlightStyle = emphasisStyle,
            )
        }

        PremiumSubscriptionStatus.PAUSED -> AnnotatedString(
            stringResource(id = Universal Password ManagerString.subscription_paused_description),
        )

        PremiumSubscriptionStatus.EXPIRED -> annotatedStringResource(
            id = Universal Password ManagerString.subscription_expired_description,
            args = arrayOf(suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        null -> null
    }
}

@Composable
private fun SubscriptionLineItem(
    label: String,
    value: String,
    testTag: String,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = Universal Password ManagerTheme.typography.bodyLarge,
    labelColor: Color = Universal Password ManagerTheme.colorScheme.text.secondary,
    valueStyle: TextStyle = Universal Password ManagerTheme.typography.bodyMedium,
    valueColor: Color = Universal Password ManagerTheme.colorScheme.text.primary,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .padding(vertical = 16.dp)
            .testTag(tag = testTag),
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor,
        )
        Text(
            text = value,
            style = valueStyle,
            color = valueColor,
        )
    }
}

@Preview
@OmitFromCoverage
@Composable
private fun PlanScreenFreeCloudAccount_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            FreeCloudContent(
                viewState = PlanState.ViewState.Content.Free.Cloud(
                    rate = "$1.67",
                    checkoutUrl = null,
                    isAwaitingPremiumStatus = false,
                    isPremiumUpgradePending = false,
                ),
                handlers = PlanHandlers(
                    onBackClick = {},
                    onUpgradeNowClick = {},
                    onDismissError = {},
                    onRetryClick = {},
                    onRetryPricingClick = {},
                    onClosePricingErrorClick = {},
                    onCancelWaiting = {},
                    onGoBackClick = {},
                    onSyncClick = {},
                    onContinueClick = {},
                    onManagePlanClick = {},
                    onCancelPremiumClick = {},
                    onConfirmCancelClick = {},
                    onDismissCancelConfirmation = {},
                    onDismissPortalError = {},
                    onRetryPortalClick = {},
                    onRetrySubscriptionClick = {},
                ),
            )
        }
    }
}

@Preview
@OmitFromCoverage
@Composable
private fun PlanScreenFreeSelfHostedFreeAccount_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            FreeSelfHostedContent()
        }
    }
}

@Preview
@OmitFromCoverage
@Composable
private fun PlanScreenPremiumAccount_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            PremiumContent(
                viewState = PlanState.ViewState.Content.Premium(
                    status = PremiumSubscriptionStatus.ACTIVE,
                    billingAmountText = Universal Password ManagerString.billing_rate_per_year.asText("$19.80"),
                    storageCostText = "$24.00",
                    discountAmountText = "-$2.10",
                    estimatedTaxText = "$3.85",
                    totalText = Universal Password ManagerString.billing_rate_per_year.asText("$45.55"),
                    nextChargeTotalText = "$45.55",
                    nextChargeDateText = "April 2, 2026",
                    showCancelButton = true,
                ),
                handlers = PlanHandlers(
                    onBackClick = {},
                    onUpgradeNowClick = {},
                    onDismissError = {},
                    onRetryClick = {},
                    onRetryPricingClick = {},
                    onClosePricingErrorClick = {},
                    onCancelWaiting = {},
                    onGoBackClick = {},
                    onSyncClick = {},
                    onContinueClick = {},
                    onManagePlanClick = {},
                    onCancelPremiumClick = {},
                    onConfirmCancelClick = {},
                    onDismissCancelConfirmation = {},
                    onDismissPortalError = {},
                    onRetryPortalClick = {},
                    onRetrySubscriptionClick = {},
                ),
            )
        }
    }
}

@Preview
@OmitFromCoverage
@Composable
private fun PlanScreenPremiumAccountZeroState_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            PremiumContent(
                viewState = PlanState.ViewState.Content.Premium(
                    status = PremiumSubscriptionStatus.ACTIVE,
                    billingAmountText = Universal Password ManagerString.billing_rate_per_year.asText("$19.80"),
                    storageCostText = null,
                    discountAmountText = null,
                    estimatedTaxText = "$0.00",
                    totalText = Universal Password ManagerString.billing_rate_per_year.asText("$19.80"),
                    nextChargeTotalText = "$19.80",
                    nextChargeDateText = "April 2, 2026",
                    showCancelButton = true,
                ),
                handlers = PlanHandlers(
                    onBackClick = {},
                    onUpgradeNowClick = {},
                    onDismissError = {},
                    onRetryClick = {},
                    onRetryPricingClick = {},
                    onClosePricingErrorClick = {},
                    onCancelWaiting = {},
                    onGoBackClick = {},
                    onSyncClick = {},
                    onContinueClick = {},
                    onManagePlanClick = {},
                    onCancelPremiumClick = {},
                    onConfirmCancelClick = {},
                    onDismissCancelConfirmation = {},
                    onDismissPortalError = {},
                    onRetryPortalClick = {},
                    onRetrySubscriptionClick = {},
                ),
            )
        }
    }
}
