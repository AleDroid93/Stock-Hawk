package com.udacity.stockhawk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.StockExample;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;

public class StockHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        StockExample stock = null;
        if(getIntent() != null)
            if(getIntent().hasExtra("stock"))
                stock = getIntent().getParcelableExtra("stock");
        LineChart chart = (LineChart) findViewById(R.id.chart);

        StockExample[] dataObjects = {  new StockExample("mon", null, 60.05, null),
                                        new StockExample("tue", null, 60.34, null),
                                        new StockExample("wed", null, 62.12, null),
                                        new StockExample("tho", null,61.50, null),
                                        new StockExample("fri", null,61.73, null),
                                        new StockExample("sat", null,60.23, null),
                                        new StockExample("sun", null,60.88, null)};

        List<Entry> entries = new ArrayList<>();
        float c = 1;
        for (StockExample data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new Entry(c++, (float) data.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Stocks"); // add entries to dataset
        dataSet.setColor(255);
        dataSet.setValueTextColor(255);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
        if(stock != null) {
            ((TextView)findViewById(R.id.textView)).setText(stock.getId());
            ((TextView)findViewById(R.id.textView2)).setText(String.valueOf(stock.getPrice()));

            /* fornire una parsificazione dell'history
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeStamp);

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            */
            //((TextView) findViewById(R.id.textView3)).setText(stock.getHistory());
        }
    }
}
