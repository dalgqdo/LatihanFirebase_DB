package com.example.androidlatihan15_firebasedb_aldi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import android.widget.Toast
import com.example.androidlatihan15_firebasedb_aldi.adapters.BukuAdapter
import com.example.androidlatihan15_firebasedb_aldi.helpers.PrefHelper
import com.example.androidlatihan15_firebasedb_aldi.model.BukuModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.halaman_depan.*

class HalamanDepan : AppCompatActivity(), BukuAdapter.FirebaseDataListener {

    override fun onUpdate(buku: BukuModel, position: Int) {
            val datax = buku.getKey()
            val intent = Intent(this, TambahData::class.java)
            intent.putExtra("kode", datax)
            startActivity(intent)
        }


    override fun onDeleteData(buku: BukuModel, position: Int) {
        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        if (dbref != null) {
            dbref.child(buku.getKey()).removeValue().addOnSuccessListener {
                Toast.makeText(this, "data berhasil dihapus", Toast.LENGTH_SHORT).show()
                bukuAdapter!!.notifyDataSetChanged()
            }
        }
    }


    private var bukuAdapter: BukuAdapter? = null
    private var rcView: RecyclerView? = null
    private var list: MutableList<BukuModel>? = null
    lateinit var dbref: DatabaseReference
    lateinit var helperPrefs: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_depan)
        helperPrefs = PrefHelper(this)
        rcView = findViewById(R.id.rc_view)
        rcView!!.layoutManager = LinearLayoutManager(this)
        rcView!!.setHasFixedSize(true)

        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                e("TAGERROR", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList<BukuModel>()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(BukuModel::class.java)
                    addDataAll!!.setKey(dataSnapshot.key!!)
                    list!!.add(addDataAll!!)
                    bukuAdapter = BukuAdapter(this@HalamanDepan, list!!)
                }
                rcView!!.adapter = bukuAdapter
            }

        })

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, TambahData::class.java))

        }
    }


}