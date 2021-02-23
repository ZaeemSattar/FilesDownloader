package com.zaeem.filesdownloader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zaeem.filesdownloader.data.COMPLETED
import com.zaeem.filesdownloader.data.DownloadData
import com.zaeem.filesdownloader.data.NOT_STARTED
import com.zaeem.filesdownloader.data.STARTED
import com.zaeem.filesdownloader.databinding.RowDownloadingBinding

class DownloadingAdapter() : RecyclerView.Adapter<DownloadingAdapter.DownloadingViewHolder>() {

    private val photoItems: ArrayList<DownloadData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadingViewHolder {
        val binding = RowDownloadingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DownloadingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadingViewHolder, position: Int) {
        holder.bind(photoItems[position])
    }

    override fun getItemCount() = photoItems.size

    fun updateItems(photosList: List<DownloadData>) {
        photoItems.clear()
        photoItems.addAll(photosList)
        notifyDataSetChanged()
    }

    inner class DownloadingViewHolder(val itemBinding: RowDownloadingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DownloadData) {
            itemBinding.downloadData = item
            when (item.downloaded)
            {
                COMPLETED ->
                {
                    itemBinding.progressBar.visibility= View.INVISIBLE
                    itemBinding.doneIv.visibility= View.VISIBLE

                }
                STARTED->
                {
                    itemBinding.progressBar.visibility= View.VISIBLE

                }
                NOT_STARTED->{
                    itemBinding.progressBar.visibility= View.INVISIBLE

                }
            }
        }
    }
}