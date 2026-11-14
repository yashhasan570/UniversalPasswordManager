package com.universalpasswordmanager.data.autofill.di

import android.content.Context
import android.view.autofill.AutofillManager
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.data.auth.repository.AuthRepository
import com.universalpasswordmanager.data.autofill.builder.FillResponseBuilder
import com.universalpasswordmanager.data.autofill.builder.FillResponseBuilderImpl
import com.universalpasswordmanager.data.autofill.builder.FilledDataBuilder
import com.universalpasswordmanager.data.autofill.builder.FilledDataBuilderImpl
import com.universalpasswordmanager.data.autofill.builder.SaveInfoBuilder
import com.universalpasswordmanager.data.autofill.builder.SaveInfoBuilderImpl
import com.universalpasswordmanager.data.autofill.manager.AutofillCompletionManager
import com.universalpasswordmanager.data.autofill.manager.AutofillCompletionManagerImpl
import com.universalpasswordmanager.data.autofill.manager.AutofillEnabledManager
import com.universalpasswordmanager.data.autofill.manager.AutofillEnabledManagerImpl
import com.universalpasswordmanager.data.autofill.manager.AutofillTotpManager
import com.universalpasswordmanager.data.autofill.manager.AutofillTotpManagerImpl
import com.universalpasswordmanager.data.autofill.manager.browser.BrowserAutofillDialogManager
import com.universalpasswordmanager.data.autofill.manager.browser.BrowserAutofillDialogManagerImpl
import com.universalpasswordmanager.data.autofill.manager.browser.BrowserThirdPartyAutofillEnabledManager
import com.universalpasswordmanager.data.autofill.manager.browser.BrowserThirdPartyAutofillEnabledManagerImpl
import com.universalpasswordmanager.data.autofill.manager.FillAssistManager
import com.universalpasswordmanager.data.autofill.parser.AutofillParser
import com.universalpasswordmanager.data.autofill.parser.AutofillParserImpl
import com.universalpasswordmanager.data.autofill.processor.AutofillProcessor
import com.universalpasswordmanager.data.autofill.processor.AutofillProcessorImpl
import com.universalpasswordmanager.data.autofill.provider.AutofillCipherProvider
import com.universalpasswordmanager.data.autofill.provider.AutofillCipherProviderImpl
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.universalpasswordmanager.data.platform.manager.FirstTimeActionManager
import com.universalpasswordmanager.data.platform.manager.PolicyManager
import com.universalpasswordmanager.data.platform.manager.ciphermatching.CipherMatchingManager
import com.universalpasswordmanager.data.platform.manager.clipboard.UniversalClipboardManager
import com.universalpasswordmanager.data.platform.manager.event.OrganizationEventManager
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.universalpasswordmanager.data.vault.repository.VaultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides dependencies within the autofill package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AutofillModule {

    @Singleton
    @Provides
    fun providesAutofillManager(
        @ApplicationContext context: Context,
    ): AutofillManager = context.getSystemService(AutofillManager::class.java)

    @Singleton
    @Provides
    fun providesAutofillEnabledManager(): AutofillEnabledManager =
        AutofillEnabledManagerImpl()

    @Singleton
    @Provides
    fun providesBrowserAutofillEnabledManager(): BrowserThirdPartyAutofillEnabledManager =
        BrowserThirdPartyAutofillEnabledManagerImpl()

    @Singleton
    @Provides
    fun providesBrowserAutofillDialogManager(
        autofillEnabledManager: AutofillEnabledManager,
        browserThirdPartyAutofillEnabledManager: BrowserThirdPartyAutofillEnabledManager,
        clock: Clock,
        firstTimeActionManager: FirstTimeActionManager,
        settingsDiskSource: SettingsDiskSource,
    ): BrowserAutofillDialogManager = BrowserAutofillDialogManagerImpl(
        autofillEnabledManager = autofillEnabledManager,
        browserThirdPartyAutofillEnabledManager = browserThirdPartyAutofillEnabledManager,
        clock = clock,
        firstTimeActionManager = firstTimeActionManager,
        settingsDiskSource = settingsDiskSource,
    )

    @Singleton
    @Provides
    fun provideAutofillCompletionManager(
        autofillParser: AutofillParser,
        dispatcherManager: DispatcherManager,
        organizationEventManager: OrganizationEventManager,
        totpManager: AutofillTotpManager,
    ): AutofillCompletionManager =
        AutofillCompletionManagerImpl(
            autofillParser = autofillParser,
            dispatcherManager = dispatcherManager,
            organizationEventManager = organizationEventManager,
            totpManager = totpManager,
        )

    @Singleton
    @Provides
    fun providesAutofillParser(
        settingsRepository: SettingsRepository,
        fillAssistManager: FillAssistManager,
        featureFlagManager: FeatureFlagManager,
    ): AutofillParser =
        AutofillParserImpl(
            settingsRepository = settingsRepository,
            fillAssistManager = fillAssistManager,
            featureFlagManager = featureFlagManager,
        )

    @Singleton
    @Provides
    fun providesAutofillTotpManager(
        clock: Clock,
        clipboardManager: UniversalClipboardManager,
        authRepository: AuthRepository,
        settingsRepository: SettingsRepository,
        vaultRepository: VaultRepository,
    ): AutofillTotpManager =
        AutofillTotpManagerImpl(
            clock = clock,
            clipboardManager = clipboardManager,
            authRepository = authRepository,
            settingsRepository = settingsRepository,
            vaultRepository = vaultRepository,
        )

    @Singleton
    @Provides
    fun providesAutofillCipherProvider(
        authRepository: AuthRepository,
        cipherMatchingManager: CipherMatchingManager,
        vaultRepository: VaultRepository,
        policyManager: PolicyManager,
    ): AutofillCipherProvider =
        AutofillCipherProviderImpl(
            authRepository = authRepository,
            cipherMatchingManager = cipherMatchingManager,
            vaultRepository = vaultRepository,
            policyManager = policyManager,
        )

    @Singleton
    @Provides
    fun providesAutofillProcessor(
        dispatcherManager: DispatcherManager,
        filledDataBuilder: FilledDataBuilder,
        fillResponseBuilder: FillResponseBuilder,
        parser: AutofillParser,
        policyManager: PolicyManager,
        saveInfoBuilder: SaveInfoBuilder,
        settingsRepository: SettingsRepository,
    ): AutofillProcessor =
        AutofillProcessorImpl(
            dispatcherManager = dispatcherManager,
            filledDataBuilder = filledDataBuilder,
            fillResponseBuilder = fillResponseBuilder,
            parser = parser,
            policyManager = policyManager,
            saveInfoBuilder = saveInfoBuilder,
            settingsRepository = settingsRepository,
        )

    @Singleton
    @Provides
    fun providesFillDataBuilder(
        autofillCipherProvider: AutofillCipherProvider,
    ): FilledDataBuilder = FilledDataBuilderImpl(
        autofillCipherProvider = autofillCipherProvider,
    )

    @Singleton
    @Provides
    fun providesFillResponseBuilder(): FillResponseBuilder = FillResponseBuilderImpl()

    @Singleton
    @Provides
    fun providesSaveInfoBuilder(
        settingsRepository: SettingsRepository,
    ): SaveInfoBuilder =
        SaveInfoBuilderImpl(
            settingsRepository = settingsRepository,
        )
}
