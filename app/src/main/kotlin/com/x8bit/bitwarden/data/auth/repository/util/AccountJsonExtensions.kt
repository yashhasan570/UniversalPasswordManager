package com.x8bit.universalpasswordmanager.data.auth.repository.util

import .crypto.Kdf
import com.work.model.KdfJson
import com.work.model.KdfTypeJson
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.model.AccountJson
import com.x8bit.universalpasswordmanager.data.auth.util.KdfParamsConstants

/**
 * Convert [AccountJson.Profile] to [Kdf] params for use with Universal Password Manager SDK.
 */
fun AccountJson.Profile.toSdkParams(): Kdf {
    return when (this.kdfType) {
        KdfTypeJson.ARGON2_ID -> Kdf.Argon2id(
            iterations = (kdfIterations ?: KdfParamsConstants.DEFAULT_ARGON2_ITERATIONS).toUInt(),
            memory = (kdfMemory ?: KdfParamsConstants.DEFAULT_ARGON2_MEMORY).toUInt(),
            parallelism = (kdfParallelism ?: KdfParamsConstants.DEFAULT_ARGON2_PARALLELISM)
                .toUInt(),
        )

        KdfTypeJson.PBKDF2_SHA256 -> Kdf.Pbkdf2(
            iterations = (kdfIterations ?: KdfParamsConstants.DEFAULT_PBKDF2_ITERATIONS).toUInt(),
        )

        else -> Kdf.Pbkdf2(iterations = KdfParamsConstants.DEFAULT_PBKDF2_ITERATIONS.toUInt())
    }
}

/**
 * Convert [AccountJson.Profile] to [KdfJson] params for use with Universal Password Manager network requests.
 */
fun AccountJson.Profile.toKdfRequestModel(): KdfJson =
    when (val kdfType = this.kdfType ?: KdfTypeJson.PBKDF2_SHA256) {
        KdfTypeJson.ARGON2_ID -> KdfJson(
            kdfType = kdfType,
            iterations = this.kdfIterations ?: KdfParamsConstants.DEFAULT_ARGON2_ITERATIONS,
            memory = this.kdfMemory ?: KdfParamsConstants.DEFAULT_ARGON2_MEMORY,
            parallelism = this.kdfParallelism ?: KdfParamsConstants.DEFAULT_ARGON2_PARALLELISM,
        )

        KdfTypeJson.PBKDF2_SHA256 -> KdfJson(
            kdfType = kdfType,
            iterations = this.kdfIterations ?: KdfParamsConstants.DEFAULT_PBKDF2_ITERATIONS,
            memory = this.kdfMemory,
            parallelism = this.kdfParallelism,
        )
    }
