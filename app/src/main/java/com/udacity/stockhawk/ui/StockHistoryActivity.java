package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
    private int backState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        StockExample stock = null;
        if (getIntent() != null) {
            if (getIntent().hasExtra("stock"))
                stock = getIntent().getParcelableExtra("stock");
            if(getIntent().hasExtra(Intent.ACTION_ANSWER)){
                backState = getIntent().getIntExtra(Intent.ACTION_ANSWER, 1);
            }
        }
        LineChart chart = (LineChart) findViewById(R.id.chart);


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
            ((TextView)findViewById(R.id.textView)).setText(stock.getId() + " ("+ stock.getSym() + ")");
            ((TextView)findViewById(R.id.textView2)).setText(String.valueOf(stock.getPrice()));
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


    /**
     * This method check if the "back" button is touched from the Action Bar.
     * If it's touched, then it return to the last state of the previous activity.
     * @param item the menu item selected
     * @return true if the "back button" is touched, the superclass method result instead
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(Intent.EXTRA_TEXT, backState);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
