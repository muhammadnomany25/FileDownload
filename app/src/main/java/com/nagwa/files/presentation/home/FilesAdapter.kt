package com.nagwa.files.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nagwa.files.R
import com.nagwa.files.core.data.source.local.entity.FileStatusModel
import com.nagwa.files.databinding.LayoutFileRowBinding

/**
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
class FilesAdapter(val mListener: IFileDownload) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    inner class FileViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun onBind(file: FileStatusModel) {
            val fileRowBinding = dataBinding as LayoutFileRowBinding
            with(fileRowBinding) {
                dataModel = file
            }

            itemView.setOnClickListener {
                mListener.downloadFile(file)
            }
        }
    }
}