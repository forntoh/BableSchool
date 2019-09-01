package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

abstract class RankingRepository : BaseRepository() {

    abstract suspend fun retrieveTopSchools(): LiveData<MutableList<TopSchool>>

    abstract suspend fun retrieveTopStudents(): LiveData<MutableList<TopStudent>>
    abstract fun resetState()
}