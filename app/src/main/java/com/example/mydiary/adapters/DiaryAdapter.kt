package com.example.mydiary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mydiary.databinding.DiaryItemBinding
import com.example.mydiary.models.Diary

class DiaryAdapter(var diaryList: MutableList<Diary>, val listener: onClick) : Adapter<DiaryAdapter.MyVh>() {

    fun filter(diaryList: MutableList<Diary>) {
        this.diaryList = diaryList
        notifyDataSetChanged()
    }
    class MyVh(val binding: DiaryItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
        return MyVh(DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyVh, position: Int) {
        holder.binding.aboutTv.text = diaryList[position].about
        holder.binding.createTimeTv.text = diaryList[position].deleted_time
        holder.binding.nameTv.text = diaryList[position].name
        holder.itemView.setOnClickListener {
            listener.onItemClick(diaryList[position])
        }
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    interface onClick{
        fun onItemClick(diary: Diary)
    }

}