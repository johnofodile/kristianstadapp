package com.example.hkrguide.chatactivity

import android.os.Bundle
import android.util.Log
import com.example.hkrguide.BaseActivity
import com.example.hkrguide.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title="Select User"
       // val adapter= GroupAdapter<ViewHolder>()
       // adapter.add(UserItem())
      //  adapter.add(UserItem())
       // adapter.add(UserItem())

      //  messageView.adapter=adapter
        fetchUsers()
    }
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("NewMessage", "change")

                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    Log.d("NewMessage", "secoind")


                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                        Log.d("NewMessage", "secoind")

                    }
                }
                messageView.adapter = adapter

            }

             override fun onCancelled(error: DatabaseError) {

            }


        })
    }


    }




class UserItem(val user:User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.newMessageTextView.text=user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageViewNewMessage)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message    }

}



