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
            .baseUrl("https://us-central1-sportsfete-732bf.cloudfunctions.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/leaderboard")
    Call<List<Leaderboard>> getLeaderBoard();

    @GET("/events_by_day")
    Call<List<EventDetailsPOJO>> getSchedule2(@Query("day") int day);

    @GET("/events_by_status")
    Call<List<StatusEventDetailsPOJO>> getEventByStatus(@Query("status") String status);

    @FormUrlEncoded
    @POST("user/marathon/register")
    Call<String> registerForMarathon(@Field("rollno") String rollNo, @Field("password") String password);

    @FormUrlEncoded
    @POST("/marathon")
    Call<JsonObject> registerUserForMarathon(@FieldMap Map<String, String> params);

    @GET("/fixture")
    Call<List<FixturePOJO>> getfixture(@Query("sport") String sport);

}