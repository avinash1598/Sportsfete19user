package spider.app.sportsfete.Leaderboard;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import spider.app.sportsfete.R;

/**
 * Created by akashj on 21/1/17.
 */

public class LeaderboardRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView departmentTV, scoreTV,rankTv;
    ImageView medalIv, arrow_indicator, department_icon;
    ExpandableLayout expandableLayout;
    RecyclerView  recyclerView;

    public LeaderboardRecyclerViewHolder(View itemView) {
        super(itemView);
        departmentTV= (TextView) itemView.findViewById(R.id.department);
        scoreTV= (TextView) itemView.findViewById(R.id.score);
        medalIv= (ImageView) itemView.findViewById(R.id.medal);
        rankTv= (TextView) itemView.findViewById(R.id.department_rank);
        expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLayout);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.score_distribution);
        arrow_indicator = (ImageView) itemView.findViewById(R.id.arrow_indicator);
        department_icon = (ImageView) itemView.findViewById(R.id.department_icon);
    }
}
