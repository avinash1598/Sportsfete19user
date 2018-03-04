package spider.app.sportsfete.Schedule;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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
import spider.app.sportsfete.API.EventDetailsPOJO;
import spider.app.sportsfete.DatabaseHelper;
import spider.app.sportsfete.DepartmentUpdateCallback;
import spider.app.sportsfete.EventInfo.EventInfoActivity;
import spider.app.sportsfete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Day1Fragment extends Fragment implements Callback<List<EventDetailsPOJO>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG="Day1Fragment";
    List<EventDetailsPOJO> eventList;
    Day1EventsDetailRecyclerAdapter eventRecyclerAdapter;
    RecyclerView recyclerView;
    LoadingView loadingView;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<EventDetailsPOJO>> call;
    ApiInterface apiInterface;
    DatabaseHelper helper;
    Dao<EventDetailsPOJO,Long> dao;
    int selectedDay=0;
    String selectedDept;
    Context context;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    SharedPreferences prefs;
    DepartmentUpdateCallback departmentUpdateCallback;
    boolean isVisibleToUser=false;
    LinearLayout shared_ll;

    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context contextBroadcast, Intent intent) {
            getSelectedDept();
            //updateAdapter();
            departmentUpdateCallback.updateScheduleFragment();
        }
    };

    public Day1Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        context=getContext();
        departmentUpdateCallback= (DepartmentUpdateCallback) getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
        eventList = new ArrayList<>();

        getSelectedDept();

        try {
            helper= OpenHelperManager.getHelper(context,DatabaseHelper.class);
            dao=helper.getEventsDetailDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView= (RecyclerView) getActivity().findViewById(R.id.day_1_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        shared_ll = (LinearLayout) getActivity().findViewById(R.id.scene_transition);

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);

        loadingView = (LoadingView)getActivity(). findViewById(R.id.day_1_loading_view);
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

        Log.d(TAG, "onViewCreated: selectedDept"+selectedDept);

        eventRecyclerAdapter=new Day1EventsDetailRecyclerAdapter(eventList, getActivity(), new Day1EventsDetailRecyclerAdapter.MyAdapterListener() {
            @Override
            public void onItemSelected(int position, View view) {
                EventDetailsPOJO selectedEvent=eventList.get(position);
                Intent intent = new Intent(context, EventInfoActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        "scene_transition");
                intent.putExtra("SELECTED_EVENT", new Gson().toJson(selectedEvent));
                //ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(eventRecyclerAdapter);

        swipeRefreshLayout= (SwipeRefreshLayout) getActivity().findViewById(R.id.day_1_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        updateAdapter();

        if(ScheduleFragment.refresh_check) {
            if (bundle == null) {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        }else {

        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("update_department");
        getActivity().registerReceiver(receiver, filter);

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
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(String s) {
                Log.d("cbdhcb","day1 click");
                EventDetailsPOJO selectedEvent=eventList.get(Integer.parseInt(s));
                Intent intent = new Intent(context, EventInfoActivity.class);

                intent.putExtra("SELECTED_EVENT", new Gson().toJson(selectedEvent));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResponse(Call<List<EventDetailsPOJO>> call, Response<List<EventDetailsPOJO>> response) {
        final List<EventDetailsPOJO> responseList=response.body();
        Log.d("response","----------");
        swipeRefreshLayout.setRefreshing(false);
        if(responseList!=null){
            Log.d("response list size","----------"+responseList.size());
            if(responseList.size()>0) {
                Log.d(TAG, "onResponse:response received ");

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //TableUtils.clearTable(helper.getConnectionSource(), EventDetailsPOJO.class);
                            DeleteBuilder<EventDetailsPOJO, Long> deleteBuilder = dao.deleteBuilder();
                            deleteBuilder.where().eq("day",0);
                            deleteBuilder.delete();
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
                                //updateAdapter();
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
    public void onFailure(Call<List<EventDetailsPOJO>> call, Throwable t) {
        //Log.d(TAG, "onFailure: "+t.toString());
        t.printStackTrace();
        loadingView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(context, "Device Offline", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        Log.d("refresh","swipe refresh");
        call = apiInterface.getSchedule2(0);
        call.enqueue(this);
        //loadingView.startAnimation();
        //loadingView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Day 1 Schedule fetched");
        mFirebaseAnalytics.logEvent("Schedule",bundle);
    }

    public void getSelectedDept() {
        if (getActivity() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            selectedDept = prefs.getString("DEPT", "ALL");
        }
    }

    public void updateAdapter(){
        Log.d("selected department",selectedDept+"");
        List<EventDetailsPOJO>dbList,newDbList=new ArrayList<>();
        try {
            QueryBuilder<EventDetailsPOJO,Long> queryBuilder= null;
            queryBuilder = helper.getEventsDetailDao().queryBuilder();
            Log.d(TAG, "updateAdapter: "+selectedDay);
            queryBuilder.where().eq("day",selectedDay);
            dbList=queryBuilder.query();
            Log.d("db size","-----------"+dbList.size());
            if(selectedDept.equals("ALL")){
                eventList.clear();
                eventList.addAll(dbList);
                eventRecyclerAdapter.notifyDataSetChanged();
            }else {
                for(EventDetailsPOJO eventDetails : dbList){
                    if(eventDetails.getDept1().equalsIgnoreCase(selectedDept) ||
                            eventDetails.getDept2().equalsIgnoreCase(selectedDept)){
                        newDbList.add(eventDetails);
                    }
                }
                eventList.clear();
                eventRecyclerAdapter.notifyDataSetChanged();
                eventList.addAll(newDbList);
                eventRecyclerAdapter.notifyDataSetChanged();
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
