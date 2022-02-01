package com.nagwa.files.app

import androidx.multidex.MultiDexApplication
import com.nagwa.files.dagger.component.AppComponent
import com.ganby.dagger.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
class ApplicationClass : MultiDexApplication(), HasAndroidInjector {
    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }


    /**
     * Initializing of dagger
     */
    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .context(this.applicationContext)
            .build()
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any?> {
        return dispatchingAndroidInjector
    }

}