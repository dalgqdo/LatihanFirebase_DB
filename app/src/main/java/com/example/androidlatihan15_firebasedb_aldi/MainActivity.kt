package com.example.androidlatihan15_firebasedb_aldi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Log.e
import android.widget.Toast
import com.example.androidlatihan15_firebasedb_aldi.helpers.PrefHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var RC_SIGN_IN = 7
    private lateinit var mGoogleSignIn: GoogleSignInClient
    private lateinit var fAuth: FirebaseAuth
    private lateinit var helperPref: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helperPref = PrefHelper(this)
        fAuth = FirebaseAuth.getInstance()
        helperPref = PrefHelper(this)

        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignIn = GoogleSignIn.getClient(this, gso)
        sign_in_button.setOnClickListener { signIn() }
        btn_reg.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email.isNotEmpty() || password.isNotEmpty() || !email.equals("") || !password.equals("")) {
                fAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        startActivity(Intent(this, HalamanDepan::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "LOGIN GAGAL", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "LOGIN GAGAL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignIn.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("FAUTH_LOGIN", "firebaseAuth : ${account.id}")

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            if (it.isSuccessful) {
//                Toast.makeText(this,
//                    "Login Sukses ${fAuth.currentUser!!.displayName}",
//                    Toast.LENGTH_SHORT).show()
                val user = fAuth.currentUser
                updateUI(user)
            } else {
                Toast.makeText(
                    this,
                    "GAGAL LOGIN", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            helperPref.saveUID(user.uid)
            startActivity(Intent(this, HalamanDepan::class.java))
        } else {
            e("TAG_GAGAL", "USER TIDAK ADA")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("AUTH_LOGIN", "LOGIN GAGAL", e)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = fAuth.currentUser
        if (user != null) {
            updateUI(user)
        }
    }


}
