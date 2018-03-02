package spider.app.sportsfete.Leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import spider.app.sportsfete.API.Leaderboard;
import spider.app.sportsfete.R;

/**
 * Created by akashj on 21/1/17.
 */

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewHolder>{

    private static final String TAG="LeaderboardAdapter";
    List<Leaderboard> standingList = new ArrayList<>();
    List<String> points_distribution = new ArrayList<>();
    Context context;
    Typeface inconsolataBoldFont,hammersmithOneFont;
    int gold,silver,bronze;

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

    MyAdapterListener onClickListener;

    public interface MyAdapterListener {
        void onItemSelected(int position, ExpandableLayout expandableLayout, ImageView imageView);
    }

    public LeaderboardRecyclerAdapter(List<Leaderboard> standingList, Context context, MyAdapterListener myAdapterListener){
        this.standingList = standingList;
        this.context=context;
        inconsolataBoldFont = Typeface.createFromAsset(context.getAssets(),  "fonts/InconsolataBold.ttf");
        hammersmithOneFont = Typeface.createFromAsset(context.getAssets(),  "fonts/HammersmithOneRegular.ttf");
        gold=Color.parseColor("#fbc02d");
        silver=Color.parseColor("#9e9e9e");
        bronze=Color.parseColor("#a1887f");
        onClickListener = myAdapterListener;
        setHasStableIds(true);
    }

    @Override
    public LeaderboardRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaderboardRecyclerViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.single_leaderboard_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final LeaderboardRecyclerViewHolder holder, final int position) {
        Leaderboard standing=standingList.get(position);

        holder.departmentTV.setTypeface(inconsolataBoldFont);
        holder.departmentTV.setText(standing.getDept());
        holder.scoreTV.setTypeface(hammersmithOneFont);
        holder.scoreTV.setText(standing.getTotal()+"");
        holder.rankTv.setTypeface(hammersmithOneFont);
        holder.rankTv.setText((position+1)+"");

        points_distribution.clear();
        points_distribution.addAll(standing.getSplitup());

        switch(position){
            case 0: holder.medalIv.setImageDrawable(context.getDrawable(R.drawable.gold));
            case 1: holder.medalIv.setImageDrawable(context.getDrawable(R.drawable.silver));
            case 2: holder.medalIv.setImageDrawable(context.getDrawable(R.drawable.bronze));
        }

        setDeptIcon(holder.department_icon,standing.getDept().trim());

        if(!standing.getSplitup().isEmpty()) {
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            PointsDistributionRecyclerAdapter recyclerAdapter = new PointsDistributionRecyclerAdapter(points_distribution, context);
            holder.recyclerView.setAdapter(recyclerAdapter);
        }

        holder.arrow_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemSelected(position, holder.expandableLayout, holder.arrow_indicator);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setDeptIcon(ImageView imageView, String dept){
        switch(dept){
            case "ARCH":imageView.setImageDrawable(context.getDrawable(dept_icon[0]));break;
            case "CHEM":imageView.setImageDrawable(context.getDrawable(dept_icon[1]));break;
            case "CIVIL":imageView.setImageDrawable(context.getDrawable(dept_icon[2]));break;
            case "CSE":imageView.setImageDrawable(context.getDrawable(dept_icon[3]));break;
            case "DOMS":imageView.setImageDrawable(context.getDrawable(dept_icon[4]));break;
            case "ECE":imageView.setImageDrawable(context.getDrawable(dept_icon[5]));break;
            case "EEE":imageView.setImageDrawable(context.getDrawable(dept_icon[6]));break;
            case "ICE":imageView.setImageDrawable(context.getDrawable(dept_icon[7]));break;
            case "MCA":imageView.setImageDrawable(context.getDrawable(dept_icon[8]));break;
            case "MECH":imageView.setImageDrawable(context.getDrawable(dept_icon[9]));break;
            case "META":imageView.setImageDrawable(context.getDrawable(dept_icon[10]));break;
            case "MTECH":imageView.setImageDrawable(context.getDrawable(dept_icon[11]));break;
            case "PHD":imageView.setImageDrawable(context.getDrawable(dept_icon[12]));break;
            case "PROD":imageView.setImageDrawable(context.getDrawable(dept_icon[13]));break;
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
