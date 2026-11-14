package com.universalpasswordmanager.ui.platform.components.header

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Reusable header element that is clickable for expanding or collapsing content.
 *
 * @param collapsedText Text to display when the content is collapsed.
 * @param expandedText Text to display when the content is expanded.
 * @param showExpansionIndicator Whether to show an indicator to expand or collapse the content.
 * @param modifier modifier for the composable.
 * @param interactionSource A [MutableInteractionSource] for observing and emitting interactions
 * for this component.
 */
@Composable
fun UniversalExpandingHeader(
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    collapsedText: String = stringResource(id = UniversalString.additional_options),
    expandedText: String = collapsedText,
    showExpansionIndicator: Boolean = true,
    shape: Shape = UniversalTheme.shapes.content,
    insets: PaddingValues = PaddingValues(top = 16.dp, bottom = 8.dp),
) {
    ExpandingHeaderRow(
        isExpanded = isExpanded,
        onClick = onClick,
        shape = shape,
        insets = insets,
        showExpansionIndicator = showExpansionIndicator,
        expansionIndicatorColor = UniversalTheme.colorScheme.icon.secondary,
        interactionSource = interactionSource,
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isExpanded,
            label = "UniversalExpandingHeaderTitle_animation",
            // Make the animation shorter when the text is the same to avoid crossfading the same
            // text.
            animationSpec = tween(
                durationMillis = if (expandedText != collapsedText) {
                    AnimationConstants.DefaultDurationMillis
                } else {
                    0
                },
            ),
            modifier = Modifier.weight(weight = 1f, fill = false),
        ) { expanded ->
            if (expanded) {
                Text(
                    text = expandedText,
                    color = UniversalTheme.colorScheme.text.interaction,
                    style = UniversalTheme.typography.labelLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )
            } else {
                Text(
                    text = collapsedText,
                    color = UniversalTheme.colorScheme.text.interaction,
                    style = UniversalTheme.typography.labelLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
    }
}
