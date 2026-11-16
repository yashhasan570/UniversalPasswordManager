package com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.model.CardStyle
import .ui.platform.composition.LocalClock
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import kotlinx.collections.immutable.toImmutableList
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

/**
 * Displays UX for choosing deletion date of a send.
 *
 * @param enforcedDeletionHours The deletion window enforced by the SendControls policy, or `null`
 * when no deletion date is enforced. When enforced, the matching option is selected and the button
 * is locked.
 * @param onDateSelect The callback for being notified of updates to the selected date.
 * @param isEnabled Whether the button is enabled.
 * @param modifier A [Modifier] that you can use to apply custom modifications to the composable.
 * @param clock The clock used for calculating the selected date.
 */
@Composable
fun AddEditSendDeletionDateChooser(
    enforcedDeletionHours: Int?,
    onDateSelect: (Instant) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    clock: Clock = LocalClock.current,
) {
    val options = DeletionOption.entries.associateWith { it.text() }
    val enforcedOption = enforcedDeletionHours?.let { hours ->
        DeletionOption.entries.firstOrNull {
            it.offsetMillis == hours.hours.inWholeMilliseconds
        }
    }
    var selectedOption: DeletionOption by rememberSaveable {
        mutableStateOf(value = DeletionOption.SEVEN_DAYS)
    }
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.deletion_date),
        isEnabled = isEnabled && enforcedDeletionHours == null,
        options = options.values.toImmutableList(),
        selectedOption = (enforcedOption ?: selectedOption).text(),
        onOptionSelected = { selected ->
            selectedOption = options.entries.first { it.value == selected }.key
            onDateSelect(
                clock.instant().plus(selectedOption.offsetMillis, ChronoUnit.MILLIS),
            )
        },
        supportingText = stringResource(
            id = enforcedDeletionHours
                ?.let { Universal Password ManagerString.this_date_is_enforced_by_your_organization }
                ?: Universal Password ManagerString.deletion_date_info,
        ),
        insets = PaddingValues(top = 6.dp, bottom = 4.dp),
        cardStyle = CardStyle.Full,
        modifier = modifier,
    )
}

private enum class DeletionOption(
    val text: Text,
    val offsetMillis: Long,
) {
    ONE_HOUR(
        text = Universal Password ManagerString.one_hour.asText(),
        offsetMillis = 1.hours.inWholeMilliseconds,
    ),
    ONE_DAY(
        text = Universal Password ManagerString.one_day.asText(),
        offsetMillis = 1.days.inWholeMilliseconds,
    ),
    TWO_DAYS(
        text = Universal Password ManagerString.two_days.asText(),
        offsetMillis = 2.days.inWholeMilliseconds,
    ),
    THREE_DAYS(
        text = Universal Password ManagerString.three_days.asText(),
        offsetMillis = 3.days.inWholeMilliseconds,
    ),
    SEVEN_DAYS(
        text = Universal Password ManagerString.seven_days.asText(),
        offsetMillis = 7.days.inWholeMilliseconds,
    ),
    FOURTEEN_DAYS(
        text = Universal Password ManagerString.fourteen_days.asText(),
        offsetMillis = 14.days.inWholeMilliseconds,
    ),
    THIRTY_DAYS(
        text = Universal Password ManagerString.thirty_days.asText(),
        offsetMillis = 30.days.inWholeMilliseconds,
    ),
}
