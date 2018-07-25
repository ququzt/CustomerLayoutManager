package com.zt.customerlayoutmanager.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zt.customerlayoutmanager.R;
import com.zt.customerlayoutmanager.bean.ItemInfo;
import com.zt.customerlayoutmanager.utils.ConfigUtil;
import com.zt.customerlayoutmanager.utils.DensityUtils;

import java.util.ArrayList;

/**
 * Created by zl on 2018/7/22.
 */

public class FollowLayoutManager extends RecyclerView.LayoutManager {
    private int mLeft,mTop;
    private int mTotalOffset; //总的滑动距离
    private int mItemWidth,mItemHeight;
    private int mFirstLeft;     //当前位置距离左边最大的距离
    private int mCount;
    private static final int LEFT_COUNT = 3;   //左边显示几个
    private Context mContext;

    private static final String TAG = "FollowLayoutManager";

    public FollowLayoutManager(Context context){
        this.mContext = context;
        ConfigUtil.initConfig(context, DensityUtils.dp2px(context, context.getResources().getDimension(R.dimen.translate)),0.1f);
    }
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || state.isPreLayout()) return;
        detachAndScrapAttachedViews(recycler);
        mLeft = getPaddingLeft();
        mTop = getPaddingTop();
        mCount = getItemCount();
        mItemWidth = DensityUtils.dp2px(mContext,170);
        mItemHeight = DensityUtils.dp2px(mContext,210);
        mFirstLeft = (int) (mItemWidth*0.6f)+mLeft;
        fill(0,recycler);
    }

    private void fill(int offset,RecyclerView.Recycler recycler){
        if (getItemCount() == 0) return;
        int currentPosition = offset/mItemWidth;    //当前位置
        int spacePosition = offset%mItemWidth;      //偏移量
        float spacePercent = Math.abs(spacePosition * 1.0f / mItemWidth);   //偏移量的比例

        ArrayList<ItemInfo> itemInfos = new ArrayList<>();
        //计算当前界面显示的哪几个itemView，以及位置
        //显示当前itemview的左边三个，右边按空余空间计算显示数量

        //计算当前itemview的右边view
        for (int i=currentPosition+1;i<getItemCount();i++){
            int left = mFirstLeft-spacePosition + (i-currentPosition) * mItemWidth;
            float scale = 1.0f;
            if (left < -mItemWidth || left>getWidth()+mItemWidth){
                break;
            }else {
                ItemInfo itemInfo = new ItemInfo(left,scale);
                itemInfos.add(itemInfo);
            }
        }

        //添加计算当前itemview以及左边的的位置和缩放
        int firstLeft = mFirstLeft;
        for (int i=currentPosition,j=1;i>=0;i--){
            int maxLeft = (int) (mFirstLeft*Math.pow(0.5,j));
            int left = (int) (firstLeft - maxLeft*spacePercent);

            float scale = (float) (Math.pow(1.0f-ConfigUtil.DEFAULT_SCALE,j-1)*(1-ConfigUtil.DEFAULT_SCALE*spacePercent));
            firstLeft = firstLeft/2;
            ItemInfo itemInfo = new ItemInfo(left,scale);
            itemInfos.add(0,itemInfo);
            if (j>LEFT_COUNT){
                break;
            }
            j++;
        }

        int layoutCount = itemInfos.size();
        int startPosition = currentPosition>LEFT_COUNT?currentPosition-LEFT_COUNT:0;  //要显示到界面开始itemview的位置
        int endPosition = startPosition+layoutCount-1;              //要显示到界面结束itemview的位置

        //当前显示在界面的itemview如果不是要即将显示的itemview则回收
        for (int i = 0;i<getChildCount();i++){
            View view = getChildAt(i);
            int position = getPosition(view);
            if (position <startPosition || position > endPosition){
                removeAndRecycleView(view,recycler);
            }
        }

        detachAndScrapAttachedViews(recycler);

        //显示当前itemview
        for (int i=0;i<layoutCount;i++){
           View view =  recycler.getViewForPosition(startPosition+i);
           addView(view);
           measureChildWithMargins(view,0,0);
           ItemInfo itemInfo = itemInfos.get(i);
           layoutDecoratedWithMargins(view,itemInfo.getLeft(),mTop,itemInfo.getLeft()+getDecoratedMeasuredWidth(view),mTop+getDecoratedMeasuredHeight(view));
           view.setScaleY(itemInfo.getScale());
           view.setScaleX(itemInfo.getScale());
        }


    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int peddingOffset = mTotalOffset + dx;
        mTotalOffset = Math.min(Math.max(peddingOffset,0),mCount*mItemWidth-getWidth()+mFirstLeft);

        fill(mTotalOffset,recycler);
        return mTotalOffset -  peddingOffset+dx;
    }


    /**
     * 获取RecyclerView的显示宽度
     */
    public void getmItemWidth(View view,int width,int height) {
       int widSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
       int heifhtSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
       measureChild(view,width,height);
    }
}
