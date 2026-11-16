package .authenticator.ui.platform.model

import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import kotlinx.serialization.Serializable

/**
 * Models a relay key to be mapped to an instance of [Universal Password ManagerSnackbarData] being sent
 * between producers and consumers of the data.
 */
@Serializable
enum class SnackbarRelay {
    IMPORT_SUCCESS,
    ITEM_ADDED,
    ITEM_SAVED,
}
