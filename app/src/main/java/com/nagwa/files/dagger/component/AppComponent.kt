package com.nagwa.files.dagger.component

import android.content.Context
import com.nagwa.files.app.ApplicationClass
import com.nagwa.files.dagger.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
public interface AppComponent {
    fun inject(appClass: ApplicationClass)

    @Component.Builder
   public interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent

    }
}