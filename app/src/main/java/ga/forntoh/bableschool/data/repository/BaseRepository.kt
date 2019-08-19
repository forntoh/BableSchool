package ga.forntoh.bableschool.data.repository

import kotlinx.coroutines.CoroutineScope

abstract class BaseRepository {
    lateinit var scope: CoroutineScope
}