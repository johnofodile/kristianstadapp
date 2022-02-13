package com.example.hkrguide.chatactivity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.hkrguide.BaseActivity
import com.example.hkrguide.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        UsernameL.visibility = View.INVISIBLE
        loginButtonL.setOnClickListener {
            val email=emailL.text.toString()
            val password=passwordL.text.toString()
            Log.d("Login","Attempt login with email/pw")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
              //  .addOnCompleteListener {  }
        }
    }
}