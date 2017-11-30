package spider.app.sportsfete.Leaderboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import spider.app.sportsfete.R;

/**
 * Created by akashj on 21/1/17.
 */

public class LeaderboardRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView departmentTV, scoreTV,rankTv;
    ImageView medalIv;
    public LeaderboardRecyclerViewHolder(View itemView) {
        super(itemView);
        departmentTV= (TextView) itemView.findViewById(R.id.department);
        scoreTV= (TextView) itemView.findViewById(R.id.score);
        medalIv= (ImageView) itemView.findViewById(R.id.medal);
        rankTv= (TextView) itemView.findViewById(R.id.department_rank);
    }
}
