package spider.app.sportsfete;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import spider.app.sportsfete.Following.SubscribeFragment;
import spider.app.sportsfete.Home.HomeFragment;
import spider.app.sportsfete.Leaderboard.LeaderboardFragment;
import spider.app.sportsfete.Marathon.MarathonRegistration;
import spider.app.sportsfete.SportDetails.SportDetailsFragment;
import spider.app.sportsfete.Schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,DepartmentUpdateCallback {

    private FlowingDrawer flowingDrawer;
    Menu menu;
    int lastViewFragment=0;


    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowingDrawer.toggleMenu();
            }
        });

        flowingDrawer = (FlowingDrawer) findViewById(R.id.flowing_drawer_layout);
        flowingDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        menu=navigationView.getMenu();
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

       flowingDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    //Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                //Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
        //String token = FirebaseInstanceId.getInstance().getToken();
        //if(token!=null)
        //Log.d("token",token);
        FirebaseMessaging.getInstance().subscribeToTopic("important");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            lastViewFragment=0;
            HomeFragment homeFragment =new HomeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Live");
        }else if(id==R.id.nav_leaderboard){
            lastViewFragment=1;
            LeaderboardFragment leaderboardFragment=new LeaderboardFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,leaderboardFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("LeaderBoard");
        }else if(id==R.id.nav_schedule){
            lastViewFragment=2;
            ScheduleFragment scheduleFragment=new ScheduleFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,scheduleFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Schedule");

        }
        else if(id==R.id.nav_following){
            lastViewFragment=3;
            SubscribeFragment subscribeFragment =new SubscribeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, subscribeFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Following");
        }
        else if(id==R.id.nav_events){
            lastViewFragment=4;
            SportDetailsFragment sportDetailsFragment = new SportDetailsFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, sportDetailsFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Sports");
        }else if(id == R.id.nav_registration){
            lastViewFragment = 5;
            MarathonRegistration marathonRegistration = new MarathonRegistration();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, marathonRegistration);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Marathon Registration");
        }
        flowingDrawer.closeMenu(true);

          return true;
    }


    @Override
    public void updateScheduleFragment() {
        /*
        ScheduleFragment scheduleFragment=new ScheduleFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,scheduleFragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Schedule");
        */
    }
}
