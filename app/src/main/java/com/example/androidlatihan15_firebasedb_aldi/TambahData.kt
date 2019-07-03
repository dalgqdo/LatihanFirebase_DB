package com.example.androidlatihan15_firebasedb_aldi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.androidlatihan15_firebasedb_aldi.helpers.PrefHelper
import com.example.androidlatihan15_firebasedb_aldi.model.BukuModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tambah_data.*

class TambahData : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var helperPref: PrefHelper
    var datax : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah_data)

        datax = intent.getStringExtra("kode")
        helperPref = PrefHelper(this)

        if (datax != null){
            showdataFromDB()
        }

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
        val counterID : Int
        if (datax !=  null){
            counterID = datax!!.toInt()
        }else{
            counterID = helperPref.getCounterId()
        }

        dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/$uidUser")
        dbRef.child("$counterID/id").setValue(uidUser)
        dbRef.child("$counterID/nama").setValue(nama)
        dbRef.child("$counterID/judulBuku").setValue(judul)
        dbRef.child("$counterID/tanggal").setValue(tgl)
        dbRef.child("$counterID/description").setValue(desc)

        Toast.makeText(this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show()

        if(datax == null){
            helperPref.saveCounterId(counterID+1)
        }

//        helperPref.saveCounterId(counterID + 1)
        onBackPressed()
    }

    fun showdataFromDB(){
        dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/${helperPref.getUID()}/${datax}")
        dbRef. addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                        val buku = p0.getValue(BukuModel::class.java)
                        et_namaPenulis.setText(buku!!.getNama())
                        et_judulBuku.setText(buku.getJudulBuku())
                        et_tanggal.setText(buku.getTanggal())
                        et_deskripsi.setText(buku.getDescription())


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}