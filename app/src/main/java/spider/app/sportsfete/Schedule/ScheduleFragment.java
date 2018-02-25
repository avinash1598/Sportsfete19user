package spider.app.sportsfete.Schedule;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spider.app.sportsfete.DepartmentUpdateCallback;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();

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
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.schedule_tab_layout);
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
    }

    private void getSelectedDay() {
        selectedDay=prefs.getInt("SELECTED_DAY",1);
    }

    public void onUpdateDept(String updatedDept){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("DEPT",updatedDept);
        editor.putInt("DEPT_INDEX",deptList.indexOf(selectedDept));
        editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_department_selector, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.dropdown) showDialog();
        return true;
    }

    public void showDialog() {
        selectedDay=viewPager.getCurrentItem()+1;
        putSelectedDay();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.DialogTheme);
        builder.setTitle(R.string.dialog_title);

        builder.setSingleChoiceItems(dialogItems, prefs.getInt("DEPT_INDEX",0),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        selectedDept=dialogItems[which];
                        onUpdateDept(selectedDept);
                        Intent intent = new Intent();
                        intent.setAction("update_department");
                        getActivity().sendBroadcast(intent);
                        //departmentUpdateCallback.updateScheduleFragment();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getEventsLastUpdate(){
        lastUpdatedTimestamp=prefs.getString("EVENTS_LAST_UPDATED","PULL DOWN TO REFRESH");

    }

    private void showEventsLastUpdate(){
        if(viewGroup.getContext()!=null)
            Snackbar.make(viewGroup, lastUpdatedTimestamp,Snackbar.LENGTH_SHORT).show();
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
}
