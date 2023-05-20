package com.example.myloginapplication.Model;

public class Admin {
    private double priceDay,priceNight,priceGas,prizeStandard;
    private String date;
    private String loggedEmail;

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

    public double getPrizeStandard() {
        return prizeStandard;
    }

    public void setPrizeStandard(double prizeStandard) {
        this.prizeStandard = prizeStandard;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoggedEmail() {
        return loggedEmail;
    }

    public void setLoggedEmail(String loggedEmail) {
        this.loggedEmail = loggedEmail;
    }
}
