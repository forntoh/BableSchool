package ga.forntoh.bableschool.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.*
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
    fun getCourseNotesAsync(@Path("uid") uid: String): Deferred<List<CourseResponse>>

    @GET("top_students/{uid}")
    fun getTopStudentsAsync(@Path("uid") uid: String): Deferred<List<TopStudent>>

    @FormUrlEncoded
    @POST("news")
    fun getNewsAsync(@Field("uid") uid: String): Deferred<MutableList<NewsResponse>>

    @FormUrlEncoded
    @POST("time_table")
    fun getTimetableAsync(@Field("class_code") clazz: String?, @Field("school") school: String?): Deferred<List<Period>>

    @FormUrlEncoded
    @POST("postComment")
    fun postCommentAsync(
            @Field("subject") subject: String,
            @Field("data") comment: String
    ): Deferred<Comment>

    @FormUrlEncoded
    @POST("like")
    fun likeNewsAsync(
            @Field("uid") uid: String,
            @Field("subject") subject: String
    ): Deferred<Likes>

    @FormUrlEncoded
    @POST("profile/{uid}")
    fun getUserProfileAsync(
            @Path("uid") uid: String,
            @Field("password") password: String
    ): Deferred<User>

    @FormUrlEncoded
    @POST("profile/setpassword")
    fun updatePasswordAsync(
            @Field("matricule") uid: String,
            @Field("oldpassword") oldPassword: String,
            @Field("newpassword") newPassword: String
    ): Deferred<User>

    @FormUrlEncoded
    @POST("scores/{uid}")
    fun getTermScoresAsync(
            @Path("uid") uid: String,
            @Field("term") term: Int,
            @Field("year") year: String
    ): Deferred<MutableList<ScoreWithCourse>>

    @FormUrlEncoded
    @POST("rank/{uid}")
    fun annualRankAsync(
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


