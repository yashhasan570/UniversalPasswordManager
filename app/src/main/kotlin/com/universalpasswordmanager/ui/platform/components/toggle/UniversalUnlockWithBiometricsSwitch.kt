package com.universalpasswordmanager.ui.platform.components.toggle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.universalpasswordmanager.core.data.manager.biometrics.model.BiometricSupportStatus
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.resource.UniversalString

/**
 * Displays a switch for enabling or disabling unlock with biometrics functionality.
 *
 * @param isChecked Indicates that the switch should be checked or not.
 * @param biometricSupportStatus Indicates what type of biometrics are supported on device.
 * @param onDisableBiometrics Callback invoked when the toggle has be turned off.
 * @param onEnableBiometrics Callback invoked when the toggle has be turned on.
 * @param modifier The [Modifier] to be applied to the switch.
 */
@Composable
fun UniversalUnlockWithBiometricsSwitch(
    biometricSupportStatus: BiometricSupportStatus,
    isChecked: Boolean,
    onDisableBiometrics: () -> Unit,
    onEnableBiometrics: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    val biometricsDescription: String = when (biometricSupportStatus) {
        BiometricSupportStatus.CLASS_3_SUPPORTED -> {
            stringResource(UniversalString.class_3_biometrics_description)
        }

        BiometricSupportStatus.CLASS_2_SUPPORTED -> {
            stringResource(UniversalString.class_2_biometrics_description)
        }

        BiometricSupportStatus.NOT_SUPPORTED -> return
    }
    UniversalSwitch(
        modifier = modifier,
        label = stringResource(UniversalString.unlock_with_biometrics),
        isChecked = isChecked,
        onCheckedChange = { toggled ->
            if (toggled) {
                onEnableBiometrics()
            } else {
                onDisableBiometrics()
            }
        },
        enabled = biometricSupportStatus == BiometricSupportStatus.CLASS_3_SUPPORTED,
        supportingText = biometricsDescription,
        cardStyle = cardStyle,
    )
}
