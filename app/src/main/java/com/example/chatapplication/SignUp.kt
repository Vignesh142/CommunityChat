package com.example.chatapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp : Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        editEmail= findViewById(R.id.edit_email)
        editName= findViewById(R.id.edit_name)
        editPassword= findViewById(R.id.edit_password)
        btnSignUp = findViewById(R.id.btn_signUp)
        mAuth= FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener{
            val name= editName.text.toString()
            val email= editEmail.text.toString()
            val password= editPassword.text.toString()
            if(name == "" || email == "" || password == "")
                Toast.makeText(this@SignUp,"Fill all the details",Toast.LENGTH_SHORT)
                    .show()
            else
                signUp(name,email,password)
        }
    }

    private fun signUp(name: String,email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    val intent= Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUp, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef= FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}