package ga.forntoh.bableschool.data.db

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
object AppDatabase {
    const val NAME: String = "AppDB"
    const val VERSION = 2
}