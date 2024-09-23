package com.example.samojlov_av_homework_module_13_number_8

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PERSON_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "person_table"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_IMAGE + " TEXT" + ")")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(clothes: Clothes) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, clothes.id)
        contentValues.put(KEY_NAME, clothes.name)
        contentValues.put(KEY_DESCRIPTION, clothes.description)
        contentValues.put(KEY_IMAGE, clothes.image)
        try {
            db.insert(TABLE_NAME, null, contentValues)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun readClothes(): MutableList<Clothes> {
        val clothesList = mutableListOf<Clothes>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return clothesList
        }

        var clothesId: Int
        var clothesName: String
        var clothesDescription: String
        var clothesImage: String
        if (cursor.moveToFirst()) {
            do {
                clothesId = cursor.getInt(cursor.getColumnIndex("id"))
                clothesName = cursor.getString(cursor.getColumnIndex("name"))
                clothesDescription = cursor.getString(cursor.getColumnIndex("description"))
                clothesImage = cursor.getString(cursor.getColumnIndex("image"))
                val clothes = Clothes(clothesId, clothesName, clothesDescription, clothesImage)
                clothesList.add(clothes)
            } while (cursor.moveToNext())
        }
        return clothesList
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    fun updateProduct(clothes: Clothes) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, clothes.id)
        contentValues.put(KEY_NAME, clothes.name)
        contentValues.put(KEY_DESCRIPTION, clothes.description)
        contentValues.put(KEY_IMAGE, clothes.image)
        try {
            db.update(TABLE_NAME, contentValues, "id=" + clothes.id, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }
}