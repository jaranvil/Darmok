package com.nscc.jared.landgrab;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> messages = new ArrayList<>();

    public ChatListAdapter(Activity context, ArrayList<String> messages, String[] test) {
        super(context, R.layout.chat_list_item, test);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.chat_list_item, null, true);

        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvMessage = (TextView) rowView.findViewById(R.id.tvMessage);

        String[] data = messages.get(position).split(":");

        tvName.setText(data[0]);
        tvMessage.setText(data[1]);

        return rowView;
    }
}
