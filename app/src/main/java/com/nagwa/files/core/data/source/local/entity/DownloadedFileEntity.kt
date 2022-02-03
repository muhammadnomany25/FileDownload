package com.nagwa.files.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data Class that represents the downloaded file entity object to be in the local database structure
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Entity(tableName = "Downloaded")
data class DownloadedFileEntity(@PrimaryKey val id: Int, val fileId: Int, var downloadManagerId:Long)