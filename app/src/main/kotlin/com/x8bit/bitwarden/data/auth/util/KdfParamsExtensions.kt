package com.x8bit.universalpasswordmanager.data.auth.util

import .crypto.Kdf
import com.work.model.PreLoginResponseJson

/**
 * Convert [PreLoginResponseJson.KdfParams] to [Kdf] params for use with Universal Password Manager SDK.
 */
fun PreLoginResponseJson.KdfParams.toSdkParams(): Kdf = when (this) {
    is PreLoginResponseJson.KdfParams.Argon2ID -> {
        Kdf.Argon2id(
            iterations = this.iterations,
            memory = this.memory,
            parallelism = this.parallelism,
        )
    }

    is PreLoginResponseJson.KdfParams.Pbkdf2 -> {
        Kdf.Pbkdf2(
            iterations = this.iterations,
        )
    }
}
