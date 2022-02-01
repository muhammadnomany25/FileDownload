package com.nagwa.files.core.data.repository

import com.nagwa.files.core.data.source.local.entity.FileEntity
import com.nagwa.files.core.domain.FileModel
import io.reactivex.Single

/**
 * To make an interaction between [TheFilesRepository] & [GetFilesUseCase]
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
interface FileDataSource {
    fun getCloudFiles(): Single<List<FileModel>>
    fun getLocalFiles(): Single<List<FileEntity>>
    fun setDownloaded(file: FileEntity)
}