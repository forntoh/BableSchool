package ga.forntoh.bableschool.ui.category.forum

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.model.forum.DefaultDialog
import ga.forntoh.bableschool.data.model.forum.FUser
import ga.forntoh.bableschool.data.model.forum.Message
import ga.forntoh.bableschool.internal.IncomingMessageViewHolder
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.utilities.enableWhiteStatusBar
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.toolbar_chat.*

class ChatActivity : BaseActivity() {

    private lateinit var chatDialog: DefaultDialog
    private val fUsers: ArrayList<FUser> = ArrayList()
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val messagesListAdapter by lazy {
        val holdersConfig = MessageHolders().apply {
            setIncomingTextConfig(IncomingMessageViewHolder::class.java ,R.layout.item_incoming_text_message)
        }
        MessagesListAdapter<Message>(AppStorage(this).loadUser()?.profileData?.matriculation, holdersConfig) { imageView, url, _ ->
            imageView.setBackgroundColor(Color.WHITE)
            Picasso.get().load(url).fit().centerCrop().into(imageView)
        }
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
            database.getReference("unseenMsgCountData").child(chatDialog.id).child(AppStorage(this@ChatActivity).loadUser()?.profileData?.matriculation
                    ?: return).setValue(0)
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

        chatDialog = Gson().fromJson(intent.getStringExtra("dialog"), DefaultDialog::class.java)

        toolbarTitle.text = chatDialog.dialogName
        toolbarSubtitle.text = "${chatDialog.activeForumUsers!!.size} students"
        Picasso.get().load(chatDialog.dialogPhoto).fit().centerCrop().into(photo)

        val myRef = database.getReference("forumGroup").child(chatDialog.id)

        fUsers.addAll(chatDialog.users)
        fUsers.forEachIndexed { index, user ->
            user.color = resources.getStringArray(R.array.material_colors)[index % fUsers.size]
        }

        myRef.addChildEventListener(messageListener)

        messagesList.setAdapter(messagesListAdapter)

        input.setInputListener {
            if (it.trim().isNotEmpty()) {
                val message = Message(myRef.push().key, it.toString().trim(), hashMapOf(Pair("timestamp", ServerValue.TIMESTAMP)), null, AppStorage(this).loadUser()?.profileData?.matriculation
                        ?: return@setInputListener false)

                val messageValues = message.toMap()
                val childUpdates = HashMap<String, Any>()
                childUpdates["""/forumGroup/${chatDialog.id}/${message.id}"""] = messageValues
                database.getReference("forumGroupMetadata").child(chatDialog.id).child("lastMessageId").setValue(message.id)

                database.reference.updateChildren(childUpdates)

                chatDialog.activeForumUsers?.filter { id -> id != AppStorage(this).loadUser()?.profileData?.matriculation }?.forEach { uid ->
                    database.getReference("unseenMsgCountData").child(chatDialog.id).child(uid).apply {
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
