package com.nagwa.files.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import com.nagwa.files.core.data.source.local.entity.FileStatusModel
import com.nagwa.files.presentation.home.FileType
import java.io.File

/**
 * Handle common functions used in files and its parsing
 *
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
object FileUtils {

    /**
     * Get the files based on file type
     */
    fun getExtensionFileBased(file: FileStatusModel): String {
        return if (file.type.equals(FileType.PDF.value))
            "pdf"
        else "mp4"
    }

    /**
     * Return file path based on the download file id that stored in the download manager
     */
    @SuppressLint("Range")
    fun getFilePath(context: Context, downloadId: Long): File? {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val c: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
        return if (c != null) {
            c.moveToFirst()
            try {
                val fileUri: String =
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val mFile = File(Uri.parse(fileUri).path)
                val fileName = mFile.absolutePath
                mFile
            } catch (e: Exception) {
                Log.e("error", "Could not open the downloaded file")
                null
            }
        } else null
    }


    //file extension
    fun getMimeType(file: File): String {
        val extension = getExtension(file.name) ?: ""
        return if (extension.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            extension.substring(1)
        ).toString() else "application/octet-stream"

    }

    /**
     * Return file extension based on file name
     */
    private fun getExtension(uri: String?): String? {
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

    @RequiresApi(Build.VERSION_CODES.R)
    fun accessAllFile(context: Context){
        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}