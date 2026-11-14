package com.universalpasswordmanager.cxf.manager

import com.universalpasswordmanager.cxf.manager.model.ExportCredentialsResult

/**
 * A manager for completing the Credential Exchange processes.
 */
interface CredentialExchangeCompletionManager {

    /**
     * Complete the Credential Exchange export process with the provided [exportResult].
     *
     * @param exportResult The result of the export operation.
     */
    fun completeCredentialExport(exportResult: ExportCredentialsResult)
}
