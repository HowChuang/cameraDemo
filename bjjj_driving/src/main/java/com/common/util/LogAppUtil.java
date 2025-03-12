package com.common.util;

import android.text.TextUtils;
import android.util.Log;


/**
 * @author Rendy
 * @descripte 运行日志
 * @2015年1月7日
 */
public class LogAppUtil {
    public static boolean IS_DEBUG = true;
    private static String TAG = "common";
    private static int i = 0;

    public static void Show(String local, String value) {
        if (IS_DEBUG) {
            Log.d(TAG, local + i + "_:" + "\n" + value);
        }
    }

    public static void ShowParams(String local, String value) {
        if (IS_DEBUG) {
            Log.d(TAG, local + i + "_:" + "\n" + value);
        }
    }

    public static void Show(String local, int value) {
        if (IS_DEBUG) {
            Log.d(TAG, local + i + "_:" + "\n" + value);
        }
    }

    /**
     * 自动显示当前的方法类名
     **/
    public static void Show(String value) {
        if (IS_DEBUG) {
            Log.d(TAG, value);
        }
    }

    public static void ShowE(String local, String value) {
        if (IS_DEBUG) {
            Log.e(TAG, local + i + "_:" + "\n" + value);
        }
    }

    public static void ShowE(String local) {
        if (IS_DEBUG) {
            Log.e(TAG, local);
        }
    }

    private static String getMethodName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(LogAppUtil.class.getName())) {
                continue;
            }
            return "[" + sts.getClass().getSimpleName() + "." + st.getMethodName() + "]" + " (Line:_" + st.getLineNumber() + ") ";
        }
        return null;
    }

    public static void showLongValue(String result) {
        if (!TextUtils.isEmpty(result) && result.length() > 4000) {
            for (int i = 0; i < result.length(); i += 4000) {
                if (i + 4000 < result.length())
                    Log.d("AAAAAAA", result.substring(i, i + 4000));
                else
                    Log.d("AAAAAAA" + i, result.substring(i, result.length()));
            }
        } else
            Log.d(TAG, "" + result);

    }
}
