package spider.app.sportsfete18.Schedule;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import spider.app.sportsfete18.API.ApiInterface;
import spider.app.sportsfete18.API.EventDetailsPOJO;
import spider.app.sportsfete18.DatabaseHelper;
import spider.app.sportsfete18.DepartmentUpdateCallback;
import spider.app.sportsfete18.EventInfo.EventInfoActivity;
import spider.app.sportsfete18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Day2Fragment extends Fragment implements Callback<List<EventDetailsPOJO>>, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG="Day2Fragment";
    List<EventDetailsPOJO> eventList=new ArrayList<>();
    Day2EventsDetailRecyclerAdapter eventRecyclerAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<EventDetailsPOJO>> call;
    ApiInterface apiInterface;
    DatabaseHelper helper;
    Dao<EventDetailsPOJO,Long> dao;
    int selectedDay=1;
    String selectedDept;
    Context context;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    SharedPreferences prefs;
    DepartmentUpdateCallback departmentUpdateCallback;
    boolean isVisibleToUser=false;

    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context contextBroadcast, Intent intent) {
            getSelectedDept();
            updateAdapter();
            //departmentUpdateCallback.updateScheduleFragment();
        }
    };

    BroadcastReceiver receiver2 = new BroadcastReceiver(){
        @Override
        public void onReceive(Context contextBroadcast, Intent intent) {
            recyclerView.smoothScrollToPosition(0);
        }
    };

    public Day2Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        context=getContext();
        departmentUpdateCallback= (DepartmentUpdateCallback) getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        getSelectedDept();

        try {
            helper= OpenHelperManager.getHelper(context,DatabaseHelper.class);
            dao=helper.getEventsDetailDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView= (RecyclerView) getActivity().findViewById(R.id.day_2_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);

        Log.d(TAG, "onViewCreated: selectedDept"+selectedDept);

        eventRecyclerAdapter=new Day2EventsDetailRecyclerAdapter(eventList,getActivity());
        recyclerView.setAdapter(eventRecyclerAdapter);

        swipeRefreshLayout= (SwipeRefreshLayout) getActivity().findViewById(R.id.day_2_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        updateAdapter();
/*
        if(ScheduleFragment.refresh_check) {
            if (bundle == null) {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        }else {

        }
*/
        if (bundle == null) {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }

        setClickListener();

        IntentFilter filter = new IntentFilter();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("scroll_to_top0");
        filter.addAction("update_department");
        if(getActivity()!=null) {
            getActivity().registerReceiver(receiver, filter);
            getActivity().registerReceiver(receiver2, filter2);
        }
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
        swipeRefreshLayout.setRefreshing(false);
        if(responseList!=null){
            if(responseList.size()>0) {
                Log.d(TAG, "onResponse:response received ");
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //TableUtils.clearTable(helper.getConnectionSource(), EventDetailsPOJO.class);
                            DeleteBuilder<EventDetailsPOJO, Long> deleteBuilder = dao.deleteBuilder();
                            deleteBuilder.where().eq("day",1);
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
                                swipeRefreshLayout.setRefreshing(false);
                                updateAdapter();
                                //departmentUpdateCallback.updateScheduleFragment();
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
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(context, "Device Offline", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        call = apiInterface.getSchedule2(1);
        call.enqueue(this);
        //loadingView.startAnimation();
        //loadingView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Day 2 Schedule fetched");
        mFirebaseAnalytics.logEvent("Schedule",bundle);
    }

    public void getSelectedDept() {
        if (getActivity() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            selectedDept = prefs.getString("DEPT", "ALL");
        }
    }

    public void updateAdapter(){
        List<EventDetailsPOJO>dbList,newDbList=new ArrayList<>();
        try {
            QueryBuilder<EventDetailsPOJO,Long> queryBuilder= null;
            queryBuilder = helper.getEventsDetailDao().queryBuilder();
            Log.d(TAG, "updateAdapter: "+selectedDay);
            queryBuilder.where().eq("day",selectedDay);
            dbList=queryBuilder.query();
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
                eventList.addAll(newDbList);

                Collections.sort(eventList, new Comparator<EventDetailsPOJO>(){
                    @Override
                    public int compare(EventDetailsPOJO o1, EventDetailsPOJO o2) {
                        return (int) (o1.getStartTime() - o2.getStartTime());
                    }
                });

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

    @Override
    public void onDestroyView(){
        Runtime.getRuntime().gc();
        super.onDestroyView();
    }
}
