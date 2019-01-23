package ga.forntoh.bableschool.data.db

import ga.forntoh.bableschool.data.model.main.Period

interface PeriodDao {

    fun retrievePeriods(): MutableList<Period>

    fun savePeriods(periods: List<Period>)
}