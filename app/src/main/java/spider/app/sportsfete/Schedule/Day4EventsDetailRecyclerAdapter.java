package spider.app.sportsfete.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.subjects.PublishSubject;
import spider.app.sportsfete.API.EventDetailsPOJO;
import spider.app.sportsfete.R;

/**
 * Created by dhananjay on 21/1/17.
 */

public class Day4EventsDetailRecyclerAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private static final String TAG="EventAdapter";
    List<EventDetailsPOJO> eventList;
    Context context;
    Animation blinkAnimation;
    Typeface inconsolataBoldFont,francoisOneRegularFont;
    Typeface hammersmithOnefont;
    private final PublishSubject<String> onClickSubject = PublishSubject.create();

    private static final String FLAG_STATUS_UPCOMING = "upcoming";
    private static final String FLAG_STATUS_LIVE = "live";
    private static final String FLAG_STATUS_COMPLETED = "completed";

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

    public Day4EventsDetailRecyclerAdapter(List<EventDetailsPOJO> eventList, Context context){
        this.eventList=eventList;
        this.context=context;
        inconsolataBoldFont = Typeface.createFromAsset(context.getAssets(),  "fonts/InconsolataBold.ttf");
        francoisOneRegularFont = Typeface.createFromAsset(context.getAssets(),  "fonts/FrancoisOneRegular.ttf");
        hammersmithOnefont = Typeface.createFromAsset(context.getAssets(),  "fonts/HammersmithOneRegular.ttf");
        blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
        blinkAnimation.setDuration(250);
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        setHasStableIds(true);

    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(EventViewHolder holder, final int position) {
        EventDetailsPOJO event=eventList.get(position);

        String status="";
        if(event.getEliminationType()!=null) {
            {
                if (event.getEliminationType().equalsIgnoreCase("single")||
                        event.getEliminationType().equalsIgnoreCase("double")) {
                    //TODO:
                    holder.nonVersusEventLl.setVisibility(View.GONE);
                    holder.versusEventLl.setVisibility(View.VISIBLE);
                    holder.team1Tv.setTypeface(inconsolataBoldFont);
                    holder.team1Tv.setText(event.getDept1());
                    if(event.getDept1().contains("WINNER OF")||event.getDept1().contains("LOSER OF")){
                        holder.dept_icon1.setVisibility(View.GONE);
                    }
                    if(event.getDept1().contains("WINNER OF")||event.getDept1().contains("LOSER OF")){
                        holder.dept_icon2.setVisibility(View.GONE);
                    }
                    setDeptIcon(holder.dept_icon1,event.getDept1().trim());
                    holder.team1Tv.setSelected(true);
                    holder.team1Tv.setSingleLine(true);
                    holder.team2Tv.setText(event.getDept2());
                    setDeptIcon(holder.dept_icon2,event.getDept2().trim());
                    holder.team2Tv.setTypeface(inconsolataBoldFont);
                    holder.team2Tv.setSelected(true);
                    holder.team2Tv.setSingleLine(true);
                } else {
                    holder.nonVersusEventLl.setVisibility(View.VISIBLE);
                    holder.versusEventLl.setVisibility(View.GONE);

                }
            }

            blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
            blinkAnimation.setDuration(250);
            blinkAnimation.setStartOffset(20);
            blinkAnimation.setRepeatMode(Animation.REVERSE);
            blinkAnimation.setRepeatCount(Animation.INFINITE);

            holder.nameTV.setTypeface(hammersmithOnefont);
            holder.nameTV.setText(event.getName());
            holder.roundTv.setTypeface(hammersmithOnefont);
            String round = event.getRound();
            if (!round.equals(event.getFixture()))
                round = round + " : " + event.getFixture();
            holder.roundTv.setText(round);
            holder.venueTV.setTypeface(francoisOneRegularFont);
            holder.venueTV.setText(event.getVenue());
            holder.statusTv.setAllCaps(true);

            if (eventList.get(position).getStatus().equals(FLAG_STATUS_UPCOMING)) {
                holder.statusTv.setTextColor(Color.parseColor("#009688"));
                status = "UPCOMING";
                holder.startTimeTv.setText(getCurrentTime(Long.valueOf(event.getStartTime())));
            } else if (eventList.get(position).getStatus().equals(FLAG_STATUS_LIVE)) {
                holder.statusTv.setTextColor(Color.parseColor("#FF5722"));
                status = "LIVE";
                holder.statusTv.startAnimation(blinkAnimation);
                holder.startTimeTv.setText(getCurrentTime(Long.valueOf(event.getStartTime())));
            } else {
                holder.statusTv.setTextColor(Color.parseColor("#4CAF50"));
                status = "COMPLETED";
                holder.startTimeTv.setText("");
                String winner = event.getWinner();
                //setting color
                if (event.getWinner().equalsIgnoreCase(event.getDept1())) {
                    holder.team1Tv.setTextColor(Color.parseColor("#fbc02d"));
                } else if (event.getWinner().equalsIgnoreCase(event.getDept2())) {
                    holder.team2Tv.setTextColor(Color.parseColor("#fbc02d"));
                }
                holder.startTimeTv.setText(getCurrentTime(Long.valueOf(event.getStartTime())));
            }
            holder.statusTv.setText(status);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSubject.onNext(String.valueOf(position));
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setDeptIcon(CircleImageView imageView, String dept){
        switch(dept){
            case "ARCHI":imageView.setImageResource((dept_icon[0]));break;
            case "CHEM":imageView.setImageResource((dept_icon[1]));break;
            case "CIVIL":imageView.setImageResource((dept_icon[2]));break;
            case "CSE":imageView.setImageResource((dept_icon[3]));
                imageView.setFillColor(Color.parseColor("#16282a"));break;
            case "DOMS":imageView.setImageResource((dept_icon[4]));break;
            case "ECE":imageView.setImageResource((dept_icon[5]));break;
            case "EEE":imageView.setImageResource((dept_icon[6]));break;
            case "ICE":imageView.setImageResource((dept_icon[7]));break;
            case "MCA":imageView.setImageResource((dept_icon[8]));break;
            case "MECH":imageView.setImageResource((dept_icon[9]));break;
            case "META":imageView.setImageResource((dept_icon[10]));break;
            case "M.TECH":imageView.setImageResource((dept_icon[11]));break;
            case "Phd/MSc/MS":imageView.setImageResource((dept_icon[12]));break;
            case "PROD":imageView.setImageResource((dept_icon[13]));break;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private String getCurrentTime(Long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, new Date(time*1000L));
    }
}