package com.zcbl.suishoupai.camers2;

import android.util.Size;

import com.common.util.LogAppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cxk on 2017/12/8.
 * <p>
 * 这里为了方便，将部分方法封装到这个Util里面
 * <p>
 * 1、相机宽高正好相等于当前屏幕宽高 优先选择
 * 2、相机宽度能够被遮罩层覆盖，组件高度和相机高度一致 优先选择
 * 3、通过宽高比率找一个最相近的来优化
 * 4：取到最大的相机会卡，但是清晰会变形
 * 5：取到最小的相机会严重模糊  综合AB 取一个中间让用户能够使用
 * 通过宽高找一个适中的来展示，部分会变相形，对焦会不准确（如果矩阵强制转换，可能会溃问题，没有经过测试）
 * <p>
 * <p>
 * 1、《录制视频不清晰，看码率和相机预览尺寸调整》
 */

public class Camera2Util {

    //是否重新设置 显示空间的宽高
    public static boolean needChangeTextSize;
    public static int SURFACE_HEIGHT;

    /**
     * 尺寸静态不满足，设置动态满足
     *
     * @return
     */
    public static Size getThreeDatas(Size[] sizeMapOriginal, int surfaceWidth, int surfaceHeight) {
        //优先满足
        List<Size> sizeList = new ArrayList<>();
        for (Size size : sizeMapOriginal) {
            if (size.getWidth() > Camera2Tools.PREVIEW_MINE_HEIGHT) {
                sizeList.add(size);
            }
        }

        Size retSize = null;
        //根据大小设置
        int ReqTmpWidth;
        int ReqTmpHeight;
        ReqTmpWidth = surfaceHeight;
        ReqTmpHeight = surfaceWidth;
        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        for (Size size : sizeMapOriginal) {
            curRatio = ((float) size.getWidth()) / size.getHeight();
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        //匹配不到返回最大
        if (retSize == null) {
            needChangeTextSize = false;
            return sizeMapOriginal[0];
        } else if (retSize.getWidth() < 700) {
            needChangeTextSize = false;
            if (sizeMapOriginal.length > 0) {
                int temp = sizeMapOriginal.length / 2;
                return sizeMapOriginal[temp];
            } else {
                return sizeMapOriginal[0];
            }
        } else {
            //有可能会特别大 会卡但是清晰
            needChangeTextSize = false;
            return retSize;

        }


    }

    /**
     * @param sizeMap    相机尺寸集合
     * @param viewWidth  显示组建宽度
     * @param viewHeight 显示组建高度
     * @return
     */
    public static Size getMinPreSizeNew(Size[] sizeMap, int viewWidth, final int viewHeight) {
        List<Size> sizeWidthEqual = new ArrayList<>();
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        LogAppUtil.ShowE("相机尺寸列表");

        for (Size size : sizeMap) {
            LogAppUtil.ShowE(size.getWidth() + "__" + size.getHeight());
        }
        LogAppUtil.ShowE("相机尺寸列表END");

        for (Size size : sizeMap) {
            LogAppUtil.ShowE(size.getWidth() + "__" + size.getHeight());
            //选择更高倍率相机尺寸 红米手机高配版尺寸太大，手机会卡死
//            if (size.getWidth() > viewHeight && size.getHeight() > viewWidth) {
//                float beishuCamera = size.getWidth() * 1.0f / size.getHeight() * 1.0f;
//                float beishuView = viewHeight * 1.0f / viewWidth * 1.0f;
//                LogAppUtil.ShowE("beishuCamera:" + beishuCamera + ";" + size.getHeight() + ":" + size.getWidth());
//                LogAppUtil.ShowE("beishuView:" + beishuView + ";" + viewWidth + ":" + viewHeight);
//                if (beishuCamera == beishuView || Math.abs(beishuCamera - beishuView) <= 0.01) {
//                    return size;
//                }
//            }
            //优先选择大小相等的尺寸
//            if ((size.getWidth() == viewHeight) && (size.getHeight() == viewWidth)) {
//                needChangeTextSize = false;
//                return size;
//            }
            if ((size.getWidth() == viewHeight) && (size.getHeight() == viewWidth)) {
                needChangeTextSize = false;
                return size;
            }
            //再次选择能够满足我们遮罩层的的容差的
            if (size.getHeight() == viewWidth) {
                sizeWidthEqual.add(size);
            }
        }
        if (sizeWidthEqual.size() > 0) {
            //找一个分辨率最大的尺寸
            Collections.sort(sizeWidthEqual, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return Math.abs(o1.getWidth() - viewHeight) - Math.abs(o2.getWidth() - viewHeight);//相机宽度由小到大排序
                }
            });
            LogAppUtil.ShowE("最优尺寸列表");
            for (Size size : sizeWidthEqual) {
                LogAppUtil.ShowE(size.getWidth() + "__" + size.getHeight());
                //优先选择大小相等的尺寸
            }
            needChangeTextSize = true;
            //图片宽度和屏幕高度越接近 像素越好
            return sizeWidthEqual.get(0);
        } else {
            //以上都不满足自己设置一个条件选择一个相机尺寸展示。
            return getThreeDatas(sizeMap, viewWidth, viewHeight);
        }
    }
    /**
     * 核心方法，这里是通过从sizeMap中获取和Textureview宽高大小相同的map，然后在获取接近自己想获取到的尺寸
     * 之所以这么做是因为我们要确保预览尺寸不要太大，这样才不会太卡
     *
     * @param sizeMap
     * @param surfaceWidth
     * @param surfaceHeight
     * @return
     */
    public static Size getMinPreSize(Size[] sizeMap, int surfaceWidth, int surfaceHeight) {
        int ReqTmpWidth;
        int ReqTmpHeight;
        ReqTmpWidth = surfaceHeight;
        ReqTmpHeight = surfaceWidth;


        List<Size> sizeWidthEqual = new ArrayList<>();
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
//        LogAppUtil.ShowE("所有尺寸：\n");
        for (Size size : sizeMap) {
//            LogAppUtil.ShowE(size.getWidth() + "__" + size.getHeight());
            //优先选择大小相等的尺寸
            if ((size.getWidth() == ReqTmpWidth) && (size.getHeight() == ReqTmpHeight)) {
                needChangeTextSize = false;
                return size;
            }
            //再次选择能够满足我们遮罩层的的容差的
            if (size.getHeight() == ReqTmpHeight) {
                if (size.getWidth() > Camera2Tools.PREVIEW_MINE_HEIGHT && size.getWidth() < surfaceHeight) {
                    LogAppUtil.ShowE("相机高度相等，宽度满足小的尺寸\n");
                    sizeWidthEqual.add(size);
                }
            }
        }
        if (sizeWidthEqual.size() > 0) {
            //找一个分辨率最大的尺寸
            Collections.sort(sizeWidthEqual, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return o1.getWidth() - o2.getWidth();//相机宽度由小到大排序
                }
            });
            needChangeTextSize = true;
            //图片宽度和屏幕高度越接近 像素越好
            return sizeWidthEqual.get(sizeWidthEqual.size() - 1);
        } else {
            return getThreeDatas(sizeMap, surfaceWidth, surfaceHeight);
        }
    }

    /**
     * 选出最大的分辨率 部分手机会黑屏（红米） 适配性太差  但是清晰，！！！！！终端适配可以使用
     *
     * @param sizeMap
     * @param surfaceWidth
     * @param surfaceHeight
     * @return
     */
    public static Size getMaxPreSize(Size[] sizeMap, int surfaceWidth, int surfaceHeight) {
        List<Size> sizeWidthEqual = new ArrayList<>();
        LogAppUtil.ShowE("所有尺寸：\n");
        for (Size size : sizeMap) {
            LogAppUtil.ShowE(size.getWidth() + "__" + size.getHeight());
            int needHeight = surfaceWidth * size.getWidth() / size.getHeight();
            if (needHeight > Camera2Tools.PREVIEW_MINE_HEIGHT && needHeight < surfaceHeight) {
                sizeWidthEqual.add(size);
            }
        }

        if (sizeWidthEqual.size() > 0) {
            //找一个分辨率最大的尺寸
            Collections.sort(sizeWidthEqual, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return o1.getWidth() - o2.getWidth();//相机宽度由小到大排序
                }
            });
            Size tempSize = sizeWidthEqual.get(sizeWidthEqual.size() - 1);
            needChangeTextSize = true;
            SURFACE_HEIGHT = surfaceWidth * tempSize.getWidth() / tempSize.getHeight();
            LogAppUtil.ShowE("getMaxPreSize()_需要的高度：" + SURFACE_HEIGHT);
            //图片宽度和屏幕高度越接近 像素越好
            return sizeWidthEqual.get(sizeWidthEqual.size() - 1);
        } else {
            return getMinPreSize(sizeMap, surfaceWidth, surfaceHeight);
        }

    }


}
