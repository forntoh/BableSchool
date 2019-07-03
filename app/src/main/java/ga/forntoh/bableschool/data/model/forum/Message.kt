package ga.forntoh.bableschool.data.model.forum

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

@IgnoreExtraProperties
data class Message(private var id: String? = "0", private var text: String = "", @PropertyName("createdAt") var timestampCreated: HashMap<String, Any>? = null, private var user: FUser? = FUser(), var userId: String = "") : IMessage {

    override fun getId(): String? = id

    override fun getText(): String? = text

    override fun getUser(): IUser? = user

    override fun getCreatedAt(): Date = Date(if (timestampCreated?.get("timestamp") == null) 0 else timestampCreated?.get("timestamp") as Long)

    fun setUser(value: FUser?) {
        value?.let { user = it }
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