package ga.forntoh.bableschool.data.model.forum

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.stfalcon.chatkit.commons.models.IDialog

@IgnoreExtraProperties
class DefaultDialog(private val id: String = "", private val dialogPhoto: String = "", private val dialogName: String = "", private var lastMessage: Message? = null, private var unreadCount: Int = 0, private var users: List<FUser> = ArrayList(), val lastMessageId: String? = null, val activeForumUsers: List<String>? = null) : IDialog<Message> {

    override fun getId(): String = id

    override fun getDialogPhoto(): String = dialogPhoto

    override fun getDialogName(): String = dialogName

    override fun getUsers(): List<FUser> = users

    @Exclude
    override fun getLastMessage(): Message? = lastMessage

    @Exclude
    override fun setLastMessage(message: Message?) {
        this.lastMessage = message
    }

    fun setUnreadCount(count: Int) {
        unreadCount = count
    }

    override fun getUnreadCount(): Int = unreadCount

    fun addUser(user: FUser) {
        (users as ArrayList).add(user)
    }

}
