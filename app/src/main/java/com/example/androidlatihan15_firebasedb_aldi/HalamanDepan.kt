package com.example.androidlatihan15_firebasedb_aldi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import com.example.androidlatihan15_firebasedb_aldi.adapters.BukuAdapter
import com.example.androidlatihan15_firebasedb_aldi.helpers.PrefHelper
import com.example.androidlatihan15_firebasedb_aldi.model.BukuModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.halaman_depan.*

class HalamanDepan : AppCompatActivity() {

    private var bukuAdapter: BukuAdapter? = null
    private var rcView: RecyclerView? = null
    private var list: MutableList<BukuModel> = ArrayList<BukuModel>()
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
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(BukuModel::class.java)
                    list.add(addDataAll!!)
                }
                bukuAdapter = BukuAdapter(applicationContext, list)
                rcView!!.adapter = bukuAdapter
            }

        })

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, TambahData::class.java))

        }
    }
}