package com.example.mydiary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mydiary.databinding.FolderItemBinding
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder

class FolderAdapter(var folderList: MutableList<Folder>, val listener: onClick) : Adapter<FolderAdapter.MyVh>() {

    fun filter(folderList: MutableList<Folder>) {
        this.folderList = folderList
        notifyDataSetChanged()
    }
    class MyVh(val binding: FolderItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
        return MyVh(FolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    override fun onBindViewHolder(holder: MyVh, position: Int) {
        holder.binding.folderName.text = folderList[position].name
        holder.itemView.setOnClickListener {
            listener.onItemClick(folderList[position])
        }
    }

    interface onClick{
        fun onItemClick(folder: Folder)
    }
}