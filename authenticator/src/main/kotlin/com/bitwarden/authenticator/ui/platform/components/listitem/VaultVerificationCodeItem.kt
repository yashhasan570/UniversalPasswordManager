package .authenticator.ui.platform.components.listitem

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .authenticator.ui.platform.components.listitem.model.VaultDropdownMenuAction
import .authenticator.ui.platform.components.listitem.model.VerificationCodeDisplayItem
import .core.util.persistentListOfNotNull
import .ui.platform.base.util.cardStyle
import .ui.platform.components.animation.AnimateNullableContentVisibility
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.indicator.Universal Password ManagerCircularCountdownIndicator
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * The verification code item displayed to the user.
 *
 * @param displayItem he model containing all relevant data to be displayed.
 * @param onItemClick The lambda function to be invoked when the item is clicked.
 * @param onDropdownMenuClick A lambda function invoked when a dropdown menu action is clicked.
 * @param cardStyle The card style to be applied to this item.
 * @param modifier The modifier for the item.
 */
@Composable
fun VaultVerificationCodeItem(
    displayItem: VerificationCodeDisplayItem,
    onItemClick: () -> Unit,
    onDropdownMenuClick: (VaultDropdownMenuAction) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    VaultVerificationCodeItem(
        authCode = displayItem.authCode,
        nextAuthCode = displayItem.nextAuthCode,
        primaryLabel = displayItem.title,
        secondaryLabel = displayItem.subtitle,
        periodSeconds = displayItem.periodSeconds,
        timeLeftSeconds = displayItem.timeLeftSeconds,
        alertThresholdSeconds = displayItem.alertThresholdSeconds,
        startIcon = displayItem.startIcon,
        onItemClick = onItemClick,
        onDropdownMenuClick = onDropdownMenuClick,
        showOverflow = displayItem.showOverflow,
        showMoveToUniversal Password Manager = displayItem.showMoveToUniversal Password Manager,
        cardStyle = cardStyle,
        modifier = modifier,
    )
}

/**
 * The verification code item displayed to the user.
 *
 * @param authCode The code for the item.
 * @param primaryLabel The label for the item. Represents the OTP issuer.
 * @param secondaryLabel The supporting label for the item. Represents the OTP account name.
 * @param periodSeconds The times span where the code is valid.
 * @param timeLeftSeconds The seconds remaining until a new code is needed.
 * @param alertThresholdSeconds The time threshold in seconds to display an expiration warning.
 * @param startIcon The leading icon for the item.
 * @param onItemClick The lambda function to be invoked when the item is clicked.
 * @param onDropdownMenuClick A lambda function invoked when a dropdown menu action is clicked.
 * @param showOverflow Whether overflow menu should be available or not.
 * @param showMoveToUniversal Password Manager Whether the option to move the item to Universal Password Manager is displayed.
 * @param cardStyle The card style to be applied to this item.
 * @param nextAuthCode The next verification code to preview when nearing expiration.
 * @param modifier The modifier for the item.
 */
@Suppress("LongMethod", "MagicNumber")
@Composable
fun VaultVerificationCodeItem(
    authCode: String,
    primaryLabel: String?,
    secondaryLabel: String?,
    periodSeconds: Int,
    timeLeftSeconds: Int,
    alertThresholdSeconds: Int,
    startIcon: IconData,
    onItemClick: () -> Unit,
    onDropdownMenuClick: (VaultDropdownMenuAction) -> Unit,
    showOverflow: Boolean,
    showMoveToUniversal Password Manager: Boolean,
    cardStyle: CardStyle,
    nextAuthCode: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .testTag(tag = "Item")
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                onClick = onItemClick,
                role = Role.Button,
                paddingStart = 16.dp,
                paddingEnd = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        Universal Password ManagerIcon(
            iconData = startIcon,
            tint = Universal Password ManagerTheme.colorScheme.icon.primary,
            modifier = Modifier.size(size = 24.dp),
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.weight(weight = 1f),
        ) {
            if (!primaryLabel.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.testTag(tag = "Name"),
                    text = primaryLabel,
                    style = Universal Password ManagerTheme.typography.bodyLarge,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (!secondaryLabel.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.testTag(tag = "Username"),
                    text = secondaryLabel,
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Universal Password ManagerCircularCountdownIndicator(
            modifier = Modifier.testTag(tag = "CircularCountDown"),
            timeLeftSeconds = timeLeftSeconds,
            periodSeconds = periodSeconds,
            alertThresholdSeconds = alertThresholdSeconds,
        )

        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                modifier = Modifier.testTag(tag = "AuthCode"),
                text = authCode
                    .chunked(size = 3) { it.padEnd(length = 3, padChar = ' ') }
                    .joinToString(separator = " "),
                style = Universal Password ManagerTheme.typography.sensitiveInfoSmall.copy(
                    // Force LTR text direction so TOTP digit groups
                    // retain correct order in RTL locales
                    textDirection = TextDirection.Ltr,
                ),
                color = Universal Password ManagerTheme.colorScheme.text.primary,
            )

            AnimateNullableContentVisibility(
                targetState = nextAuthCode,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                label = "AnimateNextCode",
            ) { code ->
                val nextCodeDescription = stringResource(
                    id = Universal Password ManagerString.next_code_x,
                    formatArgs = arrayOf(code),
                )
                Text(
                    modifier = Modifier
                        .testTag(tag = "NextAuthCode")
                        .semantics {
                            contentDescription = nextCodeDescription
                        },
                    text = code
                        .chunked(size = 3) { it.padEnd(length = 3, padChar = ' ') }
                        .joinToString(separator = " "),
                    style = Universal Password ManagerTheme.typography.sensitiveInfoSmall.copy(
                        // Force LTR text direction so TOTP digit groups
                        // retain correct order in RTL locales
                        textDirection = TextDirection.Ltr,
                    ),
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                )
            }
        }

        if (showOverflow) {
            Universal Password ManagerOverflowActionItem(
                menuItemDataList = persistentListOfNotNull(
                    OverflowMenuItemData(
                        text = stringResource(id = Universal Password ManagerString.copy),
                        onClick = { onDropdownMenuClick(VaultDropdownMenuAction.COPY_CODE) },
                    ),
                    OverflowMenuItemData(
                        text = stringResource(id = Universal Password ManagerString.edit),
                        onClick = { onDropdownMenuClick(VaultDropdownMenuAction.EDIT) },
                    ),
                    if (showMoveToUniversal Password Manager) {
                        OverflowMenuItemData(
                            text = stringResource(id = Universal Password ManagerString.copy_to_universalpasswordmanager_vault),
                            onClick = {
                                onDropdownMenuClick(VaultDropdownMenuAction.COPY_TO_UNIVERSAL PASSWORD MANAGER)
                            },
                        )
                    } else {
                        null
                    },
                    OverflowMenuItemData(
                        text = stringResource(id = Universal Password ManagerString.delete_item),
                        onClick = { onDropdownMenuClick(VaultDropdownMenuAction.DELETE) },
                    ),
                ),
                vectorIconRes = Universal Password ManagerDrawable.ic_ellipsis_horizontal,
                testTag = "Options",
            )
        } else {
            Universal Password ManagerStandardIconButton(
                vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                contentDescription = stringResource(id = Universal Password ManagerString.copy),
                onClick = onItemClick,
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview(showBackground = true)
@Composable
private fun VerificationCodeItem_preview() {
    Universal Password ManagerTheme {
        VaultVerificationCodeItem(
            authCode = "123456",
            primaryLabel = "Issuer, AKA Name",
            secondaryLabel = "username@",
            periodSeconds = 30,
            timeLeftSeconds = 15,
            alertThresholdSeconds = 7,
            startIcon = IconData.Local(Universal Password ManagerDrawable.ic_login_item),
            onItemClick = {},
            onDropdownMenuClick = {},
            showOverflow = true,
            modifier = Modifier.padding(horizontal = 16.dp),
            showMoveToUniversal Password Manager = true,
            cardStyle = CardStyle.Full,
            nextAuthCode = null,
        )
    }
}
