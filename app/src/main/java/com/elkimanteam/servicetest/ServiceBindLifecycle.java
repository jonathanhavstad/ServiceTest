package com.elkimanteam.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanhavstad on 9/29/16.
 */

public class ServiceBindLifecycle extends Service {
    private static final String TAG = "ServiceBindLifecycle";
    private LifecycleBinder lifecycleBinder;
    private ServiceAsyncTask serviceAsyncTask;

    public class ServiceAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int step = 0;
            while (true) {
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
        if (serviceAsyncTask == null) {
            serviceAsyncTask = new ServiceAsyncTask();
        }
        serviceAsyncTask.execute();
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
        if (serviceAsyncTask != null) {
            serviceAsyncTask.cancel(true);
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

    public class LifecycleBinder extends Binder {
        private ServiceBindLifecycle serviceBindLifecycle;
        private List<Subscriber> subscribers = new ArrayList<>();

        public LifecycleBinder(ServiceBindLifecycle serviceBindLifecycle) {
            this.serviceBindLifecycle = serviceBindLifecycle;
        }
        public ServiceBindLifecycle getServiceBindLifecycle() {
            return serviceBindLifecycle;
        }
        public void addSubscriber(Subscriber subscriber) {
            subscribers.add(subscriber);
        }
        public void removeSubscriber(Subscriber subscriber) {
            subscribers.remove(subscriber);
        }
        public void sendUpdate(String data) {
            for (Subscriber subscriber : subscribers) {
                subscriber.send(data);
            }
        }
    }
}
