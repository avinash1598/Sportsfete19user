package spider.app.sportsfete18.Leaderboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.j256.ormlite.dao.Dao;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import net.cachapa.expandablelayout.ExpandableLayout;

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
import spider.app.sportsfete18.API.ApiInterface;
import spider.app.sportsfete18.API.Leaderboard;
import spider.app.sportsfete18.DatabaseHelper;
import spider.app.sportsfete18.R;

/**
 * Created by akashj on 21/1/17.
 */

public class LeaderboardFragment extends Fragment implements Callback<List<Leaderboard>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG="LeaderBoardFragment";
    private FirebaseAnalytics mFirebaseAnalytics;
    private RecyclerView recyclerView;
    List<Leaderboard> standingList = new ArrayList<>();
    LeaderboardRecyclerAdapter leaderboardRecyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<Leaderboard>> call;
    ApiInterface apiInterface;
    DatabaseHelper helper;
    Dao<Leaderboard,Long> dao;
    Context context;
    SharedPreferences prefs;
    String lastUpdatedTimestamp;
    private int currentTransitionEffect =JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    View view;
    ViewGroup viewGroup;


    public LeaderboardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = container;
        return inflater.inflate(R.layout.fragment_leaderboard,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        getLeaderboardLastUpdate();

        showLeaderboardLastUpdate();

        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        context=getContext();
/*
        try {
            helper= OpenHelperManager.getHelper(context,DatabaseHelper.class);
            dao=helper.getStandingsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        //recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);
        leaderboardRecyclerAdapter = new LeaderboardRecyclerAdapter(standingList, getActivity(), new LeaderboardRecyclerAdapter.MyAdapterListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemSelected(int position, ExpandableLayout expandableLayout, ImageView imageView) {
                toggle_contents(expandableLayout, imageView);
            }
        });
        recyclerView.setAdapter(leaderboardRecyclerAdapter);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.leaderboard_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        onRefresh();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void toggle_contents(ExpandableLayout expandableLayout, ImageView arrow_indicator){
        expandableLayout.toggle();
        if(expandableLayout.isExpanded()){
            arrow_indicator.animate().setInterpolator(new LinearInterpolator()).rotationBy(180f).setDuration(300);
            //arraw_indicator.setImageDrawable(getDrawable(R.drawable.ic_arrow_up));
        }else{
            arrow_indicator.animate().setInterpolator(new LinearInterpolator()).rotationBy(-180f).setDuration(300);
            //arraw_indicator.setImageDrawable(getDrawable(R.drawable.ic_arrow_down));
        }
    }



    @Override
    public void onResponse(Call<List<Leaderboard>> call, Response<List<Leaderboard>> response) {
        final List<Leaderboard> responseList=response.body();
        swipeRefreshLayout.setRefreshing(false);
        if(responseList!=null){
            standingList.clear();
            if(responseList.size()>0) {
                Log.d(TAG, "onResponse:response received ");


                swipeRefreshLayout.setRefreshing(false);

                standingList.clear();
                standingList.addAll(responseList);

                Collections.sort(standingList, new Comparator<Leaderboard>(){
                    @Override
                    public int compare(Leaderboard o1, Leaderboard o2) {
                        return (int) (o2.getTotal() - o1.getTotal());
                    }
                });

                leaderboardRecyclerAdapter.notifyDataSetChanged();
            }

            putLeaderboardLastUpdate();
            //getLeaderboardLastUpdate();
            //showLeaderboardLastUpdate();

        }

        leaderboardRecyclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "onResponse: ");

    }

    private void putLeaderboardLastUpdate() {
        simpleDateFormat = new SimpleDateFormat("EEEE, MMMM d, h:mm,a", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        formattedDate = simpleDateFormat.format(System.currentTimeMillis());
        prefs.edit().putString("LEADERBOARD_LAST_UPDATED","Last Updated at :"+ formattedDate).apply();
    }

    private void getLeaderboardLastUpdate(){
        lastUpdatedTimestamp=prefs.getString("LEADERBOARD_LAST_UPDATED","PULL DOWN TO REFRESH");
    }

    private void showLeaderboardLastUpdate(){
        if(viewGroup.getContext()!=null){
            //Snackbar.make(viewGroup, lastUpdatedTimestamp,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<List<Leaderboard>> call, Throwable t) {
        Log.d(TAG, "onFailure: "+t.toString());
        Toast.makeText(context, "Device Offline", Toast.LENGTH_SHORT).show();
        //updateAdapter();
        leaderboardRecyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateAdapter() {
        try {
            standingList=dao.queryForAll();
            leaderboardRecyclerAdapter=new LeaderboardRecyclerAdapter(standingList, context, new LeaderboardRecyclerAdapter.MyAdapterListener() {
                @Override
                public void onItemSelected(int position, ExpandableLayout expandableLayout, ImageView imageView) {

                }
            });
            recyclerView.setAdapter(leaderboardRecyclerAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        call = apiInterface.getLeaderBoard();
        call.enqueue(this);
        //loadingView.startAnimation();
        //loadingView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LeaderBoard Refresh");
        mFirebaseAnalytics.logEvent("LeaderBoard", bundle);
    }

    @Override
    public void onDestroyView(){
        standingList.clear();
        leaderboardRecyclerAdapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();
        super.onDestroyView();
    }
}
