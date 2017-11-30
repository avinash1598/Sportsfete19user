package spider.app.sportsfete.SportDetails;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import spider.app.sportsfete.R;

public class SportDetailsActivity extends AppCompatActivity {

    private static final String TAG="SportDetailsActivity";
    String selectedSportName;
    String[] sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sport_specific_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectedSportName =getIntent().getExtras().getString("SELECTED_SPORT");
        sports=getResources().getStringArray(R.array.sport_array);
        getSupportActionBar().setTitle(sports[Integer.parseInt(selectedSportName)]);

        ViewPager viewPager = (ViewPager) findViewById(R.id.sport_specific_view_pager);
        viewPager.setAdapter(new SportDetailsViewPagerAdapter(getSupportFragmentManager(), selectedSportName));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sport_specific_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab currentTab = tabLayout.getTabAt(0);
        if (currentTab != null) {
            View customView = currentTab.getCustomView();
            if (customView != null) {
                customView.setSelected(true);
            }
            currentTab.select();
            viewPager.setCurrentItem(0);

        }
    }
}
