package ga.forntoh.bableschool.data.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.AnnualRank
import ga.forntoh.bableschool.data.model.other.Likes
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent
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

    private val _downloadedCourseNotes = MutableLiveData<List<Course>>()
    override val downloadedCourseNotes: LiveData<List<Course>>
        get() = _downloadedCourseNotes

    private val _downloadedTopStudents = MutableLiveData<List<TopStudent>>()
    override val downloadedTopStudents: LiveData<List<TopStudent>>
        get() = _downloadedTopStudents

    private val _downloadedNews = MutableLiveData<List<News>>()
    override val downloadedNews: LiveData<List<News>>
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

    private val _downloadedTermScores = MutableLiveData<List<Score>>()
    override val downloadedTermScores: LiveData<List<Score>>
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
            val fetchedCourseNotes = apiService.getCourseNotes(uid).await()
            _downloadedCourseNotes.postValue(fetchedCourseNotes)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getTopStudents(uid: String) {
        try {
            val fetchedTopStudents = apiService.getTopStudents(uid).await()
            _downloadedTopStudents.postValue(fetchedTopStudents)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getNews(uid: String) {
        try {
            val fetchedNews = apiService.getNews(uid).await()
            _downloadedNews.postValue(fetchedNews)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        }
    }

    override suspend fun getTimetable(clazz: String?, school: String?) {
        try {
            val fetchedTimetable = apiService.getTimetable(clazz, school).await()
            _downloadedTimetable.postValue(fetchedTimetable)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun postComment(comment: Comment) {
        try {
            val fetchedComment = apiService.postComment(comment.newsId.toString(), Gson().toJson(comment)).await()
            _downloadedComment.postValue(fetchedComment)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun likeNews(uid: String, newsId: Long) {
        try {
            val fetchedLikes = apiService.likeNews(uid, newsId.toString()).await()
            _downloadedLikes.postValue(fetchedLikes)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getUserProfile(uid: String, password: String) {
        try {
            val fetchedUser = apiService.getUserProfile(uid, password).await()
            _downloadedUserProfile.postValue(fetchedUser)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun updatePassword(uid: String, oldPassword: String, newPassword: String) {
        try {
            val fetchedUser = apiService.updatePassword(uid, oldPassword, newPassword).await()
            _downloadedUserProfile.postValue(fetchedUser)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getTermScores(uid: String, term: Int, year: String) {
        try {
            val fetchedScores = ArrayList<Score>()
            apiService.getTermScores(uid, term, year).await().forEach {
                it.term = term; fetchedScores.add(it)
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
            val fetchedRank = apiService.annualRank(uid, year).await()
            _downloadedAnnualRank.postValue(fetchedRank)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }
}