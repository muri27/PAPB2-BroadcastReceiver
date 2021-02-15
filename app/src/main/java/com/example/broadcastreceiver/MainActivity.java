package com.example.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //Initiate BC for be able to use
    private CustomReceiver mReceiver = new CustomReceiver();
    //Make Label
    private static final String ACTION_CUSTOM_BROADCAST=
            BuildConfig.APPLICATION_ID+".ACTION_CUSTOM_BROADCAST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        //Register BC
        this.registerReceiver(mReceiver, filter);

        //Must add dependencies before use LocalBroadcastManager in BuildGradle
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(ACTION_CUSTOM_BROADCAST));
    }

    @Override
    protected void onDestroy() {
        //Unregister BC
        this.unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void sendCustomBroadcast(View view) {
        Intent customBroadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);
        //Send Data like usual intent
        customBroadcastIntent.putExtra("DATA", "Data Broadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);
    }
}