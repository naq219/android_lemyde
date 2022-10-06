package com.project.shop.lemy.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.shop.lemy.nhacviec.ThanhHanhNiemAdapter;

public  class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public MyAdapter(Context context){

        this.context = context;
    }

    protected void showToast(String content){
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
