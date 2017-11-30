package spider.app.sportsfete.Following;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;
import spider.app.sportsfete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment{



    private static final String TAG="SubscribeFragment";
    boolean[] checked=new boolean[15];
    List<String> deptList=new ArrayList();
    String[] deptArraySharedPreference=new String[15];
    RecyclerView recyclerView;
    SubscribeRecyclerAdapter adapter;
    Context context;
    SharedPreferences prefs;
    private int currentTransitionEffect = JazzyHelper.TILT;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    public SubscribeFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context=getContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        deptArraySharedPreference=getResources().getStringArray(R.array.department_array_shared_preference);


        for ( int i = 0; i < deptArraySharedPreference.length; i ++)
            deptList.add(deptArraySharedPreference[i]);


        for (int i = 0; i < deptList.size(); i++) {
            checked[i]=prefs.getBoolean(deptArraySharedPreference[i]+"Checked",false);
        }

        recyclerView= (RecyclerView) view.findViewById(R.id.subscribe_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentTransitionEffect);
        recyclerView.setOnScrollListener(jazzyRecyclerViewScrollListener);

        adapter=new SubscribeRecyclerAdapter(deptList,context,checked);
        recyclerView.setAdapter(adapter);

        setClickListener();
    }

    void setClickListener(){
        rx.Observable<String> observable= adapter.getPositionClicks();
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                int selected=Integer.parseInt(s);
                if (!checked[selected]){
                    checked[selected]=true;
                    Toast.makeText(getContext(), "Subscribed to "+deptArraySharedPreference[selected], Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic(deptArraySharedPreference[selected]);
                    prefs.edit().putBoolean(deptArraySharedPreference[selected]+"Checked", true).apply();
                }else {
                    checked[selected]=false;
                    Toast.makeText(getContext(), "Unsubscribed to "+deptArraySharedPreference[selected], Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(deptArraySharedPreference[selected]);
                    prefs.edit().putBoolean(deptArraySharedPreference[selected]+"Checked", false).apply();
                }
            }
        });
    }

}
