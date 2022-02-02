package com.nagwa.files.presentation.home

import android.os.Bundle
import com.nagwa.files.R
import com.nagwa.files.databinding.ActivityMainBinding
import com.nagwa.files.presentation.base.BaseVmActivity

class MainActivity : BaseVmActivity<ActivityMainBinding, FilesViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel!!.loadFiles()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getVmClass(): Class<*> {
        return FilesViewModel::class.java
    }
}