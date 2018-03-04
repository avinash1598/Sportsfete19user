package spider.app.sportsfete.SportDetails;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import spider.app.sportsfete.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RuleBookFragment extends Fragment {

    private static final String TAG="RuleBookFragment";
    String selectedSport;


    public RuleBookFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        selectedSport= getArguments().getString("SELECTED_SPORT");
        return inflater.inflate(R.layout.fragment_rule_book, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RuleBook");
        mFirebaseAnalytics.logEvent("RuleBook Fragment",bundle);
        TextView alertbox_title = (TextView)
                view.findViewById(R.id.title);
        if(selectedSport.equals("0"))
        {
            alertbox_title.setText("Athletics");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Athletics), null, new MyTagHandler()));
        }
        else if(selectedSport.equals("1"))
        {
            alertbox_title.setText("Badminton");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Badminton), null, new MyTagHandler()));
        }
        if(selectedSport.equals("2"))
        {
            alertbox_title.setText("Basketball");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Basketball), null, new MyTagHandler()));
        }
        if(selectedSport.equals("3"))
        {
            alertbox_title.setText("Basketball");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Basketball_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("4"))
        {
            alertbox_title.setText("Carrom");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Carrom), null, new MyTagHandler()));
        }
        if(selectedSport.equals("5"))
        {
            alertbox_title.setText("Chess");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Chess), null, new MyTagHandler()));
        }
        if(selectedSport.equals("6"))
        {
            alertbox_title.setText("Cricket");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Cricket), null, new MyTagHandler()));
        }
        if(selectedSport.equals("7"))
        {
            alertbox_title.setText("Cricket");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Cricket_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("8"))
        {
            alertbox_title.setText("Football");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Football), null, new MyTagHandler()));
        }
        if(selectedSport.equals("9"))
        {
            alertbox_title.setText("Football");
            TextView alertbox_content = (TextView)view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Football_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("10"))
        {
            alertbox_title.setText("Handball");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Handball_Boys), null, new MyTagHandler()));
        }
        if(selectedSport.equals("11"))
        {
            alertbox_title.setText("Handball");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Handball_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("12"))
        {
            alertbox_title.setText("Hockey");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Hockey), null, new MyTagHandler()));
        }
        if(selectedSport.equals("13"))
        {
            alertbox_title.setText("Kabaddi");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Kabaddi), null, new MyTagHandler()));
        }
        if(selectedSport.equals("14"))
        {
            alertbox_title.setText("Kho Kho");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.KhoKho), null, new MyTagHandler()));
        }
        if(selectedSport.equals("15"))
        {
            alertbox_title.setText("Kho Kho");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.KhoKho_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("16"))
        {
            alertbox_title.setText("Marathon");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Marathon), null, new MyTagHandler()));
        }
        if(selectedSport.equals("17"))
        {
            alertbox_title.setText("Power Lifting");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.PowerLifting), null, new MyTagHandler()));
        }
        if(selectedSport.equals("18"))
        {
            alertbox_title.setText("Swimming");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Swimming), null, new MyTagHandler()));
        }
        if(selectedSport.equals("19"))
        {
            alertbox_title.setText("Table Tennis");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.TableTennis), null, new MyTagHandler()));
        }
        if(selectedSport.equals("20"))
        {
            alertbox_title.setText("Table Tennis");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.TableTennis), null, new MyTagHandler()));
        }

        if(selectedSport.equals("21"))
        {
            alertbox_title.setText("Tennis");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Tennis), null, new MyTagHandler()));
        }
        if(selectedSport.equals("22"))
        {
            alertbox_title.setText("Tennis");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Tennis_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("23"))
        {
            alertbox_title.setText("Throwball");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Throwball), null, new MyTagHandler()));
        }
        if(selectedSport.equals("24"))
        {
            alertbox_title.setText("Volley Ball");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Volleyball), null, new MyTagHandler()));
        }
        if(selectedSport.equals("25"))
        {
            alertbox_title.setText("Volley Ball");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.Volleyball_Girls), null, new MyTagHandler()));
        }
        if(selectedSport.equals("26"))
        {
            alertbox_title.setText("Water Polo");
            TextView alertbox_content = (TextView) view.findViewById(R.id.content);
            alertbox_content.setText(Html.fromHtml(getString(R.string.WaterPolo), null, new MyTagHandler()));
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    }
