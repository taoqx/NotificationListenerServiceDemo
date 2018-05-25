package com.example.notificationlistenerservicedemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * Created by Tao on 2018/5/25.
 */

public class PhoneReceiver extends BroadcastReceiver {

    private String mIncomingNumber;
    private boolean mIncomingFlag;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.e("PhoneReceiver---------onReceive");

        String action = intent.getAction();

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
            //去电
            mIncomingFlag = false;
            String phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            startPhoneService(context, Constant.PHONE_STATE_CALL_OUT, phoneNum);
        } else {
            //来电
            mIncomingFlag = true;
            incomingPhoneState(context, intent);
        }
    }

    /**
     * 传递参数给PhoneService处理
     * @param context
     * @param state
     * @param phoneNum
     */
    private void startPhoneService(Context context, int state, String phoneNum) {
        Intent intent = new Intent(context, PhoneService.class);
        intent.putExtra(Constant.STATE, state);
        intent.putExtra(Constant.PHONE_NUM, phoneNum);
        intent.putExtra(Constant.STATE_CHANGE, true);
        context.startService(intent);
    }

    /**
     * 判断来电状态
     * @param context
     * @param intent
     */
    private void incomingPhoneState(Context context, Intent intent) {
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Service.TELEPHONY_SERVICE);
        int callState = tManager.getCallState();
        mIncomingNumber = intent.getStringExtra("incoming_number");
        switch (callState) {
            case TelephonyManager.CALL_STATE_RINGING:
                LogUtils.i("RINGING :" + mIncomingNumber);
                startPhoneService(context, Constant.PHONE_STATE_RINGING, mIncomingNumber);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (mIncomingFlag) {
                    startPhoneService(context, Constant.PHONE_STATE_OFFHOOK, mIncomingNumber);
                    LogUtils.i("incoming ACCEPT :" + mIncomingNumber);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                startPhoneService(context, Constant.PHONE_STATE_IDLE, mIncomingNumber);
                if (mIncomingFlag) {
                    LogUtils.i("incoming IDLE :" + mIncomingNumber);
                }
                break;
        }
    }
}
