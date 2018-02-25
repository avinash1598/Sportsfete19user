package spider.app.sportsfete.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.List;

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
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_item, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, final int position) {
        EventDetailsPOJO event=eventList.get(position);

        String status="";
        if(event.getEliminationType()!=null) {
            {
                if (event.getEliminationType().equalsIgnoreCase("individual")) {
                    //TODO:
                } else {
                    holder.nonVersusEventLl.setVisibility(View.GONE);
                    holder.versusEventLl.setVisibility(View.VISIBLE);
                    holder.team1Tv.setTypeface(inconsolataBoldFont);
                    holder.team1Tv.setText(event.getDept1());
                    holder.team1Tv.setSelected(true);
                    holder.team1Tv.setSingleLine(true);
                    holder.team2Tv.setText(event.getDept2());
                    holder.team2Tv.setTypeface(inconsolataBoldFont);
                    holder.team2Tv.setSelected(true);
                    holder.team2Tv.setSingleLine(true);
                }
            }

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
                holder.startTimeTv.setText(formatStartTime(event.getStartTime()));
            } else if (eventList.get(position).getStatus().equals(FLAG_STATUS_LIVE)) {
                holder.statusTv.setTextColor(Color.parseColor("#FF5722"));
                status = "LIVE";
                holder.statusTv.startAnimation(blinkAnimation);
                holder.startTimeTv.setText("");
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

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    private Spanned formatStartTime(String unformattedStartTime){

        String date = unformattedStartTime.substring(8,10) + "-" + unformattedStartTime.substring(5,7) + "-" + unformattedStartTime.substring(0,4);
        String time = unformattedStartTime.substring(11,16);
        String formatted = date + "\nStarts at "+"<b>"+time+"</b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(formatted,Html.FROM_HTML_MODE_COMPACT);
        }
        else{
            return Html.fromHtml(formatted);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}