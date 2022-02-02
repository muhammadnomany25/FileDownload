package com.nagwa.files.core.data.source.remote

import com.nagwa.files.core.domain.FileModel
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Remote endpoints of the network requests
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
interface ApiInterface {

    @GET("/movies")
    fun fetchFiles(): Single<List<FileModel>>
}