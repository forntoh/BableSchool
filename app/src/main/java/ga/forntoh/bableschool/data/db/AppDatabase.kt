package ga.forntoh.bableschool.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.AnnualRank
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

@Database(entities = [Category::class, Comment::class, Course::class, Document::class, News::class, Period::class, Score::class, Video::class, AnnualRank::class, TopSchool::class, TopStudent::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun courseNoteDao(): CourseNoteDao
    abstract fun newsDao(): NewsDao
    abstract fun periodDao(): PeriodDao
    abstract fun rankingDao(): RankingDao
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "bableschool.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}