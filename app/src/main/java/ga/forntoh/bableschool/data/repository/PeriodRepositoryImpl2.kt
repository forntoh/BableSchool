package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.Period
import java.util.*

class PeriodRepositoryImpl2 : PeriodRepository {
    override suspend fun retrievePeriods(): MutableList<Period> = mutableListOf(
            Period("08:00", "10:00", "Electrocinétique", Calendar.MONDAY, "#59dbe0"),
            Period("12:00", "12:30", "Break", Calendar.MONDAY, "#3c3c3c"),
            Period("12:30", "14:30", "Algo et structure données", Calendar.MONDAY, "#f57f68"),
            Period("14:30", "16:30", "Electrostatique & Electromag", Calendar.MONDAY, "#87d288"),

            Period("08:00", "10:00", "Electrocinétique", Calendar.TUESDAY, "#59dbe0"),
            Period("10:00", "12:00", "Algèbre linéaire I", Calendar.TUESDAY, "#f8b552"),
            Period("12:00", "12:30", "Break", Calendar.TUESDAY, "#3c3c3c"),
            Period("12:30", "14:30", "Architecture ordinateurs et SE", Calendar.TUESDAY, "#FF4081"),
            Period("14:30", "16:30", "Electrostatique & Electromag", Calendar.TUESDAY, "#87d288"),

            Period("08:00", "10:00", "Analyse I", Calendar.WEDNESDAY, "#66A6FF"),
            Period("10:00", "12:00", "Optique", Calendar.WEDNESDAY, "#078B75"),
            Period("12:00", "12:30", "Break", Calendar.WEDNESDAY, "#3c3c3c"),
            Period("12:30", "16:30", "SEMINAIRE-ATELIER (HUAWEI) : Formation des talents aux TICS ", Calendar.WEDNESDAY, "#9C27B0"),

            Period("08:00", "10:00", "Architecture ordinateurs et SE", Calendar.THURSDAY, "#FF4081"),
            Period("10:00", "12:00", "Algèbre linéaire I", Calendar.THURSDAY, "#f8b552"),
            Period("12:00", "12:30", "Break", Calendar.THURSDAY, "#3c3c3c"),
            Period("12:30", "14:30", "Langues", Calendar.THURSDAY, "#f57f68"),

            Period("08:00", "10:00", "Optique", Calendar.FRIDAY, "#078B75"),
            Period("10:00", "12:00", "Algo et structure données", Calendar.FRIDAY, "#f57f68"),
            Period("12:00", "12:30", "Break", Calendar.FRIDAY, "#3c3c3c"),
            Period("12:30", "14:30", "Algo et structure données", Calendar.FRIDAY, "#f57f68"),
            Period("14:30", "16:30", "Sport", Calendar.FRIDAY, "#FF4081")
    )
}