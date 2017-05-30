package com.udacity.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.data.StockExample;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Alessandro on 09/05/2017.
 */

public class StocksWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new RemoteViewsFactory(){
            Context mContext = getApplicationContext();
            Cursor mCursor;
            Intent mIntent = intent;


            @Override
            public void onCreate() {
                //nothing
            }

            @Override
            public void onDataSetChanged() {
                Timber.d("CIAO");
                if (mCursor != null) mCursor.close();
                String[] quoteColumns = new String[Contract.Quote.QUOTE_COLUMNS.size()];
                for(int i = 0; i < Contract.Quote.QUOTE_COLUMNS.size(); i++) {
                    quoteColumns[i] = Contract.Quote.QUOTE_COLUMNS.get(i);
                }
                final long identityToken = Binder.clearCallingIdentity();
                mCursor = getContentResolver().query(Contract.Quote.URI,
                        quoteColumns,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (mCursor!=null)
                    mCursor.close();
            }

            @Override
            public int getCount() {
                return mCursor != null ? mCursor.getCount() : 0;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                try{
                    mCursor.moveToPosition(i);

                    String stockSymbol = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
                    String stockPrice = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE));
                    String stockChange = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));

                    float rawAbsoluteChange = mCursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                    float rawPercChange = mCursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                    int backgroundRes;
                    String currency = getString(R.string.stock_currency);

                    String percChange = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));
                    String displayMode = PrefUtils.getDisplayMode(mContext);

                    RemoteViews listItemRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_stocks_list_item);

                    if(displayMode.equals(mContext.getString(R.string.pref_display_mode_absolute_key))){
                        if(rawAbsoluteChange > 0){
                            Timber.d("VERDE");
                            backgroundRes = R.drawable.percent_change_pill_green;
                            listItemRemoteView.setTextViewText(R.id.list_item_change, "+"+String.valueOf(rawAbsoluteChange)+currency);
                        }else{
                            Timber.d("ROSSO");
                            backgroundRes = R.drawable.percent_change_pill_red;
                            listItemRemoteView.setTextViewText(R.id.list_item_change, String.valueOf(rawAbsoluteChange)+currency);
                        }
                    }else{
                        if(rawPercChange > 0){
                            Timber.d("VERDE");
                            backgroundRes = R.drawable.percent_change_pill_green;
                            listItemRemoteView.setTextViewText(R.id.list_item_change, "+"+String.valueOf(rawPercChange)+"%");
                        }else{
                            Timber.d("ROSSO");
                            backgroundRes = R.drawable.percent_change_pill_red;
                            listItemRemoteView.setTextViewText(R.id.list_item_change, String.valueOf(rawPercChange)+"%");
                        }
                    }



                    listItemRemoteView.setTextViewText(R.id.list_item_symbol, stockSymbol);

                    listItemRemoteView.setTextViewText(R.id.list_item_currency, currency);
                    listItemRemoteView.setTextViewText(R.id.list_item_price, stockPrice);

                    listItemRemoteView.setInt(R.id.list_item_change, "setBackgroundResource", backgroundRes);
                    Timber.d("SYMBOL: "+stockSymbol+", PRICE: "+stockPrice+", CHANGE: "+stockChange+", PERCHANGE: "+ percChange+", RAW_ABS: "+ rawAbsoluteChange);


                    final Intent fillInIntent = new Intent();
                    Uri stockUri = Contract.Quote.makeUriForStock(stockSymbol);
                    fillInIntent.setData(stockUri);
                    String history = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
                    String companyName = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_STOCK_NAME));
                    fillInIntent.putExtra("stock", new StockExample(history, companyName, Double.parseDouble(stockPrice), stockSymbol));
                    listItemRemoteView.setOnClickFillInIntent(R.id.widget_list_item_root, fillInIntent);

                    return listItemRemoteView;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                mCursor.moveToPosition(i);
                return mCursor.getLong(mCursor.getColumnIndex(Contract.Quote._ID));
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
