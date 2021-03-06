package com.nagwa.files.core.data.source.local.entity

import androidx.room.Ignore
import androidx.room.Relation

/**
 * Used to return the file from the database indicating if it has been downloaded or not
 *
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
data class FileStatusModel(
    var id: Int,
    val type: String = "",
    val url: String = "",
    val name: String = "",
    var showProgress: Boolean = false,
    @Relation(
        parentColumn = "id",
        entityColumn = "fileId"
    )
    var downloadedFileEntity: DownloadedFileEntity? = null
)