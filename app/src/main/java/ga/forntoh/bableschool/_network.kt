package ga.forntoh.bableschool

import com.google.gson.GsonBuilder
import ga.forntoh.bableschool.model.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @get:GET("categories")
    val functions: Single<List<Category>>

    @get:GET("news")
    val news: Observable<List<News>>

    @get:GET("top_schools")
    val topSchools: Single<List<TopSchool>>

    @FormUrlEncoded
    @POST("profile/{uid}")
    fun getUserProfile(@Path("uid") uid: String, @Field("password") password: String): Single<User>

    @FormUrlEncoded
    @POST("scores/{uid}")
    fun getTermScores(@Path("uid") uid: String, @Field("term") term: Int, @Field("year") year: String): Single<List<Score>>

    @GET("course_notes/{uid}")
    fun getCourseNotes(@Path("uid") uid: String): Single<List<Course>>

    @GET("top_students/{uid}")
    fun getTopStudents(@Path("uid") uid: String): Single<List<TopStudent>>

    @FormUrlEncoded
    @POST("rank/{uid}")
    fun annualRank(@Path("uid") uid: String, @Field("year") year: String): Single<AnnualRank>
}

object RetrofitBuilder {

    private const val BASE_URL = "http://babelschool.inchtechs.com/API/"

    private val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val builder1 = originalRequest.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Connection", "close")
            val newRequest = builder1.build()
            return@addInterceptor chain.proceed(newRequest)
        }
        builder.build()
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .build()
    }

    fun <T> createService(service: Class<T>): T = retrofit.create(service)

}


