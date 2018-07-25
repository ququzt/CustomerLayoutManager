package com.zt.customerlayoutmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zt.customerlayoutmanager.layoutmanager.FollowLayoutManager;

import java.util.ArrayList;

/**
 * Created by zl on 2018/7/22.
 */

public class FollowActivity extends AppCompatActivity {
    private static final String TAG = "FollowActivity";
    private RecyclerView recyclerView;
    private ArrayList<Integer> list = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        list.add(R.mipmap.bg_1);
        list.add(R.mipmap.bg_2);
        list.add(R.mipmap.bg_3);
        list.add(R.mipmap.bg_4);
        list.add(R.mipmap.bg_5);
        recyclerView = findViewById(R.id.recycle_follow);
        recyclerView.setLayoutManager(new FollowLayoutManager(this));
        recyclerView.setAdapter(new FollowAdapter());
    }

    class FollowAdapter extends RecyclerView.Adapter<ItemHolder>{

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.e(TAG, "onCreateViewHolder: ");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_follow,parent,false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.imageView.setBackgroundResource(list.get(position%5));
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }


    class ItemHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_follow);
        }
    }
}
