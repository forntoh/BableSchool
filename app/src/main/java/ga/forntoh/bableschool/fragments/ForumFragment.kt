package ga.forntoh.bableschool.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.dialogs.DialogsList
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.stfalcon.chatkit.utils.DateFormatter
import ga.forntoh.bableschool.ChatActivity
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.model.DefaultDialog
import ga.forntoh.bableschool.model.FUser
import ga.forntoh.bableschool.model.Message
import java.util.*

class ForumFragment : Fragment() {

    private val database by lazy { FirebaseDatabase.getInstance() }
    private val dialogsList by lazy { root.findViewById<DialogsList>(R.id.dialogsList) }
    private val dialogsListAdapter by lazy {
        DialogsListAdapter<DefaultDialog>(R.layout.item_dialog) { imageView, url, _ ->
            imageView.setBackgroundColor(Color.WHITE)
            Picasso.get().load(url).fit().centerCrop().into(imageView)
        }
    }
    private val dialogListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val forum = dataSnapshot.getValue(DefaultDialog::class.java) ?: return

            val users = ArrayList<FUser>()
            forum.setUsers(users)

            // Get list of users
            for (i in 0 until forum.activeForumUsers!!.size) {
                database.getReference("users")
                        .child(forum.activeForumUsers[i])
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                users.add(dataSnapshot.getValue(FUser::class.java)!!)
                            }

                            override fun onCancelled(databaseError: DatabaseError) = Unit
                        })
                if (i == forum.activeForumUsers.size - 1) {
                    dialogsListAdapter.updateItemById(forum)
                    getLastMessage(forum, true)
                }
            }
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            val forum = dataSnapshot.getValue(DefaultDialog::class.java) ?: return
            getLastMessage(forum, false)
        }

        private fun getLastMessage(forum: DefaultDialog, toAdd: Boolean) {
            database.getReference("forumGroup")
                    .child(forum.id)
                    .child(forum.lastMessageId!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val message = dataSnapshot.getValue(Message::class.java) ?: return
                            message.id = dataSnapshot.key
                            database.getReference("users")
                                    .child(message.userId)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            message.setUser(dataSnapshot.getValue(FUser::class.java))
                                            if (toAdd) {
                                                forum.lastMessage = message
                                                dialogsListAdapter.addItem(forum)
                                                dialogsListAdapter.sortByLastMessageDate()
                                            } else
                                                dialogsListAdapter.updateDialogWithMessage(forum.id, message)
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) = Unit
                                    })
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
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_forum, container, false)

        with(dialogsListAdapter) {
            setDatesFormatter { date ->
                return@setDatesFormatter when {
                    DateFormatter.isToday(date) -> DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString()
                    DateFormatter.isYesterday(date) -> getString(R.string.date_header_yesterday)
                    else -> DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR)
                }
            }
            setOnDialogClickListener { dialog ->
                startActivity(Intent(root.context, ChatActivity::class.java).apply {
                    putExtra("dialog", Gson().toJson(dialog))
                })
            }
            dialogsList.setAdapter<DefaultDialog>(this)
        }

        val myRef = database.getReference("forumGroupMetadata")

        myRef.addChildEventListener(dialogListener)

        return root
    }
}