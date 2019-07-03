package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.Period

interface PeriodRepository {

    suspend fun retrievePeriods(): MutableList<Period>
}