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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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

        if(standing.getDept().equalsIgnoreCase("m_tech")){
            holder.departmentTV.setText("M.Tech");
        }else if(standing.getDept().equalsIgnoreCase("phd_msc_ms")) {
            holder.departmentTV.setText("PhD/MsC/MS");
        }

        holder.medalIv.setVisibility(View.GONE);
        {
            if(standingList.get(0)==standingList.get(1)||
                    standingList.get(1)!=standingList.get(2)){
                holder.medalIv.setVisibility(View.GONE);
            }else{
                switch(position){
                    case 0: holder.medalIv.setImageDrawable(context.getDrawable(R.drawable.gold));
                        holder.medalIv.setVisibility(View.VISIBLE);break;
                    case 1: holder.medalIv.setImageDrawable(context.getDrawable(R.drawable.silver));
                        holder.medalIv.setVisibility(View.VISIBLE);break;
                    case 2: holder.medalIv.setImageDrawable(context.getDrawable(R.drawable.bronze));
                        holder.medalIv.setVisibility(View.VISIBLE);break;
                }
            }
        }

        setDeptIcon(holder.department_icon,standing.getDept().trim());

        if(!standing.getSplitup().isEmpty()) {
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));

            Collections.sort(points_distribution, new Comparator<String>(){
                @Override
                public int compare( String o1, String o2) {
                    return (int) (Integer.parseInt(o2.split(":")[1].trim()) -
                            Integer.parseInt(o1.split(":")[1].trim()));
                }
            });

            PointsDistributionRecyclerAdapter recyclerAdapter = new PointsDistributionRecyclerAdapter(points_distribution, context);
            holder.recyclerView.setAdapter(recyclerAdapter);
        }else{
            points_distribution.clear();
            PointsDistributionRecyclerAdapter recyclerAdapter = new PointsDistributionRecyclerAdapter(points_distribution, context);
            holder.recyclerView.setAdapter(recyclerAdapter);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemSelected(position, holder.expandableLayout, holder.arrow_indicator);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setDeptIcon(CircleImageView imageView, String dept){
        switch(dept){
            case "ARCHI":imageView.setImageResource((dept_icon[0]));
                imageView.setFillColor(Color.WHITE);break;
            case "CHEM":imageView.setImageResource((dept_icon[1]));
                imageView.setFillColor(Color.WHITE);break;
            case "CIVIL":imageView.setImageResource((dept_icon[2]));
                imageView.setFillColor(Color.WHITE);break;
            case "CSE":imageView.setImageResource((dept_icon[3]));
                imageView.setFillColor(Color.parseColor("#16282a"));break;
            case "DOMS":imageView.setImageResource((dept_icon[4]));
                imageView.setFillColor(Color.WHITE);break;
            case "ECE":imageView.setImageResource((dept_icon[5]));
                imageView.setFillColor(Color.WHITE);break;
            case "EEE":imageView.setImageResource((dept_icon[6]));
                imageView.setFillColor(Color.WHITE);break;
            case "ICE":imageView.setImageResource((dept_icon[7]));
                imageView.setFillColor(Color.WHITE);break;
            case "MCA":imageView.setImageResource((dept_icon[8]));
                imageView.setFillColor(Color.WHITE);break;
            case "MECH":imageView.setImageResource((dept_icon[9]));
                imageView.setFillColor(Color.WHITE);break;
            case "META":imageView.setImageResource((dept_icon[10]));
                imageView.setFillColor(Color.WHITE);break;
            case "M_TECH":imageView.setImageResource((dept_icon[11]));
                imageView.setFillColor(Color.WHITE);break;
            case "Phd_MSc_MS":imageView.setImageResource((dept_icon[12]));
                imageView.setFillColor(Color.WHITE);break;
            case "PROD":imageView.setImageResource((dept_icon[13]));
                imageView.setFillColor(Color.WHITE);break;
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
