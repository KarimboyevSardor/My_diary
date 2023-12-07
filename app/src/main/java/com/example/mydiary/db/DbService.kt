package com.example.mydiary.db

import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder

interface DbService {
    //TODO: DIARY
    fun getDiary() : MutableList<Diary>

    fun addDiary(diary: Diary)

    fun updateDiary(diary: Diary)

    fun deletedDiary(diary: Diary)

    //TODO: PACK
    fun getPack() : MutableList<Folder>

    fun addPack(folder: Folder)

    fun updatePack(folder: Folder, oldName: String)

    fun deletePack(folder: Folder)

}