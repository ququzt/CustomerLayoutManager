package com.zt.customerlayoutmanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zt.customerlayoutmanager.callback.DiandianCallBack;
import com.zt.customerlayoutmanager.callback.ItemAdapterListener;
import com.zt.customerlayoutmanager.layoutmanager.DiandianLayoutManager;
import java.util.ArrayList;

/**
 * Created by zl on 2018/7/20.
 */

public class DiandianActivity extends AppCompatActivity {
    private static final String TAG = "DiandianActivity";
    private RecyclerView recyclerView;
    private ImageView ivFollow;
    private ImageView ivCancel;
    private ArrayList<Integer> list = new ArrayList();

    private int width,offset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diandian);
        ivFollow = findViewById(R.id.iv_follow);
        ivCancel = findViewById(R.id.iv_cancel);
        list.add(R.mipmap.bg_1);
        list.add(R.mipmap.bg_2);
        list.add(R.mipmap.bg_3);
        list.add(R.mipmap.bg_4);
        list.add(R.mipmap.bg_5);
        recyclerView = findViewById(R.id.recycler_diandian);
        recyclerView.setLayoutManager(new DiandianLayoutManager(this));
        DiandianAdapter adapter = new DiandianAdapter();
        recyclerView.setAdapter(adapter);
        DiandianCallBack callback = new DiandianCallBack(adapter);
        callback.setOffsetListener(new MyOffsetListener());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        width = ivCancel.getWidth();
        offset = width/4;
    }

    class DiandianAdapter extends RecyclerView.Adapter<DiandianHolder> implements ItemAdapterListener{

        @NonNull
        @Override
        public DiandianHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diandian,parent,false);
            return new DiandianHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiandianHolder holder, int position) {
            holder.imageView.setBackgroundResource(list.get(position));
            holder.ivPhoto.setBackgroundResource(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void swip(int position) {
          Integer data =  list.remove(position);
          list.add(data);
          notifyDataSetChanged();
        }

    }

    class DiandianHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView ivPhoto;
        public DiandianHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
        }
    }



    //滑动监听，显示下面视图的动画
    class MyOffsetListener implements DiandianCallBack.OffsetListener{

        @Override
        public void offset(double percent, int direction) {

            int itemWidth = (int) (width+offset*percent);
            int itemHeight = itemWidth;
            if (direction==1){
               LinearLayout.LayoutParams followParams = (LinearLayout.LayoutParams) ivFollow.getLayoutParams();
               followParams.width = itemWidth;
               followParams.height = itemHeight;
               ivFollow.setLayoutParams(followParams);

            }else if(direction==0){
              LinearLayout.LayoutParams cancelParams = (LinearLayout.LayoutParams) ivCancel.getLayoutParams();
              cancelParams.width = itemWidth;
              cancelParams.height = itemHeight;
              ivCancel.setLayoutParams(cancelParams);
            }


        }
    }

}
