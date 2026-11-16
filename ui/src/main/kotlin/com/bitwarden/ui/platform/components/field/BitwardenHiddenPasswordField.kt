package .ui.platform.components.field

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.nullableTestTag
import .ui.platform.components.field.color.universalpasswordmanagerTextFieldColors
import .ui.platform.components.field.toolbar.Universal Password ManagerEmptyTextToolbar
import .ui.platform.components.model.CardStyle
import .ui.platform.components.util.passwordVisualTransformation
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled password field that is completely hidden and non-interactable.
 *
 * @param label Label for the text field.
 * @param value Current text on the text field.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier Modifier for the composable.
 * @param passwordFieldTestTag The optional test tag associated with the inner password field.
 */
@Composable
fun Universal Password ManagerHiddenPasswordField(
    label: String?,
    value: String,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    passwordFieldTestTag: String? = null,
) {
    CompositionLocalProvider(value = LocalTextToolbar provides Universal Password ManagerEmptyTextToolbar) {
        TextField(
            modifier = modifier
                .cardStyle(cardStyle = cardStyle, paddingVertical = 6.dp)
                .nullableTestTag(tag = passwordFieldTestTag),
            textStyle = Universal Password ManagerTheme.typography.sensitiveInfoSmall,
            label = label?.let { { Text(text = it) } },
            value = value,
            onValueChange = { },
            visualTransformation = passwordVisualTransformation(staticCharacterCount = 8),
            singleLine = true,
            enabled = false,
            readOnly = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = universalpasswordmanagerTextFieldColors(),
        )
    }
}

@Preview
@Composable
private fun Universal Password ManagerHiddenPasswordField_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerHiddenPasswordField(
            label = "Label",
            value = "Password",
            cardStyle = CardStyle.Full,
        )
    }
}
