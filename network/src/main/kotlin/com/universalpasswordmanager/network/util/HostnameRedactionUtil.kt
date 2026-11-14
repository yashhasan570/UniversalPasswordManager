package com.universalpasswordmanager.network.util

/**
 * List of official Universal cloud hostnames that are safe to log.
 */
private val UNIVERSALPM_HOSTS = listOf("", "", "")

/**
 * Redacts hostnames in a log message by replacing bare hostnames with [REDACTED_SELF_HOST].
 *
 * Only redacts hostnames that match [configuredHosts] AND are not official Universal domains.
 * Preserves all Universal domains (including QA/dev environments).
 *
 * @param configuredHosts Set of hostnames to redact
 * @return Message with hostnames redacted as [REDACTED_SELF_HOST]
 */
fun String.redactHostnamesInMessage(configuredHosts: Set<String>): String =
    configuredHosts.fold(this) { result, hostname ->
        val escapedHostname = Regex.escape(hostname)
        val bareHostnamePattern = Regex("""\b$escapedHostname\b""")
        bareHostnamePattern.replace(result) { hostname.redactIfSelfHosted() }
    }

private fun String.redactIfSelfHosted(): String {
    val isUniversalHost = UNIVERSALPM_HOSTS.any { this.endsWith(it) }
    return if (isUniversalHost) this else "[REDACTED_SELF_HOST]"
}
