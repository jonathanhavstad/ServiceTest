package com.elkimanteam.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanhavstad on 9/29/16.
 */

public class ServiceExample extends Service {
    private static final String TAG = "ServiceBindLifecycle";
    private LifecycleBinder lifecycleBinder;
    private ServiceThread serviceThread;

    public class ServiceThread extends Thread {
        private boolean execute;

        public void shouldExecute(boolean execute) {
            this.execute = execute;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            int step = 0;
            while (execute) {
                if (lifecycleBinder != null) {
                    lifecycleBinder.sendUpdate(String.valueOf(step++));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        if (serviceThread == null) {
            serviceThread = new ServiceThread();
        }
        serviceThread.shouldExecute(true);
        serviceThread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        if (serviceThread != null) {
            serviceThread.shouldExecute(false);
        }
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind()");
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        if (lifecycleBinder == null) {
            lifecycleBinder = new LifecycleBinder(this);
        }
        return lifecycleBinder;
    }

    public interface Subscriber {
        void send(String data);
    }
}
