package spider.app.sportsfete.Following;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;

import spider.app.sportsfete.R;

/**
 * Created by dhananjay on 6/2/17.
 */

public class SubscribeViewHolder extends RecyclerView.ViewHolder  {

    TextView departmentNameTv;
    SparkButton sparkButton;

    public SubscribeViewHolder(View itemView) {
        super(itemView);
        departmentNameTv= (TextView) itemView.findViewById(R.id.subscribe_dept_name);
        sparkButton= (SparkButton) itemView.findViewById(R.id.subscribe_spark_button);
    }
}
