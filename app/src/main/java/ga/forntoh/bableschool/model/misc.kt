package ga.forntoh.bableschool.model

data class AnnualRank(var average: String? = null, var position: String? = null)

data class TopSchool(val image: String? = null, val schoolName: String? = null, val topStudentName: String = "", val average: Double = 0.toDouble())

data class TopStudent(val image: String? = null, val name: String = "", val surname: String? = null, val school: String? = null, val average: Double = 0.toDouble())
