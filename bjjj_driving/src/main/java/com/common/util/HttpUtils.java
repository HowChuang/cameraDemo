package com.common.util;

import android.text.TextUtils;

import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建日期：2019/4/18 上午11:11
 * 描述:  网络请求字段处理
 * 作者: 赵伟闯
 */
public class HttpUtils {
    public static String TAG = "HttpUtils";

    /**
     * @param values key value 键值对 传入
     * @return 服务器请求的参数
     */
    public static String getUrlParams(String... values) {
        StringBuilder params = new StringBuilder("");
        for (int i = 0; i < values.length; i += 2) {
            params.append(values[i] + "=" + values[i + 1] + "&");
        }
        return params.substring(0, params.length() > 0 ? params.length() - 1
                : 0);
    }

    /**
     * @param values
     * @return 返回按照字母倒叙排序参数的顺序字符串  a=2&b=1&c=3...
     */
    public static String getParamsCBA(String jsonkey, String jsonString, String... values) {
        HashMap<String, String> maps = new HashMap<String, String>();

        if (!TextUtils.isEmpty(jsonString)) {
            maps.put(jsonkey, jsonString);
        }

        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i += 2) {
                maps.put(values[i], values[i + 1]);
            }
            Object[] keys = maps.keySet().toArray();
            Arrays.sort(keys);
            StringBuffer hql = new StringBuffer();
            String tem = "";
            if (keys.length > 0) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    String key = (String) keys[i];
                    if (!TextUtils.isEmpty(maps.get(key))) {
                        hql.append(key + "=" + maps.get(key) + "&");
                    }
                }
                if (hql.length() > 0) {
                    tem = hql.substring(0, hql.toString().length() - 1);
                }
                LogAppUtil.Show("倒叙字段_：" + tem);
                return tem;
            }
        }
        return null;
    }

    /**
     * @param shenpi
     * @return a|b|c..|z
     */
    public static String getFormatParams(List<String> shenpi) {
        if (shenpi == null || shenpi.size() == 0) {
            return null;
        }
        StringBuilder params = new StringBuilder("");
        for (int i = 0; i < shenpi.size(); i++) {
            params.append(shenpi.get(i) + "|");
        }
        return params.substring(0, params.length() > 0 ? params.length() - 1
                : 0);
    }

    public static Map<String, String> getParams(String... values) {
        Map<String, String> params = new HashMap<>();
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i += 2) {
                //判断key值不能为空
                if (!TextUtils.isEmpty(values[i])) {
                    params.put(values[i], values[i + 1]);
                }
            }
        }
        //防止参数内容为空的bug
        if (params.size() == 0) {
            return null;
        }
        return params;
    }

    // URLEncoder编码
    public static String getURLCoder(String string) {
        try {
            string = URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return string;
    }

    /*
     * MD5加密
     */
    public static String MD5(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * @param gbString
     * @return 返回转换后的 Unicode 的数值
     */
    public static String encodeUnicode(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * 北京进京证
     *
     * @param values
     * @return
     */
    public static Map<String, String> getJJZHParams(String... values) {
        return getJJZHParams(null, null, values);
    }

    public static Map<String, String> getJJZHParams(String key, JSONArray array, String... values) {
        Map<String, String> params = new HashMap<>();
        try {
            JSONObject objService = new JSONObject();
            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i += 2) {
                    if (!TextUtils.isEmpty(values[i + 1])) {
                        objService.put(values[i], values[i + 1]);
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject getCGSParams(String... values) {
        Map<String, String> params = new HashMap<>();
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i += 2) {
                if (!TextUtils.isEmpty(values[i + 1])) {
                    params.put(values[i], values[i + 1]);
                }
            }
        }

        //字段排序
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        //参数拼接生成加密字符串
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            if (!TextUtils.isEmpty(params.get(key))) {
                stringBuffer.append(key + "=" + params.get(key) + "&");
            }
        }
        String sign = MD5(stringBuffer.toString()).toUpperCase();
        params.put("sign", sign);
        JSONObject jsonObject = new JSONObject(params);
        LogAppUtil.Show("[getCGSParams]_请求参数：\n" + jsonObject.toString());

        return jsonObject;
    }


    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 注意: 如果参数的值为空则不参与签名
     * 实现步骤:
     * 第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序
     * 第二步：参数拼接完毕之后,拼接密钥key
     * 第三步：取MD5运算值
     *
     * @return
     */
    public static JSONObject getSuiShouPaiJsons(String... values) {
        //开发时的默认值

        return null;
    }

    public static JSONObject getJjzDkJsons(String... values) {

        JSONObject jsonObj = new JSONObject();
        return jsonObj;
    }

    /**
     * @param values
     * @return 返回按照参数首字母排列顺序的字符串  a=2&b=1&c=3...
     */
    public static String getParamsABC(String... values) {
        HashMap<String, String> maps = new HashMap<String, String>();
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i += 2) {
                maps.put(values[i], values[i + 1]);
            }
            Object[] keys = maps.keySet().toArray();
            Arrays.sort(keys);
            StringBuffer hql = new StringBuffer();
            String tem = "";
            if (keys.length > 0) {
                for (int i = 0; i < keys.length; i++) {
                    String key = (String) keys[i];
                    if (!TextUtils.isEmpty(maps.get(key))) {
                        hql.append(key + "=" + maps.get(key) + "&");
                    }
                }
                if (hql.length() > 0) {
                    tem = hql.substring(0, hql.toString().length() - 1);
                }
                return tem;
            }
        }
        return null;
    }


    public static JSONObject decodeServerData(Response<String> response, String requestType, String url) {
        return null;
    }

}
