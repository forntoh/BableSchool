package ga.forntoh.bableschool.network;

import java.util.List;

import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.model.User;
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

    @GET("categories")
    Single<List<Category>> getFunctions();

}