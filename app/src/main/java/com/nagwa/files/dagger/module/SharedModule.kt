package com.nagwa.files.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.nagwa.files.shared_prefs.UserSaver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *  * Module of Shared Prefs for Dagger to use in DI
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Module
class SharedModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences(ctx.applicationInfo.name, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserSaver(
        preferences: SharedPreferences
    ): UserSaver {
        return UserSaver(preferences)
    }
}