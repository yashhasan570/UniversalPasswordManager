package com.work.model

import com.work.exception.CookieRedirectException
import com.work.exception.LocalNetworkAccessException
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Represents different types of errors that can occur in the Universal Password Manager application.
 */
sealed class Universal Password ManagerError {
    /**
     * An abstract property that holds the underlying throwable that caused the error.
     */
    abstract val throwable: Throwable

    /**
     * Errors related to HTTP requests and responses.
     */
    data class Http(override val throwable: HttpException) : Universal Password ManagerError() {
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
    data class Network(override val throwable: IOException) : Universal Password ManagerError()

    /**
     * Other types of errors not covered by any special cases.
     */
    data class Other(override val throwable: Throwable) : Universal Password ManagerError()
}

/**
 * Convert a [Throwable] into a [Universal Password ManagerError].
 */
fun Throwable.toUniversal Password ManagerError(): Universal Password ManagerError {
    return when (this) {
        // The LocalNetworkAccessException and CookieRedirectException are subclasses of
        // IOException thrown when specific conditions are met. They must be checked before
        // IOException to avoid being classified as a generic Network error. We synthesize an
        // Http error with a JSON body so the exception's message propagates through the existing
        // parseErrorBodyOrNull pipeline used by service-layer recoverCatching blocks.
        is LocalNetworkAccessException,
        is CookieRedirectException,
            -> {
            Universal Password ManagerError.Http(
                throwable = HttpException(
                    Response.error<Any>(
                        HTTP_CODE_BAD_REQUEST,
                        """{"message": "${this.message}"}""".toResponseBody(),
                    ),
                ),
            )
        }

        is IOException -> Universal Password ManagerError.Network(this)
        is HttpException -> Universal Password ManagerError.Http(this)
        else -> Universal Password ManagerError.Other(this)
    }
}

private const val HTTP_CODE_BAD_REQUEST: Int = 400
