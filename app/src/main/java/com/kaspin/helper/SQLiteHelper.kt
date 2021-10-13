package com.kaspin.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kaspin.data.model.Barang
import com.kaspin.data.model.BarangDataClass
import java.lang.Exception

class SQLiteHelper(context: Context): SQLiteOpenHelper (context, DB_NAME, null, DB_VERSION){

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "kaspin.db"
        private const val TBL_BARANG = "tbl_barang"
        private const val _ID = "id_barang"
        private const val _KDBARANG = "kode_barang "
        private const val _NAMABARANG = "nama_barang"
        private const val _QTY = "stock"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblBarang = "CREATE TABLE IF NOT EXISTS $TBL_BARANG (" +
                "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$_KDBARANG VARCHAR(50) UNIQUE, " +
                "$_NAMABARANG VARCHAR(225) NOT NULL, " +
                "$_QTY INTEGER NOT NULL" +
                ")"
        db?.execSQL(createTblBarang)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertBarang(data: Barang): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(_ID, data.id_barang)
        contentValues.put(_KDBARANG, data.kode_barang)
        contentValues.put(_NAMABARANG, data.nama_barang)
        contentValues.put(_QTY, data.stock)

        val isSuccess = db.insert(TBL_BARANG, null, contentValues)
        db.close()
        return isSuccess
    }

    fun updateBarang(data: Barang): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(_ID, data.id_barang)
        contentValues.put(_KDBARANG, data.kode_barang)
        contentValues.put(_NAMABARANG, data.nama_barang)
        contentValues.put(_QTY, data.stock)

        val success = db.update(TBL_BARANG, contentValues, "id_barang=" + data.id_barang, null)
        db.close()
        return success
    }

    fun deleteBarang(idBarang: Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(_ID, idBarang)

        val success = db.delete(TBL_BARANG, "id_barang=" + idBarang, null)
        db.close()
        return success
    }

    fun getAllBarang(): ArrayList<Barang>{
        val dataList: ArrayList<Barang> = ArrayList()
        val _query = "SELECT * FROM ${TBL_BARANG}"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(_query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(_query)
            return ArrayList()
        }

        var dataClass = BarangDataClass()

        cursor?.let {
            if (cursor.moveToFirst()){
                do {
                    dataClass.id_barang = cursor.getInt(cursor.getColumnIndex("id_barang"))
                    dataClass.kode_barang = cursor.getString(cursor.getColumnIndex("kode_barang"))
                    dataClass.nama_barang = cursor.getString(cursor.getColumnIndex("nama_barang"))
                    dataClass.stock = cursor.getInt(cursor.getColumnIndex("stock"))

                    val data = Barang(dataClass.id_barang, dataClass.kode_barang, dataClass.nama_barang, dataClass.stock)
                    dataList.add(data)
                }while (cursor.moveToNext())
            }
        }

        return dataList
    }
}