package com.example.notificationlistenerservicedemo;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tao on 2018/5/25.
 */

public class PhoneActivity extends Activity {

    private boolean callOut;
    private String phoneNum;
    private TextView tvPhone;
    private Button btnAccept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        tvPhone = findViewById(R.id.tv_phone);
        btnAccept = findViewById(R.id.btn_accept);
        callOut = getIntent().getBooleanExtra("callOut", false);
        phoneNum = getIntent().getStringExtra("phoneNum");
        tvPhone.setText(callOut ? "去电:" + phoneNum : "来电:" + phoneNum);
        btnAccept.setVisibility(callOut ? View.GONE : View.VISIBLE);

        wakeUpAndUnlock(this);
    }

    public void killCall(View view) {
        if (AppApplication.killIntent != null) {
            try {
                AppApplication.killIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "killIntent is null", Toast.LENGTH_SHORT).show();
            NotificationService.toggleNotificationService(this);
        }
        finish();
    }

    public void acceptCall(View view) {
        if (AppApplication.acceptIntent != null) {
            try {
                AppApplication.acceptIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "acceptIntent is null", Toast.LENGTH_SHORT).show();
            NotificationService.toggleNotificationService(this);
        }
        finish();
    }

    /**
     * 唤醒屏幕
     *
     * @param context
     */
    public void wakeUpAndUnlock(Context context) {
        //屏锁管理器
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhoneService.callOut = false;
    }
}
