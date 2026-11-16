package .authenticator.data.authenticator.datasource.disk

import .authenticator.data.authenticator.datasource.disk.dao.ItemDao
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemEntity
import .core.data.repository.util.bufferedMutableSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

/**
 * Default implementation of [AuthenticatorDiskSource].
 */
class AuthenticatorDiskSourceImpl @Inject constructor(
    private val itemDao: ItemDao,
) : AuthenticatorDiskSource {

    private val forceItemsFlow = bufferedMutableSharedFlow<List<AuthenticatorItemEntity>>()

    override suspend fun saveItem(vararg authenticatorItem: AuthenticatorItemEntity) {
        itemDao.insert(*authenticatorItem)
    }

    override fun getItems(): Flow<List<AuthenticatorItemEntity>> = merge(
        forceItemsFlow,
        itemDao.getAllItems(),
    )

    override suspend fun deleteItem(itemId: String) {
        itemDao.deleteItem(itemId)
    }
}
