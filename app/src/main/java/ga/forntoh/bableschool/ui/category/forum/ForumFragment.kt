package ga.forntoh.bableschool.ui.category.forum

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.stfalcon.chatkit.utils.DateFormatter
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.model.forum.DefaultDialog
import ga.forntoh.bableschool.data.model.forum.FUser
import ga.forntoh.bableschool.data.model.forum.Message
import kotlinx.android.synthetic.main.fragment_forum.*

class ForumFragment : androidx.fragment.app.Fragment() {

    private val database by lazy { FirebaseDatabase.getInstance() }
    private val dialogsListAdapter by lazy {
        DialogsListAdapter<DefaultDialog>(R.layout.item_dialog) { imageView, url, _ ->
            if (!url.isNullOrEmpty()) Picasso.get().load(url).fit().centerCrop().into(imageView)
            else Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(imageView)
        }
    }
    private val dialogListener = object : ChildEventListener {

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val forum = dataSnapshot.getValue(DefaultDialog::class.java) ?: return
            getUsers(forum, true)
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            val forum = dataSnapshot.getValue(DefaultDialog::class.java) ?: return
            getUsers(forum, false)
        }

        private fun getUsers(forum: DefaultDialog, toAdd: Boolean) {
            if (forum.activeForumUsers?.find { it == AppStorage(context!!).loadUser()?.profileData?.matriculation }.isNullOrEmpty()) return
            forum.activeForumUsers?.forEach {
                database.getReference("users").child(it)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                forum.addUser(dataSnapshot.getValue(FUser::class.java)!!)
                                if (forum.users.size == forum.activeForumUsers.size) getLastMessage(forum, toAdd)
                            }

                            override fun onCancelled(databaseError: DatabaseError) = Unit
                        })
            }
        }

        private fun getLastMessage(forum: DefaultDialog, toAdd: Boolean) {
            database.getReference("forumGroup").child(forum.id).child(forum.lastMessageId!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val message = dataSnapshot.getValue(Message::class.java) ?: Message()
                            message.id = dataSnapshot.key
                            message.setUser(forum.users.find { it.id == message.userId })
                            forum.lastMessage = message
                            AppStorage(context!!).loadUser()?.profileData?.matriculation?.let { mat ->
                                database.getReference("unseenMsgCountData").child(forum.id)
                                        .child(mat)
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) = Unit

                                            override fun onDataChange(p0: DataSnapshot) {
                                                if (p0.value != null) forum.unreadCount = (p0.value as Long).toInt()
                                                if (toAdd) dialogsListAdapter.addItem(forum)
                                                else dialogsListAdapter.updateItemById(forum)
                                                dialogsListAdapter.sortByLastMessageDate()
                                            }
                                        })
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) = Unit
                    })
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(DefaultDialog::class.java)
            if (value != null) dialogsListAdapter.deleteById(value.id)
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) = Unit

        override fun onCancelled(databaseError: DatabaseError) = Unit
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_forum, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(dialogsListAdapter) {
            setDatesFormatter { date ->
                return@setDatesFormatter when {
                    DateFormatter.isToday(date) -> DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString()
                    DateFormatter.isYesterday(date) -> getString(R.string.date_header_yesterday)
                    else -> DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR)
                }
            }
            setOnDialogClickListener { dialog ->
                startActivity(Intent(context, ChatActivity::class.java).apply {
                    putExtra("dialog", Gson().toJson(dialog))
                })
            }
            dialogsList.setAdapter<DefaultDialog>(this)
        }

        val myRef = database.getReference("forumGroupMetadata")

        myRef.addChildEventListener(dialogListener)
    }
}