package ga.forntoh.bableschool.data.db

import com.dbflow5.query.select
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

class RankingDaoImpl(private val database: AppDatabase) : RankingDao {

    override fun retrieveTopSchools(): MutableList<TopSchool> =
            (select from TopSchool::class).queryList(database)

    override fun retrieveTopStudents(): MutableList<TopStudent> =
            (select from TopStudent::class).queryList(database)

    override fun saveTopSchools(vararg topSchools: TopSchool) =
            topSchools.forEach { it.save(database) }

    override fun saveTopStudents(vararg topStudents: TopStudent) =
            topStudents.forEach { it.save(database) }
}