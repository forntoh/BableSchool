package ga.forntoh.bableschool.data.model.forum

import com.google.firebase.database.IgnoreExtraProperties
import com.stfalcon.chatkit.commons.models.IUser

@IgnoreExtraProperties
class FUser(private val id: String? = "", private val name: String? = "", private val avatar: String? = "", var color: String? = "#FFF") : IUser {

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