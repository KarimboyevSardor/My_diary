package com.example.mydiary.db

import com.example.mydiary.objects.Diary
import com.example.mydiary.objects.Pack

interface DbService {
    //TODO: DIARY
    fun getDiary() : MutableList<Diary>

    fun addDiary(diary: Diary)

    fun updateDiary(diary: Diary)

    fun deletedDiary(diary: Diary)

    //TODO: PACK
    fun getPack() : MutableList<Pack>

    fun addPack(pack: Pack)

    fun updatePack(pack: Pack, oldName: String)

    fun deletePack(pack: Pack)

}