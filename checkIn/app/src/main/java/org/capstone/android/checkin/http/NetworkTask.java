package org.capstone.android.checkin.http;

import android.os.AsyncTask;

import androidx.preference.Preference;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private Object values;

    public NetworkTask(String url, Object values) {
        this.url = url;
        this.values = values;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;
        RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
        result = requestHttpConnection.request(url,values);

        //JSON형태로 String 반환
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
