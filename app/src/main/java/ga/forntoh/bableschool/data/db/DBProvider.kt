package ga.forntoh.bableschool.data.db

interface DBProvider<out T : AppDatabase> {
    val database: T
}