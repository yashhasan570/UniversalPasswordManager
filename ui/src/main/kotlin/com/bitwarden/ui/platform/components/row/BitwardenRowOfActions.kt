package .ui.platform.components.row

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A composable function to display a row of actions.
 *
 * This function takes in a trailing lambda which provides a `RowScope` in order to
 * layout individual actions. The actions will be arranged in a horizontal
 * sequence, spaced by 8.dp, and are vertically centered.
 *
 * @param actions The composable actions to execute within the [RowScope]. Typically used to
 * layout individual icons or buttons.
 */
@Composable
fun Universal Password ManagerRowOfActions(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        content = actions,
    )
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerRowOfIconButtons_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerRowOfActions {
            Icon(
                painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
                contentDescription = "Icon 1",
                modifier = Modifier.size(24.dp),
            )
            Icon(
                painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
                contentDescription = "Icon 2",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
