package org.capstone.android.checkin.http;

import android.content.Context;
import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private Object values;
    private Context mContext;

    public NetworkTask(String url, Object values) {
        this.url = url;
        this.values = values;
    }

    public NetworkTask(String url, Object values, Context mContext) {
        this.url = url;
        this.values = values;
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;
        RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
        if(mContext == null)
            result = requestHttpConnection.request(url,values);
        else
            result = requestHttpConnection.request(url, values, mContext);

        //JSON형태로 String 반환
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
