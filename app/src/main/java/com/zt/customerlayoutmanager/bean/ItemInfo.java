package com.zt.customerlayoutmanager.bean;

/**
 * Created by zl on 2018/7/22.
 */

public class ItemInfo {
    private int left;
    private float scale;

    public ItemInfo(int left,float scale){
        this.left = left;
        this.scale = scale;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
