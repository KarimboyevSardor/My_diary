package com.example.mydiary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mydiary.databinding.FolderItemBinding
import com.example.mydiary.databinding.FoldersItemBinding
import com.example.mydiary.models.Folder

class FoldersAdapter(var foldersList: MutableList<Folder>, val listener: ItemOnClick) : Adapter<FoldersAdapter.MyVh>() {

    fun filter(foldersList: MutableList<Folder>) {
        this.foldersList = foldersList
        notifyDataSetChanged()
    }

    class MyVh(val binding: FoldersItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
        return MyVh(FoldersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }

    override fun onBindViewHolder(holder: MyVh, position: Int) {
        holder.binding.folderNameTv.text = foldersList[position].name
        holder.itemView.setOnClickListener {
            listener.onClickItem(foldersList[position])
        }
    }

    interface ItemOnClick {
        fun onClickItem(folder: Folder)
    }

}