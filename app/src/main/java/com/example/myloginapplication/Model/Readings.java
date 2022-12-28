package com.example.myloginapplication.Model;

import java.util.Date;

public class Readings {
    private double elecDay;
    private double elecNight;
    private double gas;
    private String date;
//    date
//    Date date = new Date(int i,int j,int k);


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getElecDay() {
        return elecDay;
    }

    public void setElecDay(double elecDay) {
        this.elecDay = elecDay;
    }

    public double getElecNight() {
        return elecNight;
    }

    public void setElecNight(double elecNight) {
        this.elecNight = elecNight;
    }

    public double getGas() {
        return gas;
    }

    public void setGas(double gas) {
        this.gas = gas;
    }
}
