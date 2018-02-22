package spider.app.sportsfete.SportDetails;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

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
    String selectedSport,selectedSportName, selectedLayout = "";

    String[] sportNames = {
            "athletics",
            "badminton(Mixed)",
            "basketball(Boys)",
            "basketball(Girls)",
            "carrom(Mixed)",
            "chess(Mixed)",
            "cricket(Boys)",
            "cricket(Girls)",
            "football(Boys)",
            "football(Girls)",
            "handball",
            "hockey",
            "kabaddi",
            "kho-kho(Boys)",
            "kho-kho(Girls)",
            "marathon",
            "powerlifting",
            "swimming",
            "table tennis(Mixed)",
            "tennis(Boys)",
            "tennis(Girls)",
            "throwball",
            "volleyball(Boys)",
            "volleyball(Girls)",
            "water polo"
    };

    String[] fixture_layout_one = {
            "badminton(Mixed)",
            "basketball(Boys)",
            "basketball(Girls)",
            "cricket(Boys)",
            "cricket(Girls)",
            "handball",
            "kabaddi",
            "volleyball(Boys)"
    };

    String[] fixture_layout_two = {
            "football(Girls)",
            "table tennis(Mixed)",
            "throwball",
            "volleyball(Girls)",
            "water polo"
    };

    private LinearLayout linearLayout;
    private HorizontalScrollView horizontalScrollView;
    private float mScale = 1f;
    private float prevScale;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector gestureDetector;

    private TextView match1teams, match2teams, match3teams, match4teams, match5teams, match6teams, match7teams, match8teams,
            match9teams,match10teams,match11teams,match12teams,match13teams,match14teams;
    private TextView match1,match2,match3,match4,match5,match6,match7,match8,match9,match10,match11,match12,match13,match14;
    private TextView knockout, quarterfinals, semifinals, bronze, finals, standings;
    private TextView position1, position2, position3;

    public FixturesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        selectedSport= getActivity().getIntent().getStringExtra("SELECTED_SPORT");
        int selectedSportInt = Integer.parseInt(selectedSport);
                selectedSportName=sportNames[selectedSportInt];

        for(String sports: fixture_layout_one){
            if(sports.equalsIgnoreCase(selectedSportName)){
                selectedLayout = "one";
                return inflater.inflate(R.layout.fixture_layout, container, false);
            }
        }

        for(String sports: fixture_layout_two){
            if(sports.equalsIgnoreCase(selectedSportName)){
                selectedLayout = "two";
                return inflater.inflate(R.layout.fixture_layout_two, container, false);
            }
        }

        selectedLayout = "";

        return inflater.inflate(R.layout.fixture_layout, container, false);
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

        position1 = (TextView)getActivity().findViewById(R.id.position1);
        position2 = (TextView)getActivity().findViewById(R.id.position2);
        position3 = (TextView)getActivity().findViewById(R.id.position3);

        position1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        position2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        position3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));

        knockout = (TextView)getActivity().findViewById(R.id.knockout);
        quarterfinals = (TextView)getActivity().findViewById(R.id.quarterfinals);
        semifinals = (TextView)getActivity().findViewById(R.id.semifinals);
        bronze = (TextView)getActivity().findViewById(R.id.bronze);
        finals = (TextView)getActivity().findViewById(R.id.finals);
        standings = (TextView)getActivity().findViewById(R.id.Standings);

        knockout.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        quarterfinals.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        semifinals.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        bronze.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        finals.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        standings.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));

        match1 = (TextView)getActivity().findViewById(R.id.match1);
        match2 = (TextView)getActivity().findViewById(R.id.match2);
        match3 = (TextView)getActivity().findViewById(R.id.match3);
        match4 = (TextView)getActivity().findViewById(R.id.match4);
        match5 = (TextView)getActivity().findViewById(R.id.match5);
        match6 = (TextView)getActivity().findViewById(R.id.match6);
        match7 = (TextView)getActivity().findViewById(R.id.match7);
        match8 = (TextView)getActivity().findViewById(R.id.match8);
        match9 = (TextView)getActivity().findViewById(R.id.match9);
        match10 = (TextView)getActivity().findViewById(R.id.match10);
        match11 = (TextView)getActivity().findViewById(R.id.match11);
        match12 = (TextView)getActivity().findViewById(R.id.match12);
        if(!selectedLayout.matches("two")) {
            match13 = (TextView) getActivity().findViewById(R.id.match13);
            match14 = (TextView) getActivity().findViewById(R.id.match14);

        }

        match1teams = (TextView)getActivity().findViewById(R.id.match1teams);
        match2teams = (TextView)getActivity().findViewById(R.id.match2teams);
        match3teams = (TextView)getActivity().findViewById(R.id.match3teams);
        match4teams = (TextView)getActivity().findViewById(R.id.match4teams);
        match5teams = (TextView)getActivity().findViewById(R.id.match5teams);
        match6teams = (TextView)getActivity().findViewById(R.id.match6teams);
        match7teams = (TextView)getActivity().findViewById(R.id.match7teams);
        match8teams = (TextView)getActivity().findViewById(R.id.match8teams);
        match9teams = (TextView)getActivity().findViewById(R.id.match9teams);
        match10teams = (TextView)getActivity().findViewById(R.id.match10teams);
        match11teams = (TextView)getActivity().findViewById(R.id.match11teams);
        match12teams = (TextView)getActivity().findViewById(R.id.match12teams);
        if(!selectedLayout.matches("two")) {
            match13teams = (TextView) getActivity().findViewById(R.id.match13teams);
            match14teams = (TextView) getActivity().findViewById(R.id.match14teams);

        }

        match1teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match2teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match3teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match4teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match4.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match5teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match5.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match6teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match6.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match7teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match7.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match8teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match8.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match9teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match9.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match10teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match10.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match11teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match11.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match12teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        match12.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
        if(!selectedLayout.matches("two")) {
            match13teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            match13.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            match14teams.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            match14.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
        }

        horizontalScrollView = (HorizontalScrollView)getActivity().findViewById(R.id.parent_view);
        linearLayout = (LinearLayout)getActivity().findViewById(R.id.parent_linearLayout);

        mScaleDetector = new ScaleGestureDetector(getActivity(), new ScaleGestureDetector.SimpleOnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                float scale = 1 - detector.getScaleFactor();

                prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) // Minimum scale condition:
                    mScale = 0.1f;

                if (mScale > 10f) // Maximum scale condition:
                    mScale = 10f;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                linearLayout.startAnimation(scaleAnimation);
                return true;
            }
        });

        SportDetailsActivity.MyOnTouchListener onTouchListener = new SportDetailsActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                return mScaleDetector.onTouchEvent(ev);
            }
        };

        ((SportDetailsActivity)getActivity()).registerMyOnTouchListener(onTouchListener);

        linearLayout.setOnTouchListener(new TouchListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                Log.e("Double Tap Event", "Tapped at: ");
                if(linearLayout.getScaleX()==1f||linearLayout.getScaleY()==1f) {
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f, 1f / prevScale, 1f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    linearLayout.startAnimation(scaleAnimation);
                    prevScale = 1;
                    mScale = 1;
                }else{
                    prevScale = 1;
                    mScale = 1;
                }
            }
        });

/*
        recyclerView = (RecyclerView) view.findViewById(R.id.fixtures_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        fixturesRecyclerAdapter=new FixturesRecyclerAdapter(eventList,context,selectedSport);
        recyclerView.setAdapter(fixturesRecyclerAdapter);
*/
    }

    //green color for team won
    public void styleMatchWonText(String team1, String team2, TextView textView){
        SpannableStringBuilder sb = new SpannableStringBuilder(
                team1 + " Vs " + team2);
        StyleSpan b = new StyleSpan(Color.GREEN);
        sb.setSpan(b, 0, team1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }

    //grey color for one undecided team
    public void styleUndecidedText(String team1, TextView textView){
        SpannableStringBuilder sb = new SpannableStringBuilder(
                team1 + " Vs " + "TBD");
        StyleSpan b = new StyleSpan(Color.DKGRAY);
        sb.setSpan(b, team1.length()+4,team1.length()+7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }

    //grey color with both team undecided
    public void styleUndecidedText2(TextView textView){
        textView.setTextColor(Color.DKGRAY);
        SpannableStringBuilder sb = new SpannableStringBuilder(
                "TBD" + " Vs " + "TBD");
        StyleSpan b = new StyleSpan(Color.BLACK);
        sb.setSpan(b, 4,6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }


}
