package com.elkimanteam.servicetest;

import android.os.Binder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanhavstad on 9/30/16.
 */

public class LifecycleBinder extends Binder {
    private ServiceExample serviceBindLifecycle;
    private List<ServiceExample.Subscriber> subscribers = new ArrayList<>();

    public LifecycleBinder(ServiceExample serviceBindLifecycle) {
        this.serviceBindLifecycle = serviceBindLifecycle;
    }
    public ServiceExample getServiceBindLifecycle() {
        return serviceBindLifecycle;
    }
    public void addSubscriber(ServiceExample.Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    public void removeSubscriber(ServiceExample.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    public void sendUpdate(String data) {
        for (ServiceExample.Subscriber subscriber : subscribers) {
            subscriber.send(data);
        }
    }
}
