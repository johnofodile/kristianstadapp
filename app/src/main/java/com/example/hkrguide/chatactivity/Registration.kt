package com.example.hkrguide.chatactivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.hkrguide.BaseActivity
import com.example.hkrguide.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class Registration : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        loginButtonL.setOnClickListener {
           performRegister()
        }
            alreadyHaveAccount.setOnClickListener {
                Log.d("Registration","Try to show registration")
                //launch the launch activity
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        photoButton.setOnClickListener {
            Log.d("Registration","Try to show photo selection")
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }
        }
    var selectedPhotoUri: Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Registration", "photo was selected")
             selectedPhotoUri =data.data
            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            hdRound.setImageBitmap(bitmap)
            photoButton.alpha=0f
          //  val bitmapDrawable= BitmapDrawable(bitmap)
          //  photoButton.setBackgroundDrawable(bitmapDrawable)

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

        Log.d("Registration", "performRegisterEmail is:" + email)
        Log.d("Registration", "performregisterPassword: $password")
        FirebaseAuth.getInstance().
        createUserWithEmailAndPassword(email,password).
        addOnCompleteListener(){
            if( !it.isSuccessful) return@addOnCompleteListener
            // else if succesful
            Log.d("Registration",
                "succesfully created user with uid: ${it.result?.user?.uid}")
            uploadImageToFirebaseStorage()

        }
            .addOnFailureListener{
                Log.d("Registration","failed to create user: ${it.message}")
                Toast.makeText(this,
                    "failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }

    }
    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri==null)return
        val filename=UUID.randomUUID().toString()
    val ref=    FirebaseStorage.getInstance().getReference("/images/$filename")
  ref.putFile(selectedPhotoUri!!)
      .addOnSuccessListener { it ->
          Log.d("Registration","Successfully uploaded image:${it.metadata?.path}")
          ref.downloadUrl.addOnSuccessListener {

              Log.d("Registration","File Location: $ it")
              saveUserToFirebaseDatabase(it.toString())
          }
      }
      .addOnFailureListener{
        Log.d("Registration","Failed to upload image to storage:  ${it.message}")
      }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
       val uid=  FirebaseAuth.getInstance().uid?:""
       val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=User(uid,UsernameL.text.toString(),profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener{
                Log.d("Registration","finally we saved the user to firebase database")
                val intent=Intent(this,LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("Registration","Failed to set value to database: ${it.message}")
            }
    }

    }
class User(val uid:String,val username:String,val profileImageUrl:String) {
    constructor() : this("","","")
}



