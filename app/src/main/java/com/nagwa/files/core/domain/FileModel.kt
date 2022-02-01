package com.nagwa.files.core.domain

/**
 * Data Class that represents the returned file response from the api
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
data class FileModel(
    val id: Int = 0,
    val type: String = "",
    val url: String = "",
    val name: String = ""
)