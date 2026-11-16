package .authenticator.data.authenticator.repository

import android.net.Uri
import android.text.TextUtils
import app.cash.turbine.test
import .authenticator.data.authenticator.datasource.disk.util.FakeAuthenticatorDiskSource
import .authenticator.data.authenticator.datasource.entity.createMockAuthenticatorItemEntity
import .authenticator.data.authenticator.manager.TotpCodeManager
import .authenticator.data.authenticator.manager.model.VerificationCodeItem
import .authenticator.data.authenticator.manager.util.createMockAuthenticatorItem
import .authenticator.data.authenticator.repository.model.AuthenticatorItem
import .authenticator.data.authenticator.repository.model.CreateItemResult
import .authenticator.data.authenticator.repository.model.DeleteItemResult
import .authenticator.data.authenticator.repository.model.ExportDataResult
import .authenticator.data.authenticator.repository.model.SharedVerificationCodesState
import .authenticator.data.authenticator.repository.model.TotpCodeResult
import .authenticator.data.authenticator.repository.util.toAuthenticatorItems
import .authenticator.data.platform.manager.imports.ImportManager
import .authenticator.data.platform.manager.imports.model.ImportDataResult
import .authenticator.data.platform.manager.imports.model.ImportFileFormat
import .authenticator.data.platform.repository.SettingsRepository
import .authenticator.ui.platform.feature.settings.export.model.ExportVaultFormat
import .authenticatorbridge.manager.AuthenticatorBridgeManager
import .authenticatorbridge.manager.model.AccountSyncState
import .authenticatorbridge.model.SharedAccountData
import .core.data.manager.dispatcher.FakeDispatcherManager
import .core.data.repository.model.DataState
import .core.data.util.asFailure
import .core.data.util.asSuccess
import .core.data.util.mockBuilder
import .data.manager.file.FileManager
import .ui.platform.model.FileData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticatorRepositoryTest {

    private val mutableAccountSyncStateFlow =
        MutableStateFlow<AccountSyncState>(AccountSyncState.Loading)
    private val fakeAuthenticatorDiskSource = FakeAuthenticatorDiskSource()
    private val mockAuthenticatorBridgeManager: AuthenticatorBridgeManager = mockk {
        every { accountSyncStateFlow } returns mutableAccountSyncStateFlow
    }
    private val mockTotpCodeManager = mockk<TotpCodeManager>(relaxed = true)
    private val mockFileManager = mockk<FileManager>()
    private val mockImportManager = mockk<ImportManager>()
    private val mockDispatcherManager = FakeDispatcherManager()
    private val settingsRepository: SettingsRepository = mockk {
        every { previouslySyncedUniversal Password ManagerAccountIds } returns emptySet()
    }

    private val authenticatorRepository = AuthenticatorRepositoryImpl(
        authenticatorDiskSource = fakeAuthenticatorDiskSource,
        authenticatorBridgeManager = mockAuthenticatorBridgeManager,
        totpCodeManager = mockTotpCodeManager,
        fileManager = mockFileManager,
        importManager = mockImportManager,
        dispatcherManager = mockDispatcherManager,
        settingRepository = settingsRepository,
    )

    @BeforeEach
    fun setup() {
        mockkStatic(Uri::class)
        mockkStatic(List<SharedAccountData.Account>::toAuthenticatorItems)

        // Configure Uri.Builder for export tests that call toOtpAuthUriString()
        val mockBuiltUri = mockk<Uri>(relaxed = true)
        every { mockBuiltUri.toString() } returns "otpauth://totp/mockIssuer:mockAccountName"

        mockkConstructor(Uri.Builder::class)
        mockBuilder<Uri.Builder> { it.scheme(any()) }
        mockBuilder<Uri.Builder> { it.authority(any()) }
        mockBuilder<Uri.Builder> { it.appendPath(any()) }
        mockBuilder<Uri.Builder> { it.appendQueryParameter(any(), any()) }
        every { anyConstructed<Uri.Builder>().build() } returns mockBuiltUri
        mockkStatic(TextUtils::htmlEncode)
        every { TextUtils.htmlEncode(any()) } returns ""
    }

    @AfterEach
    fun teardown() {
        unmockkStatic(Uri::class)
        unmockkStatic(List<SharedAccountData.Account>::toAuthenticatorItems)
        unmockkConstructor(Uri.Builder::class)
        unmockkStatic(TextUtils::htmlEncode)
    }

    @Test
    fun `ciphersStateFlow should emit sorted authenticator items when disk source changes`() =
        runTest {
            val mockItem = createMockAuthenticatorItemEntity(1)
            fakeAuthenticatorDiskSource.saveItem(mockItem)
            assertEquals(
                DataState.Loaded(listOf(mockItem)),
                authenticatorRepository.ciphersStateFlow.value,
            )
        }

    @Suppress("MaxLineLength")
    @Test
    fun `sharedCodesStateFlow should emit AppNotInstalled when authenticatorBridgeManager emits AppNotInstalled`() =
        runTest {
            mutableAccountSyncStateFlow.value = AccountSyncState.AppNotInstalled
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(SharedVerificationCodesState.AppNotInstalled, awaitItem())
            }
        }

    @Suppress("MaxLineLength")
    @Test
    fun `sharedCodesStateFlow should emit SyncNotEnabled when authenticatorBridgeManager emits SyncNotEnabled`() =
        runTest {
            mutableAccountSyncStateFlow.value = AccountSyncState.SyncNotEnabled
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(SharedVerificationCodesState.SyncNotEnabled, awaitItem())
            }
        }

    @Test
    fun `sharedCodesStateFlow should emit Error when authenticatorBridgeManager emits Error`() =
        runTest {
            mutableAccountSyncStateFlow.value = AccountSyncState.Error
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(SharedVerificationCodesState.Error, awaitItem())
            }
        }

    @Test
    fun `sharedCodesStateFlow should emit Loading when authenticatorBridgeManager emits Loading`() =
        runTest {
            mutableAccountSyncStateFlow.value = AccountSyncState.Loading
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(SharedVerificationCodesState.Loading, awaitItem())
            }
        }

    @Suppress("MaxLineLength")
    @Test
    fun `sharedCodesStateFlow should emit OsVersionNotSupported when authenticatorBridgeManager emits OsVersionNotSupported`() =
        runTest {
            mutableAccountSyncStateFlow.value = AccountSyncState.OsVersionNotSupported
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(SharedVerificationCodesState.OsVersionNotSupported, awaitItem())
            }
        }

    @Test
    fun `sharedCodesStateFlow should emit Success when authenticatorBridgeManager emits Success`() =
        runTest {
            val sharedAccounts = emptyList<SharedAccountData.Account>()
            val authenticatorItems = emptyList<AuthenticatorItem>()
            val verificationCodes = mockk<List<VerificationCodeItem>>()
            every { sharedAccounts.toAuthenticatorItems() } returns authenticatorItems
            every {
                mockTotpCodeManager.getTotpCodesFlow(authenticatorItems)
            } returns MutableStateFlow(verificationCodes)
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(SharedVerificationCodesState.Loading, awaitItem())
                mutableAccountSyncStateFlow.value = AccountSyncState.Success(sharedAccounts)
                assertEquals(SharedVerificationCodesState.Success(verificationCodes), awaitItem())
            }
        }

    @Suppress("MaxLineLength")
    @Test
    fun `sharedCodesStateFlow should filter out items with empty otpUri when authenticatorBridgeManager emits Success`() =
        runTest {
            val sharedAccounts = emptyList<SharedAccountData.Account>()
            val itemWithUri = createMockAuthenticatorItem(number = 1)
            val itemWithEmptyUri = createMockAuthenticatorItem(
                number = 2,
                otpUri = "",
            )
            val allItems = listOf(itemWithUri, itemWithEmptyUri)
            val filteredItems = listOf(itemWithUri)
            val verificationCodes = mockk<List<VerificationCodeItem>>()
            every { sharedAccounts.toAuthenticatorItems() } returns allItems
            every {
                mockTotpCodeManager.getTotpCodesFlow(filteredItems)
            } returns MutableStateFlow(verificationCodes)
            authenticatorRepository.sharedCodesStateFlow.test {
                assertEquals(
                    SharedVerificationCodesState.Loading,
                    awaitItem(),
                )
                mutableAccountSyncStateFlow.value = AccountSyncState.Success(sharedAccounts)
                assertEquals(
                    SharedVerificationCodesState.Success(verificationCodes),
                    awaitItem(),
                )
                verify(exactly = 1) {
                    mockTotpCodeManager.getTotpCodesFlow(filteredItems)
                }
            }
        }

    @Test
    @Suppress("MaxLineLength")
    fun `firstTimeAccountSyncFlow should emit the first time an account syncs and update SettingsRepository`() =
        runTest {
            every { settingsRepository.previouslySyncedUniversal Password ManagerAccountIds = setOf("1") } just runs
            val sharedAccounts = listOf(
                SharedAccountData.Account(
                    userId = "1",
                    name = null,
                    email = "test@test.com",
                    environmentLabel = "",
                    cipherData = emptyList(),
                ),
            )
            authenticatorRepository.firstTimeAccountSyncFlow.test {
                mutableAccountSyncStateFlow.value = AccountSyncState.Success(sharedAccounts)
                awaitItem()
            }
            verify { settingsRepository.previouslySyncedUniversal Password ManagerAccountIds = setOf("1") }
        }

    @Test
    @Suppress("MaxLineLength")
    fun `firstTimeAccountSyncFlow should not emit if a synced account is already in previouslySyncedUniversal Password ManagerAccountIds`() =
        runTest {
            every { settingsRepository.previouslySyncedUniversal Password ManagerAccountIds } returns setOf("1")
            val sharedAccounts = listOf(
                SharedAccountData.Account(
                    userId = "1",
                    name = null,
                    email = "test@test.com",
                    environmentLabel = "",
                    cipherData = emptyList(),
                ),
            )
            authenticatorRepository.firstTimeAccountSyncFlow.test {
                mutableAccountSyncStateFlow.value = AccountSyncState.Success(sharedAccounts)
                expectNoEvents()
            }
        }

    @Test
    fun `getItemStateFlow with valid itemId should emit item when found`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)
        fakeAuthenticatorDiskSource.saveItem(mockItem)

        authenticatorRepository.getItemStateFlow(mockItem.id).test {
            assertEquals(DataState.Loaded(mockItem), awaitItem())
        }
    }

    @Test
    fun `getItemStateFlow with invalid itemId should emit null`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)
        fakeAuthenticatorDiskSource.saveItem(mockItem)

        authenticatorRepository.getItemStateFlow("invalid-id").test {
            assertEquals(DataState.Loaded(null), awaitItem())
        }
    }

    @Test
    fun `getItemStateFlow should emit Loaded with null for non-existent item`() = runTest {
        authenticatorRepository.getItemStateFlow("any-id").test {
            assertEquals(DataState.Loaded(null), awaitItem())
        }
    }

    @Test
    fun `emitTotpCodeResult should emit to totpCodeFlow`() = runTest {
        val expectedResult = TotpCodeResult.TotpCodeScan("test-code")

        authenticatorRepository.totpCodeFlow.test {
            authenticatorRepository.emitTotpCodeResult(expectedResult)
            assertEquals(expectedResult, awaitItem())
        }
    }

    @Test
    fun `createItem with valid item should return Success`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)

        val result = authenticatorRepository.createItem(mockItem)

        assertEquals(CreateItemResult.Success, result)
    }

    @Test
    fun `addItems with multiple items should return Success`() = runTest {
        val mockItem1 = createMockAuthenticatorItemEntity(1)
        val mockItem2 = createMockAuthenticatorItemEntity(2)

        val result = authenticatorRepository.addItems(mockItem1, mockItem2)

        assertEquals(CreateItemResult.Success, result)
    }

    @Test
    fun `hardDeleteItem with valid id should return Success`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)

        val result = authenticatorRepository.hardDeleteItem(mockItem.id)

        assertEquals(DeleteItemResult.Success, result)
    }

    @Test
    fun `exportVaultData with JSON format should write to fileUri`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)
        fakeAuthenticatorDiskSource.saveItem(mockItem)
        val mockUri = mockk<Uri>()

        coEvery {
            mockFileManager.stringToUri(fileUri = mockUri, dataString = any())
        } returns true

        val result = authenticatorRepository.exportVaultData(
            format = ExportVaultFormat.JSON,
            fileUri = mockUri,
        )

        assertEquals(ExportDataResult.Success, result)
        coVerify { mockFileManager.stringToUri(fileUri = mockUri, dataString = any()) }
    }

    @Test
    fun `exportVaultData with JSON format failure should return Error`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)
        fakeAuthenticatorDiskSource.saveItem(mockItem)
        val mockUri = mockk<Uri>()

        coEvery {
            mockFileManager.stringToUri(fileUri = mockUri, dataString = any())
        } returns false

        val result = authenticatorRepository.exportVaultData(
            format = ExportVaultFormat.JSON,
            fileUri = mockUri,
        )

        assertEquals(ExportDataResult.Error, result)
    }

    @Test
    fun `exportVaultData with CSV format should write to fileUri`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)
        fakeAuthenticatorDiskSource.saveItem(mockItem)
        val mockUri = mockk<Uri>()

        coEvery {
            mockFileManager.stringToUri(fileUri = mockUri, dataString = any())
        } returns true

        val result = authenticatorRepository.exportVaultData(
            format = ExportVaultFormat.CSV,
            fileUri = mockUri,
        )

        assertEquals(ExportDataResult.Success, result)
        coVerify { mockFileManager.stringToUri(fileUri = mockUri, dataString = any()) }
    }

    @Test
    fun `exportVaultData with CSV format failure should return Error`() = runTest {
        val mockItem = createMockAuthenticatorItemEntity(1)
        fakeAuthenticatorDiskSource.saveItem(mockItem)
        val mockUri = mockk<Uri>()

        coEvery {
            mockFileManager.stringToUri(fileUri = mockUri, dataString = any())
        } returns false

        val result = authenticatorRepository.exportVaultData(
            format = ExportVaultFormat.CSV,
            fileUri = mockUri,
        )

        assertEquals(ExportDataResult.Error, result)
    }

    @Test
    fun `importVaultData with valid data should return Success`() = runTest {
        val mockUri = mockk<Uri>()
        val mockFileData = FileData(
            fileName = "test.json",
            uri = mockUri,
            sizeBytes = 100L,
        )
        val testByteArray = byteArrayOf(1, 2, 3)

        coEvery {
            mockFileManager.uriToByteArray(mockUri)
        } returns testByteArray.asSuccess()

        coEvery {
            mockImportManager.import(
                importFileFormat = ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON,
                byteArray = testByteArray,
            )
        } returns ImportDataResult.Success

        val result = authenticatorRepository.importVaultData(
            format = ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON,
            fileData = mockFileData,
        )

        assertEquals(ImportDataResult.Success, result)
    }

    @Test
    fun `importVaultData with FileManager failure should return Error`() = runTest {
        val mockUri = mockk<Uri>()
        val mockFileData = FileData(
            fileName = "test.json",
            uri = mockUri,
            sizeBytes = 100L,
        )

        coEvery {
            mockFileManager.uriToByteArray(mockUri)
        } returns RuntimeException("File read error").asFailure()

        val result = authenticatorRepository.importVaultData(
            format = ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON,
            fileData = mockFileData,
        )

        assertEquals(ImportDataResult.Error(), result)
    }

    @Test
    fun `importVaultData with ImportManager failure should return Error`() = runTest {
        val mockUri = mockk<Uri>()
        val mockFileData = FileData(
            fileName = "test.json",
            uri = mockUri,
            sizeBytes = 100L,
        )
        val testByteArray = byteArrayOf(1, 2, 3)

        coEvery {
            mockFileManager.uriToByteArray(mockUri)
        } returns testByteArray.asSuccess()

        coEvery {
            mockImportManager.import(
                importFileFormat = ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON,
                byteArray = testByteArray,
            )
        } returns ImportDataResult.Error()

        val result = authenticatorRepository.importVaultData(
            format = ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON,
            fileData = mockFileData,
        )

        assertEquals(ImportDataResult.Error(), result)
    }
}
