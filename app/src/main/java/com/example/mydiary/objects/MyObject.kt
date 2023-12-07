package com.example.mydiary.objects

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.viewmodel.MyViewModel

object MyObject {
    var diaryList = mutableListOf<Diary>()
    var folderNameList = mutableListOf<Folder>()

    fun getViewModel(context: FragmentActivity) : MyViewModel {
        return ViewModelProvider(context)[MyViewModel::class.java]
    }
}