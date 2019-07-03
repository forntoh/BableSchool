package ga.forntoh.bableschool.data.db

import com.dbflow5.query.select
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.model.main.Period

class PeriodDaoImpl(private val database: AppDatabase) : PeriodDao {

    override fun retrievePeriods(): MutableList<Period> =
            (select from Period::class).queryList(database)

    override fun savePeriods(periods: List<Period>) =
            periods.forEach { it.save() }
}