package com.nagwa.files.presentation.home

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.nagwa.files.BuildConfig
import com.nagwa.files.R
import com.nagwa.files.core.data.source.local.entity.DownloadedFileEntity
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
                /**
                 * Update the file in the UI
                 */
                if (adapter != null) adapter.updateFileStatus(
                    downloadIDs[id]!!.second,
                    downloadIDs[id]!!.first,
                    true
                )
                updateFileInLocalDB(id)
                downloadIDs.remove(id)
            }
        }

        /**
         * Update file in local db
         */
        private fun updateFileInLocalDB(id: Long) {
            viewModel!!.insertDownloaded(
                DownloadedFileEntity(
                    downloadIDs[id]!!.second,
                    downloadIDs[id]!!.second,
                    downloadIDs[id]!!.first
                )
            )
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
        request.setDestinationUri(
            Uri.fromFile(
                File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS
                    ).path + "/" + fileStatusModel.name + ".${getMimeType(fileUrl)}"
                )
            )
        )
        val downloadId = downloadManager.enqueue(request)
        downloadIDs[downloadId] = Pair(downloadId, fileStatusModel.id)
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return extension
    }

    /**
     * Open downloaded file
     */
    override fun openFile(fileStatusModel: FileStatusModel) {
        val filePath = getFilePath(fileStatusModel.downloadedFileEntity!!.downloadManagerId)
        openFile(filePath!!)
    }

    @SuppressLint("Range")
    fun getFilePath(downloadId: Long): File? {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val c: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
        if (c != null) {
            c.moveToFirst()
            try {
                val fileUri: String =
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val mFile = File(Uri.parse(fileUri).path)
                val fileName = mFile.absolutePath
                return mFile
            } catch (e: Exception) {
                Log.e("error", "Could not open the downloaded file")
                return null
            }
        } else return null
    }

    fun openFile(file: File?) {
        val fileUri = file?.let {
            FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider",
                it
            )
        }
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, file?.let { getMimeType(it) })
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    //file extension
    fun getMimeType(file: File): String {
        val extension = getExtension(file.name) ?: ""
        return if (extension.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            extension.substring(1)
        ).toString() else "application/octet-stream"

    }

    fun getExtension(uri: String?): String? {
        if (uri == null) {
            return null
        }

        val dot = uri.lastIndexOf(".")
        return if (dot >= 0) {
            uri.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

    /**
     * Dispose any view model existing requests
     */
    override fun onPause() {
        super.onPause()
        if (viewModel != null) viewModel!!.dispose()
    }


}