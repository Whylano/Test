package com.example.myapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Reborn;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TheRebornAdapter extends RecyclerView.Adapter<TheRebornAdapter.RebornViewHolder> implements Filterable {

    private ArrayList<Reborn> rebornsList; // 리본 리스트

    private ArrayList<Reborn> filteredGoodsName; // 필터링된 상품명 리스트

    public TheRebornAdapter(ArrayList<Reborn> rebornsList) {
        this.rebornsList = rebornsList;
        this.filteredGoodsName = new ArrayList<>(rebornsList);
    }

    @NonNull
    @Override
    public RebornViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_the_reborn_item, parent, false);
        return new RebornViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RebornViewHolder holder, int position) {
        Reborn reborn = rebornsList.get(position); // 필터링된 상품 이미지 URL 가져오기


        Glide.with(holder.itemView.getContext())
                .load(reborn.getProductImgUrl())
                .into(holder.goods_view); // Glide를 사용하여 이미지 로드

        holder.goods_name.setText(reborn.getGoodsName()); // 상품명 설정

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 처리: 상세 화면으로 이동
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("detailImageUrl", reborn.getDetailImgUrl()); // 상세 이미지 URL 전달
                intent.putExtra("goodsNames", reborn.getGoodsName()); // 상품명 전달
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredGoodsName.size(); // 필터링된 아이템 개수 반환
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase(Locale.getDefault()); // 검색어를 소문자로 변환

                ArrayList<Reborn> filteredGoodsName = new ArrayList<>(); // 필터링된 상품명을 담을 리스트

                if (searchText.isEmpty()) {
                    // 검색어가 비어있는 경우, 전체 상품명과 이미지 URL을 필터링된 리스트에 추가
                    filteredGoodsName.addAll(rebornsList);
                } else {
                    // 검색어가 입력된 경우, 상품명을 검색하여 일치하는 상품만 필터링된 리스트에 추가
                    for (Reborn item : rebornsList) {
                        if (item.getGoodsName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                            filteredGoodsName.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredGoodsName;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredGoodsName = (ArrayList<Reborn>) results.values; // 필터링된 상품명 리스트 가져오기

                notifyDataSetChanged(); // 데이터 변경을 알림
            }
        };
    }

    public static class RebornViewHolder extends RecyclerView.ViewHolder {

        ImageView goods_view; // 상품 이미지를 보여주는 ImageView
        TextView goods_name; // 상품명을 보여주는 TextView

        public RebornViewHolder(@NonNull View itemView) {
            super(itemView);
            goods_view = itemView.findViewById(R.id.goods_view);
            goods_name = itemView.findViewById(R.id.goods_name);
        }
    }
}
