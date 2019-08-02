package ga.forntoh.bableschool.data.db

import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

interface RankingDao {

    fun retrieveTopSchools(): MutableList<TopSchool>

    fun retrieveTopStudents(): MutableList<TopStudent>

    fun saveTopSchools(vararg topSchools: TopSchool)

    fun saveTopStudents(vararg topStudents: TopStudent)
}