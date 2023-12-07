package com.example.mydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.folderNameList

class MyViewModel : ViewModel(){
    var diaryLiveData: MutableLiveData<MutableList<Diary>>? = null
    var folderNameLiveData: MutableLiveData<MutableList<Folder>>? = null

    init {
        diaryLiveData = MutableLiveData()
        diaryLiveData!!.value = diaryList
        folderNameLiveData = MutableLiveData()
        folderNameLiveData!!.value = folderNameList
    }

    fun getDiaryLiveData() : MutableLiveData<MutableList<Diary>>? {
        return diaryLiveData!!
    }

    fun getPackNameLiveData() : MutableLiveData<MutableList<Folder>>? {
        return folderNameLiveData!!
    }
}