package com.example.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var profileName: TextView
    private lateinit var backBtn: ImageView

    var receiverRoom: String?=null
    var senderRoom: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name= intent.getStringExtra("name")
        val receiverUid= intent.getStringExtra("uid")
        val senderUid= FirebaseAuth.getInstance().currentUser?.uid


        mDbRef= FirebaseDatabase.getInstance().getReference()
        senderRoom= receiverUid + senderUid
        receiverRoom= senderUid + receiverUid

        toolBar= findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        backBtn= findViewById(R.id.backbutton)
        profileName= findViewById(R.id.profile)
        profileName.text=name
        chatRecyclerView= findViewById(R.id.chatRecyclerview)
        messageBox= findViewById(R.id.messageBox)
        sendButton= findViewById(R.id.send_btn)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList, senderRoom!!, receiverRoom!!)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message= postSnapshot.getValue(Message::class.java)
                        message?.messageId=postSnapshot.key
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        sendButton.setOnClickListener {
            val message= messageBox.text.toString()
            val messageObject= Message(message, senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages")
                        .push().setValue(messageObject)
                }
            messageBox.setText("")
        }
        backBtn.setOnClickListener {
            finish()
        }

    }
}