package com.smuemsw.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AccessLogListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<AccessLogListViewData> itemList;

    public AccessLogListViewAdapter(Context context, ArrayList<AccessLogListViewData> data){

        mContext = context;
        itemList = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.activity_access_log_listview_item, null);

        TextView time = view.findViewById(R.id.accessLogListViewItemTime);
        TextView ip = view.findViewById(R.id.accessLogListViewItemIP);
        TextView location = view.findViewById(R.id.accessLogListViewItemLocation);

        time.setText(itemList.get(position).getTime());
        ip.setText(itemList.get(position).getIp());
        location.setText(itemList.get(position).getLocation());

        return view;
    }
}