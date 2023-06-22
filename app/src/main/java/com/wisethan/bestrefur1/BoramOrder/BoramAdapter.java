package com.wisethan.bestrefur1.BoramOrder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wisethan.bestrefur1.BoramOrder.model.Boram;
import com.wisethan.bestrefur1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BoramAdapter extends RecyclerView.Adapter<BoramAdapter.BoramViewHolder> implements Filterable {
    private final List<Boram> boramList;

    private final List<Boram> filteredBoramList;

    public BoramAdapter(List<Boram> boramList) {
        this.boramList = boramList;
        this.filteredBoramList = new ArrayList<>(boramList);
    }

    @NonNull
    @Override
    public BoramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_boram_item,parent,false);
        return new BoramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoramAdapter.BoramViewHolder holder, int position) {
        Boram boram = this.filteredBoramList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(boram.getProductImgUrl())
                .into(holder.goods_view);

        holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        holder.goods_name.setText(boram.getGoodsName());

        holder.itemView.setOnClickListener(v->{
            //클릭 이벤트 처리: 상세 화면으로 이동
            Intent intent = new Intent(holder.itemView.getContext(), BoramDetailActivity.class);
            intent.putExtra("detailImageUrl",boram.getDetailImgUrl());
            intent.putExtra("goodsNames",boram.getGoodsName());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredBoramList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase(Locale.getDefault());
                filteredBoramList.clear();
                if(searchText.length()==0){
                    filteredBoramList.addAll(boramList);
                }else {
                    for(Boram item : boramList){
                        if(item.getGoodsName().toLowerCase(Locale.getDefault()).contains(searchText)){
                            filteredBoramList.add(item);
                        }
                    }
                }
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    public static class BoramViewHolder extends RecyclerView.ViewHolder {
        ImageView goods_view;
        TextView goods_name;
        public BoramViewHolder(@NonNull View itemView) {
            super(itemView);
            goods_view = itemView.findViewById(R.id.goods_view);
            goods_name = itemView.findViewById(R.id.goods_name);
        }
    }
}
