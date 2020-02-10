package com.smuemsw.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccessLogActivity extends AppCompatActivity {

    ArrayList<AccessLogListViewData> accessLogDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_log);

        this.MakeSampleData();

        ListView listView = findViewById(R.id.accessLogListView);
        final AccessLogListViewAdapter accessLogListViewAdapter = new AccessLogListViewAdapter(this,accessLogDataList);

        listView.setAdapter(accessLogListViewAdapter);


    }

    public void MakeSampleData(){
        accessLogDataList = new ArrayList<AccessLogListViewData>();

        accessLogDataList.add(new AccessLogListViewData("시간 1", "IP 1", "위치 1"));
        accessLogDataList.add(new AccessLogListViewData("시간 2", "IP 2", "위치 2"));
        accessLogDataList.add(new AccessLogListViewData("시간 3", "IP 3", "위치 3"));
    }

}