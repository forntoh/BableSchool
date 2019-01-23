package ga.forntoh.bableschool.data.model.forum

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

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