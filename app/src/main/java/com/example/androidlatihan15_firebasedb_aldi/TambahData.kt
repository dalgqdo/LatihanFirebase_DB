package com.example.androidlatihan15_firebasedb_aldi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.androidlatihan15_firebasedb_aldi.helpers.PrefHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.tambah_data.*

class TambahData : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var helperPref: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah_data)

        helperPref = PrefHelper(this)
        btn_simpan.setOnClickListener {
            val nama = et_namaPenulis.text.toString()
            val judul = et_judulBuku.text.toString()
            val tgl = et_tanggal.text.toString()
            val desc = et_deskripsi.text.toString()

            if (nama.isNotEmpty() || judul.isNotEmpty() || tgl.isNotEmpty() || desc.isNotEmpty()) {
                simpanToFireBase(nama, judul, tgl, desc)
            } else {
                Toast.makeText(this, "inputan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun simpanToFireBase(nama: String, judul: String, tgl: String, desc: String) {
        val uidUser = helperPref.getUID()
        val counterID = helperPref.getCounterId()

        dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/$uidUser")
        dbRef.child("$counterID/id").setValue(uidUser)
        dbRef.child("$counterID/nama").setValue(nama)
        dbRef.child("$counterID/judulBuku").setValue(judul)
        dbRef.child("$counterID/tanggal").setValue(tgl)
        dbRef.child("$counterID/description").setValue(desc)

        Toast.makeText(this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show()

        helperPref.saveCounterId(counterID + 1)
        onBackPressed()
    }

}