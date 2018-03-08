package spider.app.sportsfete18.Home;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

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
import spider.app.sportsfete18.API.StatusEventDetailsPOJO;
import spider.app.sportsfete18.DepartmentUpdateCallback;
import spider.app.sportsfete18.EventInfo.EventInfoActivity;
import spider.app.sportsfete18.MainActivity;
import spider.app.sportsfete18.R;
import spider.app.sportsfete18.Schedule.StatusEventsDetailRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Callback<List<StatusEventDetailsPOJO>>, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG="HomeFragment";
    List<StatusEventDetailsPOJO> eventList;
    List<StatusEventDetailsPOJO> filter_eventList;
    StatusEventsDetailRecyclerAdapter eventRecyclerAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<StatusEventDetailsPOJO>> call;
    ApiInterface apiInterface;
    Context context;
    SharedPreferences prefs;
    String lastUpdatedTimestamp;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    View view;
    ViewGroup viewGroup;
    DepartmentUpdateCallback departmentUpdateCallback;
    String status = "live";
    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context contextBroadcast, final Intent intent) {
            if(intent.getStringExtra("selectedDepartment")!=null) {

                if (intent.getStringExtra("selectedDepartment").equalsIgnoreCase("ALL")) {
                    filter_eventList.clear();
                    filter_eventList.addAll(eventList);
                } else {
                    filter_eventList.clear();
                    for (StatusEventDetailsPOJO statusEventDetailsPOJO : eventList) {
                        if (statusEventDetailsPOJO.getDept1().equalsIgnoreCase(intent.getStringExtra("selectedDepartment"))
                                || statusEventDetailsPOJO.getDept2().equalsIgnoreCase(intent.getStringExtra("selectedDepartment"))) {
                            filter_eventList.add(statusEventDetailsPOJO);
                        }
                    }
                }
            }
            Log.d("filter list size",filter_eventList.size()+"");
            if(eventRecyclerAdapter!=null)
            eventRecyclerAdapter.notifyDataSetChanged();
        }
    };

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

        eventList=new ArrayList<>();
        filter_eventList=new ArrayList<>();

        getEventsLastUpdate();
        showEventsLastUpdate();

        Bundle arguments = getArguments();
        String target = arguments.getString("target");
        status = target;

        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
        recyclerView= (RecyclerView) view.findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);

        eventRecyclerAdapter=new StatusEventsDetailRecyclerAdapter(filter_eventList, getActivity(), new StatusEventsDetailRecyclerAdapter.MyAdapterListener() {
            @Override
            public void onItemSelected(int position, View view1, View view2, View view3, View view4) {
                StatusEventDetailsPOJO selectedEvent=eventList.get(position);
                Intent intent = new Intent(context, EventInfoActivity.class);
                intent.putExtra("SELECTED_EVENT", new Gson().toJson(selectedEvent));
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(eventRecyclerAdapter);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        onRefresh();
        setClickListener();

        IntentFilter filter = new IntentFilter();
        filter.addAction("update_home_fragment_department");
        if(getActivity()!=null) {
            getActivity().registerReceiver(receiver, filter);
        }

    }

    void setClickListener(){
        rx.Observable<String> observable= eventRecyclerAdapter.getPositionClicks();
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                StatusEventDetailsPOJO selectedEvent=eventList.get(Integer.parseInt(s));
                Intent intent = new Intent(context, EventInfoActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        "scene_transition");
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

            putEventsLastUpdate();
            getEventsLastUpdate();
            showEventsLastUpdate();


            for (int i = 0; i <responseList.size() ; i++)
                    eventList.add(responseList.get(i));

            if(eventList.size()==0 && viewGroup.getContext()!=null){
                Snackbar.make(viewGroup,"There are currently no "+status+" events!", Snackbar.LENGTH_LONG).show();
            }else{
                Collections.sort(eventList, new Comparator<StatusEventDetailsPOJO>(){
                    @Override
                    public int compare(StatusEventDetailsPOJO o1, StatusEventDetailsPOJO o2) {
                        return (int) (o1.getStartTime() - o2.getStartTime());
                    }
                });

                if (((MainActivity)getActivity()).selectedDepartment.equalsIgnoreCase("ALL")) {
                    filter_eventList.clear();
                    filter_eventList.addAll(eventList);
                } else {
                    filter_eventList.clear();
                    for (StatusEventDetailsPOJO statusEventDetailsPOJO : eventList) {
                        if (statusEventDetailsPOJO.getDept1().equalsIgnoreCase(((MainActivity)getActivity()).selectedDepartment)
                                || statusEventDetailsPOJO.getDept2().equalsIgnoreCase(((MainActivity)getActivity()).selectedDepartment)) {
                            filter_eventList.add(statusEventDetailsPOJO);
                        }
                    }
                }
            }
        }

        eventRecyclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "onResponse: ");
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<List<StatusEventDetailsPOJO>> call, Throwable t) {
        Log.d(TAG, "onFailure: "+t.toString());
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(context, "Device Offline", Toast.LENGTH_SHORT).show();
        eventRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        call = apiInterface.getEventByStatus(status);
        call.enqueue(this);
        swipeRefreshLayout.setRefreshing(true);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Live events Fetched");
        mFirebaseAnalytics.logEvent("LiveEvents",bundle);
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

    @Override
    public void onDestroyView(){
        if(getActivity()!=null)
            getActivity().unregisterReceiver(receiver);
        Runtime.getRuntime().gc();
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
