package com.nagwa.files.core.interactors.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * This abstract class is used as a base class for the use cases of the app
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
abstract class BaseInteractor<T> {
    private var disposable: Disposable? = null
    abstract fun buildUseCaseSingle(): Single<T>

    /**
     * proceeding the use case and handles its results
     */
    fun execute(
        onSuccess: ((t: T) -> Unit),
        onError: ((t: Throwable) -> Unit),
        onFinished: () -> Unit = {}
    ) {
        dispose()
        disposable = buildUseCaseSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate(onFinished)
            .subscribe(onSuccess, onError)
    }

    private fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}