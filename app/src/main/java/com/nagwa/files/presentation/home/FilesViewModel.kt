package com.nagwa.files.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nagwa.files.core.data.mapper.FileMapper
import com.nagwa.files.core.data.source.local.AppDatabase
import com.nagwa.files.core.data.source.local.entity.DownloadedFileEntity
import com.nagwa.files.core.interactors.base.GetFilesInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
class FilesViewModel @Inject constructor(
    private val getFilesInteractor: GetFilesInteractor,
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val TAG = FilesViewModel::class.java.simpleName
    private var disposable: Disposable? = null
    var fileUiModel = FileUiModel()
    private val fileMapper: FileMapper = FileMapper()

    /**
     * Load cloud files
     */
    fun loadFiles() {
        fileUiModel.status.value = Status.LOADING.value
        getFilesInteractor.execute(onSuccess = {
            appDatabase.fileDao.insert(fileMapper.mapToEntityList(it))
            loadAfterCaching()
        }, onError = {
            fileUiModel.status.value = Status.ERROR.value
            Log.e(TAG, it.localizedMessage)
        })
    }

    /**
     * Loading data from local database
     */
    private fun loadAfterCaching() {
        dispose()
        disposable = appDatabase.fileDao.loadAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val result = if (it.isNullOrEmpty()) ArrayList() else it
                fileUiModel.filesData.value = result
                fileUiModel.status.value = Status.SUCCESS.value
            }, {
                fileUiModel.status.value = Status.ERROR.value
                Log.e(TAG, it.localizedMessage)
            })
    }

    /**
     * Mark file as downloaded in the db
     */
    fun insertDownloaded(downloadedFileEntity: DownloadedFileEntity) {
        appDatabase.fileDao.insertDownloaded(downloadedFileEntity)
    }

    /**
     * Clear disposables if exist
     */
    fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}