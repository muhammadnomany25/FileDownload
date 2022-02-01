package com.nagwa.files.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nagwa.files.core.data.source.local.entity.FileEntity
import io.reactivex.Single

/**
 * it provides access to [File] through database
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fileList: List<FileEntity>): List<Long>

    @Query("SELECT * FROM File")
    fun loadAll(): Single<List<FileEntity>>
}