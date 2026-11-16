package .authenticator.data.platform.provider

import .annotation.OmitFromCoverage
import .data.repository.model.Environment
import .data.repository.util.baseApiUrl
import .data.repository.util.baseEventsUrl
import .data.repository.util.baseIdentityUrl
import com.work.interceptor.BaseUrlsProvider

/**
 * Default implementation of [BaseUrlsProvider].
 */
@OmitFromCoverage
object BaseUrlsProviderImpl : BaseUrlsProvider {
    override fun getBaseApiUrl(): String = Environment.Prod.Us.baseApiUrl
    override fun getBaseIdentityUrl(): String = Environment.Prod.Us.baseIdentityUrl
    override fun getBaseEventsUrl(): String = Environment.Prod.Us.baseEventsUrl
    override fun getBaseFillAssistUrl(): String? = null
}
