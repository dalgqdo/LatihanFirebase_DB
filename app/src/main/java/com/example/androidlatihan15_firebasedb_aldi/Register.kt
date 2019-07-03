package com.example.androidlatihan15_firebasedb_aldi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.androidlatihan15_firebasedb_aldi.helpers.PrefHelper
import com.example.androidlatihan15_firebasedb_aldi.model.UsersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register_act.*

class Register : AppCompatActivity(){


    lateinit var ref : DatabaseReference
    lateinit var fAuth: FirebaseAuth
    lateinit var helperPref : PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_act)

        fAuth = FirebaseAuth.getInstance()
        helperPref = PrefHelper(this)

        btn_reg.setOnClickListener {
            val nama = et_nama.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (nama.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty() ||!email.equals("") ||
                !email.equals("") || !password.equals("")) {
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            saveData(nama, email, password)
                        } else {
                            Toast.makeText(this, "REGISTRASI GAGAL", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email/password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveData(nama: String, email: String, password: String) {
        val counterID = helperPref.getCounterId()

        ref = FirebaseDatabase.getInstance().getReference("Data User/${fAuth.currentUser?.uid}")
        ref.child("namaUser").setValue(nama)
        ref.child("Email").setValue(email)
        ref.child("Password").setValue(password)

        Toast.makeText(this, "REGISTRASI BERHASIL", Toast.LENGTH_SHORT).show()
        helperPref.saveCounterId(counterID + 1)
        onBackPressed()
    }

}