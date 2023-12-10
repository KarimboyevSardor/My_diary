package com.example.mydiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mydiary.databinding.DiaryItemBinding
import com.example.mydiary.models.Diary
import com.example.mydiary.viewmodel.MyViewModel

class DeleteDiaryAdapter(var diaryList: MutableList<Diary>, private val listener: OnItemClick, private val activity: FragmentActivity) : Adapter<DeleteDiaryAdapter.MyVh>() {

    private var checkBoxIsShow = false
    private var chooseItem = mutableListOf<Diary>()
    private lateinit var viewModel: MyViewModel
    fun filter(diaryList: MutableList<Diary>) {
        this.diaryList = diaryList
        notifyDataSetChanged()
    }

    class MyVh(val binding: DiaryItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
        return MyVh(DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    override fun onBindViewHolder(holder: MyVh, position: Int) {
        viewModel = ViewModelProvider(activity)[MyViewModel::class.java]
        viewModel.addChooseDeleteItem(checkBoxIsShow)
        holder.binding.aboutTv.text = diaryList[position].about
        holder.binding.createTimeTv.text = diaryList[position].deleted_time
        holder.binding.nameTv.text = diaryList[position].name
        holder.itemView.setOnLongClickListener {
            showCheckBox(holder.binding,position)
            listener.onItemClick(chooseItem)
            true
        }
        holder.itemView.setOnClickListener {
            if (chooseItem.isNotEmpty()) {
                if (chooseItem.any { it == diaryList[position] }) {
                    chooseItem.remove(diaryList[position])
                    holder.binding.radioBtn.isChecked = false
                    holder.binding.radioBtn.visibility = View.GONE
                    listener.onItemClick(chooseItem)
                    if (chooseItem.size == 0) {
                        holder.binding.radioBtn.visibility = View.GONE
                        checkBoxIsShow = viewModel.getChooseDeleteItem()!!.value!!
                        viewModel.addChooseDeleteItem(false)
                        listener.onItemClick(mutableListOf())
                    }
                } else {
                    chooseItem.add(diaryList[position])
                    holder.binding.radioBtn.visibility = View.VISIBLE
                    holder.binding.radioBtn.isChecked = true
                    listener.onItemClick(chooseItem)
                }
            } else {
                viewModel.addChooseDeleteItem(false)
                holder.binding.radioBtn.isChecked = false
                holder.binding.radioBtn.visibility = View.GONE
            }
        }
    }

    private fun showCheckBox(binding: DiaryItemBinding, position: Int) {
        binding.radioBtn.visibility = View.VISIBLE
        checkBoxIsShow = true
        binding.radioBtn.isChecked = true
        chooseItem.add(diaryList[position])
        viewModel.addChooseDeleteItem(true)
    }

    interface OnItemClick {
        fun onItemClick(diaryList: MutableList<Diary>)
    }


}