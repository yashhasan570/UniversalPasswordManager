package com.universalpasswordmanager.data.autofill.manager

import android.app.Activity
import android.content.Intent
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.vault.CipherView
import com.universalpasswordmanager.data.autofill.builder.FilledDataBuilder
import com.universalpasswordmanager.data.autofill.builder.FilledDataBuilderImpl
import com.universalpasswordmanager.data.autofill.model.AutofillRequest
import com.universalpasswordmanager.data.autofill.parser.AutofillParser
import com.universalpasswordmanager.data.autofill.util.buildDataset
import com.universalpasswordmanager.data.autofill.util.createAutofillSelectionResultIntent
import com.universalpasswordmanager.data.autofill.util.getAutofillAssistStructureOrNull
import com.universalpasswordmanager.data.autofill.util.toAutofillAppInfo
import com.universalpasswordmanager.data.autofill.util.toAutofillCipherProvider
import com.universalpasswordmanager.data.platform.manager.event.OrganizationEventManager
import com.universalpasswordmanager.data.platform.manager.model.OrganizationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Primary implementation of [AutofillCompletionManager].
 */
@Suppress("LongParameterList")
class AutofillCompletionManagerImpl(
    private val autofillParser: AutofillParser,
    dispatcherManager: DispatcherManager,
    private val filledDataBuilderProvider: (CipherView) -> FilledDataBuilder =
        { createSingleItemFilledDataBuilder(cipherView = it) },
    private val organizationEventManager: OrganizationEventManager,
    private val totpManager: AutofillTotpManager,
) : AutofillCompletionManager {
    private val mainScope = CoroutineScope(dispatcherManager.main)

    override fun completeAutofill(
        activity: Activity,
        cipherView: CipherView,
    ) {
        val autofillAppInfo = activity.toAutofillAppInfo()
        val assistStructure = activity
            .intent
            ?.getAutofillAssistStructureOrNull()
            ?: run {
                Timber.w("Assist structure not found")
                activity.cancelAndFinish()
                return
            }

        val autofillRequest = autofillParser
            .parse(
                autofillAppInfo = autofillAppInfo,
                assistStructure = assistStructure,
            )
        if (autofillRequest !is AutofillRequest.Fillable) {
            Timber.w("Request is not fillable")
            activity.cancelAndFinish()
            return
        }

        val fillDataBuilder = filledDataBuilderProvider(cipherView)
        // We'll launch a coroutine here but this code will technically run synchronously given
        // how we've constructed a single-item AutofillCipherProvider.
        mainScope.launch {
            val dataset = fillDataBuilder
                .build(autofillRequest)
                .filledPartitions
                .firstOrNull()
                ?.buildDataset(
                    autofillAppInfo = autofillAppInfo,
                    authIntentSender = null,
                )
                ?: run {
                    Timber.w("Dataset not found")
                    activity.cancelAndFinish()
                    return@launch
                }
            totpManager.tryCopyTotpToClipboard(cipherView = cipherView)
            val resultIntent = createAutofillSelectionResultIntent(dataset)
            Timber.d("Autofill success")
            activity.setResultAndFinish(resultIntent = resultIntent)
            cipherView.id?.let {
                organizationEventManager.trackEvent(
                    event = OrganizationEvent.CipherClientAutoFilled(cipherId = it),
                )
            }
        }
    }
}

private fun createSingleItemFilledDataBuilder(
    cipherView: CipherView,
): FilledDataBuilder =
    FilledDataBuilderImpl(
        autofillCipherProvider = cipherView.toAutofillCipherProvider(),
    )

private fun Activity.cancelAndFinish() {
    this.setResult(Activity.RESULT_CANCELED)
    this.finish()
}

private fun Activity.setResultAndFinish(resultIntent: Intent) {
    this.setResult(Activity.RESULT_OK, resultIntent)
    this.finish()
}
