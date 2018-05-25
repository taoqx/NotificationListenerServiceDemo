package com.example.notificationlistenerservicedemo;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

/**
 * Created by Tao on 2018/5/25.
 */

public class NotificationService extends NotificationListenerService {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (sbn.getNotification().actions != null) {
            for (Notification.Action action : sbn.getNotification().actions) {
                LogUtils.e("action:" + action.title.toString());
                if ("接听".equals(action.title.toString())) {
                    AppApplication.acceptIntent = action.actionIntent;
                } else if ("挂断".equals(action.title.toString()) || "拒接".equals(action.title.toString())) {
                    AppApplication.killIntent = action.actionIntent;
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    /**
     * 重置NotificationService
     */
    public static void toggleNotificationService(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(mContext, com.example.notificationlistenerservicedemo.NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(mContext, com.example.notificationlistenerservicedemo.NotificationService.class)
                , PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
