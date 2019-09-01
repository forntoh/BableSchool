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

    private val _downloadedTermScores = MutableLiveData<List<Score>>()
    override val downloadedTermScores: LiveData<List<Score>>
        get() = _downloadedTermScores

    private val _downloadedAnnualRank = MutableLiveData<AnnualRank>()
    override val downloadedAnnualRank: LiveData<AnnualRank>
        get() = _downloadedAnnualRank

    override suspend fun categories() {
        try {
            val fetchedCategories = apiService.functions()
            if (fetchedCategories.isSuccessful)
                _downloadedCategories.postValue(fetchedCategories.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun topSchools() {
        try {
            val fetchedTopSchools = apiService.topSchools()
            if (fetchedTopSchools.isSuccessful)
                _downloadedTopSchools.postValue(fetchedTopSchools.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getCourseNotes(uid: String) {
        try {
            val fetchedCourseNotes = apiService.getCourseNotesAsync(uid)
            if (fetchedCourseNotes.isSuccessful)
                _downloadedCourseNotes.postValue(fetchedCourseNotes.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getTopStudents(uid: String) {
        try {
            val fetchedTopStudents = apiService.getTopStudentsAsync(uid)
            if (fetchedTopStudents.isSuccessful)
                _downloadedTopStudents.postValue(fetchedTopStudents.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getNews(uid: String) {
        try {
            val fetchedNews = apiService.getNewsAsync(uid)
            if (fetchedNews.isSuccessful)
                _downloadedNews.postValue(fetchedNews.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        }
    }

    override suspend fun getTimetable(clazz: String?, school: String?) {
        try {
            val fetchedTimetable = apiService.getTimetableAsync(clazz, school)
            if (fetchedTimetable.isSuccessful)
                _downloadedTimetable.postValue(fetchedTimetable.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun postComment(comment: Comment) {
        try {
            val fetchedComment = apiService.postCommentAsync(comment.newsId.toString(), Gson().toJson(comment))
            if (fetchedComment.isSuccessful)
                _downloadedComment.postValue(fetchedComment.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun likeNews(uid: String, newsId: Long) {
        try {
            val fetchedLikes = apiService.likeNewsAsync(uid, newsId.toString())
            if (fetchedLikes.isSuccessful)
                _downloadedLikes.postValue(fetchedLikes.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getUserProfile(uid: String, password: String) {
        try {
            val fetchedUser = apiService.getUserProfileAsync(uid, password)
            if (fetchedUser.isSuccessful)
                _downloadedUserProfile.postValue(fetchedUser.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun updatePassword(uid: String, oldPassword: String, newPassword: String) {
        try {
            val fetchedUser = apiService.updatePasswordAsync(uid, oldPassword, newPassword)
            if (fetchedUser.isSuccessful)
                _downloadedUserProfile.postValue(fetchedUser.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun getTermScores(uid: String, term: Int, year: String) {
        try {
            val fetchedScores = ArrayList<Score>()

            val response = apiService.getTermScoresAsync(uid, term, year)

            if (response.isSuccessful) {
                response.body()?.forEach {
                    it.term = term
                    fetchedScores.add(it)
                }
                _downloadedTermScores.postValue(fetchedScores)
            }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }

    override suspend fun annualRank(uid: String, year: String) {
        try {
            val fetchedRank = apiService.annualRankAsync(uid, year)
            if (fetchedRank.isSuccessful)
                _downloadedAnnualRank.postValue(fetchedRank.body())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet", e)
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeout", "Could not connect to server")
        }
    }
}