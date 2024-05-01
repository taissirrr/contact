package com.example.contactsapp.databaseManager

import MyProfileHelper
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.contactsapp.models.Contact

class ProfilManager
    (var con: Context) {
    var mabase: SQLiteDatabase? = null
    fun open(file: String?) {
        val helper = MyProfileHelper(con, file, null, 1)
        mabase = helper.writableDatabase
    }

    fun insert(ch1: String?, ch2: String?, ch3: String?, isFav: Int) {
        val v = ContentValues()
        v.put(MyProfileHelper.col_last_name, ch1)
        v.put(MyProfileHelper.col_first_name, ch2)
        v.put(MyProfileHelper.col_num, ch3)
        v.put(MyProfileHelper.isFavorite, isFav)
        mabase!!.insert(MyProfileHelper.table_pos, null, v)
    }

    fun getFavs(): ArrayList<Contact> {
        val data: ArrayList<Contact> = ArrayList()
        val cr = mabase!!.query(
            MyProfileHelper.table_pos, arrayOf(
                MyProfileHelper.col_id, MyProfileHelper.col_first_name,
                MyProfileHelper.col_last_name, MyProfileHelper.col_num,MyProfileHelper.isFavorite
            ), null, null, null, null, null
        )
        cr.moveToFirst()
        while (!cr.isAfterLast) {
            val i1 = cr.getInt(0)
            val i2 = cr.getString(1)
            val i3 = cr.getString(2)
            val i4 = cr.getString(3)
            val i5 = cr.getInt(4)
            if (i5==1)
                data.add(Contact(i1, i2, i3, i4,i5))
            cr.moveToNext()
        }
        return data
    }

    fun getContacts(): ArrayList<Contact> {
        val data: ArrayList<Contact> = ArrayList()
        val cr = mabase!!.query(
            MyProfileHelper.table_pos, arrayOf(
                MyProfileHelper.col_id, MyProfileHelper.col_first_name,
                MyProfileHelper.col_last_name, MyProfileHelper.col_num,MyProfileHelper.isFavorite
            ), null, null, null, null, null
        )
        cr.moveToFirst()
        while (!cr.isAfterLast) {
            val i1 = cr.getInt(0)
            val i2 = cr.getString(1)
            val i3 = cr.getString(2)
            val i4 = cr.getString(3)
            val i5 = cr.getInt(4)
            data.add(Contact(i1, i2, i3, i4,i5))
            cr.moveToNext()
        }
        return data
    }

    fun modify(id: Int, lon: String?, lat: String?, num: String?, isFav:Int): Long {
        val a: Int
        val v = ContentValues()
        v.put(MyProfileHelper.col_id, id)
        v.put(MyProfileHelper.col_last_name, lon)
        v.put(MyProfileHelper.col_first_name, lat)
        v.put(MyProfileHelper.col_num, num)
        v.put(MyProfileHelper.isFavorite, isFav)
        a = mabase!!.update(
            MyProfileHelper.table_pos,
            v,
            MyProfileHelper.col_id + "=" + id, null
        )
        return a.toLong()
    }

    fun delete(id: Int): Long {
        val a: Int = mabase!!.delete(
            MyProfileHelper.table_pos,
            MyProfileHelper.col_id + "=" + id, null
        )
        return a.toLong()
    }
}

