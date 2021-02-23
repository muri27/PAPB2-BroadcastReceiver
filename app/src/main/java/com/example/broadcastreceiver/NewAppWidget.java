package com.example.broadcastreceiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
//Used Shared Preference for Update to know the last data in app
public class NewAppWidget extends AppWidgetProvider {
    //Initiate name of shared preference
    private static final String SHARED_PREF_FILE=BuildConfig.APPLICATION_ID+".PREF";
    //For Label Counter
    private static final String COUNT_KEY="count";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Set SharedPreferences
        SharedPreferences prefs=context.getSharedPreferences(SHARED_PREF_FILE, 0);
        //"count" for know how many the data updated
        int count=prefs.getInt(COUNT_KEY+appWidgetId, 0);
        //Everytime update counter ++
        count++;
        //Set Date
        String timeString=DateFormat.getTimeInstance(DateFormat.SHORT, Locale.UK)
                .format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String dateString= sdf.format(calendar.getTime());

        //...+"" automate become String
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.app_widget_id, appWidgetId+"");
        views.setTextViewText(R.id.app_widget_count, count+"");
        views.setTextViewText(R.id.app_widget_update_date, dateString+"");
        views.setTextViewText(R.id.app_widget_update_time, timeString+"");

        //Save the last update to shared preference
        //Every 30 min date will be automatically update
        SharedPreferences.Editor prefEditor=prefs.edit();
        prefEditor.putInt(COUNT_KEY+appWidgetId, count);
        prefEditor.apply();

        //Set button when it's clicked it's update
        Intent intentUpdate=new Intent(context,NewAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //Take the id widget and put in putExtra
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        //To send widget must use pendingIntent and it didn't work
        //when use intent.start()
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,
                appWidgetId,intentUpdate,PendingIntent.FLAG_UPDATE_CURRENT);
        //Define which button that trigger the intent
        views.setOnClickPendingIntent(R.id.button_update,pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}