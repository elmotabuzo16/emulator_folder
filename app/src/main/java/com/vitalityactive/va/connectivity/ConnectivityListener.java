package com.vitalityactive.va.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;

import com.vitalityactive.va.events.EventDispatcher;

public class ConnectivityListener {
    public ConnectivityListener(Context context, final EventDispatcher eventDispatcher) {
        connectivityManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isOnline()) {
                    eventDispatcher.dispatchEvent(ConnectionEvent.CONNECTED);
                } else {
                    eventDispatcher.dispatchEvent(ConnectionEvent.DISCONNECTED);
                }
            }
        }, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public boolean isOnline() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private android.net.ConnectivityManager connectivityManager;
}
