package com.example.androidlatihan15_firebasedb_aldi.model

class BukuModel {
    private var nama: String? = null
    private var tanggal: String? = null
    private var judulBuku: String? = null
    private var id: String? = null

    constructor()
    constructor(nama: String, tanggal: String, judul: String) {
        this.nama = nama
        this.tanggal = tanggal
        this.judulBuku = judul
    }

    fun getId(): String {
        return id!!
    }

    fun getNama(): String {
        return nama!!
    }

    fun getTanggal(): String {
        return tanggal!!
    }

    fun getJudulBuku(): String {
        return judulBuku!!
    }


    fun setId(id: String) {
        this.id = id
    }

    fun setNama(nama: String) {
        this.nama = nama
    }

    fun setTanggal(tanggal: String) {
        this.tanggal = tanggal
    }

    fun setJudul(judul: String) {
        this.judulBuku = judul
    }
}