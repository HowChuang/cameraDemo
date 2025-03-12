package com.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.zcbl.manager.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    private static final String ROOT = "bjjj";
    public static final String FILE_ROOT = ROOT + "/cache";
    public static final String FILE_SIGN = ROOT + "/sign";

    /**
     * 返回SD卡存储路径
     * 动态创建 防止人为删除
     * <p>
     * 这是返回的一个文件夹地址
     *
     * @return
     */
    public static String getFileAddress(Context context) {
        String path;
        try {
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            // 判断sd卡是否存在
            if (sdCardExist) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    // 获取SD卡根目录
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + context.getPackageName();
                } else {
                    // 获取沙盒file根目录
                    path = context.getExternalFilesDir(null).getPath();
                }
            } else {
                // 获取系统文件根目录
                path = Environment.getRootDirectory().getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * @param mContext
     * @return 解决相同文件下，相同文件名问题。 每次都要重新创建一个新的文件来解决这个问题
     */
    private static String getSign(Context mContext) {
        String Address;
        if (getSdCardState()) {
            Address = Environment.getExternalStorageDirectory().getPath() + File.separator;
        } else {
            Address = mContext.getCacheDir().getAbsolutePath() + File.separator;
        }
        Address = Address + FILE_SIGN + File.separator + System.currentTimeMillis() + File.separator;
        File baseFile = new File(Address);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        return Address;
    }


    /**
     * @param context
     * @return
     */
    public static String getNoFilePath(Context context) {
        return FileUtil.getFileAddress(context) + dateToString() + ".jpg";
    }

    public static String getNoFilePath() {
        return FileUtil.getFileAddress(MyApplication.getApplication()) + dateToString() + ".jpg";
    }

    public static String getNoFilePathMp4() {
        return FileUtil.getFileAddress(MyApplication.getApplication()) + dateToString() + ".mp4";
    }

    public static String getNoFilePath(String type) {
        return FileUtil.getFileAddress(MyApplication.getApplication()) + dateToString() + "." + type;
    }

    public static String getNoFilePathAppendName(String name) {
        return FileUtil.getFileAddress(MyApplication.getApplication()) + dateToString() + "_" + name + ".jpg";
    }

    /**
     * @param context
     * @param name    文件的名字
     * @return 解决相同文件下，相同文件名问题。 每次都要重新创建一个新的文件来解决这个问题
     */
    public static String getSignFilePath(Context context, String name) {
        return FileUtil.getSign(context) + name + ".png";
    }

    /**
     * 删除所有的图片
     *
     * @param context
     */
    public static void deleteAllFile(Context context) {
        deleteDirectory(getFileAddress(context) + ROOT);
    }

    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteFile(File filePath) {
        if (filePath == null) {
            return true;
        }
        if (filePath.isFile() && filePath.exists()) {
            return filePath.delete();
        }
        return false;
    }

    public static boolean getSdCardState() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    //获取图片旋转角度
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bm;
    }

    public static Bitmap getSmallBitmap(String filePath, int degree) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 960, 1280);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        bm = rotateBitmap(bm, 90);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return bm;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * @param filePath 压缩之后保存到本地的图片
     * @return
     */
    public static File getSmallLocaBitmap(String filePath) {
        return saveMyBitmap(getSmallBitmap(filePath));
    }

    /**
     * @param mBitmap 保存 bitmap 到本地
     */
    private static File saveMyBitmap(Bitmap mBitmap) {
        if (mBitmap == null) {
            return null;
        }
        File f = new File(getNoFilePath());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return f;
    }

    public static File saveMyBitmap(Bitmap mBitmap, String file) {
        if (mBitmap == null) {
            return null;
        }
        File f = new File(file);
        if (f.exists()) {
            f.delete();
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return f;
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString() {
        return new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
    }

    /**
     * @param filePath
     * @param newName
     * @return
     */
    public static String renameFile(String filePath, String newName) {
        File file = new File(filePath);
        if (file.exists()) {
            File newfile = new File(file.getParent() + File.pathSeparator + dateToString() + "_" + newName + ".jpg");
            if (file.renameTo(newfile)) {
                //会有空图片
                if (file.exists()) {
                    file.delete();
                }
                return newfile.getAbsolutePath();
            }
        }
        return null;
    }
}
