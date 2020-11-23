package com.project.checkcovid19.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.project.checkcovid19.R;
import com.project.checkcovid19.constants.Constants;
import com.project.checkcovid19.dao.CovidDao;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class CheckCovid19Widget extends AppWidgetProvider {
    GpsService gpsService;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            String text1 = intent.getStringExtra("text1");
            String text2 = intent.getStringExtra("text2");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.check_covid19_widget);
            ComponentName componentName = new ComponentName(context, CheckCovid19Widget.class);
            remoteViews.setTextViewText(R.id.appwidget_text, text1);
            remoteViews.setTextViewText(R.id.appwidget_text2, text2);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }

    }

}

