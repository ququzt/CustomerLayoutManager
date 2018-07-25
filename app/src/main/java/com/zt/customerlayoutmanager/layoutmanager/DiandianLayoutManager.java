package com.zt.customerlayoutmanager.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zt.customerlayoutmanager.R;
import com.zt.customerlayoutmanager.utils.ConfigUtil;
import com.zt.customerlayoutmanager.utils.DensityUtils;

/**
 * Created by zl on 2018/7/20.
 */

public class DiandianLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "DiandianLayoutManager";
    private Context mContext;

    public DiandianLayoutManager(Context context){
        this.mContext = context;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //默认显示4张图片
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || state.isPreLayout()) return;
        ConfigUtil.initConfig(mContext, DensityUtils.dp2px(mContext, mContext.getResources().getDimension(R.dimen.translate)),0.05f);
        if (getItemCount() < ConfigUtil.DEFAULT_COUNT){
            throw new IllegalMonitorStateException("child count must more "+ConfigUtil.DEFAULT_COUNT);
        }

        detachAndScrapAttachedViews(recycler);
        for (int i=ConfigUtil.DEFAULT_COUNT-1;i>=0;i--){
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view,0,0);
            int itemWidth = getDecoratedMeasuredWidth(view);
            int itemHeight = getDecoratedMeasuredHeight(view);
            int left = (getWidth() - itemWidth)/2;
            int top = (getHeight() - itemHeight)/4;

            layoutDecoratedWithMargins(view,left,top,left+itemWidth,top+itemHeight);

            if (i >0){
                view.setScaleX(1.0f-ConfigUtil.DEFAULT_SCALE*i);
                if (i < ConfigUtil.DEFAULT_COUNT -1 ){
                    view.setTranslationY(ConfigUtil.DEFAULT_TRANSLATE*i);
                }else {
                    view.setTranslationY(ConfigUtil.DEFAULT_TRANSLATE*(i-1));
                }
            }
        }

    }
}
