package com.nagwa.files.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nagwa.files.core.data.source.local.dao.FileDao
import com.nagwa.files.core.data.source.local.entity.DownloadedFileEntity
import com.nagwa.files.core.data.source.local.entity.FileEntity

/**
 * To manage data items that can be accessed, updated and maintain relationships between them in the local database
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Database(entities = [FileEntity::class, DownloadedFileEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val fileDao: FileDao

    companion object {
        const val DB_NAME = "NagwaFilesDatabase.db"
    }
}