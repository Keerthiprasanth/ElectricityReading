package com.example.myloginapplication.Model;

public class Admin {
    private double priceDay,priceNight,priceGas;
    private String date;

    public double getPriceDay() {
        return priceDay;
    }

    public void setPriceDay(double priceDay) {
        this.priceDay = priceDay;
    }

    public double getPriceNight() {
        return priceNight;
    }

    public void setPriceNight(double priceNight) {
        this.priceNight = priceNight;
    }

    public double getPriceGas() {
        return priceGas;
    }

    public void setPriceGas(double priceGas) {
        this.priceGas = priceGas;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
