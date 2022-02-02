package com.nagwa.files.presentation.home

import androidx.lifecycle.MutableLiveData
import com.nagwa.files.core.data.source.local.entity.FileStatusModel

/**
 * Used to save livedata and it state (Success, Error, Loading)
 *
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
data class FileUiModel(
    var filesData: MutableLiveData<List<FileStatusModel>> = MutableLiveData<List<FileStatusModel>>(),
    var status: MutableLiveData<Int> = MutableLiveData()
)

/**
 * Enum class that represents the different status of requests
 */
enum class Status(var value: Int) {
    LOADING(1), SUCCESS(2), ERROR(3)
}


/**
 * Enum class that represents the different file types
 */
enum class FileType(var value: String) {
    VIDEO("VIDEO"), PDF("PDF")
}