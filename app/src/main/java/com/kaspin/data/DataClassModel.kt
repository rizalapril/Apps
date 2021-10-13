package com.kaspin.data.model

data class BarangDataClass(var id_barang: Int = 0,
                           var kode_barang: String = "",
                           var nama_barang: String = "",
                           var stock: Int = 0)

data class HeaderTransaksiDataClass(var id_transaksi: Int? = 0,
                                    var id_detail_transaksi: String = "",
                                    var nama_transaksi: String = "",
                                    var status: Int = 0)

data class DetailTransaksiDataClass(var id_detail_transaksi: String = "",
                                    var id_barang: Int = 0,
                                    var kode_barang: String = "",
                                    var nama_barang: String = "",
                                    var stock: Int = 0)