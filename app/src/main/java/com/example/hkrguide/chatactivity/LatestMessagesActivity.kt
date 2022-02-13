package com.example.hkrguide.chatactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.hkrguide.BaseActivity
import com.example.hkrguide.R
import com.google.firebase.auth.FirebaseAuth

class LatestMessagesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        val toolbar = findViewById<Toolbar>(R.id.toolbar3)
        setSupportActionBar(toolbar)

        Log.d("LatestMessagesActivity","oncreatemethod")

        verifyUserIsLoggedIn()
        Log.d("LatestMessagesActivity","userverified")


    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, Registration::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("LatestMessagesActivity","menuinflaterfound")

        menuInflater.inflate(R.menu.navchat_menu, menu)
        Log.d("LatestMessagesActivity","menuinflaterfound")
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)



                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Registration::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)

    }


}


