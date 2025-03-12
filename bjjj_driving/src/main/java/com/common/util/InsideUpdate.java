package com.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @Author Rendy
 * <p>
 * 2015-11-20
 * <p>
 * TODO 软件内部通信机制
 */
public class InsideUpdate {
    public interface UpdateNotify {
        /**
         * @param action 更新指令
         * @param value  回传值 可变参数可以不传值
         */
        void updateUi(int action, Object... value);
    }

    private InsideUpdate() {
    }

    private static WeakHashMap<UpdateNotify, Void> mCalBaks = new WeakHashMap<UpdateNotify, Void>();

    public static void addClientNotify(UpdateNotify updateNotify) {
        mCalBaks.put(updateNotify, null);
    }

    /**
     * @param updateNotify
     */
    public static void removeClientNotify(UpdateNotify updateNotify) {
        mCalBaks.remove(updateNotify);
    }

    /**
     * @param action 指令 （软件内部不可以存在重复的action）
     * @param value  回传值
     */
    public static void sendNotify(int action, Object... value) {
        List<UpdateNotify> datas = new ArrayList<UpdateNotify>(
                mCalBaks.keySet());
        for (UpdateNotify updateNotify : datas) {
            updateNotify.updateUi(action, value);
        }
    }
}
