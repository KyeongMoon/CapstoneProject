package org.capstone.android.checkin.data;

public class AccessLogListViewData {
    private String time;
    private String ip;
    private String location;

    public AccessLogListViewData(String time, String ip, String location){
        this.time = time;
        this.ip = ip;
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }

    public String getLocation() {
        return location;
    }
}
