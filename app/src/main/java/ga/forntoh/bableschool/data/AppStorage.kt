package ga.forntoh.bableschool.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ga.forntoh.bableschool.data.model.main.User
import ga.forntoh.bableschool.internal.DataKey
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class AppStorage {

    companion object {

        @Volatile
        private var INSTANCE: AppStorage? = null

        operator fun invoke(context: Context): AppStorage = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: AppStorage().also {
                                it.preferences = context.getSharedPreferences(context.packageName + ".DB", Context.MODE_PRIVATE)
                                INSTANCE = it
                            }
                }

    }

    private lateinit var preferences: SharedPreferences

    fun saveUser(user: User) = save("user", Gson().toJson(user))

    fun loadUser(): User? = Gson().fromJson(load("user"), object : TypeToken<User?>() {}.type)

    fun clearUser() = clear("user")

    fun getLastSaved(key: DataKey): ZonedDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(preferences.getLong(key.name, 0)),
            ZoneId.systemDefault()
    )

    fun setLastSaved(key: DataKey, time: ZonedDateTime) {
        preferences.edit().apply { putLong(key.name, time.toInstant().toEpochMilli()); apply() }
    }

    fun getChangedPassword(): Boolean {
        val user = loadUser()
        return if (user != null) {
            user.username != user.profileDataMap()["Password"]
        } else false
    }

    fun clearLastSaved(key: DataKey) = clear(key.name)

    private fun save(key: String, value: String?) {
        if (value.isNullOrEmpty()) return
        val editor = preferences.edit()
        editor.putString(key, encode(value))
        editor.apply()
    }

    private fun load(key: String): String = with(preferences.getString(key, "")) {
        if (this.isNullOrEmpty()) "" else decode(this)
    }

    private fun clear(key: String) {
        preferences.edit().apply { remove(key); apply() }
    }

    private fun decode(thing: String): String = String(Base64.decode(thing, Base64.DEFAULT))

    private fun encode(thing: String): String = Base64.encodeToString(thing.toByteArray(), Base64.DEFAULT)

}
