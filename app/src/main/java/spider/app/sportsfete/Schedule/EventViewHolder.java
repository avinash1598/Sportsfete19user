package spider.app.sportsfete.Schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import spider.app.sportsfete.R;

/**
 * Created by dhananjay on 21/1/17.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    TextView nameTV,participantsTV,venueTV,team1Tv,team2Tv,statusTv, startTimeTv, roundTv, vsTv;
    LinearLayout versusEventLl,nonVersusEventLl;

    public EventViewHolder(View itemView) {
        super(itemView);
        nameTV= (TextView) itemView.findViewById(R.id.event_name);
        roundTv = (TextView)itemView.findViewById(R.id.event_round);
        participantsTV = (TextView) itemView.findViewById(R.id.non_versus_event_participants);
        venueTV= (TextView) itemView.findViewById(R.id.event_venue);
        versusEventLl= (LinearLayout) itemView.findViewById(R.id.versus_event);
        nonVersusEventLl= (LinearLayout) itemView.findViewById(R.id.non_versus_event);
        team1Tv= (TextView) itemView.findViewById(R.id.team_1);
        team2Tv= (TextView) itemView.findViewById(R.id.team_2);
        statusTv= (TextView) itemView.findViewById(R.id.event_status);
        startTimeTv=(TextView) itemView.findViewById(R.id.event_timestamp);
        vsTv = (TextView)itemView.findViewById(R.id.vs_text);
    }
}
