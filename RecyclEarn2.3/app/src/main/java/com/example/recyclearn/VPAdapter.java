package com.example.recyclearn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    ArrayList<seller_viewpager_item> sellerViewpagerItemArrayList;

    public VPAdapter(ArrayList<seller_viewpager_item> sellerViewpagerItemArrayList) {
        this.sellerViewpagerItemArrayList = sellerViewpagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View  view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_viewpager_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        seller_viewpager_item sellerViewpagerItem = sellerViewpagerItemArrayList.get(position);

        holder.imageview.setImageResource(sellerViewpagerItem.imageId);
        holder.tvHeading.setText(sellerViewpagerItem.heading);
        holder.tvDesc.setText(sellerViewpagerItem.description);

    }

    @Override
    public int getItemCount() {
        return sellerViewpagerItemArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView imageview;
        TextView tvHeading, tvDesc;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageview = itemView.findViewById(R.id.ivimage);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }

    }

}

