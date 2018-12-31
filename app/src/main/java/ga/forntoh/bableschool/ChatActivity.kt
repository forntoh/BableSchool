package ga.forntoh.bableschool

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
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
import kotlinx.android.synthetic.main.toolbar_chat.*

class ChatActivity : BaseActivity() {

    private lateinit var dialog: DefaultDialog
    private val fUsers: ArrayList<FUser> = ArrayList()
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val messageInput by lazy { findViewById<MessageInput>(R.id.input) }
    private val messagesList by lazy { findViewById<MessagesList>(R.id.messagesList) }
    private val messagesListAdapter by lazy {
        MessagesListAdapter<Message>(StorageUtil.getInstance(this).loadMatriculation(), ImageLoader { imageView, url, _ ->
            imageView.setBackgroundColor(Color.WHITE)
            Picasso.get().load(url).fit().centerCrop().into(imageView)
        })
    }
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
            database.getReference("unseenMsgCountData").child(dialog.id).child(StorageUtil.getInstance(baseContext).loadMatriculation()).setValue(0)
            message.id = p0.key
            message.setUser(fUsers.find { it.id == message.userId })
            when (i) {
                0 -> messagesListAdapter.addToStart(message, true)
                1 -> messagesListAdapter.deleteById(message.id)
                else -> messagesListAdapter.update(message)
            }
        }
    }
    private val toolbarTitle by lazy { findViewById<TextView>(R.id.title) }
    private val toolbarSubtitle by lazy { findViewById<TextView>(R.id.subtitle) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        disableFlags(true)
        enableWhiteStatusBar()

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dialog = Gson().fromJson(intent.getStringExtra("dialog"), DefaultDialog::class.java)

        toolbarTitle.text = dialog.dialogName
        toolbarSubtitle.text = "${dialog.activeForumUsers!!.size} students"
        Picasso.get().load(dialog.dialogPhoto).fit().centerCrop().into(photo)

        val myRef = database.getReference("forumGroup").child(dialog.id)

        fUsers.addAll(dialog.users)

        myRef.addChildEventListener(messageListener)

        messagesList.setAdapter(messagesListAdapter)

        messageInput.setInputListener {
            if (it.trim().isNotEmpty()) {
                val message = Message(myRef.push().key, it.toString().trim(), hashMapOf(Pair("timestamp", ServerValue.TIMESTAMP)), null, StorageUtil.getInstance(baseContext).loadMatriculation())

                val messageValues = message.toMap()
                val childUpdates = HashMap<String, Any>()
                childUpdates["""/forumGroup/${dialog.id}/${message.id}"""] = messageValues
                database.getReference("forumGroupMetadata").child(dialog.id).child("lastMessageId").setValue(message.id)

                database.reference.updateChildren(childUpdates)

                dialog.activeForumUsers?.filter { id -> id != StorageUtil.getInstance(baseContext).loadMatriculation() }?.forEach { uid ->
                    database.getReference("unseenMsgCountData").child(dialog.id).child(uid).apply {
                        addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) = Unit

                            override fun onDataChange(p0: DataSnapshot) {
                                setValue(p0.getValue(Long::class.java)?.plus(1) ?: 1)
                            }
                        })
                    }
                }
            }
            it.isNotEmpty()
        }
    }
}
