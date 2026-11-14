package com.universalpasswordmanager.network.model

import com.universalpasswordmanager.network.exception.CookieRedirectException
import com.universalpasswordmanager.network.exception.LocalNetworkAccessException
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Represents different types of errors that can occur in the Universal application.
 */
sealed class UniversalError {
    /**
     * An abstract property that holds the underlying throwable that caused the error.
     */
    abstract val throwable: Throwable

    /**
     * Errors related to HTTP requests and responses.
     */
    data class Http(override val throwable: HttpException) : UniversalError() {
        /**
         * The error code of the HTTP response.
         */
        val code: Int get() = throwable.code()

        /**
         * The response body of the HTTP response.
         */
        val responseBodyString: String? by lazy {
            throwable.response()?.errorBody()?.string()
        }
    }

    /**
     * Errors related to network.
     */
    data class Network(override val throwable: IOException) : UniversalError()

    /**
     * Other types of errors not covered by any special cases.
     */
    data class Other(override val throwable: Throwable) : UniversalError()
}

/**
 * Convert a [Throwable] into a [UniversalError].
 */
fun Throwable.toUniversalError(): UniversalError {
    return when (this) {
        // The LocalNetworkAccessException and CookieRedirectException are subclasses of
        // IOException thrown when specific conditions are met. They must be checked before
        // IOException to avoid being classified as a generic Network error. We synthesize an
        // Http error with a JSON body so the exception's message propagates through the existing
        // parseErrorBodyOrNull pipeline used by service-layer recoverCatching blocks.
        is LocalNetworkAccessException,
        is CookieRedirectException,
            -> {
            UniversalError.Http(
                throwable = HttpException(
                    Response.error<Any>(
                        HTTP_CODE_BAD_REQUEST,
                        """{"message": "${this.message}"}""".toResponseBody(),
                    ),
                ),
            )
        }

        is IOException -> UniversalError.Network(this)
        is HttpException -> UniversalError.Http(this)
        else -> UniversalError.Other(this)
    }
}

private const val HTTP_CODE_BAD_REQUEST: Int = 400
