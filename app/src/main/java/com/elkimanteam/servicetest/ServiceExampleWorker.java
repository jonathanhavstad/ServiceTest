package com.elkimanteam.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanhavstad on 9/30/16.
 */

public class ServiceExampleWorker extends IntentService {
    private static final String TAG = "ServiceExampleWorker";

    private boolean shouldExecute;
    private IMessageBinder.Stub messageBinder;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public ServiceExampleWorker() {
        super("ServiceExampleWorker");
        shouldExecute = true;
        messageBinder = new IMessageBinder.Stub() {
            private List<ISubscriber> subscriberList = new ArrayList<>();

            @Override
            public void addSubscriber(ISubscriber subscriber) throws RemoteException {
                subscriberList.add(subscriber);
            }

            @Override
            public void removeSubscriber(ISubscriber subscriber) throws RemoteException {
                subscriberList.remove(subscriber);
            }

            @Override
            public void sendUpdate(String data) throws RemoteException {
                for (ISubscriber subscriber : subscriberList) {
                    subscriber.send(data);
                }
            }

            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }
        };
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Log.d(TAG, "Executing worker thread!");
        int step = 0;
        while (shouldExecute) {
            if (messageBinder != null) {
                try {
                    messageBinder.sendUpdate(String.valueOf(step++));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shouldExecute = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageBinder.asBinder();
    }
}
