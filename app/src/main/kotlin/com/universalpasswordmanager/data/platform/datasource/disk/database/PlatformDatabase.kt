package com.universalpasswordmanager.data.platform.datasource.disk.database

import androidx.room3.AutoMigration
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.universalpasswordmanager.data.platform.datasource.disk.dao.OrganizationEventDao
import com.universalpasswordmanager.data.platform.datasource.disk.entity.OrganizationEventEntity
import com.universalpasswordmanager.data.vault.datasource.disk.convertor.InstantTypeConverter

/**
 * Room database for storing any persisted data for platform data.
 */
@Database(
    entities = [
        OrganizationEventEntity::class,
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@ColumnTypeConverters(InstantTypeConverter::class)
abstract class PlatformDatabase : RoomDatabase() {
    /**
     * Provides the DAO for accessing organization event data.
     */
    abstract fun organizationEventDao(): OrganizationEventDao
}
