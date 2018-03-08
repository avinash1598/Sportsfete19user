package spider.app.sportsfete18.SportDetails;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import spider.app.sportsfete18.API.Event;
import spider.app.sportsfete18.R;

import static android.content.ContentValues.TAG;

/**
 * Created by akashj on 8/2/17.
 */
public class FixturesRecyclerAdapter extends RecyclerView.Adapter<FixturesViewHolder> {

    private final String selectedSport;
    List<Event> eventList;
    Context context;
    Animation blinkAnimation;
    Typeface inconsolataBoldFont, francoisOneRegularFont;
    Typeface hammersmithOnefont;
    private final PublishSubject<String> onClickSubject = PublishSubject.create();

    private static final String FLAG_STATUS_UPCOMING = "u";
    private static final String FLAG_STATUS_LIVE = "l";
    private static final String FLAG_STATUS_COMPLETED = "c";

    public FixturesRecyclerAdapter(List<Event> eventList, Context context, String selectedSport) {

        this.eventList = eventList;
        this.context = context;
        this.selectedSport = selectedSport;
        inconsolataBoldFont = Typeface.createFromAsset(context.getAssets(), "fonts/InconsolataBold.ttf");
        francoisOneRegularFont = Typeface.createFromAsset(context.getAssets(), "fonts/FrancoisOneRegular.ttf");
        hammersmithOnefont = Typeface.createFromAsset(context.getAssets(), "fonts/HammersmithOneRegular.ttf");
        blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
        blinkAnimation.setDuration(250);
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        setHasStableIds(true);

    }


    @Override
    public FixturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixturesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_fixtures_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FixturesViewHolder holder, final int position) {

        Log.i(TAG, "onBindViewHolder: "+ position);
        Event event=eventList.get(position);
        Log.i(TAG, "ChooseEvent: "+event.getName() + selectedSport);
            if (event.getParticipants().size() == 2) {
                holder.nonVersusEventLl.setVisibility(View.GONE);
                holder.versusEventLl.setVisibility(View.VISIBLE);
                holder.team1Tv.setTypeface(inconsolataBoldFont);
                holder.team2Tv.setTypeface(inconsolataBoldFont);
                holder.team1Tv.setText(event.getParticipants().get(0));
                holder.team2Tv.setText(event.getParticipants().get(1));
            }else if (event.getParticipants().size() == 0) {
                holder.nonVersusEventLl.setVisibility(View.GONE);
                holder.versusEventLl.setVisibility(View.VISIBLE);
                holder.team1Tv.setTypeface(inconsolataBoldFont);
                holder.team1Tv.setText(event.getTeamA());
                holder.team1Tv.setSelected(true);
                holder.team1Tv.setSingleLine(true);
                if(event.getTeamB()==null){
                    holder.team2Tv.setVisibility(View.GONE);
                    holder.vsTv.setVisibility(View.GONE);
                }else{
                    holder.team2Tv.setText(event.getTeamB());
                    holder.team2Tv.setTypeface(inconsolataBoldFont);
                    holder.team2Tv.setSelected(true);
                    holder.team2Tv.setSingleLine(true);
                }
            }else {
                String participantsString = "";
                holder.nonVersusEventLl.setVisibility(View.VISIBLE);
                holder.versusEventLl.setVisibility(View.GONE);
                for (int i = 0; i < event.getParticipants().size(); i++) {
                    participantsString += (event.getParticipants().get(i) + "\t\t\t");
                }
                holder.participantsTV.setTypeface(inconsolataBoldFont);
                holder.participantsTV.setText(participantsString);
            }
            String round=event.getRound();
            if(!round.equals(event.getFixture()))
                round=round+" : "+event.getFixture();
            holder.roundTv.setText(round);
            holder.roundTv.setTypeface(francoisOneRegularFont);
            holder.venueTV.setTypeface(francoisOneRegularFont);
            holder.venueTV.setText(event.getVenue());
            holder.statusTv.setAllCaps(true);
            Log.d("event_name",event.getName());
            if(event.getName().contains("BOYS")){
                holder.boys_girls.setText("Boys");
            }
            else if(event.getName().contains("GIRLS")){
                holder.boys_girls.setText("Girls");
            }
            else if(event.getName().contains("MIXED")){
                holder.boys_girls.setText("Mixed");
            }

            String status = "";
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
                String winner=event.getWinner();
                //setting color
                if(event.getWinner()!=null){
                    if(event.getParticipants().size()!=0){
                        if(winner.equals(event.getParticipants().get(0)))
                            holder.team1Tv.setTextColor(Color.parseColor("#fbc02d"));
                        else
                            holder.team2Tv.setTextColor(Color.parseColor("#fbc02d"));
                    }else if(event.getTeamA()!=null&&event.getWinner().equals(event.getTeamA()))
                        holder.team1Tv.setTextColor(Color.parseColor("#fbc02d"));
                    else if(event.getTeamB()!=null&&event.getWinner().equals(event.getTeamB()))
                        holder.team2Tv.setTextColor(Color.parseColor("#fbc02d"));
                }
            }
            holder.statusTv.setText(status);
        }





    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    private Spanned formatStartTime(String unformattedStartTime){
        String date = unformattedStartTime.substring(0,10);
        String time = unformattedStartTime.substring(11,16);
        String formatted = date + "\nStarts at "+"<b>"+time+"</b>";
        return Html.fromHtml(formatted);
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