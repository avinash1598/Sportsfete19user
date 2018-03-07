package spider.app.sportsfete;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import spider.app.sportsfete.Following.SubscribeFragment;
import spider.app.sportsfete.Home.HomeFragment;
import spider.app.sportsfete.Leaderboard.LeaderboardFragment;
import spider.app.sportsfete.Marathon.MarathonRegistration;
import spider.app.sportsfete.Schedule.ScheduleViewPagerAdapter;
import spider.app.sportsfete.SportDetails.SportDetailsFragment;
import spider.app.sportsfete.Schedule.ScheduleFragment;

import static java.lang.Integer.TYPE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,DepartmentUpdateCallback {

    public static MenuItem prevItem = null;
    int currentFragmentId = 1;

    ExpandableLayout expandableLayout;

    private DrawerLayout flowingDrawer;
    Menu menu;
    int lastViewFragment=0;
    LinearLayout scalingll;
    public Toolbar toolbar;
    public View view;
    NavigationTabBar navigationTabBar;
    NavigationView navigationView;

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
        flowingDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //BlurSupport.addTo(flowingDrawer);

        scalingll = (LinearLayout) findViewById(R.id.scaling_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        menu=navigationView.getMenu();
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ArrayList<View> menuItems = new ArrayList<>(); // save Views in this array
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
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Live");
        } else if (id == R.id.nav_leaderboard) {
            //clearBackStackInclusive("F2");
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment = 1;
            LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, leaderboardFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("LeaderBoard");

        }else if(id==R.id.nav_schedule){
            //clearBackStackInclusive("F2");
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment=2;
            Bundle arguments = new Bundle();
            arguments.putBoolean("refresh", true);
            ScheduleFragment scheduleFragment=new ScheduleFragment();
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
            //clearBackStackInclusive("F2");
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment=3;
            SubscribeFragment subscribeFragment =new SubscribeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, subscribeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Following");
        }
        else if(id==R.id.nav_events){
            //clearBackStackInclusive("F2");
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment=4;
            SportDetailsFragment sportDetailsFragment = new SportDetailsFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, sportDetailsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Sports");
        }else if(id == R.id.nav_registration){
            //clearBackStackInclusive("F2");
            navigationTabBar.setVisibility(View.GONE);
            lastViewFragment = 5;
            MarathonRegistration marathonRegistration = new MarathonRegistration();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, marathonRegistration);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Marathon Registration");
        }

                navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ArrayList<View> menuItems = new ArrayList<>(); // save Views in this array
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

            navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ArrayList<View> menuItems = new ArrayList<>(); // save Views in this array
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

    public void clearBackStackInclusive(String tag) {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void clearFragments(){

    }
}
