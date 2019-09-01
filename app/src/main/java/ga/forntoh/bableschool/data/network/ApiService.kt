package ga.forntoh.bableschool.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import ga.forntoh.bableschool.data.model.main.*
import ga.forntoh.bableschool.data.model.other.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("categories")
    suspend fun functions(): Response<List<Category>>

    @GET("top_schools")
    suspend fun topSchools(): Response<List<TopSchool>>

    @GET("course_notes/{uid}")
    suspend fun getCourseNotesAsync(@Path("uid") uid: String): Response<List<CourseResponse>>

    @GET("top_students/{uid}")
    suspend fun getTopStudentsAsync(@Path("uid") uid: String): Response<List<TopStudent>>

    @FormUrlEncoded
    @POST("news")
    suspend fun getNewsAsync(@Field("uid") uid: String): Response<MutableList<NewsResponse>>

    @FormUrlEncoded
    @POST("time_table")
    suspend fun getTimetableAsync(@Field("class_code") clazz: String?, @Field("school") school: String?): Response<List<Period>>

    @FormUrlEncoded
    @POST("postComment")
    suspend fun postCommentAsync(
            @Field("subject") subject: String,
            @Field("data") comment: String
    ): Response<Comment>

    @FormUrlEncoded
    @POST("like")
    suspend fun likeNewsAsync(
            @Field("uid") uid: String,
            @Field("subject") subject: String
    ): Response<Likes>

    @FormUrlEncoded
    @POST("profile/{uid}")
    suspend fun getUserProfileAsync(
            @Path("uid") uid: String,
            @Field("password") password: String
    ): Response<User>

    @FormUrlEncoded
    @POST("setpassword")
    suspend fun updatePasswordAsync(
            @Field("matricule") uid: String,
            @Field("oldpassword") oldPassword: String,
            @Field("newpassword") newPassword: String
    ): Response<User>

    @FormUrlEncoded
    @POST("scores/{uid}")
    suspend fun getTermScoresAsync(
            @Path("uid") uid: String,
            @Field("term") term: Int,
            @Field("year") year: String
    ): Response<MutableList<Score>>

    @FormUrlEncoded
    @POST("rank/{uid}")
    suspend fun annualRankAsync(
            @Path("uid") uid: String,
            @Field("year") year: String
    ): Response<AnnualRank>

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


