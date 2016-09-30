// IMessageBinder.aidl
package com.elkimanteam.servicetest;

import com.elkimanteam.servicetest.ISubscriber;

// Declare any non-default types here with import statements

interface IMessageBinder {
    void addSubscriber(ISubscriber subscriber);

    void removeSubscriber(ISubscriber subscriber);

    void sendUpdate(String data);

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
