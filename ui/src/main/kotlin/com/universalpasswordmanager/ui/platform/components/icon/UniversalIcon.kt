package com.universalpasswordmanager.ui.platform.components.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.universalpasswordmanager.ui.platform.base.util.nullableTestTag
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter

/**
 * Represents a Universal icon that is either locally loaded or loaded using Coil.
 *
 * @param iconData Label for the text field.
 * @param modifier A [Modifier] for the composable.
 * @param tint the color to be applied as the tint for the icon.
 */
@Composable
fun UniversalIcon(
    iconData: IconData,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    when (iconData) {
        is IconData.Network -> {
            val fallbackPainter = painterResource(id = iconData.fallbackIconRes)
            AsyncImage(
                model = iconData.uri,
                contentDescription = iconData.contentDescription?.invoke(),
                placeholder = fallbackPainter,
                error = fallbackPainter,
                modifier = modifier.nullableTestTag(tag = iconData.testTag),
            )
        }

        is IconData.Local -> {
            Icon(
                painter = rememberVectorPainter(id = iconData.iconRes),
                contentDescription = iconData.contentDescription?.invoke(),
                tint = tint,
                modifier = modifier.nullableTestTag(tag = iconData.testTag),
            )
        }
    }
}
