package com.nagwa.files.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nagwa.files.core.data.mapper.FileMapper
import com.nagwa.files.core.data.source.local.AppDatabase
import com.nagwa.files.core.interactors.base.GetFilesInteractor
import com.nagwa.files.utils.exceptions.ApiException
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
    private var disposable: Disposable? = null
    var fileUiModel = FileUiModel()
    private val fileMapper: FileMapper = FileMapper()

    /**
     * Load cloud files
     */
    fun loadFiles() {
        getFilesInteractor.execute(onSuccess = {
            appDatabase.fileDao.insert(fileMapper.mapToEntityList(it))
            loadAfterCaching()
        }, onError = {
            fileUiModel = FileUiModel(status = MutableLiveData(Status.ERROR.value))
            ApiException(it.localizedMessage)
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
                fileUiModel =
                    FileUiModel(MutableLiveData(result), MutableLiveData(Status.SUCCESS.value))
            }, {
                fileUiModel = FileUiModel(status = MutableLiveData(Status.ERROR.value))
                ApiException(it.localizedMessage)
            })
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