package com.nagwa.files.presentation.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject


/**
 * @Author: Muhammad Noamany
 * @Date: 2/2/2022
 * @Email: muhammadnoamany@gmail.com
 */
abstract class BaseVmActivity<Binding : ViewDataBinding?, VM : ViewModel?> :
    BaseActivity<Binding?>() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
     var viewModel: VM? = null
    override fun getBindingVariable(): Int {
        return -1
    }

    override fun getVariableValue(): Any? {
        if (getVmClass() == null) return null
        viewModel = ViewModelProvider(this, factory).get(getVmClass() as Class<ViewModel>) as VM
        return viewModel
    }

    /**
     * Override for reset view model
     *
     * @return view model Class like (TestVM.class)
     */
    protected abstract fun getVmClass(): Class<*>


}
