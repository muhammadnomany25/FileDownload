package com.nagwa.files.dagger.module

import dagger.Module

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Module(includes = [ActivityBuilderModule::class, SharedModule::class])
class AppModule {
}