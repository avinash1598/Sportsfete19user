package spider.app.sportsfete.Leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import spider.app.sportsfete.API.Standing;
import spider.app.sportsfete.R;

/**
 * Created by akashj on 21/1/17.
 */

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewHolder>{

    private static final String TAG="LeaderboardAdapter";
    List<Standing> standingList = new ArrayList<>();
    Context context;
    Typeface inconsolataBoldFont,hammersmithOneFont;
    int gold,silver,bronze;


    public LeaderboardRecyclerAdapter(List<Standing> standingList, Context context){
        this.standingList = standingList;
        this.context=context;
        inconsolataBoldFont = Typeface.createFromAsset(context.getAssets(),  "fonts/InconsolataBold.ttf");
        hammersmithOneFont = Typeface.createFromAsset(context.getAssets(),  "fonts/HammersmithOneRegular.ttf");
        gold=Color.parseColor("#fbc02d");
        silver=Color.parseColor("#9e9e9e");
        bronze=Color.parseColor("#a1887f");
        setHasStableIds(true);
    }

    @Override
    public LeaderboardRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaderboardRecyclerViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.single_leaderboard_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LeaderboardRecyclerViewHolder holder, int position) {
        Standing standing=standingList.get(position);
        holder.departmentTV.setTypeface(inconsolataBoldFont);
        holder.departmentTV.setText(standing.getDepartmentName());
        holder.scoreTV.setTypeface(hammersmithOneFont);
        holder.scoreTV.setText(standing.getScore());
        holder.rankTv.setTypeface(hammersmithOneFont);
        holder.rankTv.setText(String.valueOf(standing.getRank()));
      if((standing.getRank()==1)&&((Integer.parseInt(standing.getScore())!=0))){
          holder.departmentTV.setTextColor(gold);
          holder.rankTv.setTextColor(gold);
          holder.scoreTV.setTextColor(gold);
          holder.medalIv.setImageResource(R.drawable.gold);
          holder.medalIv.setVisibility(View.VISIBLE);
        }else if(standing.getRank()==2&&((Integer.parseInt(standing.getScore())!=0))){
          holder.departmentTV.setTextColor(silver);
          holder.rankTv.setTextColor(silver);
          holder.scoreTV.setTextColor(silver);
          holder.medalIv.setImageResource(R.drawable.silver);
          holder.medalIv.setVisibility(View.VISIBLE);
        }else if(standing.getRank()==3&&((Integer.parseInt(standing.getScore())!=0))){
          holder.departmentTV.setTextColor(bronze);
          holder.rankTv.setTextColor(bronze);
          holder.scoreTV.setTextColor(bronze);
          holder.medalIv.setImageResource(R.drawable.bronze);
          holder.medalIv.setVisibility(View.VISIBLE);
      }
    }

    @Override
    public int getItemCount() {
        return standingList.size();
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
