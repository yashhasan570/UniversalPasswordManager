package com.universalpasswordmanager.data.platform.manager.clipboard

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.getSystemService
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.core.data.manager.toast.ToastManager
import com.universalpasswordmanager.core.util.isBuildVersionAtLeast
import com.universalpasswordmanager.ui.platform.base.util.toAnnotatedString
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import java.util.concurrent.TimeUnit

/**
 * Default implementation of the [UniversalClipboardManager] interface.
 */
@OmitFromCoverage
class UniversalClipboardManagerImpl(
    private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val toastManager: ToastManager,
) : UniversalClipboardManager {
    private val clipboardManager: ClipboardManager = requireNotNull(context.getSystemService())

    private val clearClipboardFrequencySeconds: Int?
        get() = settingsRepository.clearClipboardFrequency.frequencySeconds

    override fun setText(
        text: AnnotatedString,
        isSensitive: Boolean,
        toastDescriptorOverride: String?,
    ) {
        clipboardManager.setPrimaryClip(
            ClipData
                .newPlainText("", text)
                .apply {
                    description.extras = PersistableBundle().apply {
                        this.putBoolean(
                            if (isBuildVersionAtLeast(version = Build.VERSION_CODES.TIRAMISU)) {
                                ClipDescription.EXTRA_IS_SENSITIVE
                            } else {
                                "android.content.extra.IS_SENSITIVE"
                            },
                            isSensitive,
                        )
                    }
                },
        )
        if (!isBuildVersionAtLeast(version = Build.VERSION_CODES.TIRAMISU)) {
            val descriptor = toastDescriptorOverride
                ?.let { context.resources.getString(UniversalString.value_has_been_copied, it) }
                ?: context.resources.getString(
                    UniversalString.value_has_been_copied,
                    context.resources.getString(UniversalString.value),
                )
            toastManager.show(message = descriptor)
        }

        val frequency = clearClipboardFrequencySeconds ?: return
        val clearClipboardRequest: OneTimeWorkRequest =
            OneTimeWorkRequest
                .Builder(ClearClipboardWorker::class.java)
                .setInitialDelay(frequency.toLong(), TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "ClearClipboard",
            ExistingWorkPolicy.REPLACE,
            clearClipboardRequest,
        )
    }

    override fun setText(text: String, isSensitive: Boolean, toastDescriptorOverride: String?) {
        setText(text.toAnnotatedString(), isSensitive, toastDescriptorOverride)
    }

    override fun setText(text: String, isSensitive: Boolean, toastDescriptorOverride: Text) {
        setText(
            text.toAnnotatedString(),
            isSensitive,
            toastDescriptorOverride.toString(context.resources),
        )
    }

    override fun setText(text: Text, isSensitive: Boolean, toastDescriptorOverride: String?) {
        setText(text.toString(context.resources), isSensitive, toastDescriptorOverride)
    }
}
