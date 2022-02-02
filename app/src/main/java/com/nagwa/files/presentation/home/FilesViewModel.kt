package com.nagwa.files.presentation.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nagwa.files.core.data.source.local.AppDatabase
import com.nagwa.files.core.data.source.local.entity.FileEntity
import com.nagwa.files.core.domain.FileModel
import com.nagwa.files.core.interactors.base.GetFilesInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @Author: Muhammad Noamany
 * @Date: 2/2/2022
 * @Email: muhammadnoamany@gmail.com
 */
class FilesViewModel @Inject constructor(
    private val getFilesInteractor: GetFilesInteractor,
    private val appDatabase: AppDatabase
) : ViewModel() {
    val filesData = MutableLiveData<List<FileEntity>>()

    fun loadFiles() {
        getFilesInteractor.execute(onSuccess = {
            mapFilesResponse(it)
        }, onError = {

        }, onFinished = {

        })
    }

    private fun mapFilesResponse(filesList: List<FileModel>) {
        Observable.fromIterable(filesList).map {
            FileEntity(type = it.type, url = it.url, name = it.name)
        }.toList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       if (!it.isNullOrEmpty())
                           appDatabase.fileDao.insert(it)
                loadAfterCaching()

            },{})
    }

    private fun loadAfterCaching() {
        appDatabase.fileDao.loadAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                filesData.value = if (it.isNullOrEmpty()) ArrayList() else it
                Log.e("responseList", Gson().toJson(filesData.value))
            },{})
    }
}