package com.example.mydiary.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Pack

class MyDb(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) , DbService{
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "diary"

        //TODO: DIARY TABLE
        const val TABLE_DIARY = "diarys"
        const val ID = "id"
        const val DIARY_NAME = "name"
        const val DIARY_ABOUT = "about"
        const val DIARY_DELETED = "deleted"
        const val DIARY_FAVORITE = "favorite"
        const val DIARY_DELETED_TIME = "deleted_time"
        const val DIARY_PACK_NAME = "pack_name"

        //TODO: PACK TABLE
        const val TABLE_PACK = "packs"
        const val PACK_NAME = "name"
        const val PACK_DELETED = "deleted"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val diaryQuery = "CREATE TABLE $TABLE_DIARY($ID INTEGER NOT NULL, $DIARY_NAME TEXT NOT NULL, $DIARY_ABOUT TEXT NOT NULL, " +
                "$DIARY_DELETED INTEGER NOT NULL, $DIARY_FAVORITE INTEGER NOT NULL, $DIARY_DELETED_TIME TEXT NOT NULL, " +
                "$DIARY_PACK_NAME TEXT NOT NULL)"
        val packQuery = "CREATE TABLE $TABLE_PACK($ID INTEGER NOT NULL, $PACK_NAME TEXT NOT NULL, $PACK_DELETED INTEGER NOT NULL)"
        db?.execSQL(diaryQuery)
        db?.execSQL(packQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun getDiary(): MutableList<Diary> {
        val diaryList = mutableListOf<Diary>()
        val cursor = this.readableDatabase.rawQuery("SELECT * FROM $TABLE_DIARY", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val about = cursor.getString(2)
                val deleted = cursor.getInt(3)
                val fav = cursor.getInt(4)
                val time = cursor.getString(5)
                val packName = cursor.getString(6)
                diaryList.add(Diary(id, name, about, deleted, fav, packName, time))
            } while (cursor.moveToNext())
        }
        return diaryList
    }

    override fun addDiary(diary: Diary) {
        val content = ContentValues()
        content.put(DIARY_NAME, diary.name)
        content.put(DIARY_ABOUT, diary.about)
        content.put(DIARY_DELETED, diary.deleted)
        content.put(DIARY_DELETED_TIME, diary.deleted_time)
        content.put(DIARY_FAVORITE, diary.favorite)
        content.put(DIARY_PACK_NAME, diary.pack_name)
        this.writableDatabase.insert(TABLE_DIARY, null, content)
    }

    override fun updateDiary(diary: Diary) {
        val content = ContentValues()
        content.put(DIARY_NAME, diary.name)
        content.put(DIARY_ABOUT, diary.about)
        content.put(DIARY_DELETED, diary.deleted)
        content.put(DIARY_DELETED_TIME, diary.deleted_time)
        content.put(DIARY_FAVORITE, diary.favorite)
        content.put(DIARY_PACK_NAME, diary.pack_name)
        this.writableDatabase.update(TABLE_DIARY, content, "$ID = ?", arrayOf(diary.id.toString()))
    }

    override fun deletedDiary(diary: Diary) {
        this.writableDatabase.delete(TABLE_DIARY, "$DIARY_PACK_NAME = ?", arrayOf(diary.pack_name))
    }

    override fun getPack(): MutableList<Pack> {
        val packList = mutableListOf<Pack>()
        val cursor = this.readableDatabase.rawQuery("SELECT * FROM $TABLE_PACK", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val delete = cursor.getInt(2)
                packList.add(Pack(id, name, delete))
            } while (cursor.moveToNext())
        }
        return packList
    }

    override fun addPack(pack: Pack) {
        val content = ContentValues()
        content.put(PACK_NAME, pack.name)
        content.put(PACK_DELETED, pack.deleted)
        this.writableDatabase.insert(TABLE_PACK, null, content)
    }

    override fun updatePack(pack: Pack, oldName: String) {
        val content = ContentValues()
        content.put(PACK_NAME, pack.name)
        content.put(PACK_DELETED, pack.deleted)
        val contentValues = ContentValues()
        contentValues.put(DIARY_PACK_NAME, pack.name)
        this.writableDatabase.update(TABLE_DIARY, contentValues, "$DIARY_PACK_NAME = ?", arrayOf(oldName))
        this.writableDatabase.update(TABLE_PACK, content, "$ID = ?", arrayOf(pack.id.toString()))
    }

    override fun deletePack(pack: Pack) {
        this.writableDatabase.delete(TABLE_DIARY, "$DIARY_PACK_NAME = ?", arrayOf(pack.name))
        this.writableDatabase.delete(TABLE_PACK, "$PACK_NAME = ?", arrayOf(pack.name))
    }

}