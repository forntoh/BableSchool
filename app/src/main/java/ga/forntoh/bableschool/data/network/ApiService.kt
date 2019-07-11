package ga.forntoh.bableschool.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.AnnualRank
import ga.forntoh.bableschool.data.model.other.Likes
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
//import tech.linjiang.pandora.Pandora
import java.util.concurrent.TimeUnit

interface ApiService {

    @get:GET("categories")
    val functions: Deferred<List<Category>>

    @get:GET("top_schools")
    val topSchools: Deferred<List<TopSchool>>

    @GET("course_notes/{uid}")
    fun getCourseNotes(@Path("uid") uid: String): Deferred<List<Course>>

    @GET("top_students/{uid}")
    fun getTopStudents(@Path("uid") uid: String): Deferred<List<TopStudent>>

    @FormUrlEncoded
    @POST("news")
    fun getNews(@Field("uid") uid: String): Deferred<MutableList<News>>

    @FormUrlEncoded
    @POST("time_table")
    fun getTimetable(@Field("class_code") clazz: String?, @Field("school") school: String?): Deferred<List<Period>>

    @FormUrlEncoded
    @POST("postComment")
    fun postComment(
            @Field("subject") subject: String,
            @Field("data") comment: String
    ): Deferred<Comment>

    @FormUrlEncoded
    @POST("like")
    fun likeNews(
            @Field("uid") uid: String,
            @Field("subject") subject: String
    ): Deferred<Likes>

    @FormUrlEncoded
    @POST("profile/{uid}")
    fun getUserProfile(
            @Path("uid") uid: String,
            @Field("password") password: String
    ): Deferred<User>

    @FormUrlEncoded
    @POST("profile/setpassword")
    fun updatePassword(
            @Field("matricule") uid: String,
            @Field("oldpassword") oldPassword: String,
            @Field("newpassword") newPassword: String
    ): Deferred<User>

    @FormUrlEncoded
    @POST("scores/{uid}")
    fun getTermScores(
            @Path("uid") uid: String,
            @Field("term") term: Int,
            @Field("year") year: String
    ): Deferred<MutableList<Score>>

    @FormUrlEncoded
    @POST("rank/{uid}")
    fun annualRank(
            @Path("uid") uid: String,
            @Field("year") year: String
    ): Deferred<AnnualRank>

    companion object {

        private const val BASE_URL = "http://babelschool.inchtechs.com/API/"

        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): ApiService {
            val requestInterceptor = Interceptor { chain ->

                val request = chain.request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Connection", "close")
                        .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(connectivityInterceptor)
                    //.addInterceptor(Pandora.get().interceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .build()
                    .create(ApiService::class.java)
        }
    }
}


