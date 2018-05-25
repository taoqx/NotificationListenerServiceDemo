package com.example.notificationlistenerservicedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;

/**
 * Created by Tao on 2018/5/25.
 * 保存通知栏中拨打、挂断电话的PendingIntent
 */

public class PhoneService extends Service {

    public static boolean callOut;    //去电
    private static int GRAY_SERVICE_ID = -1001;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("PhoneService==============onCreate");
        keepAliveWuWuWu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("PhoneService==============onDestroy");
    }

    /**
     * 勉强保活
     */
    private void keepAliveWuWuWu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //api 18以上需要一个可见的Notification
            Intent innerIntent = new Intent(this, InnerService.class);
            startService(innerIntent);

            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("PhoneService");
            builder.setContentText("正在运行");
            startForeground(GRAY_SERVICE_ID, builder.build());
        } else {
            //API < 18 ，此方法能有效隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getBooleanExtra(Constant.STATE_CHANGE, false)) {
            int state = intent.getIntExtra(Constant.STATE, 0);
            String phoneNum = intent.getStringExtra(Constant.PHONE_NUM);
            stateChanged(state, phoneNum);
        }
        return START_STICKY;
    }

    /**
     * 通话状态变化
     *
     * @param state
     * @param phoneNum
     */
    public void stateChanged(int state, final String phoneNum) {
        LogUtils.i("state = " + state + "\tphoneNumber = " + phoneNum);
        switch (state) {
            //响铃
            case Constant.PHONE_STATE_RINGING:
                LogUtils.e("来电响铃：" + phoneNum);
                startPhoneActivity(phoneNum);
                break;
            //挂断
            case Constant.PHONE_STATE_IDLE:
                callOut = false;
                break;
            //接听
            case Constant.PHONE_STATE_OFFHOOK:
                break;
            case Constant.PHONE_STATE_CALL_OUT:
                LogUtils.e("去电响铃：" + phoneNum);
                callOut = true;
                startPhoneActivity(phoneNum);
                break;
            default:
                break;
        }
    }

    /**
     * 打开自定义接听页面
     *
     * @param phoneNum
     */
    private void startPhoneActivity(final String phoneNum) {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PhoneService.this, PhoneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("callOut", callOut);
                intent.putExtra("phoneNum", phoneNum);
                startActivity(intent);
            }
        }, 3000);
    }

    public static void startService(Context context) {
        context.startService(new Intent(context, PhoneService.class));
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Notification.Builder builder = new Notification.Builder(this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                startForeground(GRAY_SERVICE_ID, builder.build());

                ThreadPoolUtil.getInstance().run(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        stopForeground(true);
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.cancel(GRAY_SERVICE_ID);
                        stopSelf();
                    }
                });
            }
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
