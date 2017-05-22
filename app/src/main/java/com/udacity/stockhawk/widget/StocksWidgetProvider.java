package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteIntentService;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockHistoryActivity;

import timber.log.Timber;

/**
 * Created by Alessandro on 09/05/2017.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StocksWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each App Widget that belongs to this provider

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, StocksWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stocks);
            views.setRemoteAdapter(R.id.widget_list, intent);


            // PENDING INTENT PER QUANDO SARà DISPONIBILE LA DETAIL ACTIVITY

            Intent intentStockGraph = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intentStockGraph)
                    .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget,pendingIntent);



            // Tell the AppWidgetManager to perform an update on the current app widget
            //appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Timber.d("Intent ricevuto");
        if ("com.udacity.stockhawk.ACTION_DATA_UPDATED".equals(intent.getAction())) {
            Timber.d("kitemmù");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }else{
            Timber.d("vafammokk");
        }
    }

}
