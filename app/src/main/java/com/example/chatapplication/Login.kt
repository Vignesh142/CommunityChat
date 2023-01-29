package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp : Button

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth= FirebaseAuth.getInstance()

        editEmail= findViewById(R.id.edit_email)
        editPassword= findViewById(R.id.edit_password)
        btnLogin= findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signUp)
        supportActionBar?.hide()
        btnSignUp.setOnClickListener{
            val intent= Intent(this,SignUp:: class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener{
            val email= editEmail.text.toString()
            val password= editPassword.text.toString()
            if(email == "" || password == "")
                Toast.makeText(this@Login,"Fill all the details",Toast.LENGTH_SHORT)
                    .show()
            else
                login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent= Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Login, "User does not exists",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}