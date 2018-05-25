package com.example.notificationlistenerservicedemo;

import android.app.Application;
import android.app.PendingIntent;

/**
 * Created by Tao on 2018/5/25.
 */

public class AppApplication extends Application {
    public static PendingIntent acceptIntent;
    public static PendingIntent killIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        PhoneService.startService(this);
        NotificationService.toggleNotificationService(this);
    }

}
