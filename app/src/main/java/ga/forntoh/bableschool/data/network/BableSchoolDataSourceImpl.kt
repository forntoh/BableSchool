package ga.forntoh.bableschool.data.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.*
import ga.forntoh.bableschool.internal.NoConnectivityException
import java.net.SocketTimeoutException

@SuppressLint("LogNotTimber")
class BableSchoolDataSourceImpl(private val apiService: ApiService) : BableSchoolDataSource {

    private val _downloadedCategories = MutableLiveData<List<Category>>()
    override val downloadedCategories: LiveData<List<Category>>
        get() = _downloadedCategories

    private val _downloadedTopSchools = MutableLiveData<List<TopSchool>>()
    override val downloadedTopSchools: LiveData<List<TopSchool>>
        get() = _downloadedTopSchools

    private val _downloadedCourseNotes = MutableLiveData<List<CourseResponse>>()
    override val downloadedCourseNotes: LiveData<List<CourseResponse>>
        get() = _downloadedCourseNotes

    private val _downloadedTopStudents = MutableLiveData<List<TopStudent>>()
    override val downloadedTopStudents: LiveData<List<TopStudent>>
        get() = _downloadedTopStudents

    private val _downloadedNews = MutableLiveData<List<NewsResponse>>()
    override val downloadedNews: LiveData<List<NewsResponse>>
        get() = _downloadedNews

    private val _downloadedTimetable = MutableLiveData<List<Period>>()
    override val downloadedTimetable: LiveData<List<Period>>
        get() = _downloadedTimetable

    private val _downloadedComment = MutableLiveData<Comment>()
    override val downloadedComment: LiveData<Comment>
        get() = _downloadedComment

    private val _downloadedLikes = MutableLiveData<Likes>()
    override val downloadedLikes: LiveData<Likes>
        get() = _downloadedLikes

    private val _downloadedUserProfile = MutableLiveData<User>()
    override val downloadedUserProfile: LiveData<User>
        get() = _downloadedUserProfile

    private val _downloadedTermScores = MutableLiveData<List<ScoreWithCourse>>()
    override val downloadedTermScores: LiveData<List<ScoreWithCourse>>
        get() = _downloadedTermScores

    private val _downloadedAnnualRank = MutableLiveData<AnnualRank>()
    override val downloadedAnnualRank: LiveData<AnnualRank>
        get() = _downloadedAnnualRank

    override suspend fun categories() {
        try {
            val fetchedCategories = apiService.functions.await()
            _downloadedCategories.postValue(fetchedCategories)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun topSchools() {
        try {
            val fetchedTopSchools = apiService.topSchools.await()
            _downloadedTopSchools.postValue(fetchedTopSchools)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getCourseNotes(uid: String) {
        try {
            val fetchedCourseNotes = apiService.getCourseNotesAsync(uid).await()
            _downloadedCourseNotes.postValue(fetchedCourseNotes)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getTopStudents(uid: String) {
        try {
            val fetchedTopStudents = apiService.getTopStudentsAsync(uid).await()
            _downloadedTopStudents.postValue(fetchedTopStudents)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getNews(uid: String) {
        try {
            val fetchedNews = apiService.getNewsAsync(uid).await()
            _downloadedNews.postValue(fetchedNews)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        }
    }

    override suspend fun getTimetable(clazz: String?, school: String?) {
        try {
            val fetchedTimetable = apiService.getTimetableAsync(clazz, school).await()
            _downloadedTimetable.postValue(fetchedTimetable)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun postComment(comment: Comment) {
        try {
            val fetchedComment = apiService.postCommentAsync(comment.newsId.toString(), Gson().toJson(comment)).await()
            _downloadedComment.postValue(fetchedComment)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun likeNews(uid: String, newsId: Long) {
        try {
            val fetchedLikes = apiService.likeNewsAsync(uid, newsId.toString()).await()
            _downloadedLikes.postValue(fetchedLikes)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getUserProfile(uid: String, password: String) {
        try {
            val fetchedUser = apiService.getUserProfileAsync(uid, password).await()
            _downloadedUserProfile.postValue(fetchedUser)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun updatePassword(uid: String, oldPassword: String, newPassword: String) {
        try {
            val fetchedUser = apiService.updatePasswordAsync(uid, oldPassword, newPassword).await()
            _downloadedUserProfile.postValue(fetchedUser)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getTermScores(uid: String, term: Int, year: String) {
        try {
            val fetchedScores = ArrayList<ScoreWithCourse>()
            apiService.getTermScoresAsync(uid, term, year).await().forEach {
                it.score?.term = term
                fetchedScores.add(it)
            }
            _downloadedTermScores.postValue(fetchedScores)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun annualRank(uid: String, year: String) {
        try {
            val fetchedRank = apiService.annualRankAsync(uid, year).await()
            _downloadedAnnualRank.postValue(fetchedRank)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }
}