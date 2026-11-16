package .authenticator.data.auth.datasource.disk.di

import android.content.SharedPreferences
import .authenticator.data.auth.datasource.disk.AuthDiskSource
import .authenticator.data.auth.datasource.disk.AuthDiskSourceImpl
import .data.datasource.disk.di.EncryptedPreferences
import .data.datasource.disk.di.KeystoreEncryptedPreferences
import .data.datasource.disk.di.UnencryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides persistence-related dependencies in the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthDiskModule {

    @Provides
    @Singleton
    fun provideAuthDiskSource(
        @EncryptedPreferences encryptedSharedPreferences: SharedPreferences,
        @KeystoreEncryptedPreferences keystoreEncryptedPreferences: SharedPreferences,
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
    ): AuthDiskSource =
        AuthDiskSourceImpl(
            encryptedSharedPreferences = encryptedSharedPreferences,
            keystoreEncryptedPreferences = keystoreEncryptedPreferences,
            sharedPreferences = sharedPreferences,
        )
}
