package spider.app.sportsfete18;


import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;
import spider.app.sportsfete18.Following.SubscribeFragment;
import spider.app.sportsfete18.Home.HomeFragment;
import spider.app.sportsfete18.Leaderboard.LeaderboardFragment;
import spider.app.sportsfete18.Marathon.MarathonRegistration;
import spider.app.sportsfete18.Schedule.DeptSelectionRecyclerAdapter;
import spider.app.sportsfete18.SportDetails.SportDetailsFragment;
import spider.app.sportsfete18.Schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,DepartmentUpdateCallback {

    public static MenuItem prevItem = null;
    ArrayList<View> menuItems;
    private DrawerLayout flowingDrawer;
    Menu menu;
    int lastViewFragment=0;
    LinearLayout scalingll;
    public Toolbar toolbar;
    public View view;
    NavigationTabBar navigationTabBar;
    NavigationView navigationView;
    RecyclerView dept_recycler;
    RelativeLayout selection_header;

    HomeFragment homeFragment;
    LeaderboardFragment leaderboardFragment;
    ScheduleFragment scheduleFragment;
    SubscribeFragment subscribeFragment;
    SportDetailsFragment sportDetailsFragment;
    MarathonRegistration marathonRegistration;

    String[] deptArraySharedPreference=new String[15];
    List<String> deptlist, recycler_deptList;
    DeptSelectionRecyclerAdapter recyclerAdapter;

    public ImageView switch_filter;
    public String selectedDepartment = "ALL";
    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setTitle("");

        for(int i = 0; i < toolbar.getChildCount(); i++)
        { View view = toolbar.getChildAt(i);
            Log.d("font set","true"+"");
            if(view instanceof TextView) {
                TextView textView = (TextView) view;

                textView.setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf"));
                Log.d("font set","true"+"");
            }
        }


       navigationTabBar = (NavigationTabBar) findViewById(R.id.custom_navigation);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.live_match),
                        Color.parseColor("#4ab556"))
                        .selectedIcon(getResources().getDrawable(R.drawable.live_matches_unselected))
                        .title("Live")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.upcoming),
                        Color.parseColor("#4ab556")
                ).title("Upcoming")
                        .selectedIcon(getResources().getDrawable(R.drawable.upcoming_unselected))
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.completed),
                        Color.parseColor("#4ab556")
                ).title("Completed")
                        .selectedIcon(getResources().getDrawable(R.drawable.completedunselected))
                        .badgeTitle("state")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/InconsolataBold.ttf"));
        navigationTabBar.setSelected(true);
        navigationTabBar.setModelIndex(0);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {

            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
                Log.d("pos",""+index);
                switch (index){
                    case 0: Bundle arguments = new Bundle();
                            arguments.putString("target", "live");
                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setArguments(arguments);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, homeFragment,"LIVE");
                            transaction.commit();
                            invalidateOptionsMenu();
                            getSupportActionBar().setTitle("Live");
                            break;

                    case 1: Bundle arguments2 = new Bundle();
                            arguments2.putString("target", "upcoming");
                            HomeFragment homeFragment2 = new HomeFragment();
                            homeFragment2.setArguments(arguments2);
                            FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                            transaction2.replace(R.id.fragment_container, homeFragment2,"UPCOMING");
                            transaction2.commit();
                            invalidateOptionsMenu();
                            getSupportActionBar().setTitle("Upcoming");
                            break;

                    case 2: Bundle arguments3 = new Bundle();
                            arguments3.putString("target", "completed");
                            HomeFragment homeFragment3 = new HomeFragment();
                            homeFragment3.setArguments(arguments3);
                            FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                            transaction3.replace(R.id.fragment_container, homeFragment3,"COMPLETED");
                            transaction3.commit();
                            invalidateOptionsMenu();
                            getSupportActionBar().setTitle("Completed");
                            break;
                }
            }
        });



        view =  findViewById(R.id.view_id);
        switch_filter = (ImageView)findViewById(R.id.switch_filter);
        flowingDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        scalingll = (LinearLayout) findViewById(R.id.scaling_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        menu=navigationView.getMenu();
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        setDrawerTypeface();

        flowingDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                scalingll.setPivotX(scalingll.getWidth());
                //scalingll.setScaleX((float) (1.0-0.1*slideOffset));
                //scalingll.setScaleY((float) (1.0-0.05*slideOffset));
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //String token = FirebaseInstanceId.getInstance().getToken();
        //if(token!=null)
        //Log.d("token",token);
        FirebaseMessaging.getInstance().subscribeToTopic("important");

        deptArraySharedPreference=getResources().getStringArray(R.array.filter_department_array);
        deptlist = new ArrayList<>();
        recycler_deptList = new ArrayList<>();

        for ( int i = deptArraySharedPreference.length-1; i >=0; i--)
            deptlist.add(deptArraySharedPreference[i]);

        deptArraySharedPreference=getResources().getStringArray(R.array.department_array);
        for ( int i = deptArraySharedPreference.length-1; i >=0; i--)
            recycler_deptList.add(deptArraySharedPreference[i]);

        selection_header = (RelativeLayout) findViewById(R.id.selection_header);
        dept_recycler = (RecyclerView) findViewById(R.id.main_dept_recycler);
        dept_recycler.setHasFixedSize(true);
        dept_recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,true));
        recyclerAdapter = new DeptSelectionRecyclerAdapter(recycler_deptList, "ALL",
                MainActivity.this, new DeptSelectionRecyclerAdapter.MyAdapterListener() {
            @Override
            public void onItemSelected(int position, View view) {
                bounceElement((TextView)view);
                selectedDepartment = deptlist.get(position);
                recyclerAdapter.setSelectedDepartment(deptlist.get(position));
                Intent intent = new Intent();
                intent.setAction("update_home_fragment_department");
                intent.putExtra("selectedDepartment",""+deptlist.get(position));
                sendBroadcast(intent);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        dept_recycler.setAdapter(recyclerAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dept_recycler.smoothScrollToPosition(14);
            }
        },300);

        switch_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipAnimation(dept_recycler);
            }
        });
    }

    public void bounceElement(TextView textView){
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        ButtonBounce interpolator = new ButtonBounce(0.2, 10);
        myAnim.setInterpolator(interpolator);
        textView.startAnimation(myAnim);
    }

    public void flipAnimation(RecyclerView recyclerView){
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.flipping);
        anim.setTarget(recyclerView);
        anim.setDuration(500);
        anim.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                flowingDrawer.openDrawer(Gravity.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        if (id == R.id.nav_home) {
            //clearBackStackInclusive("F2");
            Runtime.getRuntime().gc();
            //clearFragments();
            //clearBackStackInclusive();
            navigationTabBar.setSelected(true);
            navigationTabBar.setModelIndex(0);
            navigationTabBar.setVisibility(View.VISIBLE);
            lastViewFragment = 0;
            Bundle arguments = new Bundle();
            arguments.putString("target", "live");
            homeFragment = new HomeFragment();
            homeFragment.setArguments(arguments);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Live");
            selection_header.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_leaderboard) {
            //clearBackStackInclusive("F2");
            Runtime.getRuntime().gc();
            //clearFragments();
            //clearBackStackInclusive();
            selection_header.setVisibility(View.GONE);
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment = 1;
            leaderboardFragment = new LeaderboardFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, leaderboardFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("LeaderBoard");

        }else if(id==R.id.nav_schedule){
            Runtime.getRuntime().gc();
            //clearBackStackInclusive("F2");
            //clearFragments();
            //clearBackStackInclusive();
            selection_header.setVisibility(View.GONE);
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment=2;
            Bundle arguments = new Bundle();
            arguments.putBoolean("refresh", true);
            scheduleFragment=new ScheduleFragment();
            scheduleFragment.setArguments(arguments);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,scheduleFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Schedule");

        }
        else if(id==R.id.nav_following){
            Runtime.getRuntime().gc();
            //clearBackStackInclusive("F2");
            //clearFragments();
            //clearBackStackInclusive();
            selection_header.setVisibility(View.GONE);
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment=3;
            subscribeFragment =new SubscribeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, subscribeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Following");
        }
        else if(id==R.id.nav_events){
            Runtime.getRuntime().gc();
            //clearBackStackInclusive("F2");
            //clearFragments();
            //clearBackStackInclusive();
            selection_header.setVisibility(View.GONE);
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment=4;
            sportDetailsFragment = new SportDetailsFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, sportDetailsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Sports");
        }else if(id == R.id.nav_registration){
            Runtime.getRuntime().gc();
            //clearBackStackInclusive("F2");
            //clearFragments();
            //clearBackStackInclusive();
            selection_header.setVisibility(View.GONE);
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment = 5;
            marathonRegistration = new MarathonRegistration();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, marathonRegistration);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Marathon Registration");
        }

                setDrawerTypeface();

                for(int i = 0; i < toolbar.getChildCount(); i++)
                { View view = toolbar.getChildAt(i);
                    if(view instanceof TextView) {
                        TextView textView = (TextView) view;
                        textView.setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf"));
                    }
                }

            }
        },320);

        flowingDrawer.closeDrawer(Gravity.START);
          return true;
    }

    public void setDrawerTypeface(){
        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                menuItems = new ArrayList<>(); // save Views in this array
                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this); // remove the global layout listener
                for (int i = 0; i < menu.size(); i++) {// loops over menu items  to get the text view from each menu item
                    final MenuItem item = menu.getItem(i);
                    navigationView.findViewsWithText(menuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                }
                for (final View menuItem : menuItems) {// loops over the saved views and sets the font
                    ((TextView) menuItem).setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf"), Typeface.BOLD);
                }
            }
        });

    }

    @Override
    public void updateScheduleFragment() {

        Bundle arguments = new Bundle();
        arguments.putBoolean("refresh", false);
        ScheduleFragment scheduleFragment=new ScheduleFragment();
        scheduleFragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,scheduleFragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Schedule");

    }

    @Override
    public void updateHomeFragment(String target) {

        navigationTabBar.setSelected(true);
        navigationTabBar.setModelIndex(0);
        navigationTabBar.setVisibility(View.VISIBLE);
        lastViewFragment = 0;
        Bundle arguments = new Bundle();
        arguments.putString("target", target);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment);
        fragmentTransaction.commit();
        selection_header.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed(){
        if(lastViewFragment!=0){

            navigationTabBar.setSelected(true);
            navigationTabBar.setModelIndex(0);
            navigationTabBar.setVisibility(View.VISIBLE);
            lastViewFragment = 0;
            Bundle arguments = new Bundle();
            arguments.putString("target", "live");
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(arguments);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Live");
            navigationView.setCheckedItem(R.id.nav_home);
            selection_header.setVisibility(View.VISIBLE);

            setDrawerTypeface();

            for(int i = 0; i < toolbar.getChildCount(); i++)
            { View view = toolbar.getChildAt(i);
                if(view instanceof TextView) {
                    TextView textView = (TextView) view;

                    textView.setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/HammersmithOneRegular.ttf"));
                }
            }

        }else
            super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
