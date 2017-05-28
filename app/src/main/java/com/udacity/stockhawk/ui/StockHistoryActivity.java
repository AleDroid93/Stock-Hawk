package com.udacity.stockhawk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.HistoryItem;
import com.udacity.stockhawk.data.StockExample;
import com.udacity.stockhawk.mock.MockUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;
import yahoofinance.Stock;

public class StockHistoryActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private ArrayList<HistoryItem> dataObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        StockExample stock = null;
        if (getIntent() != null)
            if (getIntent().hasExtra("stock"))
                stock = getIntent().getParcelableExtra("stock");
        LineChart chart = (LineChart) findViewById(R.id.chart);
        // Loading history


        if (stock != null) {
            Timber.d("\n HISTORY OF "+ stock.getId() +"\n"+ stock.getHistory());

            String hist = stock.getHistory();
            dataObjects = MockUtils.historyToArrayList(hist);
            Collections.reverse(dataObjects);
        }

        List<Entry> entries = new ArrayList<>();
        float c = 1;
        for (HistoryItem hi : dataObjects) {
            // turn your data into Entry objects
            float fPrice = Float.parseFloat(String.valueOf(hi.getPrice()));
            entries.add(new Entry(c++, fPrice));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Stock History"); // add entries to dataset
        dataSet.setColor(255);
        dataSet.setValueTextColor(255);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setOnChartValueSelectedListener(this);
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        HistoryItem selectedItem = dataObjects.get((int)e.getX()-1);
        Timber.d("DATE: "+ selectedItem.getDate() + ", PRICE: "+ selectedItem.getPrice());
    }

    @Override
    public void onNothingSelected() {

    }
}
