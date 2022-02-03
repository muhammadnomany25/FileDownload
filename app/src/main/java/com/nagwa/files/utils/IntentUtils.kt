package com.nagwa.files.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.nagwa.files.BuildConfig
import java.io.File

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
object IntentUtils {

    /**
     * Open File intent
     */
    fun openFile(context: Context, file: File?) {
        val fileUri = file?.let {
            FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider",
                it
            )
        }
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, file?.let { FileUtils.getMimeType(it) })
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
}