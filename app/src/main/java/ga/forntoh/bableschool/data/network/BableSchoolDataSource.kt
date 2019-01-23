package ga.forntoh.bableschool.data.network

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.AnnualRank
import ga.forntoh.bableschool.data.model.other.Likes
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

interface BableSchoolDataSource {

    val downloadedCategories: LiveData<List<Category>>
    val downloadedTopSchools: LiveData<List<TopSchool>>
    val downloadedCourseNotes: LiveData<List<Course>>
    val downloadedTopStudents: LiveData<List<TopStudent>>
    val downloadedNews: LiveData<List<News>>
    val downloadedTimetable: LiveData<List<Period>>
    val downloadedComment: LiveData<Comment>
    val downloadedLikes: LiveData<Likes>
    val downloadedUserProfile: LiveData<User>
    val downloadedTermScores: LiveData<List<Score>>
    val downloadedAnnualRank: LiveData<AnnualRank>

    suspend fun categories()
    suspend fun topSchools()
    suspend fun getCourseNotes(uid: String)
    suspend fun getTopStudents(uid: String)
    suspend fun getNews(uid: String)
    suspend fun getTimetable(clazz: String)
    suspend fun postComment(comment: Comment)
    suspend fun likeNews(uid: String, newsId: Long)
    suspend fun getUserProfile(uid: String, password: String)
    suspend fun getTermScores(uid: String, term: Int, year: String)
    suspend fun annualRank(uid: String, year: String)

}