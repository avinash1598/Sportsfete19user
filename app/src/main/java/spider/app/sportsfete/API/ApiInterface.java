package spider.app.sportsfete.API;

/**
 * Created by srikanth on 21/1/17.
 */

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiInterface {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://139.59.58.3/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("scores")
    Call<List<Standing>> getLeaderBoard();

    @GET("events/day/{day}")
    Call<List<Event>> getSchedule(@Path("day") int day);

    @FormUrlEncoded
    @POST("user/marathon/register")
    Call<String> registerForMarathon(@Field("rollno") String rollNo, @Field("password") String password);

    @FormUrlEncoded
    @POST("/marathon")
    Call<JsonObject> registerUserForMarathon(@FieldMap Map<String, String> params);

}