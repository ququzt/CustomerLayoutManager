package com.zt.customerlayoutmanager.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.zt.customerlayoutmanager.R;

/**
 * Created by zl on 2018/7/21.
 */

public class ConfigUtil {
    public static float DEFAULT_SCALE = 0.05f;    //缩放
    public static float DEFAULT_TRANSLATE; //平移
    public static int DEFAULT_COUNT;   //显示数量


    public static void initConfig(Context context,int translate,float scale) {
        DEFAULT_TRANSLATE = translate;
        DEFAULT_SCALE = scale;
        DEFAULT_COUNT = 4;
    }

}
