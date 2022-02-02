package com.nagwa.files.dagger.module

import com.nagwa.files.presentation.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module of Injecting app activities for Dagger to use in DI
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun MainActivity(): MainActivity?

}