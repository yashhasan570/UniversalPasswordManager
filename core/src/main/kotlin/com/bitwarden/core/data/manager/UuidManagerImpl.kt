package .core.data.manager

import .annotation.OmitFromCoverage
import java.util.UUID

/**
 * Default implementation of [UuidManager] that generates random UUIDs.
 */
@OmitFromCoverage
class UuidManagerImpl : UuidManager {
    override fun generateUuid(): String = UUID.randomUUID().toString()
}
