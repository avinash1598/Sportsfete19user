package spider.app.sportsfete.SportDetails;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;
import spider.app.sportsfete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SportDetailsFragment extends Fragment {

    private static final String TAG="SportDetailsFragment";
    RecyclerView recyclerView;
    Context context;
    SportDetailsRecyclerAdapter recyclerAdapter;

    String[] sportsArray ;
    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    public SportDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context=getContext();

        recyclerView= (RecyclerView) view.findViewById(R.id.rule_book_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);
        sportsArray = getResources().getStringArray(R.array.sport_array);
        recyclerAdapter=new SportDetailsRecyclerAdapter(sportsArray,context);
        recyclerView.setAdapter(recyclerAdapter);

        setClickListener();
    }

    void setClickListener(){
        rx.Observable<String> observable= recyclerAdapter.getPositionClicks();
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Intent intent=new Intent(getActivity(), SportDetailsActivity.class);
                intent.putExtra("SELECTED_SPORT",s);
                intent.putExtra("FIXTURE_TYPE",5);
                startActivity(intent);


            }
        });
    }
}
