package com.nagwa.files.core.interactors.base

import com.nagwa.files.core.data.repository.FileRepository
import com.nagwa.files.core.data.source.local.entity.FileEntity
import io.reactivex.Single
import javax.inject.Inject

/**
 * handles the response that returns data
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
class GetFilesInteractor @Inject constructor(private val fileRepository: FileRepository) :
    BaseInteractor<List<FileEntity>>() {

    override fun buildUseCase(): Single<List<FileEntity>> {
        return fileRepository.getLocalFiles()
    }
}