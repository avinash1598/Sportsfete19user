package spider.app.sportsfete.SportDetails;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.subjects.PublishSubject;
import spider.app.sportsfete.R;

/**
 * Created by dhananjay on 6/2/17.
 */

public class IndividualEventRecyclerAdapter extends RecyclerView.Adapter<SportDetailsViewHolder> {

    String[] eventList;
    private final PublishSubject<String> onClickSubject = PublishSubject.create();
    Typeface hammersmithOnefont;
    Context context;

    IndividualEventRecyclerAdapter(String[] eventList, Context context){
        this.eventList=eventList;
        this.context=context;
        setHasStableIds(true);
        hammersmithOnefont = Typeface.createFromAsset(context.getAssets(),  "fonts/HammersmithOneRegular.ttf");
    }

    @Override
    public SportDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SportDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_rule_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SportDetailsViewHolder holder, final int position) {
        holder.sportNameTv.setText(eventList[position]);
        holder.sportNameTv.setTypeface(hammersmithOnefont);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.length;
    }

    public Observable<String> getPositionClicks(){
        return onClickSubject.asObservable();
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
