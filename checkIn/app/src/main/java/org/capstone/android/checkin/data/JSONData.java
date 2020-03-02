package org.capstone.android.checkin.data;

import java.util.ArrayList;

//http 테스트 객체
public class JSONData {
    private String id;
    private String pwd;
    private ArrayList<String> list;
    private String[] arr;

    public JSONData(String id, String pwd, ArrayList<String> list, String[] arr) {
        this.id = id;
        this.pwd = pwd;
        this.list = list;
        this.arr = arr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }
}
