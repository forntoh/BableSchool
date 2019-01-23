package ga.forntoh.bableschool.data.model.forum

import com.google.firebase.database.IgnoreExtraProperties
import com.stfalcon.chatkit.commons.models.IUser

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