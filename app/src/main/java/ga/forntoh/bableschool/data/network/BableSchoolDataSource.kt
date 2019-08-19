package ga.forntoh.bableschool.data.network

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.*

interface BableSchoolDataSource {

    val downloadedCategories: LiveData<List<Category>>
    val downloadedTopSchools: LiveData<List<TopSchool>>
    val downloadedCourseNotes: LiveData<List<CourseResponse>>
    val downloadedTopStudents: LiveData<List<TopStudent>>
    val downloadedNews: LiveData<List<NewsResponse>>
    val downloadedTimetable: LiveData<List<Period>>
    val downloadedComment: LiveData<Comment>
    val downloadedLikes: LiveData<Likes>
    val downloadedUserProfile: LiveData<User>
    val downloadedTermScores: LiveData<List<ScoreWithCourse>>
    val downloadedAnnualRank: LiveData<AnnualRank>

    suspend fun categories()
    suspend fun topSchools()
    suspend fun getCourseNotes(uid: String)
    suspend fun getTopStudents(uid: String)
    suspend fun getNews(uid: String)
    suspend fun getTimetable(clazz: String?, school: String?)
    suspend fun postComment(comment: Comment)
    suspend fun likeNews(uid: String, newsId: Long)
    suspend fun getUserProfile(uid: String, password: String)
    suspend fun updatePassword(uid: String, oldPassword: String, newPassword: String)
    suspend fun getTermScores(uid: String, term: Int, year: String)
    suspend fun annualRank(uid: String, year: String)

}