package com.universalpasswordmanager.ui.platform.components.field

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
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.nullableTestTag
import com.universalpasswordmanager.ui.platform.components.field.color.bitwardenTextFieldColors
import com.universalpasswordmanager.ui.platform.components.field.toolbar.UniversalEmptyTextToolbar
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.util.passwordVisualTransformation
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Represents a Universal-styled password field that is completely hidden and non-interactable.
 *
 * @param label Label for the text field.
 * @param value Current text on the text field.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier Modifier for the composable.
 * @param passwordFieldTestTag The optional test tag associated with the inner password field.
 */
@Composable
fun UniversalHiddenPasswordField(
    label: String?,
    value: String,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    passwordFieldTestTag: String? = null,
) {
    CompositionLocalProvider(value = LocalTextToolbar provides UniversalEmptyTextToolbar) {
        TextField(
            modifier = modifier
                .cardStyle(cardStyle = cardStyle, paddingVertical = 6.dp)
                .nullableTestTag(tag = passwordFieldTestTag),
            textStyle = UniversalTheme.typography.sensitiveInfoSmall,
            label = label?.let { { Text(text = it) } },
            value = value,
            onValueChange = { },
            visualTransformation = passwordVisualTransformation(staticCharacterCount = 8),
            singleLine = true,
            enabled = false,
            readOnly = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = bitwardenTextFieldColors(),
        )
    }
}

@Preview
@Composable
private fun UniversalHiddenPasswordField_preview() {
    UniversalTheme {
        UniversalHiddenPasswordField(
            label = "Label",
            value = "Password",
            cardStyle = CardStyle.Full,
        )
    }
}
