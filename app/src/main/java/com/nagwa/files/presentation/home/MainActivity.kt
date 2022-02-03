package com.nagwa.files.presentation.home

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nagwa.files.R
import com.nagwa.files.core.data.source.local.entity.DownloadedFileEntity
import com.nagwa.files.core.data.source.local.entity.FileStatusModel
import com.nagwa.files.databinding.ActivityMainBinding
import com.nagwa.files.presentation.base.BaseVmActivity
import com.nagwa.files.presentation.home.adapter.FilesAdapter
import com.nagwa.files.utils.FileUtils.accessAllFile
import com.nagwa.files.utils.FileUtils.getExtensionFileBased
import com.nagwa.files.utils.FileUtils.getFilePath
import com.nagwa.files.utils.IntentUtils
import java.io.File


class MainActivity : BaseVmActivity<ActivityMainBinding, FilesViewModel>(), IFileDownload {
    /**
     * @usage start the activity from anyWhere of the app
     */
    fun start(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

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
                    false
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
        if (checkPermission()) {
            enqueueRequestToDownloadManager(fileStatusModel)
        } else {
            requestPermission { enqueueRequestToDownloadManager(fileStatusModel) }
        }
    }

    /**
     * Send the file download request to the download manager and proceed it
     */
    private fun enqueueRequestToDownloadManager(fileStatusModel: FileStatusModel) {
        adapter.updateFileStatus(fileStatusModel.id, -1, true)
        val fileUrl = fileStatusModel.url
        if (!URLUtil.isValidUrl(fileUrl)) return
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
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
                    ).path + "/" + fileStatusModel.name + ".${getExtensionFileBased(fileStatusModel)}"
                )
            )
        )
        val downloadId = downloadManager.enqueue(request)
        downloadIDs[downloadId] = Pair(downloadId, fileStatusModel.id)
    }


    /**
     * Open downloaded file
     */
    override fun openFile(fileStatusModel: FileStatusModel) {
        val filePath = getFilePath(this, fileStatusModel.downloadedFileEntity!!.downloadManagerId)
        IntentUtils.openFile(this, filePath!!)
    }

    /**
     * Dispose any view model existing requests
     */
    override fun onPause() {
        super.onPause()
        if (viewModel != null) viewModel!!.dispose()
    }


    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission(action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report?.areAllPermissionsGranted() == true) {
                            action.invoke()
                        } else {
                            accessAllFile(this@MainActivity)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        } else {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report?.areAllPermissionsGranted() == true) {
                            action.invoke()
                        } else {
                            errorMsg("Allow permission for storage access!")
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }

    }

    private fun errorMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}