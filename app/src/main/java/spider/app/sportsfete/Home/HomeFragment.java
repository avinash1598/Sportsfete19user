package spider.app.sportsfete.Home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import spider.app.sportsfete.API.EventDetailsPOJO;
import spider.app.sportsfete.API.StatusEventDetailsPOJO;
import spider.app.sportsfete.DatabaseHelper;
import spider.app.sportsfete.EventInfo.EventInfoActivity;
import spider.app.sportsfete.R;
import spider.app.sportsfete.Schedule.StatusEventsDetailRecyclerAdapter;
import spider.app.sportsfete.Schedule.StatusEventsDetailRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Callback<List<StatusEventDetailsPOJO>>, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG="HomeFragment";
    List<StatusEventDetailsPOJO> eventList=new ArrayList<>();
    StatusEventsDetailRecyclerAdapter eventRecyclerAdapter;
    RecyclerView recyclerView;
    LoadingView loadingView;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<StatusEventDetailsPOJO>> call;
    ApiInterface apiInterface;
    DatabaseHelper helper;
    Dao<StatusEventDetailsPOJO,Long> dao;
    Context context;
    SharedPreferences prefs;
    String lastUpdatedTimestamp;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    View view;
    ViewGroup viewGroup;

    String status = "live";

    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    public HomeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = container;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();
        this.view=view;
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        getEventsLastUpdate();

        showEventsLastUpdate();

        Bundle arguments = getArguments();
        String target = arguments.getString("target");
        status = target;

        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        try {
            helper= OpenHelperManager.getHelper(context,DatabaseHelper.class);
            dao=helper.getStatusEventsDetailDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView= (RecyclerView) view.findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);

        loadingView = (LoadingView)view. findViewById(R.id.home_loading_view);
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


        eventRecyclerAdapter=new StatusEventsDetailRecyclerAdapter(eventList,context);
        recyclerView.setAdapter(eventRecyclerAdapter);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        onRefresh();

        setClickListener();
    }

    void setClickListener(){
        rx.Observable<String> observable= eventRecyclerAdapter.getPositionClicks();
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                StatusEventDetailsPOJO selectedEvent=eventList.get(Integer.parseInt(s));
                Intent intent = new Intent(context, EventInfoActivity.class);
                intent.putExtra("SELECTED_EVENT", new Gson().toJson(selectedEvent));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResponse(Call<List<StatusEventDetailsPOJO>> call, Response<List<StatusEventDetailsPOJO>> response) {
        final List<StatusEventDetailsPOJO> responseList=response.body();
        Log.d("response","");
        if(responseList!=null){
            eventList.clear();
            Log.d("list size","-------"+responseList.size());
            if(responseList.size()>0) {
                Log.d(TAG, "onResponse:response received ");
                /*
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TableUtils.clearTable(helper.getConnectionSource(), StatusEventDetailsPOJO.class);
                            for (int i = 0; i <responseList.size() ; i++) {
                                dao.create(responseList.get(i));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                */
            }

            putEventsLastUpdate();
            getEventsLastUpdate();
            showEventsLastUpdate();

            for (int i = 0; i <responseList.size() ; i++) {
                {
                    eventList.add(responseList.get(i));
                }
            }
            if(eventList.size()==0 && viewGroup.getContext()!=null){
                Snackbar.make(viewGroup,"There are currently no live events!", Snackbar.LENGTH_LONG).show();
            }
        }
        eventRecyclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "onResponse: ");

        //loadingView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<List<StatusEventDetailsPOJO>> call, Throwable t) {
        Log.d(TAG, "onFailure: "+t.toString());
        //loadingView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(context, "Device Offline", Toast.LENGTH_SHORT).show();
        updateAdapter();
        eventRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        call = apiInterface.getEventByStatus(status);
        call.enqueue(this);
        //loadingView.startAnimation();
        //loadingView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Live events Fetched");
        mFirebaseAnalytics.logEvent("LiveEvents",bundle);
    }

    public void updateAdapter(){
        try {
            QueryBuilder<StatusEventDetailsPOJO,Long> queryBuilder= null;
            queryBuilder = helper.getStatusEventsDetailDao().queryBuilder();
            queryBuilder.where().eq("status","l");
            eventList=queryBuilder.query();
            eventRecyclerAdapter=new StatusEventsDetailRecyclerAdapter(eventList,context);
            recyclerView.setAdapter(eventRecyclerAdapter);
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

    private void getEventsLastUpdate(){
        lastUpdatedTimestamp=prefs.getString("EVENTS_LAST_UPDATED","PULL DOWN TO REFRESH");

    }

    private void showEventsLastUpdate(){
        if(viewGroup.getContext()!=null){
            //Snackbar.make(viewGroup, lastUpdatedTimestamp,Snackbar.LENGTH_LONG).show();
        }
    }
}
