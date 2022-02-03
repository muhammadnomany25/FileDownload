package com.nagwa.files.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data Class that represents the entity object to be in the local database structure
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Entity(tableName = "File")
data class FileEntity(
    @PrimaryKey var id: Int,
    val type: String = "",
    val url: String = "",
    val name: String = "",
    var showProgress: Boolean = false
)