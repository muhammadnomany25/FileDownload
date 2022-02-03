package com.nagwa.files.presentation.home

import com.nagwa.files.core.data.source.local.entity.FileStatusModel

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
interface IFileDownload {
    fun downloadFile(fileStatusModel: FileStatusModel)
    fun openFile(fileStatusModel: FileStatusModel)
}