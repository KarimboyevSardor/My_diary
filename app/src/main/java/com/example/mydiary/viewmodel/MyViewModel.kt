package com.example.mydiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.folderNameList

class MyViewModel : ViewModel() {
    var diaryLiveData: MutableLiveData<MutableList<Diary>>? = null
    var folderNameLiveData: MutableLiveData<MutableList<Folder>>? = null
    var folderNameLiveData1: MutableLiveData<String>? = null
    var chooseDeleteItem1: MutableLiveData<Boolean>? = null

    init {
        folderNameLiveData1 = MutableLiveData()
        chooseDeleteItem1 = MutableLiveData()
        diaryLiveData = MutableLiveData()
        diaryLiveData?.value = diaryList
        folderNameLiveData = MutableLiveData()
        folderNameLiveData?.value = folderNameList
    }

    fun addChooseDeleteItem(show: Boolean) {
        chooseDeleteItem1!!.value = show
    }

    fun getChooseDeleteItem() : MutableLiveData<Boolean>? {
        return chooseDeleteItem1!!
    }

    fun addFolderName(name: String) {
        folderNameLiveData1!!.value = name
    }

    fun getFolderName() : MutableLiveData<String>? {
        return folderNameLiveData1!!
    }

    fun getDiary() : MutableLiveData<MutableList<Diary>>? {
        return diaryLiveData!!
    }

    fun getPackName() : MutableLiveData<MutableList<Folder>>? {
        return folderNameLiveData!!
    }
}