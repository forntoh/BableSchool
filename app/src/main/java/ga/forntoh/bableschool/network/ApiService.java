package ga.forntoh.bableschool.network;

import java.util.List;

import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.model.Course;
import ga.forntoh.bableschool.model.Score;
import ga.forntoh.bableschool.model.User;
import ga.forntoh.bableschool.model.misc.AnnualRank;
import ga.forntoh.bableschool.model.misc.TopSchool;
import ga.forntoh.bableschool.model.misc.TopStudent;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @FormUrlEncoded
    @POST("profile/{uid}")
    Single<User> getUserProfile(@Path("uid") String uid, @Field("password") String password);

    @FormUrlEncoded
    @POST("scores/{uid}")
    Single<List<Score>> getTermScores(@Path("uid") String uid, @Field("term") int term, @Field("year") String year);

    @GET("categories")
    Single<List<Category>> getFunctions();

    @GET("course_notes/{uid}")
    Single<List<Course>> getCourseNotes(@Path("uid") String uid);

    @GET("top_schools")
    Single<List<TopSchool>> getTopSchools();

    @GET("top_students/{uid}")
    Single<List<TopStudent>> getTopStudents(@Path("uid") String uid);

    @FormUrlEncoded
    @POST("rank/{uid}")
    Single<AnnualRank> annualRank(@Path("uid") String uid, @Field("year") String year);
}