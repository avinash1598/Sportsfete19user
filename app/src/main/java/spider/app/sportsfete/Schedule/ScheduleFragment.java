package spider.app.sportsfete.Schedule;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spider.app.sportsfete.DepartmentUpdateCallback;
import spider.app.sportsfete.Leaderboard.PointsDistributionRecyclerAdapter;
import spider.app.sportsfete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment{

    private static final String TAG="ScheduleFragment";
    String selectedDept;
    String[] dialogItems;
    Context context;
    DepartmentUpdateCallback departmentUpdateCallback;
    List deptList;
    SharedPreferences prefs;
    String lastUpdatedTimestamp;
    ViewGroup viewGroup;
    int selectedDay;
    int index;
    ScheduleViewPagerAdapter scheduleViewPagerAdapter;
    ViewPager viewPager;
    RecyclerView recyclerView;
    String[] deptArraySharedPreference=new String[15];
    List<String> deptlist;
    DeptSelectionRecyclerAdapter recyclerAdapter;

    public static boolean refresh_check = true;

    public ScheduleFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = container;
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    TabLayout tabLayout;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();


        Bundle arguments = getArguments();
        refresh_check = arguments.getBoolean("refresh");
            if(!arguments.getBoolean("refresh",true)) {
                Log.d("boolean------------",false+"");
            }

        deptArraySharedPreference=getResources().getStringArray(R.array.department_array);
        deptlist = new ArrayList<>();

        for ( int i = deptArraySharedPreference.length-1; i >=0; i--)
            deptlist.add(deptArraySharedPreference[i]);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        getEventsLastUpdate();

        showEventsLastUpdate();

        getSelectedDay();

        index=selectedDay-1;

        departmentUpdateCallback= (DepartmentUpdateCallback) context;
        dialogItems = getResources().getStringArray(R.array.department_array);
        deptList=new ArrayList();
        deptList= Arrays.asList(dialogItems);
        viewPager = (ViewPager)view. findViewById(R.id.schedule_view_pager);
        scheduleViewPagerAdapter = new ScheduleViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(scheduleViewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.schedule_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab currentTab = tabLayout.getTabAt(index);
        if (currentTab != null) {
            View customView = currentTab.getCustomView();
            if (customView != null) {
                customView.setSelected(true);
            }
            currentTab.select();
            viewPager.setCurrentItem(index);
        }

        LinearLayout tabLay = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        TextView tabTextView = (TextView) tabLay.getChildAt(1);
        tabTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        final Typeface hammersmithOnefont = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf");
        tabTextView.setTypeface(hammersmithOnefont);
        tabLay = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);
        tabTextView = (TextView) tabLay.getChildAt(1);
        tabTextView.setTypeface(hammersmithOnefont);

        tabLay = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(2);
        tabTextView = (TextView) tabLay.getChildAt(1);
        tabTextView.setTypeface(hammersmithOnefont);

        tabLay = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(3);
        tabTextView = (TextView) tabLay.getChildAt(1);
        tabTextView.setTypeface(hammersmithOnefont);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout tabLay = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLay.getChildAt(1);
                tabTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tabTextView.setTypeface(hammersmithOnefont);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tabLay = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLay.getChildAt(1);
                tabTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tabTextView.setTypeface(hammersmithOnefont);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Intent intent = new Intent();
                intent.setAction("scroll_to_top"+tab.getPosition());
                getActivity().sendBroadcast(intent);
            }
        });

        selectedDept = getSelectedDept();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.dept_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,true));
        recyclerAdapter = new DeptSelectionRecyclerAdapter(deptlist, selectedDept,
                getActivity(), new DeptSelectionRecyclerAdapter.MyAdapterListener() {
            @Override
            public void onItemSelected(int position) {
                selectedDept=deptlist.get(position);
                Log.d("selected dept",selectedDept);
                recyclerAdapter.setSelectedDepartment(selectedDept);
                onUpdateDept(selectedDept);
                Intent intent = new Intent();
                intent.setAction("update_department");
                getActivity().sendBroadcast(intent);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(recyclerAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(selectedDept!=null)
                recyclerView.smoothScrollToPosition(deptlist.indexOf(selectedDept));
                else {
                    recyclerView.smoothScrollToPosition(14);
                }
            }
        },300);

    }

    public String getSelectedDept(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        selectedDept= prefs.getString("DEPT","ALL");
        return selectedDept;
    }

    private void getSelectedDay() {
        selectedDay=prefs.getInt("SELECTED_DAY",1);
    }

    public void onUpdateDept(String updatedDept){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("DEPT",updatedDept);
        editor.putInt("DEPT_INDEX",deptlist.indexOf(selectedDept));
        editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.actionbar_department_selector, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //if(item.getItemId()==R.id.dropdown) showDialog();
        return true;
    }

    private void getEventsLastUpdate(){
        lastUpdatedTimestamp=prefs.getString("EVENTS_LAST_UPDATED","PULL DOWN TO REFRESH");

    }

    private void showEventsLastUpdate(){
        if(viewGroup.getContext()!=null);
            //Snackbar.make(viewGroup, lastUpdatedTimestamp,Snackbar.LENGTH_SHORT).show();
    }

    private void putSelectedDay() {
        Log.d(TAG, "putSelectedDay: "+selectedDay);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SELECTED_DAY",selectedDay);
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedDay=viewPager.getCurrentItem()+1;
        putSelectedDay();
    }

    @Override
    public void onDestroy(){
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
