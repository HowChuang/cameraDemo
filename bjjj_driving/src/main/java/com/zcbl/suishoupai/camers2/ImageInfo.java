package com.zcbl.suishoupai.camers2;

/**
 * 创建日期：2025/3/11
 * 描述:
 * 作者: zhaoweichuang
 */
public class ImageInfo {
    private String path;
    private String imageName;
    private boolean isDkPic;

    public boolean isDkPic() {
        return isDkPic;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setDkPic(boolean b) {
        isDkPic = b;
    }
}
