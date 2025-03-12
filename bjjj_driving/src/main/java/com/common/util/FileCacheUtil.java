package com.common.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 创建日期：2019/5/21 下午4:58
 * 描述:
 * 作者: 赵伟闯
 */
public class FileCacheUtil {
    public static String getAssetValue(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return null;
    }


    //下载具体操作
    public static File download(String downloadUrl, String fileName) {
        try {
            URL url = new URL(downloadUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //打开输入流
            InputStream is = conn.getInputStream();
            //获得长度
            //创建文件夹 MyDownLoad，在存储卡下
            //下载后的文件名
            File file1 = new File(fileName);
            if (file1.exists()) {
                file1.delete();
            }
            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 添加到缓存文件
            os.flush();
            os.close();
            is.close();
            return file1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
