package com.udacity.stockhawk.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alessandro on 20/05/2017.
 */

public class StockExample implements Parcelable{

    private String history;
    private String id;
    private double price;
    private String sym;

    public StockExample(String history, String id, double price, String sym) {
        this.history = history;
        this.id = id;
        this.sym = sym;
        this.price = price;
    }

    protected StockExample(Parcel in) {
        history = in.readString();
        id = in.readString();
        price = in.readDouble();
        sym = in.readString();
    }

    public static final Creator<StockExample> CREATOR = new Creator<StockExample>() {
        @Override
        public StockExample createFromParcel(Parcel in) {
            return new StockExample(in);
        }

        @Override
        public StockExample[] newArray(int size) {
            return new StockExample[size];
        }
    };

    public String getSym() {
        return sym;
    }

    public String getHistory() {
        return history;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(history);
        parcel.writeString(id);
        parcel.writeDouble(price);
        parcel.writeString(sym);
    }


}
