package com.nagwa.files.dagger.module

import android.app.Application
import androidx.room.Room
import com.nagwa.files.app.ApplicationClass
import com.nagwa.files.core.data.source.local.AppDatabase
import com.nagwa.files.core.data.source.local.dao.FileDao
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
@Module(includes = [NetworkModule::class])
class DatabaseModule {
    @Provides
    @Singleton
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    internal fun providePhotoDao(appDatabase: AppDatabase): FileDao {
        return appDatabase.fileDao
    }

}