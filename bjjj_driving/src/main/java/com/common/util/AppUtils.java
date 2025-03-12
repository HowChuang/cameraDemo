package com.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.zcbl.bjjj_driving.R;
import com.zcbl.manager.MyApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建日期：2019/4/18 上午9:19
 * 描述: app 内部常用工具包
 */
public class AppUtils {

    private static int WINDOW_HEIGHT;
    private static int WINDOW_WIDTH;
    public static final int CAMERA_PHOTO = 10002;
    public static final int CAMERA_ALBUM = 10003;
    public static final int CAMERA_CLIPPING = 10004;
    public static final String KEY_SELECTED_LIST = "photoPickSelectedList";

    /**
     * 判断当前界面是不是被内存回收
     *
     * @param activity
     * @return
     */
    public static boolean canNextAty(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 隐藏 软键盘
     *
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 展现键盘
     *
     * @param context
     */
    @Deprecated
    public static void showKeyboard(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static void showKeyboard(final EditText editText) {
        editText.requestFocus();
        editText.setCursorVisible(true);
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, 0);
            }
        }, 150);
    }

    /**
     * isOpen若返回true，则表示输入法打开
     *
     * @param context
     * @return
     */
    public static boolean keyBoardShowing(Activity context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();

    }



    /**
     * 得到指定路径文件的的名字
     *
     * @param filePath
     * @return
     */
    public static String getStringFile(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * @param edt 弹出edittext 的输入键盘
     */
    public static void showEdtKeybord(EditText edt) {
        edt.requestFocus();
        edt.setFocusable(true);
        edt.setFocusableInTouchMode(true);

    }

    public static int dip2px(int dpValue) {
        final float scale = MyApplication.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */

    public static String getVersion(Context aty) {
        try {
            PackageManager manager = aty.getPackageManager();
            PackageInfo info = manager.getPackageInfo(aty.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String getVersion() {
        try {
            PackageManager manager = MyApplication.application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.application.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0.0";
        }
    }

    public static int getVersionCode(Context aty) {
        try {
            PackageManager manager = aty.getPackageManager();
            PackageInfo info = manager.getPackageInfo(aty.getPackageName(), 0);
            int getVersionCode = info.versionCode;
            return getVersionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getVersionCode() {
        try {
            PackageManager manager = MyApplication.application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.application.getPackageName(), 0);
            int getVersionCode = info.versionCode;
            return getVersionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (WINDOW_WIDTH != 0) {
            return WINDOW_WIDTH;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        windowManager.getDefaultDisplay().getSize(screenSize);
        WINDOW_WIDTH = screenSize.x;
        WINDOW_HEIGHT = screenSize.y;
        LogAppUtil.ShowE("当前屏幕宽高：" + WINDOW_WIDTH + "_" + WINDOW_HEIGHT);




        return WINDOW_WIDTH;
    }

    public static int getScreenHeight(Context context) {
        if (WINDOW_HEIGHT != 0) {
            return WINDOW_HEIGHT;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        windowManager.getDefaultDisplay().getSize(screenSize);
        WINDOW_WIDTH = screenSize.x;
        WINDOW_HEIGHT = screenSize.y;
        return WINDOW_HEIGHT;


    }

    /**
     * 打开手机的设置界面
     *
     * @param mContext
     */
    public static void startPhoneSetting(Context mContext) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivity(localIntent);
    }

    /**
     * 获取手机品牌，使用时候注意判断，忽略大小写
     * “huawei”equalsIgnoreCase（brand）
     *
     * @return
     */
    public static String getPhoneBrand() {
        String brand = Build.BRAND;
        return brand;
    }


    public static void showToast(Context context, String string) {
        ToastUtils.show(string);

    }


    public static void showToast(String string) {
        ToastUtils.show(string);

    }

    public static void showToast(int string) {
        ToastUtils.show(string);
    }

    public static void updatepMedia(Context context, File newFile) {
        //通知手机我加入了新图片 不然手机一段时间内不会在相册中显示
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(newFile);
        intent.setData(uri);
        context.sendBroadcast(intent);

    }



    /**
     * 将View转换为Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static TextView updateLeftView(TextView tv, int dawId) {

        if (dawId <= 0) {
            tv.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable rightD = tv.getResources().getDrawable(dawId);
            rightD.setBounds(0, 0, rightD.getMinimumWidth(), rightD.getMinimumHeight());
            tv.setCompoundDrawables(rightD, null, null, null);
        }
        return tv;
    }

    public static TextView updateTextDrawable(TextView tv, int dawId, boolean left, boolean top, boolean right, boolean bottom) {

        Drawable rightD = tv.getResources().getDrawable(dawId);
        rightD.setBounds(0, 0, rightD.getMinimumWidth(), rightD.getMinimumHeight());
        if (left) {
            tv.setCompoundDrawables(rightD, null, null, null);
        } else if (top) {
            tv.setCompoundDrawables(null, rightD, null, null);
        } else if (right) {
            tv.setCompoundDrawables(null, null, rightD, null);
        } else if (bottom) {
            tv.setCompoundDrawables(null, null, null, rightD);
        }


        return tv;
    }




    public static Spanned textValueUpdate(String color, String left,
                                          String content, String right) {
        return Html.fromHtml(left + "<font color=\"" + color + "\">" + content
                + "</font>" + right);
    }

    /**
     * 只显示汉字
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        //只允许汉字
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String removeEmoji(String str) {
        //过滤Emoji表情
        Pattern p = Pattern.compile("[^\\u0000-\\uFFFF]");
        //过滤Emoji表情和颜文字
        //Pattern p = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\ud83e\\udd00-\\ud83e\\uddff]|[\\u2300-\\u23ff]|[\\u2500-\\u25ff]|[\\u2100-\\u21ff]|[\\u0000-\\u00ff]|[\\u2b00-\\u2bff]|[\\u2d06]|[\\u3030]");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * @param content 替换的内容
     * @param endNum  最后留几个字符  例如  content="栗广生" endNum=1   结果= **生
     * @return
     */
    public static String replaceWithSecret(String content, int endNum) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }

        int len = content.length();

        int endIndex = len - endNum;
        char[] cardChar = content.toCharArray();

        for (int j = 0; j < endIndex; j++) {
            cardChar[j] = '*';
        }

        return new String(cardChar);

    }

    public static String replaceWithSecret(String content, int startNum, int endNum) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }

        int len = content.length();

        int endIndex = len - endNum;
        char[] cardChar = content.toCharArray();

        for (int j = 0; j < endIndex; j++) {
            if (j < startNum) {
                continue;
            }
            cardChar[j] = '*';
        }

        return new String(cardChar);

    }

    public static String getSecretID(String idNO) {
        if (TextUtils.isEmpty(idNO)) {
            return idNO;
        }
        return idNO.replaceAll("(\\d{3})\\d*(\\d{4})", "$1***********$2");
    }

    public static String getSecretPhone(String idNO) {
        if (TextUtils.isEmpty(idNO)) {
            return idNO;
        }
        return idNO.replaceAll("(\\d{3})\\d*(\\d{4})", "$1****$2");
    }

    public static String getSecretCarBrand(String carBrand) {
        if (TextUtils.isEmpty(carBrand)) {
            return carBrand;
        }
        return carBrand.replaceAll("(.{2}).*(.{2})", "$1****$2");

    }

    public static String getSecretCarno(String carBrand) {
        if (TextUtils.isEmpty(carBrand)) {
            return carBrand;
        }
        return carBrand.replaceAll("(.{2}).*(.{2})", "$1***$2");

    }


    /**
     * 读取asset
     *
     * @param context
     * @param fileName
     * @return
     */
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

    //网络监听
    public static boolean judgeNetIsConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 部分手机不展示toast 这个来替代
     *
     * @param view
     * @param vlaue
     */
    public static void showProgessDialog(Activity activity, View view, String vlaue) {
        ToastUtils.show(vlaue);
    }

    public static void showNewToast(String vlaue) {
        if (ToastUtils.getToast() != null) {
            ToastUtils.getToast().cancel();
        }
        ToastUtils.show(vlaue);
    }

    public static String delChinessAndEmpty(String value) {
        if (TextUtils.isEmpty(value)) {
            return value;
        }
        value = value.replaceAll("[\u4e00-\u9fa5]", "");
        return value.replaceAll(" ", "");
    }

    public static boolean contantsChinessAndEmpty(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher mat = pat.matcher(value);
        if (value.contains(" ") || mat.find()) {
            return true;
        }
        return false;
    }


    /**
     * sh
     *
     * @param path
     * @return
     */
    public static String getImageType(String path) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        // If we have to resize this image, first get the natural bounds.
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, decodeOptions);
        String type = decodeOptions.outMimeType;
        if (TextUtils.isEmpty(type)) {
            type = "";
        } else {
            type = type.substring(6, type.length());
        }
        return type;
    }


    public static void photoCliping(Activity activity, Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CAMERA_CLIPPING);
    }

    /**
     * 读取URL中的参数
     *
     * @param URL
     * @return
     */
    public static Map<String, String> urlSplit(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                for (int i = 1; i < arrSplit.length; i++) {
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }


    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }

    /**
     * 内容显示 关键字高亮
     *
     * @param textView
     * @param content
     * @param keWord
     */
    public static void textKeyHight(TextView textView, String content, String keWord) {

        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(keWord);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(R.color.app_main_color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        textView.setText(s);
    }



    public static Animation getRoteAnimation(View imageView) {
        imageView.setVisibility(View.VISIBLE);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                imageView.getContext(), R.anim.anim_loding);
        imageView.startAnimation(hyperspaceJumpAnimation);
        return hyperspaceJumpAnimation;
    }

    public static Animation showInAnimation(View imageView) {
        imageView.setVisibility(View.VISIBLE);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                imageView.getContext(), R.anim.show_frome_samle);
        imageView.startAnimation(hyperspaceJumpAnimation);
        return hyperspaceJumpAnimation;
    }

    public static Animation showUpDown(View imageView) {
        imageView.setVisibility(View.VISIBLE);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                imageView.getContext(), R.anim.anim_from_up_to_down);
        imageView.startAnimation(hyperspaceJumpAnimation);
        return hyperspaceJumpAnimation;
    }

    public static void updateLinearLayout(View view, int widhtDp, int heightDp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = AppUtils.dip2px(widhtDp);
        params.height = AppUtils.dip2px(heightDp);
    }

    public static void updateRelayutLayout(View view, int widhtDp, int heightDp) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = AppUtils.dip2px(widhtDp);
        params.height = AppUtils.dip2px(heightDp);
    }


    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            //"\\u只是代号，请根据具体所需添加相应的符号"
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String unicodeToString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换
            int data = Integer.parseInt(hex[i], 16);
            // 拼接成string
            string.append((char) data);
        }

        return string.toString();
    }


    public static boolean isLocServiceEnable(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        LogAppUtil.Show("执行结束");
        if (gps || network) {
            return true;
        }
        return false;
    }


    public static void notAllowGbs(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.expose_hint_dialog);
        //加粗 标题
        TextView tv_title = (TextView) window.findViewById(R.id.tv_expose_hint_dialog);
        tv_title.setText("温馨提示");
        tv_title.getPaint().setFakeBoldText(true);
        //内容
        TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
        tv_content.setText("请您开启手机定位服务");
        tv_content.setVisibility(View.VISIBLE);
        //
        TextView left_cancel = (TextView) window.findViewById(R.id.but_expose_hint_cancel);
        TextView right_cancle = (TextView) window.findViewById(R.id.but_expose_hint_confirm);
        right_cancle.setTextColor(activity.getResources().getColor(R.color.ssp_blue));

        left_cancel.setTextColor(activity.getResources().getColor(R.color.ssp_blue));
        left_cancel.setVisibility(View.GONE);
        window.findViewById(R.id.line_bottom).setVisibility(View.GONE);

        right_cancle.setText("确定");
        right_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }





    /**
     * 复制到黏贴版
     *
     * @param content
     * @param context
     */
    public static void copyContentToClipboard(String content, Context context) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    public static void openTencent(Activity activity, String dlat, String dlon, String dname) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobilePoisearch
            intent.setData(Uri.parse("qqmap://map/search?keyword=" + dname + "&center=CurrentLocation&radius=1000&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"));
//            intent.setData(Uri.parse("qqmap://map/routeplan?type=bus&from=我的位置&fromcoord=0,0"
//                    + "&to=" + dname
//                    + "&tocoord=" + dlat + "," + dlon
//                    + "&policy=1&referer=myapp"));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showNewToast("腾讯地图未安装");

        }


    }

    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * t = 0（驾车）   = 1（公交   ）= 2（步行   ）= 3（骑行）= 4（火车）= 5（长途客车）
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    public static void openGaoDeMap(Activity activity, String dlat, String dlon, String dname) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
// API介绍地址           https://lbs.amap.com/api/amap-mobile/guide/android/navigation
            // 和系统冲突舍弃
//            intent.setData(Uri.parse("amapuri://route/plan/?route=1&sourceApplication=" + R.string.app_name + "&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + dname + "&dev=0&t=0"));
            //位置剖搜索
            intent.setData(Uri.parse("androidamap://poi?" + "sourceApplication=" + R.string.app_name
                    + "&keywords=" + dname
                    + "&dev=0"));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showNewToast("高德地图未安装");

        }

    }

    /**
     * 打开百度地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
     * 当 mode=transit 时 ： sy = 0：推荐路线 、 2：少换乘 、 3：少步行 、 4：不坐地铁 、 5：时间短 、 6：地铁优先
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    public static void openBaiduMap(Activity activity, String dlat, String dlon, String dname) {
        try {
//            https://lbsyun.baidu.com/index.php?title=uri/api/android
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                    + dname
                    + "|latlng:" + dlat + "," + dlon
                    + "&sy=3&index=0&target=1"));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showNewToast("百度地图未安装");

        }
    }


    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    public static void openTencentPoint(Activity activity, String dlat, String dlon, String dname) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobilePoisearch
            intent.setData(Uri.parse("qqmap://map/routeplan?to=" + dname + "&type=bike&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&tocoord=" + dlat + "," + dlon + ""));
//            intent.setData(Uri.parse("qqmap://map/routeplan?type=bus&from=我的位置&fromcoord=0,0"
//                    + "&to=" + dname
//                    + "&tocoord=" + dlat + "," + dlon
//                    + "&policy=1&referer=myapp"));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showNewToast("腾讯地图未安装");

        }


    }

    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * t = 0（驾车）   = 1（公交   ）= 2（步行   ）= 3（骑行）= 4（火车）= 5（长途客车）
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    public static void openGaoDeMapPoint(Activity activity, String dlat, String dlon, String dname) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
// API介绍地址           https://lbs.amap.com/api/amap-mobile/guide/android/navigation
            // 和系统冲突舍弃
//            intent.setData(Uri.parse("amapuri://route/plan/?route=1&sourceApplication=" + R.string.app_name + "&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + dname + "&dev=0&t=0"));
            //位置剖搜索
//            intent.setData(Uri.parse("androidamap://poi?" + "sourceApplication=" + R.string.app_name
//                    + "&keywords=" + dname
//                    + "&dev=0"));


//            intent.setData(Uri.parse("amapuri://route/plan/?featureName=OnRideNavi&rideType=elebike&sourceApplication=" + R.string.app_name + "&lat=" + dlat + "&lon=" + dlon + "&dev=0"));
            intent.setData(Uri.parse("amapuri://route/plan/?route=1&sourceApplication=" + R.string.app_name + "&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + dname + "&dev=0&t=3&rideType=elebike"));

            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showNewToast("高德地图未安装");

        }

    }

    /**
     * 打开百度地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
     * 当 mode=transit 时 ： sy = 0：推荐路线 、 2：少换乘 、 3：少步行 、 4：不坐地铁 、 5：时间短 、 6：地铁优先
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    public static void openBaiduMapPoint(Activity activity, String dlat, String dlon, String dname) {
        try {
//            https://lbsyun.baidu.com/index.php?title=uri/api/android
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
//                    + dname
//                    + "|latlng:" + dlat + "," + dlon
//                    + "&sy=3&index=0&target=1"));
            intent.setData(Uri.parse("baidumap://map/direction?region=beijin&destination=" + dlat + "," + dlon + "&origin=" + MyApplication.application.Latitude + "," + MyApplication.application.Longitude +
                    "&coord_type=bd09ll&mode=riding&src=andr.baidu.openAPIdemo"));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showNewToast("百度地图未安装");

        }
    }

    /**
     * 限制字节 一个汉字当作两个字节，一个字母数字当作一个处理
     *
     * @param tv
     * @param value
     * @param max_value
     */
    public static void setUserName(TextView tv, String value, int max_value) {
        if (TextUtils.isEmpty(value)) {
            tv.setText(value);
            return;
        }
        int max = max_value;
        try {
            if (value.getBytes("GBK").length <= max) {
                tv.setText(value);
                //字节数超出限制
            } else {
                String tmp = value.substring(0, value.length() - 1);
                while (tmp.getBytes("GBK").length > max) {
                    tmp = value.substring(0, tmp.length() - 1);
                }
                tv.setText(tmp + "...");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否超过最大数量
     */
    public static boolean surpassMax(String value, int max_value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        int max = max_value;
        try {
            if (value.getBytes("GBK").length <= max) {
                return false;
                //字节数超出限制
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

}
