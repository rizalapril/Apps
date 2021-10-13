package com.kaspin.util

class Constants {
    companion object {
        val DB_VERSION = 1
        val DB_NAME = "kaspin.db"
        val TBL_BARANG = "tbl_barang"
        val TBL_TRANSAKSI = "tbl_transaksi"
        val TBL_DETAIL_TRANSAKSI = "tbl_detail_transaksi"

        //tbl barang field
        val _ID = "id_barang"
        val _KDBARANG = "kode_barang "
        val _NAMABARANG = "nama_barang"
        val _QTY = "stock"

        //tbl transaksi field
        val _ID_TRANSAKSI = "id_transaksi"
        val _ID_DETAIL_TRANSAKSI = "id_detail_transaksi"
        val _NAMA_TRANSAKSI = "nama_transaksi"
        val _STATUS_TRANSAKSI = "status"

        //tbl detail transaksi field
        val _ID_BARANG = "id_barang"
        val _KD_BARANG = "kode_barang "
        val _NAMA_BARANG = "nama_barang"
        val _QTY_ = "stock"

        //pref
        val PREF_ID_DETAIL_TRANSAKSI = "pref_id_detail_transaksi"
    }
}