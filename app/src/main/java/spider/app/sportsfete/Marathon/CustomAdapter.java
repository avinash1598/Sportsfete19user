package spider.app.sportsfete.Marathon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import spider.app.sportsfete.R;

/**
 * Created by AVINASH on 1/24/2018.
 */

public class CustomAdapter extends ArrayAdapter<String> {

    Context context;
    String[] items;

    public CustomAdapter(@NonNull Context context, int resource, String[] items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(Typeface.createFromAsset(context.getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        if(items[position].equalsIgnoreCase("select department")){
            view.setTextColor(Color.parseColor("#60000000"));
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(Typeface.createFromAsset(context.getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        return view;
    }
}
