package com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.util

import .crypto.Kdf
import com.work.model.KdfJson
import com.work.model.KdfTypeJson
import com.work.model.KdfTypeJson.ARGON2_ID
import com.work.model.KdfTypeJson.PBKDF2_SHA256
import com.x8bit.universalpasswordmanager.data.auth.util.KdfParamsConstants.DEFAULT_ARGON2_MEMORY
import com.x8bit.universalpasswordmanager.data.auth.util.KdfParamsConstants.DEFAULT_ARGON2_PARALLELISM

/**
 * Convert a [Kdf] to a [KdfTypeJson].
 */
fun Kdf.toKdfTypeJson(): KdfTypeJson =
    when (this) {
        is Kdf.Argon2id -> ARGON2_ID
        is Kdf.Pbkdf2 -> PBKDF2_SHA256
    }

/**
 * Convert a [Kdf] to [KdfJson]
 */
fun Kdf.toKdfRequestModel(): KdfJson =
    when (this) {
        is Kdf.Argon2id -> KdfJson(
            kdfType = toKdfTypeJson(),
            iterations = iterations.toInt(),
            memory = memory.toInt(),
            parallelism = parallelism.toInt(),
        )

        is Kdf.Pbkdf2 -> KdfJson(
            kdfType = toKdfTypeJson(),
            iterations = iterations.toInt(),
            memory = null,
            parallelism = null,
        )
    }

/**
 * Convert a [KdfJson] to a [Kdf].
 */
fun KdfJson.toKdf(): Kdf =
    when (this.kdfType) {
        ARGON2_ID -> Kdf.Argon2id(
            iterations = iterations.toUInt(),
            memory = memory?.toUInt() ?: DEFAULT_ARGON2_MEMORY.toUInt(),
            parallelism = parallelism?.toUInt() ?: DEFAULT_ARGON2_PARALLELISM.toUInt(),
        )
        PBKDF2_SHA256 -> Kdf.Pbkdf2(iterations = iterations.toUInt())
    }
