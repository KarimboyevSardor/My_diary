package com.example.mydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Pack
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.packNameList

class MyViewModel : ViewModel(){
    var diaryLiveData: MutableLiveData<MutableList<Diary>>? = null
    var packNameLiveData: MutableLiveData<MutableList<Pack>>? = null

    init {
        diaryLiveData = MutableLiveData()
        diaryLiveData!!.value = diaryList
        packNameLiveData = MutableLiveData()
        packNameLiveData!!.value = packNameList
    }

    fun getDiaryLiveData() : MutableLiveData<MutableList<Diary>>? {
        return diaryLiveData!!
    }

    fun getPackNameLiveData() : MutableLiveData<MutableList<Pack>>? {
        return packNameLiveData!!
    }
}