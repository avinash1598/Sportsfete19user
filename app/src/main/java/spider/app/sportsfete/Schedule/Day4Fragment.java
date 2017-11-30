package spider.app.sportsfete.Schedule;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.saeid.fabloading.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import spider.app.sportsfete.API.ApiInterface;
import spider.app.sportsfete.API.Event;
import spider.app.sportsfete.DatabaseHelper;
import spider.app.sportsfete.DepartmentUpdateCallback;
import spider.app.sportsfete.EventInfo.EventInfoActivity;
import spider.app.sportsfete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Day4Fragment extends Fragment implements Callback<List<Event>>, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG="Day4Fragment";
    List<Event> eventList=new ArrayList<>();
    EventRecyclerAdapter eventRecyclerAdapter;
    RecyclerView recyclerView;
    LoadingView loadingView;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<Event>> call;
    ApiInterface apiInterface;
    DatabaseHelper helper;
    Dao<Event,Long> dao;
    int selectedDay=4;
    String selectedDept;
    Context context;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    View view;
    SharedPreferences prefs;
    DepartmentUpdateCallback departmentUpdateCallback;
    boolean isVisibleToUser=false;

    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    public Day4Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_4, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        context=getContext();
        departmentUpdateCallback= (DepartmentUpdateCallback) getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        getSelectedDept();

        try {
            helper= OpenHelperManager.getHelper(context,DatabaseHelper.class);
            dao=helper.getEventsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView= (RecyclerView) view.findViewById(R.id.day_4_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);


        loadingView = (LoadingView)view. findViewById(R.id.day_4_loading_view);

        loadingView.addAnimation(Color.WHITE,R.drawable.basketball, LoadingView.FROM_LEFT);
        loadingView.addAnimation(Color.WHITE,R.drawable.cricket, LoadingView.FROM_TOP);
        loadingView.addAnimation(Color.WHITE,R.drawable.badminton, LoadingView.FROM_RIGHT);
        loadingView.addAnimation(Color.WHITE,R.drawable.tennisball, LoadingView.FROM_BOTTOM);
        loadingView.addAnimation(Color.WHITE,R.drawable.chess, LoadingView.FROM_LEFT);
        loadingView.addAnimation(Color.WHITE,R.drawable.pingpong, LoadingView.FROM_TOP);
        loadingView.addAnimation(Color.WHITE,R.drawable.waterpolo, LoadingView.FROM_RIGHT);
        loadingView.addAnimation(Color.WHITE,R.drawable.soccer, LoadingView.FROM_BOTTOM);
        loadingView.setRepeat(100);
        loadingView.setVisibility(View.GONE);

        updateAdapter();
        Log.d(TAG, "onViewCreated: selectedDept"+selectedDept);

        eventRecyclerAdapter=new EventRecyclerAdapter(eventList,context);
        recyclerView.setAdapter(eventRecyclerAdapter);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.day_4_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        setClickListener();
    }

    private void putSelectedDay() {
        Log.d(TAG, "putSelectedDay: "+selectedDay);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SELECTED_DAY",selectedDay);
        editor.apply();
    }

    void setClickListener(){
        rx.Observable<String> observable= eventRecyclerAdapter.getPositionClicks();
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Event selectedEvent=eventList.get(Integer.parseInt(s));
                Intent intent = new Intent(context, EventInfoActivity.class);
                intent.putExtra("SELECTED_EVENT", new Gson().toJson(selectedEvent));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
        final List<Event> responseList=response.body();
        if(responseList!=null){
            if(responseList.size()>0) {
                Log.d(TAG, "onResponse:response received ");
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TableUtils.clearTable(helper.getConnectionSource(), Event.class);
                            for (int i = 0; i <responseList.size() ; i++) {
                                dao.create(responseList.get(i));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                putEventsLastUpdate();
                                loadingView.setVisibility(View.INVISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                departmentUpdateCallback.updateScheduleFragment();
                            }
                        });
                    }
                });
                thread.start();
            }
        }
        Log.d(TAG, "onResponse: ");


    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        Log.d(TAG, "onFailure: "+t.toString());
        loadingView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(context, "Device Offline", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        call = apiInterface.getSchedule(-1);
        call.enqueue(this);
        loadingView.startAnimation();
        loadingView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Day 4 Schedule fetched");
        mFirebaseAnalytics.logEvent("Schedule",bundle);
    }

    public void getSelectedDept (){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        selectedDept= prefs.getString("DEPT","ALL");
    }

    public void updateAdapter(){
        List<Event>dbList,newDbList=new ArrayList<>();
        try {
            QueryBuilder<Event,Long> queryBuilder= null;
            queryBuilder = helper.getEventsDao().queryBuilder();
            Log.d(TAG, "updateAdapter: "+selectedDay);
            queryBuilder.where().eq("day",selectedDay);
            dbList=queryBuilder.query();
            if(selectedDept.equals("ALL")){
                eventList=dbList;
            }else {
                for (int i = 0; i < dbList.size(); i++) {
                    if(dbList.get(i).getParticipants().contains(selectedDept)){
                        newDbList.add(dbList.get(i));
                    }
                    else if(dbList.get(i).getParticipants().size()==0 && dbList.get(i).getTeamA()!=null &&
                            dbList.get(i).getTeamB()!=null)
                    {
                        if (dbList.get(i).getTeamA().contains(selectedDept) || dbList.get(i).getTeamB().contains(selectedDept)) {
                            newDbList.add(dbList.get(i));
                        }
                    }
                }
                eventList=newDbList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void putEventsLastUpdate() {
        simpleDateFormat = new SimpleDateFormat("EEEE, MMMM d, h:mm a", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        formattedDate = simpleDateFormat.format(System.currentTimeMillis());
        prefs.edit().putString("EVENTS_LAST_UPDATED","Last Updated at : "+ formattedDate).apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isVisibleToUser){
            putSelectedDay();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
    }

}
