package com.nagwa.files.core.data.repository

import com.nagwa.files.core.data.source.local.AppDatabase
import com.nagwa.files.core.data.source.local.dao.FileDao
import com.nagwa.files.core.data.source.local.entity.FileEntity
import com.nagwa.files.core.data.source.remote.ApiInterface
import com.nagwa.files.core.domain.FileModel
import io.reactivex.Single

/**
 * * This repository is responsible for
 * fetching data [Files] from server
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
class FileRepository(private val apiInterface: ApiInterface, private val appDatabase: AppDatabase) :
    FileDataSource {
    /**
     * Load files from network
     */
    override fun getCloudFiles(): Single<List<FileModel>> {
        return apiInterface.fetchFiles()
    }

    /**
     * Load files from local database
     */
    override fun getLocalFiles(): Single<List<FileEntity>> {
        return appDatabase.fileDao.loadAll()
    }

    /**
     * Mark file as downloaded
     */
    override fun setDownloaded(file: FileEntity) {
        appDatabase.fileDao.insert(ArrayList())
    }


}