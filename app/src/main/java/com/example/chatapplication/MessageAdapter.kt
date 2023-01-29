package com.example.chatapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MessageAdapter(val context: Context, val messageList: ArrayList<Message>,val senderRoom: String,val receiverRoom:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val ITEM_RECEIVE=1
        val ITEM_SENT=2

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage= itemView.findViewById<TextView>(R.id.txt_sent_msg)
    }
    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage= itemView.findViewById<TextView>(R.id.txt_receive_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            val view: View= LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }else{
            val view: View= LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage= messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currMessage.senderId))
            return ITEM_SENT
        else
            return ITEM_RECEIVE
    }
    override fun onBindViewHolder(Mainholder: RecyclerView.ViewHolder, position: Int) {
        val currMessege= messageList[position]

        if(Mainholder.javaClass==SentViewHolder::class.java) {
            val holder = Mainholder as SentViewHolder
            holder.sentMessage.text = currMessege.message
            holder.itemView.setOnClickListener {
                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_msg, null)
                val dialog = AlertDialog.Builder(context).setTitle("Delete Message")
                    .setView(view).create()
                view.findViewById<TextView>(R.id.everyone).setOnClickListener {
                    currMessege.message = "This message is removed"
                    currMessege.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom).child("messages").child(it1).setValue(currMessege)
                    }
                    currMessege.messageId?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(receiverRoom).child("messages").child(it1)
                                .setValue(currMessege)
                    }
                    dialog.dismiss()
                }
                view.findViewById<TextView>(R.id.me).setOnClickListener {
                    currMessege.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom).child("messages").child(it1).setValue(null)
                    }
                    dialog.dismiss()
                }
                view.findViewById<TextView>(R.id.cancel).setOnClickListener { dialog.dismiss() }
                dialog.show()
            }
        }
        else{
                val holder = Mainholder as ReceiveViewHolder
                holder.receiveMessage.text = currMessege.message
                holder.itemView.setOnClickListener {
                    val view: View = LayoutInflater.from(context).inflate(R.layout.delete_msg, null)
                    val dialog = AlertDialog.Builder(context).setTitle("Delete Message")
                        .setView(view).create()
                    view.findViewById<TextView>(R.id.everyone).setOnClickListener {
                        currMessege.message = "This message is removed"
                        currMessege.messageId?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(senderRoom).child("messages").child(it1)
                                .setValue(currMessege)
                        }
                        currMessege.messageId?.let { it1 ->
                                FirebaseDatabase.getInstance().reference.child("chats")
                                    .child(receiverRoom).child("messages").child(it1)
                                    .setValue(currMessege)
                        }
                        dialog.dismiss()
                    }
                    view.findViewById<TextView>(R.id.me).setOnClickListener {
                        currMessege.messageId?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(senderRoom).child("messages").child(it1).setValue(null)
                        }
                        dialog.dismiss()
                    }
                    view.findViewById<TextView>(R.id.cancel).setOnClickListener { dialog.dismiss() }
                    dialog.show()
                }
            }
        }
}