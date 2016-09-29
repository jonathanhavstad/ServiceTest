package com.elkimanteam.servicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jonathanhavstad on 9/29/16.
 */

public class ServiceMessageReceiver extends BroadcastReceiver {
    public interface MessageHandler {
        void sendMessage(String data);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
