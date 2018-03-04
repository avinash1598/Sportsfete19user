package spider.app.sportsfete.SportDetails;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spider.app.sportsfete.API.ApiInterface;
import spider.app.sportsfete.API.Event;
import spider.app.sportsfete.API.FixturePOJO;
import spider.app.sportsfete.API.StatusEventDetailsPOJO;
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
    ArrayList<FixturePOJO> fixtureArrayList;

    public final String fixtureType = "two";
    String[] sportNames;

    Integer[] fixture_type = {
            R.layout.fixture_layout_one,
            R.layout.fixture_layout_two,
            R.layout.fixture_layout_three,
            R.layout.fixture_layout_four,
            R.layout.fixture_layout_five,
            R.layout.fixture_layout_six,
            R.layout.fixture_layout_seven,
            R.layout.fixture_layout_eight,
            R.layout.fixture_layout_nine,
            R.layout.fixture_layout_ten,
            R.layout.fixture_layout_eleven
    };

    Integer[] matchId = {
            0,
            R.id.match1,
            R.id.match2,
            R.id.match3,
            R.id.match4,
            R.id.match5,
            R.id.match6,
            R.id.match7,
            R.id.match8,
            R.id.match9,
            R.id.match10,
            R.id.match11,
            R.id.match12,
            R.id.match13,
            R.id.match14,
            R.id.match15,
            R.id.match16,
            R.id.match17,
            R.id.match18,
            R.id.match19,
            R.id.match20,
            R.id.match21,
            R.id.match22,
            R.id.match23,
            R.id.match24,
            R.id.match25,
            R.id.match26
    };

    Integer[] matchTeamsId = {
            0,
            R.id.match1teams,
            R.id.match2teams,
            R.id.match3teams,
            R.id.match4teams,
            R.id.match5teams,
            R.id.match6teams,
            R.id.match7teams,
            R.id.match8teams,
            R.id.match9teams,
            R.id.match10teams,
            R.id.match11teams,
            R.id.match12teams,
            R.id.match13teams,
            R.id.match14teams,
            R.id.match15teams,
            R.id.match16teams,
            R.id.match17teams,
            R.id.match18teams,
            R.id.match19teams,
            R.id.match20teams,
            R.id.match21teams,
            R.id.match22teams,
            R.id.match23teams,
            R.id.match24teams,
            R.id.match25teams,
            R.id.match26teams
    };

    TextView[] match;
    TextView[] matchTeams;

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
    private TextView grpAteam1,grpAteam2,grpAteam3,grpAteam4;
    private TextView grpBteam1,grpBteam2,grpBteam3,grpBteam4;
    private TextView grpCteam1,grpCteam2,grpCteam3,grpCteam4;
    private TextView grpDteam1,grpDteam2,grpDteam3,grpDteam4;

    public FixturesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sportNames = getActivity().getResources().getStringArray(R.array.sport_array);
        selectedSport= getActivity().getIntent().getStringExtra("SELECTED_SPORT");
        int selectedSportInt = Integer.parseInt(selectedSport);
                selectedSportName=sportNames[selectedSportInt];

        if((getActivity().getIntent().getIntExtra("FIXTURE_TYPE",0) - 1)<0){
            return inflater.inflate(R.layout.fixture_layout_zero, container, false);
        }

        return inflater.inflate(fixture_type[getActivity().getIntent().getIntExtra("FIXTURE_TYPE",0)-1],
                container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        context = getContext();

        fixtureArrayList = new ArrayList<>();

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

        match = new TextView[matchId.length];
        matchTeams = new TextView[matchTeamsId.length];

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

        if(knockout!=null) {

            knockout.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            quarterfinals.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            semifinals.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            bronze.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            finals.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            standings.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));

        }

        if(getActivity().findViewById(R.id.grpAteam1)!=null) {

            grpAteam1 = (TextView) getActivity().findViewById(R.id.grpAteam1);
            grpAteam2 = (TextView) getActivity().findViewById(R.id.grpAteam2);
            grpAteam3 = (TextView) getActivity().findViewById(R.id.grpAteam3);

            grpAteam1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpAteam2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpAteam3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));

            grpBteam1 = (TextView) getActivity().findViewById(R.id.grpBteam1);
            grpBteam2 = (TextView) getActivity().findViewById(R.id.grpBteam2);
            grpBteam3 = (TextView) getActivity().findViewById(R.id.grpBteam3);

            grpBteam1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpBteam2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpBteam3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));

            grpCteam1 = (TextView) getActivity().findViewById(R.id.grpCteam1);
            grpCteam2 = (TextView) getActivity().findViewById(R.id.grpCteam2);
            grpCteam3 = (TextView) getActivity().findViewById(R.id.grpCteam3);
            grpCteam4 = (TextView) getActivity().findViewById(R.id.grpCteam4);

            grpCteam1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpCteam2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpCteam3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));

            if(grpCteam4!=null){
                grpCteam4.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            }

            grpDteam1 = (TextView) getActivity().findViewById(R.id.grpDteam1);
            grpDteam2 = (TextView) getActivity().findViewById(R.id.grpDteam2);
            grpDteam3 = (TextView) getActivity().findViewById(R.id.grpDteam3);

            grpDteam1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpDteam2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));
            grpDteam3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HammersmithOneRegular.ttf"));

        }

        for(int i = 0; i<matchId.length; i++){
            //Log.d("position",""+i);
            match[i] = (TextView)getActivity().findViewById(matchId[i]);
            if(match[i]!=null)
                match[i].setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
            matchTeams[i] = (TextView)getActivity().findViewById(matchTeamsId[i]);
            if(matchTeams[i]!=null)
                matchTeams[i].setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getFixtureForGame(selectedSportName.toUpperCase().replace("("," ("));
            }
        },300);

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

    public void getFixtureForGame(String sports_name){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-sportsfete-732bf.cloudfunctions.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getfixture(sports_name).enqueue(new Callback<List<FixturePOJO>>() {
            @Override
            public void onResponse(Call<List<FixturePOJO>> call, Response<List<FixturePOJO>> response) {
                if(response.isSuccessful()){
                    Log.d("response","successful"+" : "+response.body().size());
                    fixtureArrayList.clear();
                    fixtureArrayList.addAll(response.body());
                    setElements();
                }
            }

            @Override
            public void onFailure(Call<List<FixturePOJO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setElements(){
        for(FixturePOJO fixtureElement: fixtureArrayList){
            Log.d("index",fixtureElement.getFixtureIndex()+"");

            String text = "";
            match[fixtureElement.getFixtureIndex()].setText("MATCH"+fixtureElement.getFixture());

            if(fixtureElement.getDept1().contains("WINNER OF")){
                text = text + "W" + fixtureElement.getDept1().trim().split(" ")[2]+" Vs ";
                /*matchTeams[fixtureElement.getFixtureIndex()].setText(
                        "W"+fixtureElement.getDept1().trim().split(" ")[2]+" Vs "+"" +
                                "W"+fixtureElement.getDept2().trim().split(" ")[2]);
*/
            }
            else if(fixtureElement.getDept1().contains("LOSER OF")){
                text = text + "L" + fixtureElement.getDept1().trim().split(" ")[2]+" Vs ";
                /*matchTeams[fixtureElement.getFixtureIndex()].setText(
                        "W"+fixtureElement.getDept1().trim().split(" ")[2]+" Vs "+"" +
                                "L"+fixtureElement.getDept2().trim().split(" ")[2]);
 */
            }
            else{
                text = text +  fixtureElement.getDept1().trim()+" Vs ";
            }

            if(fixtureElement.getDept2().contains("WINNER OF")){
                text = text +  "W"+ fixtureElement.getDept2().trim().split(" ")[2];
                /*matchTeams[fixtureElement.getFixtureIndex()].setText(
                        "L"+fixtureElement.getDept1().trim().split(" ")[2]+" Vs "+"" +
                                "W"+fixtureElement.getDept2().trim().split(" ")[2]);
 */
            }

            else if(fixtureElement.getDept2().contains("LOSER OF")){
                text = text +  "L"+ fixtureElement.getDept2().trim().split(" ")[2];
               /* matchTeams[fixtureElement.getFixtureIndex()].setText(
                        "L"+fixtureElement.getDept1().trim().split(" ")[2]+" Vs "+"" +
                                "L"+fixtureElement.getDept2().trim().split(" ")[2]);
*/
            }else{
                text = text +  fixtureElement.getDept2().trim();
            }

            matchTeams[fixtureElement.getFixtureIndex()].setText(text);
        }
    }

}
