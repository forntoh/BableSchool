package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

interface RankingRepository {

    suspend fun retrieveTopSchools(): MutableList<TopSchool>

    suspend fun retrieveTopStudents(): MutableList<TopStudent>
}