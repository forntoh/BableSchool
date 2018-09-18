package ga.forntoh.bableschool.network;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "http://babelschool.inchtechs.com/API/";

    private static final OkHttpClient client = buildClient();
    private static final Retrofit retrofit = buildRetrofit(client);

    private static OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();
            Request.Builder builder1 = originalRequest.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Connection", "close");
            Request newRequest = builder1.build();
            return chain.proceed(newRequest);
        });
        return builder.build();
    }

    private static Retrofit buildRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setLenient()
                                .create()))
                .build();
    }

    public static <T> T createService(Class<T> service) {
        return retrofit.create(service);
    }

}
