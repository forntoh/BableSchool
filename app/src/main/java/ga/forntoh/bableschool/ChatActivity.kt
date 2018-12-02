package ga.forntoh.bableschool

import android.os.Bundle
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import ga.forntoh.bableschool.model.DefaultDialog
import ga.forntoh.bableschool.model.FUser
import ga.forntoh.bableschool.model.Message

class ChatActivity : BaseActivity() {

    private val database by lazy { FirebaseDatabase.getInstance() }
    private val messageInput by lazy { findViewById<MessageInput>(R.id.input) }
    private val messagesList by lazy { findViewById<MessagesList>(R.id.messagesList) }
    private val messagesListAdapter by lazy { MessagesListAdapter<Message>(StorageUtil.getInstance(this).loadMatriculation(), ImageLoader { imageView, url, _ -> Picasso.get().load(url).fit().centerCrop().into(imageView) }) }
    private val messageListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) = Unit

        override fun onChildMoved(p0: DataSnapshot, p1: String?) = Unit

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            loadUserInfo(p0, p0.getValue(Message::class.java) ?: return, 0)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            loadUserInfo(p0, p0.getValue(Message::class.java) ?: return, 1)
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            loadUserInfo(p0, p0.getValue(Message::class.java) ?: return, 2)
        }

        private fun loadUserInfo(p0: DataSnapshot, message: Message, i: Int) {
            message.id = p0.key
            database.getReference("users")
                    .child(message.userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            message.setUser(dataSnapshot.getValue(FUser::class.java))
                            when (i) {
                                0 -> messagesListAdapter.addToStart(message, true)
                                1 -> messagesListAdapter.deleteById(message.id)
                                else -> messagesListAdapter.update(message)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) = Unit
                    })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        disableFlags(true)
        enableWhiteStatusBar()

        val dialog = Gson().fromJson(intent.getStringExtra("dialog"), DefaultDialog::class.java)

        val myRef = database.getReference("forumGroup").child(dialog.id)

        myRef.orderByChild("createdAt/timestamp").addChildEventListener(messageListener)

        messagesList.setAdapter(messagesListAdapter)

        messageInput.setInputListener {
            if (it.isNotEmpty()) {
                val message = Message(myRef.push().key, it.toString(), hashMapOf(Pair("timestamp", ServerValue.TIMESTAMP)), null, StorageUtil.getInstance(this).loadMatriculation())

                val messageValues = message.toMap()
                val childUpdates = HashMap<String, Any>()
                childUpdates["""/forumGroup/${dialog.id}/${message.id}"""] = messageValues
                database.getReference("forumGroupMetadata").child(dialog.id).child("lastMessageId").setValue(message.id)

                database.reference.updateChildren(childUpdates)
            }
            it.isNotEmpty()
        }
    }
}
