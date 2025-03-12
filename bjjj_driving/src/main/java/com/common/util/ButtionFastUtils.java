package com.common.util;

/**
 * 创建日期：2020/7/7 上午10:46
 * 描述: 按钮快速点击控制
 * 作者: 赵伟闯
 */
public class ButtionFastUtils {
    private static long TIME;
    private static long INTERVAL = 1000;

    public static boolean canNext() {
        if ((System.currentTimeMillis() - TIME) > INTERVAL) {
            TIME = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

}
