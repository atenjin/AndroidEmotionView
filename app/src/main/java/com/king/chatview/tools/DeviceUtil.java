package com.king.chatview.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import java.util.UUID;

/**
 * Created by Administrator on 2015/11/10.
 */
public class DeviceUtil {
    public static String getDevicePhoneNumber(Context ctx){
        TelephonyManager mTelephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String number = null;
        if(mTelephonyMgr != null){
            number = mTelephonyMgr.getLine1Number();
            if(number != null && number.length() >= 11){
                number = number.substring(number.length() - 11);
            }
        }
        return number;
    }

    private static String PREF_DEVICE_ID = "bx_share_deviceID";
    private static String PREF_KEY_DEVICE_ID = "pref_key_device";
    private static String DEVICE_ID = "";

    static public String getDeviceUdid(Context context) {

        /**
         * Firstly, check if memory exists.
         */
        if (DEVICE_ID != null && DEVICE_ID.length() > 0)
        {
            return DEVICE_ID;
        }

        /**
         * Then, check if we have saved the preference.
         */
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_ID, Context.MODE_PRIVATE);
        if (pref != null && pref.contains(PREF_KEY_DEVICE_ID))
        {
            DEVICE_ID = pref.getString(PREF_KEY_DEVICE_ID, null);
        }

        if (DEVICE_ID != null && DEVICE_ID.length() > 0)
        {
            return DEVICE_ID;
        }

        /**
         * And last, get ANDROID_ID or device id, or random id if we cannot get any unique id from android system.
         */
        try
        {
            DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            /**
             * --> 9774d56d682e549c is an android system bug.
             * --> null or "null" means cannot get android id.
             */
            if ("9774d56d682e549c".equals(DEVICE_ID) || DEVICE_ID == null || "null".equalsIgnoreCase(DEVICE_ID.trim())) {
                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                String uuid = deviceId!=null && !"null".equalsIgnoreCase(deviceId.trim()) ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();

                DEVICE_ID = uuid;
                return uuid;
            }
        }
        catch(Throwable t)
        {
            DEVICE_ID = System.currentTimeMillis() + ""; //If any exception occur, use system current time as unique id.
            t.printStackTrace();
        }
        finally
        {
            if (pref != null)
            {
                pref.edit().putString(PREF_KEY_DEVICE_ID, DEVICE_ID).commit();
            }
        }

        return DEVICE_ID;

    }

    public static int getWidthByContext(Context context){
        try {
            WindowManager winManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            return winManager.getDefaultDisplay().getWidth();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getHeightByContext(Context context){
        try {
            WindowManager winManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            return winManager.getDefaultDisplay().getHeight();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
