package ga.forntoh.bableschool.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

@IgnoreExtraProperties
class FUser(private val id: String? = null, private val name: String? = null, private val avatar: String? = null) : IUser {

    override fun getId(): String? {
        return id
    }

    override fun getName(): String? {
        return name
    }

    override fun getAvatar(): String? {
        return avatar
    }
}

@IgnoreExtraProperties
data class Message(private var id: String? = "", private var text: String = "", @PropertyName("createdAt") var timestampCreated: HashMap<String, Any>? = null, private var user: FUser? = null, var userId: String = "") : IMessage {

    override fun getId(): String? = id

    override fun getText(): String? = text

    override fun getUser(): IUser? = user

    override fun getCreatedAt(): Date = Date(timestampCreated?.get("timestamp") as Long)

    fun setUser(value: FUser?) {
        user = value
    }

    fun setId(key: String?) {
        id = key
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "text" to text,
                "userId" to userId,
                "createdAt" to timestampCreated
        )
    }

}

@IgnoreExtraProperties
class DefaultDialog(private val id: String = "", private val dialogPhoto: String = "", private val dialogName: String = "", private var lastMessage: Message? = null, private var unreadCount: Int = 0, private var users: List<FUser>? = null, val lastMessageId: String? = null, val activeForumUsers: List<String>? = null) : IDialog<Message> {

    override fun getId(): String = id

    override fun getDialogPhoto(): String = dialogPhoto

    override fun getDialogName(): String = dialogName

    override fun getUsers(): List<FUser>? = users

    @Exclude
    override fun getLastMessage(): Message? = lastMessage

    @Exclude
    override fun setLastMessage(message: Message?) {
        this.lastMessage = message
    }

    override fun getUnreadCount(): Int {
        return unreadCount
    }

    fun setUsers(list: List<FUser>) {
        users = list
    }

}
