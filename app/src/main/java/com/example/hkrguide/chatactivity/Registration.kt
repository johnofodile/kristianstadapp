package com.example.hkrguide.chatactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hkrguide.BaseActivity
import com.example.hkrguide.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*

class Registration : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val email= emailL.text.toString()
        val password=passwordL.text.toString()
        Log.d("Registration","Email is:" + email)
        Log.d("Registration","Password: $password")
        loginButtonL.setOnClickListener {
           performRegister()
        }
            alreadyHaveAccount.setOnClickListener {
                Log.d("Registration","Try to show registration")
                //launch the launch activity
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }
    private fun performRegister(){
        val email = emailL.text.toString()
        val password = passwordL.text.toString()
        if(email.isEmpty()||password.isEmpty()) {
            Toast.makeText(this,"please enter tyext in email or password",
                Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Registration", "Email is:" + email)
        Log.d("Registration", "Password: $password")
        FirebaseAuth.getInstance().
        createUserWithEmailAndPassword(email,password).
        addOnCompleteListener(){
            if( !it.isSuccessful) return@addOnCompleteListener
            // else if succesful
            Log.d("Registration",
                "succesfully created user with uid: ${it.result?.user?.uid}")
        }
            .addOnFailureListener{
                Log.d("Registration","failed to create user: ${it.message}")
                Toast.makeText(this,
                    "failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }

    }

    }
