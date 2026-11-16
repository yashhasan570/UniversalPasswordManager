package .ui.platform.components.radio

import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import .ui.platform.components.radio.color.universalpasswordmanagerRadioButtonColors

/**
 * A custom Universal Password Manager-themed radio button.
 *
 * @param isSelected Whether this radio button is selected or not.
 * @param onClick The lambda to be invoked when the item is clicked.
 * @param modifier The [Modifier] to be applied to this radio button.
 */
@Composable
fun Universal Password ManagerRadioButton(
    isSelected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    RadioButton(
        modifier = modifier,
        selected = isSelected,
        onClick = onClick,
        colors = universalpasswordmanagerRadioButtonColors(),
    )
}
