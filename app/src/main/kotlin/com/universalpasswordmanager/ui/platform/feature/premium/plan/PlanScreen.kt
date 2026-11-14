@file:Suppress("TooManyFunctions")

package com.universalpasswordmanager.ui.platform.feature.premium.plan

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
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.annotatedPluralsResource
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.spanStyleOf
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.badge.UniversalStatusBadge
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalInfoCalloutCard
import com.universalpasswordmanager.ui.platform.components.content.UniversalContentBlock
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.content.model.ContentBlockData
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.divider.UniversalHorizontalDivider
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.manager.intent.model.AuthTabData
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalPlurals
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.billing.repository.model.PremiumSubscriptionStatus
import com.universalpasswordmanager.ui.platform.composition.LocalAuthTabLaunchers
import com.universalpasswordmanager.ui.platform.feature.premium.plan.PlanState.ViewState.Error.Type.PRICING_UNAVAILABLE
import com.universalpasswordmanager.ui.platform.feature.premium.plan.PlanState.ViewState.Error.Type.SUBSCRIPTION
import com.universalpasswordmanager.ui.platform.feature.premium.plan.handlers.PlanHandlers
import com.universalpasswordmanager.ui.platform.feature.premium.plan.util.badgeColors
import com.universalpasswordmanager.ui.platform.feature.premium.plan.util.labelRes
import com.universalpasswordmanager.ui.platform.feature.premium.plan.util.showsFeatureList
import com.universalpasswordmanager.ui.platform.model.AuthTabLaunchers
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
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
                UniversalErrorContent(
                    illustrationData = IconData.Local(iconRes = UniversalDrawable.ill_data_breach),
                    message = viewState.message(),
                    buttonData = UniversalButtonData(
                        label = UniversalString.try_again.asText(),
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
                UniversalLoadingContent(
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
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.secure_checkout_didnt_load),
                message = stringResource(id = UniversalString.trouble_opening_payment_page),
                confirmButtonText = stringResource(id = UniversalString.try_again),
                dismissButtonText = stringResource(id = UniversalString.close),
                onConfirmClick = handlers.onRetryClick,
                onDismissClick = handlers.onDismissError,
                onDismissRequest = handlers.onDismissError,
            )
        }

        is PlanState.DialogState.WaitingForPayment -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.payment_not_received_yet),
                message = stringResource(id = UniversalString.return_to_stripe_to_finish),
                confirmButtonText = stringResource(id = UniversalString.go_back),
                dismissButtonText = stringResource(id = UniversalString.close),
                onConfirmClick = handlers.onGoBackClick,
                onDismissClick = handlers.onCancelWaiting,
                onDismissRequest = handlers.onCancelWaiting,
            )
        }

        is PlanState.DialogState.PendingUpgrade -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.upgrade_pending),
                message = stringResource(
                    id = UniversalString.upgrade_pending_message,
                ),
                confirmButtonText = stringResource(id = UniversalString.sync_now),
                dismissButtonText = stringResource(id = UniversalString.continue_text),
                onConfirmClick = handlers.onSyncClick,
                onDismissClick = handlers.onContinueClick,
                onDismissRequest = handlers.onContinueClick,
            )
        }

        is PlanState.DialogState.CancelConfirmation -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.continue_to_stripe),
                message = stringResource(
                    id = UniversalString
                        .youll_be_taken_to_stripe_to_manage_your_subscription_cancellation,
                    dialogState.nextRenewalDate,
                ),
                confirmButtonText = stringResource(id = UniversalString.continue_text),
                dismissButtonText = stringResource(id = UniversalString.cancel),
                onConfirmClick = handlers.onConfirmCancelClick,
                onDismissClick = handlers.onDismissCancelConfirmation,
                onDismissRequest = handlers.onDismissCancelConfirmation,
            )
        }

        is PlanState.DialogState.PortalError -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.portal_error),
                message = stringResource(id = UniversalString.trouble_loading_portal),
                confirmButtonText = stringResource(id = UniversalString.try_again),
                dismissButtonText = stringResource(id = UniversalString.close),
                onConfirmClick = handlers.onRetryPortalClick,
                onDismissClick = handlers.onDismissPortalError,
                onDismissRequest = handlers.onDismissPortalError,
            )
        }

        PlanState.DialogState.LoadingPortal -> {
            UniversalLoadingDialog(
                text = stringResource(id = UniversalString.loading_portal),
            )
        }

        is PlanState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialogState.message())
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
    UniversalFilledButton(
        label = stringResource(id = UniversalString.upgrade_to_premium),
        onClick = onUpgradeNowClick,
        icon = rememberVectorPainter(id = UniversalDrawable.ic_external_link),
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
            id = UniversalString.youll_complete_the_purchase_with_stripe_secure_checkout,
        ),
        style = UniversalTheme.typography.bodyMedium,
        color = UniversalTheme.colorScheme.text.primary,
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
        id = UniversalString.per_month_cancel_anytime_content_description,
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
            style = UniversalTheme.typography.labelMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .semantics { hideFromAccessibility() }
                .alignByBaseline(),
        )
        Spacer(modifier = Modifier.width(width = 4.dp))
        Text(
            text = stringResource(id = UniversalString.per_month_cancel_anytime),
            style = UniversalTheme.typography.labelSmall,
            color = UniversalTheme.colorScheme.text.primary,
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
        UniversalInfoCalloutCard(
            text = stringResource(
                id = UniversalString
                    .to_manage_your_premium_subscription_youll_need_to_login_to_your_web_vault_on_a_computer,
            ),
            startIcon = IconData.Local(iconRes = UniversalDrawable.ic_info_circle),
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
                // `UniversalContentBlock` vertical padding, below.
                paddingBottom = 0.dp,
            ),
    ) {
        Text(
            text = stringResource(id = UniversalString.unlock_premium_features),
            style = UniversalTheme.typography.labelLarge,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .standardHorizontalMargin(),
        )

        PremiumFeatureRows()
    }
}

@Composable
private fun ColumnScope.PremiumFeatureRows() {
    UniversalHorizontalDivider()

    val features = listOf(
        UniversalString.built_in_authenticator,
        UniversalString.emergency_access,
        UniversalString.secure_file_storage,
        UniversalString.breach_monitoring,
    )
    features.forEachIndexed { index, featureStringRes ->
        UniversalContentBlock(
            data = ContentBlockData(
                headerText = stringResource(id = featureStringRes),
                iconVectorResource = UniversalDrawable.ic_check_mark,
            ),
            headerTextStyle = UniversalTheme.typography.titleMedium,
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
                if (UniversalTheme.colorScheme.isDynamicTheme) {
                    cardStyle(cardStyle = CardStyle.Full, paddingVertical = 0.dp)
                } else {
                    paint(
                        painter = painterResource(id = UniversalDrawable.bg_card_gradient),
                        contentScale = ContentScale.FillBounds,
                    )
                }
            },
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        Image(
            painter = painterResource(id = UniversalDrawable.img_premium),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = stringResource(id = UniversalString.unlock_advanced_protection),
            style = UniversalTheme.typography.headlineSmall,
            color = UniversalTheme.colorScheme.text.primary,
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
        UniversalString.breeze_through_2fa_with_built_in_codes.asText(),
        UniversalString.run_reports_to_find_risky_passwords.asText(),
        UniversalString.keep_documents_safe_and_encrypted.asText(),
        UniversalString.add_a_trusted_emergency_contact.asText(),
        UniversalString.identify_unsecure_websites.asText(),
        UniversalString.flag_accounts_with_inactive_2fa.asText(),
        UniversalString.share_files_securely_with_anyone_using_send.asText(),
        UniversalString.receive_24_7_priority_support.asText(),
    ),
) {
    Column(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
        benefits.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier,
            ) {
                Icon(
                    painter = rememberVectorPainter(id = UniversalDrawable.ic_checkmark_small),
                    contentDescription = null,
                    tint = UniversalTheme.colorScheme.icon.secondary,
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(
                    text = it(),
                    style = UniversalTheme.typography.bodyMedium,
                    color = UniversalTheme.colorScheme.text.primary,
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

        UniversalFilledButton(
            label = stringResource(id = UniversalString.manage_plan),
            onClick = { shouldShowManagePlanDialog = true },
            icon = rememberVectorPainter(id = UniversalDrawable.ic_external_link),
            isExternalLink = true,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag("ManagePlanButton"),
        )

        if (viewState.showCancelButton) {
            Spacer(modifier = Modifier.height(12.dp))
            UniversalOutlinedButton(
                label = stringResource(id = UniversalString.cancel_premium),
                onClick = handlers.onCancelPremiumClick,
                icon = rememberVectorPainter(id = UniversalDrawable.ic_external_link),
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
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.continue_to_web_app),
            message = stringResource(
                id = UniversalString.manage_your_subscription_plan_in_the_universalpasswordmanager_web_app,
            ),
            confirmButtonText = stringResource(id = UniversalString.continue_text),
            dismissButtonText = stringResource(id = UniversalString.cancel),
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
    UniversalHorizontalDivider()
    SubscriptionLineItem(
        label = stringResource(id = UniversalString.billing_amount),
        value = viewState.billingAmountText(),
        testTag = "BillingAmountRow",
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )

    viewState.storageCostText?.let { storageCostText ->
        UniversalHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
        SubscriptionLineItem(
            label = stringResource(id = UniversalString.storage_cost),
            value = storageCostText,
            testTag = "StorageCostRow",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    viewState.discountAmountText?.let { discountAmountText ->
        UniversalHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
        SubscriptionLineItem(
            label = stringResource(id = UniversalString.discount),
            value = discountAmountText,
            testTag = "DiscountRow",
            valueColor = UniversalTheme.colorScheme.statusBadge.success.text,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    UniversalHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    SubscriptionLineItem(
        label = stringResource(id = UniversalString.estimated_tax),
        value = viewState.estimatedTaxText,
        testTag = "EstimatedTaxRow",
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )

    UniversalHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    SubscriptionLineItem(
        label = stringResource(id = UniversalString.total),
        value = viewState.totalText(),
        testTag = "TotalRow",
        labelStyle = UniversalTheme.typography.bodyLargeEmphasis,
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
                text = stringResource(id = UniversalString.premium_plan_name),
                style = UniversalTheme.typography.titleExtraLarge,
                color = UniversalTheme.colorScheme.text.primary,
            )
            status?.let {
                Spacer(modifier = Modifier.width(8.dp))
                UniversalStatusBadge(
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
                style = UniversalTheme.typography.labelLargeRegular,
                color = UniversalTheme.colorScheme.text.secondary,
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
        color = UniversalTheme.colorScheme.text.secondary,
        textStyle = UniversalTheme.typography.labelLargeRegular,
    )
    val emphasisStyle = spanStyleOf(
        color = UniversalTheme.colorScheme.text.secondary,
        textStyle = UniversalTheme.typography.labelLargeEmphasis,
    )
    return when (status) {
        PremiumSubscriptionStatus.ACTIVE -> annotatedStringResource(
            id = UniversalString.premium_next_charge_summary,
            args = arrayOf(
                nextChargeTotalText ?: PLACEHOLDER_TEXT,
                nextChargeDateText ?: PLACEHOLDER_TEXT,
            ),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.CANCELED -> annotatedStringResource(
            id = UniversalString.subscription_canceled_description,
            args = arrayOf(canceledDateText ?: suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.PENDING_CANCELLATION -> annotatedStringResource(
            id = UniversalString.subscription_pending_cancellation_description,
            args = arrayOf(cancelAtDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.UNPAID -> annotatedStringResource(
            id = UniversalString.subscription_unpaid_description,
            args = arrayOf(suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.UPDATE_PAYMENT -> annotatedStringResource(
            id = UniversalString.subscription_update_payment_description,
            args = arrayOf(suspensionDateText ?: PLACEHOLDER_TEXT),
            style = baseStyle,
            emphasisHighlightStyle = emphasisStyle,
        )

        PremiumSubscriptionStatus.PAST_DUE -> {
            val days = gracePeriodDays ?: 0
            annotatedPluralsResource(
                id = UniversalPlurals.subscription_past_due_description,
                quantity = days,
                days.toString(),
                suspensionDateText ?: PLACEHOLDER_TEXT,
                style = baseStyle,
                emphasisHighlightStyle = emphasisStyle,
            )
        }

        PremiumSubscriptionStatus.PAUSED -> AnnotatedString(
            stringResource(id = UniversalString.subscription_paused_description),
        )

        PremiumSubscriptionStatus.EXPIRED -> annotatedStringResource(
            id = UniversalString.subscription_expired_description,
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
    labelStyle: TextStyle = UniversalTheme.typography.bodyLarge,
    labelColor: Color = UniversalTheme.colorScheme.text.secondary,
    valueStyle: TextStyle = UniversalTheme.typography.bodyMedium,
    valueColor: Color = UniversalTheme.colorScheme.text.primary,
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
    UniversalTheme {
        UniversalScaffold {
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
    UniversalTheme {
        UniversalScaffold {
            FreeSelfHostedContent()
        }
    }
}

@Preview
@OmitFromCoverage
@Composable
private fun PlanScreenPremiumAccount_preview() {
    UniversalTheme {
        UniversalScaffold {
            PremiumContent(
                viewState = PlanState.ViewState.Content.Premium(
                    status = PremiumSubscriptionStatus.ACTIVE,
                    billingAmountText = UniversalString.billing_rate_per_year.asText("$19.80"),
                    storageCostText = "$24.00",
                    discountAmountText = "-$2.10",
                    estimatedTaxText = "$3.85",
                    totalText = UniversalString.billing_rate_per_year.asText("$45.55"),
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
    UniversalTheme {
        UniversalScaffold {
            PremiumContent(
                viewState = PlanState.ViewState.Content.Premium(
                    status = PremiumSubscriptionStatus.ACTIVE,
                    billingAmountText = UniversalString.billing_rate_per_year.asText("$19.80"),
                    storageCostText = null,
                    discountAmountText = null,
                    estimatedTaxText = "$0.00",
                    totalText = UniversalString.billing_rate_per_year.asText("$19.80"),
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
