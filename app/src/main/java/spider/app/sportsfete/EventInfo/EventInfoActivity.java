package spider.app.sportsfete.EventInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifTextView;
import spider.app.sportsfete.API.Event;
import spider.app.sportsfete.API.EventDetailsPOJO;
import spider.app.sportsfete.FireBaseServices.Comment;
import spider.app.sportsfete.FireBaseServices.Score;
import spider.app.sportsfete.R;

public class EventInfoActivity extends AppCompatActivity{


    private static final String TAG="EventInfoActivity";
    TextView eventNameTv,team1Tv,team2Tv,participantsTv,eventHintTv,roundTv,score1Tv,score2Tv,eventTimeTv, noCommentsTv;
    LinearLayout versusEventLl,nonVersusEventLl;
    Context context = getBaseContext();

    RecyclerView recyclerView;
    CommentRecyclerAdapter commentRecyclerAdapter;
    List<Comment> commentList=new ArrayList<>();

    GifTextView loaderGIF;
    private static String scoreHint="";

    private void setScoreColours(TextView sc1, TextView sc2){
        if(sc1!=null&&sc2!=null) {
            int score1 = Integer.parseInt(((String)sc1.getText()).replaceAll("\\s+",""));
            int score2 = Integer.parseInt(((String)sc2.getText()).replaceAll("\\s+",""));
            if (score1 > score2) {
                sc1.setTextColor(Color.parseColor("#00AA00"));  //green
                sc2.setTextColor(Color.parseColor("#AA0000"));  //red
            } else if (score1 < score2) {
                sc2.setTextColor(Color.parseColor("#00AA00"));  //green
                sc1.setTextColor(Color.parseColor("#AA0000"));  //red
            } else {
                sc1.setTextColor(Color.parseColor("#0000AA"));  //blue
                sc2.setTextColor(Color.parseColor("#0000AA"));  //blue
            }
        }

    }

    private String formatTime(String timestamp){
        if(timestamp!=null) {
            String time = timestamp.substring(0, 5);
            String hours = "";
            String minutes = timestamp.substring(3, 5);
            if (Integer.parseInt(time.substring(0, 2)) > 12) {
                hours = String.valueOf(Integer.parseInt(time.substring(0, 2)) - 12);
                return hours + ":" + minutes + "pm";
            } else {
                return hours + ":" + minutes + "am";
            }
        }else{
            return "";
        }
    }



    String event_id = "";
    boolean isCommentListLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_info_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         @Override
           public void onClick(View v) {
                      finish();
                  }
           });

         getSupportActionBar().setTitle("Match Info");


        String JsonEventInfo = "";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            JsonEventInfo = extras.getString("SELECTED_EVENT","-1");
        }
        Typeface inconsolataBoldFont = Typeface.createFromAsset(getAssets(),  "fonts/InconsolataBold.ttf");
        Typeface hammersmithOnefont = Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf");
        EventDetailsPOJO eventInfo = new Gson().fromJson(JsonEventInfo, EventDetailsPOJO.class);

        eventNameTv = (TextView)findViewById(R.id.info_event_name);
        team1Tv= (TextView)findViewById(R.id.info_team_1);
        team2Tv= (TextView)findViewById(R.id.info_team_2);
        versusEventLl= (LinearLayout) findViewById(R.id.info_versus_event);
        nonVersusEventLl= (LinearLayout) findViewById(R.id.info_non_versus_event);
        participantsTv = (TextView)findViewById(R.id.info_non_versus_event_participants);
        eventHintTv = (TextView)findViewById(R.id.info_event_hint);
        score1Tv = (TextView)findViewById(R.id.info_score1);
        score2Tv = (TextView)findViewById(R.id.info_score2);
        eventTimeTv = (TextView)findViewById(R.id.info_event_time);
        roundTv = (TextView)findViewById(R.id.info_event_round);

        loaderGIF = (GifTextView)findViewById(R.id.loader);
        noCommentsTv = (TextView)findViewById(R.id.no_comments_prompt);

        eventNameTv.setTypeface(hammersmithOnefont);
        eventNameTv.setText(eventInfo.getName());
        roundTv.setTypeface(hammersmithOnefont);
        roundTv.setText(eventInfo.getRound());

        if(eventInfo.getEliminationType().equalsIgnoreCase("individual")){
            versusEventLl.setVisibility(View.GONE);
            nonVersusEventLl.setVisibility(View.VISIBLE);
            participantsTv.setTypeface(inconsolataBoldFont);
            String participantsString="";
            /*for (int i = 0; i <eventInfo.getParticipants().size(); i++) {
                participantsString += (eventInfo.getParticipants().get(i)+"\n");
            }*/
            participantsTv.setText(participantsString);
        }else{
            versusEventLl.setVisibility(View.VISIBLE);
            nonVersusEventLl.setVisibility(View.GONE);
            team1Tv.setTypeface(inconsolataBoldFont);
            team2Tv.setTypeface(inconsolataBoldFont);
            team1Tv.setText(eventInfo.getDept1());
            team2Tv.setText(eventInfo.getDept2());
        }

        recyclerView = (RecyclerView)findViewById(R.id.commentary_recycler_view);
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentRecyclerAdapter);

        commentRecyclerAdapter.notifyDataSetChanged();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventInfo.getName());
        mFirebaseAnalytics.logEvent("EventInformation",bundle);
        //Loading comments

        //TODO : Add event id here
        event_id = String.valueOf(eventInfo.getName());
        commentList.clear();

    }

    //used to check whether if we need to call removeloadingscreen
    boolean doinbg = true;
    class RemoveLoadingGif extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            if(loaderGIF.getVisibility() == View.GONE)
            {
                doinbg = false;
            }
        }

        @Override
        protected String doInBackground(String... empty) {
            try
            {
             if(doinbg)
                Thread.sleep(15000);
            }
            catch (InterruptedException i){ }
            return null;
        }
        @Override
        protected void onPostExecute(String unused) {
            if (doinbg) {
                loaderGIF.setVisibility(View.GONE);
                Snackbar.make(viewGroup, "There are no comments for this event!", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    ViewGroup viewGroup;
    DatabaseReference databaseReference;
    ChildEventListener commentListener;
    @Override
    protected void onResume() {
        super.onResume();


        viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        //calling async task to remove the loading animation
        new RemoveLoadingGif().execute("");

        //Loading comments
        //Turns out this returns ALL the children , not only from this point onward
        databaseReference= FirebaseDatabase.getInstance().getReference("comments").child(event_id);

        commentListener = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment new_comment = dataSnapshot.child("nameValuePairs").getValue(Comment.class);
                commentList.add(0,new_comment);
                doinbg = false;
                loaderGIF.setVisibility(View.GONE);
                commentRecyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                doinbg = false;
                loaderGIF
                        //.clearAnimation();
                        .setVisibility(View.GONE);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(commentListener);

        //Fetching scores here
        DatabaseReference scoreDatabase= FirebaseDatabase.getInstance().getReference("scores").child(event_id);
        scoreDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Score score = dataSnapshot.getValue(Score.class);
                if(score!=null) {
                    try {
                        score1Tv.setText(score.getScore1());
                        score2Tv.setText(score.getScore2());
                        setScoreColours(score1Tv, score2Tv);
                    }
                    catch(NumberFormatException e){
                        score1Tv.setText(score.getScore1());
                        score2Tv.setText(score.getScore2());
                    }
                    if(score.getHint()!=null||score.getHint().length()!=0)
                        scoreHint = score.getHint();
                    eventHintTv.setText(scoreHint);
                    eventTimeTv.setText(formatTime(score.getTimestamp()));
                }
                else{
                    Log.i("EventInfoActivity", "score object is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause()
    {
        commentList.clear();
        commentRecyclerAdapter.notifyDataSetChanged();
        super.onPause();
    }
}
