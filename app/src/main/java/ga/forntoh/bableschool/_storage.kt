package ga.forntoh.bableschool

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64

class StorageUtil {

    companion object {

        @Volatile
        private var INSTANCE: StorageUtil? = null

        fun getInstance(context: Context): StorageUtil = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: StorageUtil().also {
                                it.preferences = context.getSharedPreferences(context.packageName + ".DB", Context.MODE_PRIVATE)
                                INSTANCE = it
                            }
                }

    }

    private lateinit var preferences: SharedPreferences

    fun saveMatriculation(mat: String?) = save("mat", mat)

    fun loadMatriculation(): String = load("mat")

    fun deleteMatriculation() = clear("mat")

    fun savePassword(password: String?) = save("pass", password)

    fun loadPassword(): String = load("pass")

    fun saveClass(password: String?) = save("class", password)

    fun loadClass(): String = load("class")

    private fun save(key: String, value: String?) {
        if (value.isNullOrEmpty()) return
        val editor = preferences.edit()
        editor.putString(key, encode(value))
        editor.apply()
    }

    private fun load(key: String): String = with(preferences.getString(key, "")) {
        if (this.isEmpty()) "" else decode(this)
    }

    private fun clear(key: String) {
        preferences.edit().apply { remove(key); apply() }
    }

    private fun decode(thing: String): String = String(Base64.decode(thing, Base64.DEFAULT))

    private fun encode(thing: String): String = Base64.encodeToString(thing.toByteArray(), Base64.DEFAULT)

}
