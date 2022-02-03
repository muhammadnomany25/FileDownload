package com.nagwa.files.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nagwa.files.R
import com.nagwa.files.core.data.source.local.entity.DownloadedFileEntity
import com.nagwa.files.core.data.source.local.entity.FileStatusModel
import com.nagwa.files.databinding.LayoutFileRowBinding

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
class FilesAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mListener: IFileDownload = context as IFileDownload
    private val files: MutableList<FileStatusModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val fileRowBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.layout_file_row, parent, false
        )
        return FileViewHolder(fileRowBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FileViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): FileStatusModel {
        return files[position]
    }

    override fun getItemCount(): Int {
        return files.size
    }

    fun setData(list: List<FileStatusModel>) {
        this.files.clear()
        this.files.addAll(list)
        notifyDataSetChanged()
    }

    fun updateFileStatus(id: Int, downloadId: Long, downloaded: Boolean) {
        if (downloaded) {
            val file = files.find { it.id == id }!!
            file.showProgress = false
            file.downloadedFileEntity =
                DownloadedFileEntity(0, id, downloadId)
        }
        notifyItemChanged(files.indexOf(files.find { it.id == id }))
    }

    inner class FileViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun onBind(file: FileStatusModel) {
            val fileRowBinding = dataBinding as LayoutFileRowBinding
            with(fileRowBinding) {
                dataModel = file
            }

            dataBinding.downloadIV.setOnClickListener {
                if (!URLUtil.isValidUrl(file.url)) {
                    Toast.makeText(context, "Invalid Url to download!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                file.showProgress = true
                notifyItemChanged(files.indexOf(files.find { it.id == file.id }))
                mListener.downloadFile(file)
            }
            dataBinding.openFileIV.setOnClickListener {
                mListener.openFile(file)
            }
        }
    }
}