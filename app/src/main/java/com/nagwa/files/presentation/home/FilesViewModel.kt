package com.nagwa.files.presentation.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nagwa.files.core.data.mapper.FileMapper
import com.nagwa.files.core.data.source.local.AppDatabase
import com.nagwa.files.core.data.source.local.entity.FileStatusModel
import com.nagwa.files.core.interactors.base.GetFilesInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
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
    val filesData = MutableLiveData<List<FileStatusModel>>()
    private val fileMapper: FileMapper = FileMapper()

    fun loadFiles() {
        getFilesInteractor.execute(onSuccess = {
            appDatabase.fileDao.insert(fileMapper.mapToEntityList(it))
            loadAfterCaching()
        }, onError = {

        }, onFinished = {

        })
    }

    private fun loadAfterCaching() {
        appDatabase.fileDao.loadAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                filesData.value = if (it.isNullOrEmpty()) ArrayList() else it
                Log.e("responseList", Gson().toJson(filesData.value))
            }, {

            })
    }
}