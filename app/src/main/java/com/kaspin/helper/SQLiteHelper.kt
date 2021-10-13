package com.kaspin.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kaspin.data.model.*
import com.kaspin.util.Constants
import java.lang.Exception

class SQLiteHelper(context: Context): SQLiteOpenHelper (context, Constants.DB_NAME, null, Constants.DB_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblBarang = "CREATE TABLE IF NOT EXISTS ${Constants.TBL_BARANG} (" +
                "${Constants._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${Constants._KDBARANG} VARCHAR(50) UNIQUE, " +
                "${Constants._NAMABARANG} VARCHAR(225) NOT NULL, " +
                "${Constants._QTY} INTEGER NOT NULL" +
                ")"
        db?.execSQL(createTblBarang)

        val createTblTransaksi = "CREATE TABLE IF NOT EXISTS ${Constants.TBL_TRANSAKSI} (" +
                "${Constants._ID_TRANSAKSI} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${Constants._ID_DETAIL_TRANSAKSI} VARCHAR(50) UNIQUE, " +
                "${Constants._NAMA_TRANSAKSI} VARCHAR(225) NOT NULL, " +
                "${Constants._STATUS_TRANSAKSI} INTEGER DEFAULT 0" +
                ")"
        db?.execSQL(createTblTransaksi)

        val createTblDetailTransaksi = "CREATE TABLE IF NOT EXISTS ${Constants.TBL_DETAIL_TRANSAKSI} (" +
                "${Constants._ID_DETAIL_TRANSAKSI} VARCHAR(50) NOT NULL, " +
                "${Constants._ID_BARANG} INTEGER NOT NULL, " +
                "${Constants._KD_BARANG} VARCHAR(50) NOT NULL, " +
                "${Constants._NAMA_BARANG} VARCHAR(225) NOT NULL, " +
                "${Constants._QTY_} INTEGER NOT NULL" +
                ")"
        db?.execSQL(createTblDetailTransaksi)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertBarang(data: Barang): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID, data.id_barang)
        contentValues.put(Constants._KDBARANG, data.kode_barang)
        contentValues.put(Constants._NAMABARANG, data.nama_barang)
        contentValues.put(Constants._QTY, data.stock)

        val isSuccess = db.insert(Constants.TBL_BARANG, null, contentValues)
        db.close()
        return isSuccess
    }

    fun updateBarang(data: Barang): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID, data.id_barang)
        contentValues.put(Constants._KDBARANG, data.kode_barang)
        contentValues.put(Constants._NAMABARANG, data.nama_barang)
        contentValues.put(Constants._QTY, data.stock)

        val success = db.update(Constants.TBL_BARANG, contentValues, "id_barang=" + data.id_barang, null)
        db.close()
        return success
    }

    fun updateStockBarang(idBarang: Int, stock: Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._QTY, stock)

        val success = db.update(Constants.TBL_BARANG, contentValues, "id_barang=" + idBarang, null)
        db.close()
        return success
    }

    fun deleteBarang(idBarang: Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID, idBarang)

        val success = db.delete(Constants.TBL_BARANG, "id_barang=" + idBarang, null)
        db.close()
        return success
    }

    fun getBarang(idBarang: Int): BarangDataClass{
        var data = BarangDataClass()
        val _query = "SELECT * FROM ${Constants.TBL_BARANG} WHERE id_barang=" + idBarang + ""
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(_query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(_query)
            return BarangDataClass()
        }

        var dataClass = BarangDataClass()

        cursor?.let {
            if (cursor.moveToFirst()){
                do {
                    dataClass.id_barang = cursor.getInt(cursor.getColumnIndex("id_barang"))
                    dataClass.kode_barang = cursor.getString(cursor.getColumnIndex("kode_barang"))
                    dataClass.nama_barang = cursor.getString(cursor.getColumnIndex("nama_barang"))
                    dataClass.stock = cursor.getInt(cursor.getColumnIndex("stock"))

                    val data_ = BarangDataClass(dataClass.id_barang, dataClass.kode_barang, dataClass.nama_barang, dataClass.stock)
                    data = data_
                }while (cursor.moveToNext())
            }
        }

        return data
    }

    fun getAllBarang(): ArrayList<Barang>{
        val dataList: ArrayList<Barang> = ArrayList()
        val _query = "SELECT * FROM ${Constants.TBL_BARANG}"
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

    fun getAllBarangWithStock(): ArrayList<Barang>{
        val dataList: ArrayList<Barang> = ArrayList()
        val _query = "SELECT * FROM ${Constants.TBL_BARANG} WHERE stock > 0"
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

    //transaksi
    fun insertHeaderTransaksi(data: HeaderTransaksiDataClass): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID_TRANSAKSI, data.id_transaksi)
        contentValues.put(Constants._ID_DETAIL_TRANSAKSI, data.id_detail_transaksi)
        contentValues.put(Constants._NAMA_TRANSAKSI, data.nama_transaksi)
        contentValues.put(Constants._STATUS_TRANSAKSI, data.status)

        val isSuccess = db.insert(Constants.TBL_TRANSAKSI, null, contentValues)
        db.close()
        return isSuccess
    }

    fun updateHeaderTransaksi(data: HeaderTransaksiDataClass): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID_TRANSAKSI, data.id_transaksi)
        contentValues.put(Constants._ID_DETAIL_TRANSAKSI, data.id_detail_transaksi)
        contentValues.put(Constants._NAMA_TRANSAKSI, data.nama_transaksi)
        contentValues.put(Constants._STATUS_TRANSAKSI, data.status)

        val success = db.update(Constants.TBL_TRANSAKSI, contentValues, "id_detail_transaksi="+ data.id_detail_transaksi+"", null)
        db.close()
        return success
    }

    fun insertDetailTransaksi(data: DetailTransaksiDataClass): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID_DETAIL_TRANSAKSI, data.id_detail_transaksi)
        contentValues.put(Constants._ID_BARANG, data.id_barang)
        contentValues.put(Constants._KD_BARANG, data.kode_barang)
        contentValues.put(Constants._NAMA_BARANG, data.nama_barang)
        contentValues.put(Constants._QTY_, data.stock)

        val isSuccess = db.insert(Constants.TBL_DETAIL_TRANSAKSI, null, contentValues)
        db.close()
        return isSuccess
    }

    fun updateDetailTransaksi(data: DetailTransaksiDataClass): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID_DETAIL_TRANSAKSI, data.id_detail_transaksi)
        contentValues.put(Constants._ID_BARANG, data.id_barang)
        contentValues.put(Constants._KD_BARANG, data.kode_barang)
        contentValues.put(Constants._NAMA_BARANG, data.nama_barang)
        contentValues.put(Constants._QTY_, data.stock)

        val success = db.update(Constants.TBL_DETAIL_TRANSAKSI, contentValues, "id_detail_transaksi="+ data.id_detail_transaksi+" AND id_barang=" + data.id_barang, null)
        db.close()
        return success
    }

    fun getDetailTransaksi(idDetailTransaksi: String, idBarang: Int): DetailTransaksi{
        var data_ = DetailTransaksi()
        val _query = "SELECT * FROM ${Constants.TBL_DETAIL_TRANSAKSI} WHERE id_detail_transaksi=${idDetailTransaksi} AND id_barang=${idBarang}"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(_query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(_query)
            return DetailTransaksi()
        }

        var dataClass = DetailTransaksiDataClass()

        cursor?.let {
            if (cursor.moveToFirst()){
                dataClass.id_detail_transaksi = cursor.getString(cursor.getColumnIndex("id_detail_transaksi"))
                dataClass.id_barang = cursor.getInt(cursor.getColumnIndex("id_barang"))
                dataClass.kode_barang = cursor.getString(cursor.getColumnIndex("kode_barang"))
                dataClass.nama_barang = cursor.getString(cursor.getColumnIndex("nama_barang"))
                dataClass.stock = cursor.getInt(cursor.getColumnIndex("stock"))

                val data = DetailTransaksi(dataClass.id_detail_transaksi, dataClass.id_barang, dataClass.kode_barang, dataClass.nama_barang, dataClass.stock)
                data_ = data
            }
        }

        return data_
    }

    fun getLastRecordTransaksiHeader(): HeaderTransaksi{
        var data_ = HeaderTransaksi()
        val _query = "SELECT * FROM ${Constants.TBL_TRANSAKSI} ORDER BY id_transaksi DESC LIMIT 1"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(_query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(_query)
            return HeaderTransaksi()
        }

        var dataClass = HeaderTransaksiDataClass()

        cursor?.let {
            if (cursor.moveToFirst()){
                dataClass.id_transaksi = cursor.getInt(cursor.getColumnIndex("id_transaksi"))
                dataClass.id_detail_transaksi = cursor.getString(cursor.getColumnIndex("id_detail_transaksi"))
                dataClass.nama_transaksi = cursor.getString(cursor.getColumnIndex("nama_transaksi"))
                dataClass.status = cursor.getInt(cursor.getColumnIndex("status"))

                val data = HeaderTransaksi(dataClass.id_transaksi, dataClass.id_detail_transaksi, dataClass.nama_transaksi, dataClass.status)
                data_ = data
            }
        }

        return data_
    }

    fun getTransaksiHeader(idDetailTransaksi: String): HeaderTransaksi{
        var data_ = HeaderTransaksi()
        val _query = "SELECT * FROM ${Constants.TBL_TRANSAKSI} WHERE id_detail_transaksi="+idDetailTransaksi+""
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(_query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(_query)
            return HeaderTransaksi()
        }

        var dataClass = HeaderTransaksiDataClass()

        cursor?.let {
            if (cursor.moveToFirst()){
                dataClass.id_transaksi = cursor.getInt(cursor.getColumnIndex("id_transaksi"))
                dataClass.id_detail_transaksi = cursor.getString(cursor.getColumnIndex("id_detail_transaksi"))
                dataClass.nama_transaksi = cursor.getString(cursor.getColumnIndex("nama_transaksi"))
                dataClass.status = cursor.getInt(cursor.getColumnIndex("status"))

                val data = HeaderTransaksi(dataClass.id_transaksi, dataClass.id_detail_transaksi, dataClass.nama_transaksi, dataClass.status)
                data_ = data
            }
        }

        return data_
    }

    fun getAllCheckout(idDetailTransaksi: String): ArrayList<DetailTransaksi>{
        val dataList: ArrayList<DetailTransaksi> = ArrayList()
        val _query = "SELECT * FROM ${Constants.TBL_DETAIL_TRANSAKSI} WHERE id_detail_transaksi = ${idDetailTransaksi}"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(_query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(_query)
            return ArrayList()
        }

        var dataClass = DetailTransaksiDataClass()

        cursor?.let {
            if (cursor.moveToFirst()){
                do {
                    dataClass.id_detail_transaksi = cursor.getString(cursor.getColumnIndex("id_detail_transaksi"))
                    dataClass.id_barang = cursor.getInt(cursor.getColumnIndex("id_barang"))
                    dataClass.kode_barang = cursor.getString(cursor.getColumnIndex("kode_barang"))
                    dataClass.nama_barang = cursor.getString(cursor.getColumnIndex("nama_barang"))
                    dataClass.stock = cursor.getInt(cursor.getColumnIndex("stock"))

                    val data = DetailTransaksi(dataClass.id_detail_transaksi, dataClass.id_barang, dataClass.kode_barang, dataClass.nama_barang, dataClass.stock)
                    dataList.add(data)
                }while (cursor.moveToNext())
            }
        }

        return dataList
    }

    //checkout
    fun deleteCheckout(idDetailTransaksi: String, idBarang: Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants._ID, idBarang)

        val success = db.delete(Constants.TBL_DETAIL_TRANSAKSI, "id_detail_transaksi="+ idDetailTransaksi+" AND id_barang=" + idBarang, null)
        db.close()
        return success
    }

}