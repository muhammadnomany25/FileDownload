package com.nagwa.files.presentation.base

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.nagwa.files.shared_prefs.UserSaver
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */

abstract class BaseActivity<Binding : ViewDataBinding?> : AppCompatActivity() {
    var binding: Binding? = null

    @Inject
    lateinit var userSaver: UserSaver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setLocality()
        bindView()
    }


    /**
     * Initialization of injection stuff
     */
    private fun inject() {
        AndroidInjection.inject(this)
    }

    /**
     * Set locality lang
     */
    private fun setLocality() {
        val userLocal = userSaver.getAppLang()
//        LocalityHelper().setLocality(this, userLocal)
    }

    /**
     * Set the binding views to the activity
     */
    private fun bindView() {
        if (getLayoutId() == NO_LAYOUT.toInt()) return
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding!!.setVariable(getBindingVariable(), getVariableValue())
        binding!!.executePendingBindings()
    }

    /**
     * Setup the activity toolbar
     */
    protected fun setUpToolbar(
        toolbar: Toolbar,
        indicatorResId: Int,
        title: String?,
        displayHomeAsUp: Boolean
    ) {
        toolbar.title = title ?: ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(displayHomeAsUp)
        if (indicatorResId != -1) supportActionBar!!.setHomeAsUpIndicator(indicatorResId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    open fun getVariableValue(): Any? {
        return Object()
    }

    /**
     * Override for reset Binding variable
     *
     * @return variable id
     */
    open fun getBindingVariable(): Int {
        return -1
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    companion object {
        const val NO_LAYOUT: Short = -1
    }

    /**
     * Set the updated context based on user locale
     */
//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(LocalityHelper().onAttach(base!!, UserSaver.getAppLang(base)))
//    }
}
