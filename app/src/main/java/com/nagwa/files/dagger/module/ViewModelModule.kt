package com.nagwa.files.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nagwa.files.dagger.factory.ViewModelProviderFactory
import com.nagwa.files.presentation.home.FilesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FilesViewModel::class)
    internal abstract fun bindEditPlaceViewModel(filesViewModel: FilesViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}