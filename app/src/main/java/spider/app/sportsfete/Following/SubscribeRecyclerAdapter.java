package spider.app.sportsfete.Following;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

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

    public SubscribeRecyclerAdapter(List<String> departmentList,Context context, boolean[] checked){
        this.departmentList=departmentList;
        setHasStableIds(true);
        this.departmentList=departmentList;
        this.checked=checked;
        this.context=context;
        customFont = Typeface.createFromAsset(context.getAssets(),  "fonts/InconsolataBold.ttf");
    }

    @Override
    public SubscribeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubscribeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_subscribe_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SubscribeViewHolder holder, final int position) {
        holder.departmentNameTv.setTypeface(customFont);
        holder.departmentNameTv.setText(departmentList.get(position));
        holder.sparkButton.setChecked(checked[position]);holder.sparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                onClickSubject.onNext(String.valueOf(position));
            }
        });
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
