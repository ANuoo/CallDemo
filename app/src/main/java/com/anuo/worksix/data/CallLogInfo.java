package com.anuo.worksix.data;

public class CallLogInfo {
    private String name;
    private String number;
    private String date;

    public CallLogInfo(String name, String number, String date) {
        super();
        this.name = name;
        this.number = number;
        this.date = date;
    }

    public CallLogInfo() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
