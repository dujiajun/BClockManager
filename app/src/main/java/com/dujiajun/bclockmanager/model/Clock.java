package com.dujiajun.bclockmanager.model;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cqduj on 2017/05/04.
 */

public class Clock {
    private int id;
    private int hour;
    private int minute;
    private boolean open;
   /*private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public boolean getMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean getThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean getFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean getSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public List<Boolean> getDays()
    {
        List<Boolean> days = new ArrayList<>();
        days.add(monday);
        days.add(tuesday);
        days.add(wednesday);
        days.add(thursday);
        days.add(friday);
        days.add(saturday);
        days.add(sunday);
        return days;
    }
    public void setDays(List<Boolean> days)
    {
        monday = days.get(0);
        tuesday = days.get(1);
        wednesday = days.get(2);
        thursday = days.get(3);
        friday = days.get(4);
        saturday = days.get(5);
        sunday = days.get(6);
    }



    public Clock(int hour, int minute, boolean isOpen, List<Boolean> days) {
        this.hour = hour;
        this.minute = minute;
        this.open = isOpen;
        setDays(days);
    }
*/
    public Clock(int hour, int minute, boolean isOpen) {
        this.hour = hour;
        this.minute = minute;
        this.open = isOpen;
    }

    public Clock() {
        this.hour = 6;
        this.minute = 0;
        this.open = true;
    }

    public String getTimeText() {
        String s = "";
        s += String.valueOf(getHour());
        s += ":";
        if (getMinute() < 10) s += "0";
        s += String.valueOf(getMinute());
        return s;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
