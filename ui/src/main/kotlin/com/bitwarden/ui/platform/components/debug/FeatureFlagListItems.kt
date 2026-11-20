package .ui.platform.components.debug

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import .core.data.manager.model.FlagKey
import .ui.platform.components.model.CardStyle
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.resource.Universal Password ManagerString

/**
 * Creates a list item for a [FlagKey].
 */
@Composable
fun <T : Any> FlagKey<T>.ListItemContent(
    currentValue: T,
    onValueChange: (key: FlagKey<T>, value: T) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) = when (val flagKey = this) {
    is FlagKey.DummyInt,
    FlagKey.DummyString,
        -> Unit

    FlagKey.DummyBoolean,
    FlagKey.Universal Password ManagerAuthenticationEnabled,
    FlagKey.NoLogoutOnKdfChange,
    FlagKey.CardScanner,
    FlagKey.ManageDevices,
    FlagKey.AttachmentUpdates,
    FlagKey.V2EncryptionJitPassword,
    FlagKey.V2EncryptionKeyConnector,
    FlagKey.V2EncryptionPassword,
    FlagKey.V2EncryptionTde,
    FlagKey.NewItemTypes,
    FlagKey.FillAssistTargetingRules,
    FlagKey.DebugDisableSelfHostPremiumCheck,
    FlagKey.FedRamp,
    FlagKey.SendControls,
    FlagKey.SendControlsExistingSends,
    FlagKey.IdentityAutofill,
    FlagKey.SdkPinUnlock,
    FlagKey.SdkPreLogin,
    FlagKey.UpdatedOnboardingFlow,
        -> {
        @Suppress("UNCHECKED_CAST")
        BooleanFlagItem(
            label = flagKey.getDisplayLabel(),
            key = flagKey as FlagKey<Boolean>,
            currentValue = currentValue as Boolean,
            onValueChange = onValueChange as (FlagKey<Boolean>, Boolean) -> Unit,
            cardStyle = cardStyle,
            modifier = modifier,
        )
    }
}

/**
 * The UI layout for a boolean backed flag key.
 */
@Composable
private fun BooleanFlagItem(
    label: String,
    key: FlagKey<Boolean>,
    currentValue: Boolean,
    onValueChange: (key: FlagKey<Boolean>, value: Boolean) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerSwitch(
        label = label,
        isChecked = currentValue,
        onCheckedChange = { onValueChange(key, it) },
        cardStyle = cardStyle,
        modifier = modifier,
    )
}

@Composable
private fun <T : Any> FlagKey<T>.getDisplayLabel(): String = when (this) {
    FlagKey.DummyBoolean,
    is FlagKey.DummyInt,
    FlagKey.DummyString,
        -> this.keyName

    FlagKey.NoLogoutOnKdfChange -> stringResource(Universal Password ManagerString.avoid_logout_on_kdf_change)
    FlagKey.Universal Password ManagerAuthenticationEnabled -> {
        stringResource(Universal Password ManagerString.universalpasswordmanager_authentication_enabled)
    }

    FlagKey.CardScanner -> stringResource(Universal Password ManagerString.scan_card)
    FlagKey.ManageDevices -> stringResource(Universal Password ManagerString.manage_devices)
    FlagKey.AttachmentUpdates -> stringResource(Universal Password ManagerString.attachment_updates)
    FlagKey.V2EncryptionJitPassword -> stringResource(Universal Password ManagerString.v2_encryption_jit_password)
    FlagKey.V2EncryptionKeyConnector -> stringResource(Universal Password ManagerString.v2_encryption_key_connector)
    FlagKey.V2EncryptionPassword -> stringResource(Universal Password ManagerString.v2_encryption_password)
    FlagKey.V2EncryptionTde -> stringResource(Universal Password ManagerString.v2_encryption_tde)
    FlagKey.NewItemTypes -> stringResource(Universal Password ManagerString.new_item_types)
    FlagKey.FillAssistTargetingRules -> stringResource(Universal Password ManagerString.fill_assist_targeting_rules)
    FlagKey.DebugDisableSelfHostPremiumCheck -> {
        stringResource(Universal Password ManagerString.debug_disable_self_host_premium_check)
    }

    FlagKey.FedRamp -> stringResource(Universal Password ManagerString.fed_ramp)
    FlagKey.SendControls -> stringResource(Universal Password ManagerString.send_controls)
    FlagKey.SendControlsExistingSends -> {
        stringResource(Universal Password ManagerString.send_controls_existing_sends)
    }

    FlagKey.IdentityAutofill -> stringResource(Universal Password ManagerString.identity_autofill)
    FlagKey.SdkPinUnlock -> stringResource(Universal Password ManagerString.sdk_pin_unlock)
    FlagKey.SdkPreLogin -> stringResource(Universal Password ManagerString.sdk_pre_login)
    FlagKey.UpdatedOnboardingFlow -> stringResource(Universal Password ManagerString.updated_onboarding_flow)
}
