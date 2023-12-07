package com.example.mydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.folderNameList

class MyViewModel : ViewModel() {
    private var diaryLiveData: MutableLiveData<MutableList<Diary>>? = null
    private var folderNameLiveData: MutableLiveData<MutableList<Folder>>? = null

    init {
        diaryLiveData = MutableLiveData()
        diaryLiveData?.value = diaryList
        folderNameLiveData = MutableLiveData()
        folderNameLiveData?.value = folderNameList
    }

    fun getDiary() : MutableLiveData<MutableList<Diary>>? {
        return diaryLiveData!!
    }

    fun getPackName() : MutableLiveData<MutableList<Folder>>? {
        return folderNameLiveData!!
    }
}