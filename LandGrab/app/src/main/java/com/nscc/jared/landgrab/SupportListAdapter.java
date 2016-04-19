package com.nscc.jared.landgrab;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SupportListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<Integer> supporters = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();



    public SupportListAdapter(Activity context, ArrayList<Integer> supporters, ArrayList<String> usernames, String[] test) {
        super(context, R.layout.list_item, test);
        this.context = context;
        this.supporters = supporters;
        this.usernames = usernames;


    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);

        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvSupporters = (TextView) rowView.findViewById(R.id.tvSupporters);


//        if (position % 2 == 0) {
//            rlLayout.setBackgroundColor(Color.parseColor("#444444"));
//        }

        tvName.setText(usernames.get(position));
        tvSupporters.setText(Integer.toString(supporters.get(position)));


        return rowView;
    }
}
