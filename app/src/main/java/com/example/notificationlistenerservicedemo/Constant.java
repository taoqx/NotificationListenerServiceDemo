package com.example.notificationlistenerservicedemo;

/**
 * Created by Tao on 2018/5/25.
 */

public class Constant {
    /**
     * 电话状态    挂断
     */
    public static final int PHONE_STATE_IDLE = 1;
    /**
     * 电话状态    接听
     */
    public static final int PHONE_STATE_OFFHOOK = 2;
    /**
     * 电话状态    响铃
     */
    public static final int PHONE_STATE_RINGING = 3;
    /**
     * 电话状态    拨出
     */
    public static final int PHONE_STATE_CALL_OUT = 4;

    public static final String STATE = "state";
    public static final String PHONE_NUM = "phoneNum";
    public static final String STATE_CHANGE = "stateChange";
}
