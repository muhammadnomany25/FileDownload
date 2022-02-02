package com.nagwa.files.presentation.home

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.nagwa.files.R
import com.nagwa.files.core.data.source.local.entity.FileStatusModel
import com.nagwa.files.databinding.ActivityMainBinding
import com.nagwa.files.presentation.base.BaseVmActivity
import java.io.File


class MainActivity : BaseVmActivity<ActivityMainBinding, FilesViewModel>(), IFileDownload {
    private var downloadIDs = HashMap<Long, Pair<Long, Int>>()
    private lateinit var adapter: FilesAdapter

    // Broadcast receiver to listen to file download status
    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadIDs.containsKey(id) && downloadIDs[id]!!.first == id) {
                Toast.makeText(this@MainActivity, "Download Completed", Toast.LENGTH_SHORT).show()
                // remove download id from the download ids list
                downloadIDs.remove(id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRecycler()
        observeData()
        setListeners()
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    /**
     * Set listeners for view
     */
    private fun setListeners() {
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            viewModel!!.loadFiles()
        }
    }

    /**
     * Observe view model data
     */
    private fun observeData() {
        viewModel!!.fileUiModel.status.observe(this, Observer {
            if (binding!!.swipeRefreshLayout.isRefreshing) binding!!.swipeRefreshLayout.isRefreshing =
                false
            handleResult(it)
        })
        //start fetching data
        viewModel!!.loadFiles()
    }

    /**
     * Handle the result of viewModel status
     */
    private fun handleResult(it: Int?) {
        when (it) {
            Status.SUCCESS.value -> {
                binding!!.progressBar.visibility = View.GONE
                adapter.setData(viewModel!!.fileUiModel.filesData.value!!)
            }
            Status.ERROR.value -> binding!!.progressBar.visibility = View.GONE
            else -> binding!!.progressBar.visibility = View.VISIBLE
        }
    }

    /**
     * Set Recycler view and attach it to the adapter
     */
    private fun setRecycler() {
        adapter = FilesAdapter(this)
        binding!!.recycler.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        binding!!.recycler.adapter = adapter
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getVmClass(): Class<*> {
        return FilesViewModel::class.java
    }

    /**
     * Start downloading a file
     */
    override fun downloadFile(fileStatusModel: FileStatusModel) {
        val fileUrl = fileStatusModel.url
        if (!URLUtil.isValidUrl(fileUrl)) return
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri: Uri = Uri.parse(fileUrl)
        val request = DownloadManager.Request(uri)
        request.setTitle(getString(R.string.app_name))
        request.setDescription("Downloading: " + fileStatusModel.name)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(File(externalCacheDir!!.path + "/" + fileStatusModel.name)))
        val downloadId = downloadManager.enqueue(request)
        downloadIDs[downloadId] = Pair(downloadId, fileStatusModel.id)
    }

    /**
     * Dispose any view model existing requests
     */
    override fun onPause() {
        super.onPause()
        if (viewModel != null) viewModel!!.dispose()
    }


}