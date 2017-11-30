package spider.app.sportsfete.SportDetails;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.saeid.fabloading.LoadingView;
import retrofit2.Call;
import spider.app.sportsfete.API.Event;
import spider.app.sportsfete.DatabaseHelper;
import spider.app.sportsfete.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixturesFragment extends Fragment {

    List<Event> eventList = new ArrayList<>();
    FixturesRecyclerAdapter fixturesRecyclerAdapter;
    RecyclerView recyclerView;
    DatabaseHelper helper;
    Dao<Event, Long> dao;
    Context context;
    View view;
    String selectedSport,selectedSportName;
    String[] sportNames = {
            "athletics",
            "badminton",
            "basketball",
            "carrom",
            "chess",
            "cricket",
            "football",
            "handball",
            "hockey",
            "kabaddi",
            "kho-kho",
            "marathon",
            "powerlifting",
            "swimming",
            "table tennis",
            "tennis",
            "throwball",
            "volleyball",
            "water polo"
    };



    public FixturesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        selectedSport= getActivity().getIntent().getStringExtra("SELECTED_SPORT");
        int selectedSportInt = Integer.parseInt(selectedSport);
                selectedSportName=sportNames[selectedSportInt];
        return inflater.inflate(R.layout.fragment_fixtures, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        context = getContext();

        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            dao = helper.getEventsDao();
            //eventList = dao.queryForEq();
            QueryBuilder qb = dao.queryBuilder();
            qb.where().like("name",selectedSportName+"%");
            PreparedQuery pq = qb.prepare();
            eventList = dao.query(pq);
            //Log.i("Fixtures Fragment", eventList.get(17).getName());
            Log.d(TAG, "onViewCreated:SelectedSportName "+selectedSportName);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.fixtures_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        fixturesRecyclerAdapter=new FixturesRecyclerAdapter(eventList,context,selectedSport);
        recyclerView.setAdapter(fixturesRecyclerAdapter);

    }
}
