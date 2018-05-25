package com.example.notificationlistenerservicedemo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PhoneService.startService(this);
        if (!isNotificationEnable()) {
            openNotificationSetting();
        }
    }

    /**
     * 打开通知栏使用权限设置界面
     */
    private void openNotificationSetting() {
        try {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否开启通知栏使用权限
     *
     * @return
     */
    private boolean isNotificationEnable() {
        Set<String> packages = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packages != null && packages.contains(getPackageName())) {
            return true;
        }
        return false;
    }
}
