package com.universalpasswordmanager.ui.platform.components.debug

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.universalpasswordmanager.core.data.manager.model.FlagKey
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.resource.UniversalString

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
    FlagKey.UniversalAuthenticationEnabled,
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
    UniversalSwitch(
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

    FlagKey.NoLogoutOnKdfChange -> stringResource(UniversalString.avoid_logout_on_kdf_change)
    FlagKey.UniversalAuthenticationEnabled -> {
        stringResource(UniversalString.bitwarden_authentication_enabled)
    }

    FlagKey.CardScanner -> stringResource(UniversalString.scan_card)
    FlagKey.ManageDevices -> stringResource(UniversalString.manage_devices)
    FlagKey.AttachmentUpdates -> stringResource(UniversalString.attachment_updates)
    FlagKey.V2EncryptionJitPassword -> stringResource(UniversalString.v2_encryption_jit_password)
    FlagKey.V2EncryptionKeyConnector -> stringResource(UniversalString.v2_encryption_key_connector)
    FlagKey.V2EncryptionPassword -> stringResource(UniversalString.v2_encryption_password)
    FlagKey.V2EncryptionTde -> stringResource(UniversalString.v2_encryption_tde)
    FlagKey.NewItemTypes -> stringResource(UniversalString.new_item_types)
    FlagKey.FillAssistTargetingRules -> stringResource(UniversalString.fill_assist_targeting_rules)
    FlagKey.DebugDisableSelfHostPremiumCheck -> {
        stringResource(UniversalString.debug_disable_self_host_premium_check)
    }

    FlagKey.FedRamp -> stringResource(UniversalString.fed_ramp)
    FlagKey.SendControls -> stringResource(UniversalString.send_controls)
    FlagKey.SendControlsExistingSends -> {
        stringResource(UniversalString.send_controls_existing_sends)
    }

    FlagKey.IdentityAutofill -> stringResource(UniversalString.identity_autofill)
    FlagKey.SdkPinUnlock -> stringResource(UniversalString.sdk_pin_unlock)
    FlagKey.SdkPreLogin -> stringResource(UniversalString.sdk_pre_login)
}
