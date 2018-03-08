package spider.app.sportsfete18.SportDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import spider.app.sportsfete18.R;

/**
 * Created by dhananjay on 6/2/17.
 */

public class SportDetailsViewHolder extends RecyclerView.ViewHolder {

    TextView sportNameTv;
    //ImageView ruleBookIv;

    public SportDetailsViewHolder(View itemView) {
        super(itemView);
        //ruleBookIv= (ImageView) itemView.findViewById(R.id.rule_book_image);
        sportNameTv = (TextView) itemView.findViewById(R.id.sport_name);
    }
}
