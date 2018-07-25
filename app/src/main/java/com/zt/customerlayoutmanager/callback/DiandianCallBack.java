package com.zt.customerlayoutmanager.callback;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.zt.customerlayoutmanager.utils.ConfigUtil;


/**
 * Created by zl on 2018/7/20.
 */

public class DiandianCallBack extends ItemTouchHelper.Callback {
    private static final String TAG = "DiandianCallBack";
    private int  mDirection =0 ;   //滑动方向。0：左滑；1：右滑
    private ItemAdapterListener mItemAdapterListener;
    private OffsetListener mOffsetListener;

    public DiandianCallBack(ItemAdapterListener adapterListener) {
        this.mItemAdapterListener = adapterListener;
    }

    public void setOffsetListener(OffsetListener offsetListener){
        this.mOffsetListener = offsetListener;
    }

    //返回可以滑动的方向
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
    }

    //当用户拖动一个Item从旧的位置到新的位置的时候会调用该方法
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    //当用户滑动Item达到删除条件时，会调用该方法
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemAdapterListener.swip(viewHolder.getLayoutPosition());
        mOffsetListener.offset(0,mDirection);
    }

   //我们可以在这个方法内实现我们自定义的交互规则或者自定义的动画效果
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        double dx = Math.sqrt(dX*dX+dY*dY);
        double percent =dx/ (recyclerView.getWidth()/4);

        double percent1 = Math.abs(dX/(recyclerView.getWidth()/4));


        if (percent > 1) percent = 1;
        int count = recyclerView.getChildCount();
        for (int i=0;i<count;i++){
            View view = recyclerView.getChildAt(i);
            if (i<count-1){
                int level = count-1-i;

                view.setScaleX((float) (1.0f-level* ConfigUtil.DEFAULT_SCALE+ConfigUtil.DEFAULT_SCALE*percent));
                if (i>0){
                    view.setTranslationY((float) (ConfigUtil.DEFAULT_TRANSLATE*level-ConfigUtil.DEFAULT_TRANSLATE* percent));
                }else {
                    view.setTranslationY(ConfigUtil.DEFAULT_TRANSLATE*(level-1));
                }
            }else {
                view.setTranslationY(dY);
                view.setTranslationX(dX);
                view.setRotation(dX/(recyclerView.getWidth()/4)*15f);
            }
        }

        //判断左滑还是右滑
        if (dX <0){
            mDirection = 0;
        }else if (dX>0){
            mDirection = 1;
        }

        mOffsetListener.offset(percent1,mDirection);
    }

    //当用户操作完毕某个item并且其动画也结束后会调用该方法，
    // 一般我们在该方法内恢复ItemView的初始状态，防止由于复用而产生的显示错乱问题
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0f);
        mOffsetListener.offset(0,mDirection);
    }

    //从静止状态变为拖拽或者滑动的时候会回调该方法，参数actionState表示当前的状态
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    //该方法返回true时，表示支持长按拖动
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    //该方法返回true时，表示如果用户触摸滑动了View，
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    public interface OffsetListener{
        void offset(double percent,int direction);
    }
}
