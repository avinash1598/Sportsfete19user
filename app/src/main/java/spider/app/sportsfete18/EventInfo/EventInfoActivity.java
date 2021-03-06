package spider.app.sportsfete18.EventInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifTextView;
import spider.app.sportsfete18.API.EventDetailsPOJO;
import spider.app.sportsfete18.FireBaseServices.Comment;
import spider.app.sportsfete18.FireBaseServices.Score;
import spider.app.sportsfete18.R;

public class EventInfoActivity extends AppCompatActivity{


    private static final String TAG="EventInfoActivity";
    TextView eventNameTv,team1Tv,team2Tv,participantsTv,eventHintTv,roundTv,score1Tv,score2Tv,eventTimeTv,
            noCommentsTv,eventVenue, eventstatus,eventStartTime, undecided_match1, undecided_match2, commentary,
            last_updated_timestamp;

    CircleImageView team_1_icon, team_2_icon;
    LinearLayout versusEventLl,nonVersusEventLl;
    Context context = getBaseContext();
    CardView enter_card;
    SlidingUpPanelLayout slidingUpPanelLayout;
    LinearLayout upper_bound, lower_bound;

    int upper_bound_val=0, lower_bound_val=0;

    private static final String FLAG_STATUS_UPCOMING = "upcoming";
    private static final String FLAG_STATUS_LIVE = "live";
    private static final String FLAG_STATUS_COMPLETED = "completed";
    private String status;

    RecyclerView recyclerView;
    CommentRecyclerAdapter commentRecyclerAdapter;
    List<Comment> commentList=new ArrayList<>();

    Animation blinkAnimation;
    GifTextView loaderGIF;
    private static String scoreHint="";

    int[] dept_icon = {
            R.drawable.arch1,
            R.drawable.chem1,
            R.drawable.civil2,
            R.drawable.cse2,
            R.drawable.doms2,
            R.drawable.ece2,
            R.drawable.eee2,
            R.drawable.ice2,
            R.drawable.mca2,
            R.drawable.mech2,
            R.drawable.meta2,
            R.drawable.mtech2,
            R.drawable.phd2,
            R.drawable.prod2
    };

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
                sc1.setTextColor(getResources().getColor(R.color.colorTabtext));  //buish
                sc2.setTextColor(getResources().getColor(R.color.colorTabtext));  //blueish
            }
        }

    }


    String event_id = "";
    boolean isCommentListLoaded = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        for(int i = 0; i < toolbar.getChildCount(); i++)

        { View view = toolbar.getChildAt(i);
            Log.d("font set","true"+"");
            if(view instanceof TextView) {
                TextView textView = (TextView) view;

                textView.setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf"));
                Log.d("font set","true"+"");
            }
        }

        String JsonEventInfo = "";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            JsonEventInfo = extras.getString("SELECTED_EVENT","-1");
        }
        Typeface inconsolataBoldFont = Typeface.createFromAsset(getAssets(),  "fonts/InconsolataBold.ttf");
        Typeface hammersmithOnefont = Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf");
        Typeface francoisOneRegularFont = Typeface.createFromAsset(getAssets(),  "fonts/FrancoisOneRegular.ttf");
        EventDetailsPOJO eventInfo = new Gson().fromJson(JsonEventInfo, EventDetailsPOJO.class);

        eventNameTv = (TextView)findViewById(R.id.info_event_name);
        team1Tv= (TextView)findViewById(R.id.info_team_1);
        team2Tv= (TextView)findViewById(R.id.info_team_2);
        versusEventLl= (LinearLayout) findViewById(R.id.versus_event);
        nonVersusEventLl= (LinearLayout) findViewById(R.id.non_versus_event);
        participantsTv = (TextView)findViewById(R.id.info_non_versus_event_participants);
        eventHintTv = (TextView)findViewById(R.id.info_event_hint);
        score1Tv = (TextView)findViewById(R.id.info_score1);
        score2Tv = (TextView)findViewById(R.id.info_score2);
        eventTimeTv = (TextView)findViewById(R.id.info_event_timestamp);
        roundTv = (TextView)findViewById(R.id.info_event_round);
        team_1_icon = (CircleImageView) findViewById(R.id.info_department1_icon);
        team_2_icon = (CircleImageView) findViewById(R.id.info_department2_icon);
        eventVenue = (TextView) findViewById(R.id.info_event_venue);
        eventStartTime = (TextView) findViewById(R.id.info_event_timestamp);
        eventstatus = (TextView) findViewById(R.id.info_event_status);
        enter_card = (CardView) findViewById(R.id.commentary_enter_transition);
        undecided_match1 = (TextView) findViewById(R.id.info_undecided_match1);
        undecided_match2 = (TextView) findViewById(R.id.info_undecided_match2);
        commentary = (TextView) findViewById(R.id.commentory_title);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        upper_bound = (LinearLayout) findViewById(R.id.upper_bound_height);
        lower_bound = (LinearLayout) findViewById(R.id.lower_bound_layout);
        last_updated_timestamp = (TextView) findViewById(R.id.last_updated_timestamp);


        loaderGIF = (GifTextView)findViewById(R.id.loader);
        noCommentsTv = (TextView)findViewById(R.id.no_comments_prompt);

        commentary.setTypeface(hammersmithOnefont);
        undecided_match1.setTypeface(hammersmithOnefont);
        undecided_match2.setTypeface(hammersmithOnefont);
        eventNameTv.setTypeface(hammersmithOnefont);
        eventNameTv.setText(eventInfo.getName());
        roundTv.setTypeface(hammersmithOnefont);
        participantsTv.setTypeface(hammersmithOnefont);
        roundTv.setText(eventInfo.getRound());
        score1Tv.setTypeface(hammersmithOnefont);
        score2Tv.setTypeface(hammersmithOnefont);
        eventHintTv.setTypeface(hammersmithOnefont);
        last_updated_timestamp.setTypeface(hammersmithOnefont);



        String round = eventInfo.getRound();

        if (eventInfo.getEliminationType().equalsIgnoreCase("single")||
                eventInfo.getEliminationType().equalsIgnoreCase("double")) {
            //TODO:
            nonVersusEventLl.setVisibility(View.GONE);
            versusEventLl.setVisibility(View.VISIBLE);
            team1Tv.setTypeface(inconsolataBoldFont);
            team1Tv.setText(eventInfo.getDept1());


            if(eventInfo.getDept1().contains("WINNER OF")||eventInfo.getDept1().contains("LOSER OF")){
                team_1_icon.setVisibility(View.GONE);
                undecided_match1.setVisibility(View.VISIBLE);
                if(eventInfo.getDept1().contains("WINNER OF")){
                    undecided_match1.setBackgroundResource(R.drawable.circular_border_winner);
                    undecided_match1.setText("W"+eventInfo.getDept1().trim().split(" ")[2]);
                    undecided_match1.setTextColor(Color.parseColor("#4ab556"));
                }else if(eventInfo.getDept1().contains("LOSER OF")){
                    undecided_match1.setBackgroundResource(R.drawable.circular_border_loser);
                    undecided_match1.setText("L"+eventInfo.getDept1().trim().split(" ")[2]);
                    undecided_match1.setTextColor(Color.RED);
                }
            }
            if(eventInfo.getDept2().contains("WINNER OF")||eventInfo.getDept2().contains("LOSER OF")){
                team_2_icon.setVisibility(View.GONE);
                undecided_match2.setVisibility(View.VISIBLE);
                if(eventInfo.getDept2().contains("WINNER OF")){
                    undecided_match2.setBackgroundResource(R.drawable.circular_border_winner);
                    undecided_match2.setText("W"+eventInfo.getDept2().trim().split(" ")[2]);
                    undecided_match2.setTextColor(Color.parseColor("#4ab556"));
                }else if(eventInfo.getDept2().contains("LOSER OF")){
                    undecided_match2.setBackgroundResource(R.drawable.circular_border_loser);
                    undecided_match2.setText("L"+eventInfo.getDept2().trim().split(" ")[2]);
                    undecided_match2.setTextColor(Color.RED);
                }
            }

            setDeptIcon(team_1_icon,eventInfo.getDept1().trim().toUpperCase());
            team1Tv.setSelected(true);
            team1Tv.setSingleLine(true);
            team2Tv.setText(eventInfo.getDept2());
            setDeptIcon(team_2_icon,eventInfo.getDept2().trim().toUpperCase());
            team2Tv.setTypeface(inconsolataBoldFont);
            team2Tv.setSelected(true);
            team2Tv.setSingleLine(true);
            round = round + " : " + eventInfo.getFixture();

        } else {
            nonVersusEventLl.setVisibility(View.VISIBLE);
            versusEventLl.setVisibility(View.GONE);
            participantsTv.setText("");
            for(String participants: eventInfo.getParticipatingTeams()){
                participantsTv.setText(participantsTv.getText()+"  "+participants+"  ");
            }
        }


        participantsTv.setSelected(true);

        blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
        blinkAnimation.setDuration(250);
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        eventNameTv.setTypeface(hammersmithOnefont);
        eventNameTv.setText(eventInfo.getName());
        roundTv.setTypeface(hammersmithOnefont);

        roundTv.setText(round);
        eventVenue.setTypeface(francoisOneRegularFont);
        eventVenue.setText(eventInfo.getVenue());

        if (eventInfo.getStatus().equals(FLAG_STATUS_UPCOMING)) {
            eventstatus.setTextColor(Color.parseColor("#009688"));
            status = "UPCOMING";
            eventStartTime.setText(getCurrentTime(Long.valueOf(eventInfo.getStartTime())));
        } else if (eventInfo.getStatus().equals(FLAG_STATUS_LIVE)) {
            eventstatus.setTextColor(Color.parseColor("#FF5722"));
            status = "LIVE";
            eventstatus.startAnimation(blinkAnimation);
            eventStartTime.setText(getCurrentTime(Long.valueOf(eventInfo.getStartTime())));
        } else {
            eventstatus.setTextColor(Color.parseColor("#4CAF50"));
            status = "COMPLETED";
            eventStartTime.setText("");
            String winner = eventInfo.getWinner();
            //setting color
            if (eventInfo.getWinner().equalsIgnoreCase(eventInfo.getDept1())) {
                team1Tv.setTextColor(Color.parseColor("#fbc02d"));
            } else if (eventInfo.getWinner().equalsIgnoreCase(eventInfo.getDept2())) {
                team2Tv.setTextColor(Color.parseColor("#fbc02d"));
            }
            eventStartTime.setText(getCurrentTime(Long.valueOf(eventInfo.getStartTime())));
        }

        eventstatus.setText(status);

        recyclerView = (RecyclerView)findViewById(R.id.commentary_recycler_view);
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentList, EventInfoActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentRecyclerAdapter);

        commentRecyclerAdapter.notifyDataSetChanged();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventInfo.getName());
        mFirebaseAnalytics.logEvent("EventInformation",bundle);
        //Loading comments

        //TODO : Add event id here
        event_id = eventInfo.getId();
        commentList.clear();

        //slidingUpPanelLayout.setAnchorPoint(448);

        slidingUpPanelLayout.setPanelHeight(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("heigh",""+(lower_bound.getHeight()-slidingUpPanelLayout.getHeight()));
                slidingUpPanelLayout.setPanelHeight((int) ((slidingUpPanelLayout.getHeight())
                        -(lower_bound.getHeight())));
                //slidingUpPanelLayout.setPanelHeight((int) convertDpToPixel(250,EventInfoActivity.this));
            }
        },500);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setDeptIcon(CircleImageView imageView, String dept){
        Log.d("department",dept+"");
        switch(dept){
            case "ARCHI":setImageResource((dept_icon[0]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "CHEM":setImageResource((dept_icon[1]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "CIVIL":setImageResource((dept_icon[2]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "CSE":imageView.setImageResource((dept_icon[3]));
                imageView.setFillColor(Color.parseColor("#16282a"));break;
            case "DOMS":setImageResource((dept_icon[4]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "ECE":setImageResource((dept_icon[5]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "EEE":setImageResource((dept_icon[6]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "ICE":setImageResource((dept_icon[7]),imageView);
                imageView.setFillColor(Color.parseColor("#16282a"));break;
            case "MCA":setImageResource((dept_icon[8]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "MECH":setImageResource((dept_icon[9]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "META":setImageResource((dept_icon[10]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "M_TECH":setImageResource((dept_icon[11]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "PHD_MSC_MS":setImageResource((dept_icon[12]),imageView);
                imageView.setFillColor(Color.WHITE);break;
            case "PROD":setImageResource((dept_icon[13]),imageView);
                imageView.setFillColor(Color.WHITE);break;
        }
    }

    public void setImageResource(int drawable, CircleImageView imageView){
        Picasso.with(context).load(drawable)
                .skipMemoryCache()
                .fit()
                .into(imageView);
    }

    private String getCurrentTime(Long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, new Date(time));
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
                Comment new_comment = dataSnapshot.getValue(Comment.class);
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
        DatabaseReference scoreDatabase= FirebaseDatabase.getInstance().getReference("events").child(event_id);
        scoreDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Score score = dataSnapshot.getValue(Score.class);
                Log.d("score",score.getDept1_score()+""+score.getDept2_score());
                if(score!=null) {
                    try {
                        score1Tv.setText(score.getDept1_score());
                        score2Tv.setText(score.getDept2_score());
                        last_updated_timestamp.setText("Last Updated at: "+getCurrentTime(score.getScore_timestamp()));
                        setScoreColours(score1Tv, score2Tv);
                    }
                    catch(NumberFormatException e){
                        e.printStackTrace();
                        score1Tv.setText(score.getDept1_score());
                        score2Tv.setText(score.getDept2_score());
                    }
                    if(score.getHint()!=null||score.getHint().length()!=0)
                        scoreHint = score.getHint();
                    eventHintTv.setText(scoreHint);
                    //eventTimeTv.setText(formatTime(score.getTimestamp()));
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

    @Override
    public void onBackPressed(){
        //slideDown(enter_card);
        super.onBackPressed();
    }
}
