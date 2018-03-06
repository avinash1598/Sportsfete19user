package spider.app.sportsfete.Following;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.subjects.PublishSubject;
import spider.app.sportsfete.R;

/**
 * Created by dhananjay on 6/2/17.
 */

public class SubscribeRecyclerAdapter extends RecyclerView.Adapter<SubscribeViewHolder> {

    List<String> departmentList;
    boolean[] checked;
    private final PublishSubject<String> onClickSubject = PublishSubject.create();
    Typeface customFont;
    Context context;
    MyAdapterListener onClickListener;

    public interface MyAdapterListener{
        public void buttonPressed(int position);
    }

    int[] drawable = {
            R.drawable.athletics,
            R.drawable.badminton,
            R.drawable.basketball,
            R.drawable.basketball,
            R.drawable.carrom,
            R.drawable.chess,
            R.drawable.cricket,
            R.drawable.cricket,
            R.drawable.football,
            R.drawable.football,
            R.drawable.handball,
            R.drawable.handball,
            R.drawable.hockey,
            R.drawable.kabaddi,
            R.drawable.khokho,
            R.drawable.khokho,
            R.drawable.marathon,
            R.drawable.powerlifting,
            R.drawable.swimming,
            R.drawable.tabletennis,
            R.drawable.tabletennis,
            R.drawable.tennis,
            R.drawable.tennis,
            R.drawable.thrwoball,
            R.drawable.volleyball,
            R.drawable.volleyball,
            R.drawable.waterpolo
    };

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


    public SubscribeRecyclerAdapter(List<String> departmentList,Context context, boolean[] checked, Typeface typeface, MyAdapterListener myAdapterListener){
        this.departmentList=departmentList;
        setHasStableIds(true);
        this.departmentList=departmentList;
        this.checked=checked;
        this.context=context;
        customFont = typeface;
        onClickListener = myAdapterListener;
    }

    @Override
    public SubscribeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new SubscribeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_subscribe_item,
                    parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(SubscribeViewHolder holder, final int position) {
        holder.departmentNameTv.setTypeface(customFont);
        holder.departmentNameTv.setText(departmentList.get(position));
        holder.sparkButton.setChecked(checked[position]);
        holder.sparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                //onClickSubject.onNext(String.valueOf(position));
                onClickListener.buttonPressed(position);
            }
        });

        if (departmentList.get(0).trim().equalsIgnoreCase("ATHLETICS")) {
            holder.dept_icon.setImageResource(drawable[position]);
            holder.dept_icon.setColorFilter(Color.parseColor("#16282a"));
        } else {

            if(departmentList.get(position).equalsIgnoreCase("m_tech")){
                holder.departmentNameTv.setText("M.Tech");
            }else if(departmentList.get(position).equalsIgnoreCase("phd_msc_ms")) {
                holder.departmentNameTv.setText("PhD/MsC/MS");
            }

            holder.dept_icon.setColorFilter(null);
            setDeptIcon(holder.dept_icon, departmentList.get(position).trim());
        }
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
                imageView.setFillColor(Color.parseColor("#16282a"));break;
            case "MCA":imageView.setImageResource((dept_icon[8]));
                imageView.setFillColor(Color.WHITE);break;
            case "MECH":imageView.setImageResource((dept_icon[9]));
                imageView.setFillColor(Color.WHITE);break;
            case "META":imageView.setImageResource((dept_icon[10]));
                imageView.setFillColor(Color.WHITE);break;
            case "M_TECH":imageView.setImageResource((dept_icon[11]));
                imageView.setFillColor(Color.WHITE);break;
            case "PHD_MSC_MS":imageView.setImageResource((dept_icon[12]));
                imageView.setFillColor(Color.WHITE);break;
            case "PROD":imageView.setImageResource((dept_icon[13]));
                imageView.setFillColor(Color.WHITE);break;
        }
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
    }

}
