package com.udacity.stockhawk.data;

/**
 * Created by Alessandro on 27/05/2017.
 */

public class HistoryItem {
    private String date;
    private Double price;

    public HistoryItem(String date, Double price) {
        this.date = date;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

}
