package com.example.myapplication.adapter;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Reborn;

import java.util.ArrayList;
import java.util.Locale;

public class TheRebornAdapter extends RecyclerView.Adapter<TheRebornAdapter.RebornViewHolder> implements Filterable {

    private final ArrayList<Reborn> reborns; // 상품 리스트
    private ArrayList<Reborn> filteredreborns; // 필터링된  리스트
    public static class RebornViewHolder extends RecyclerView.ViewHolder {

        ImageView goods_view; // 상품 이미지를 보여주는 ImageView
        TextView goods_name; // 상품명을 보여주는 TextView

        public RebornViewHolder(@NonNull View itemView) {
            super(itemView);
            goods_view = itemView.findViewById(R.id.goods_view);
            goods_name = itemView.findViewById(R.id.goods_name);
        }
    }

    public TheRebornAdapter(ArrayList<Reborn> reborns) {
        this.reborns = reborns;
        this.filteredreborns = new ArrayList<>(reborns);
    }

    @NonNull
    @Override
    public RebornViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_the_reborn_item, parent, false);
        return new RebornViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RebornViewHolder holder, int position) {
        String imageUrl = filteredreborns.get(position).getProductImgUrl();  // 상세 이미지 URL 가져오기

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.goods_view); // Glide를 사용하여 이미지 로드

        holder.goods_name.setText(filteredreborns.get(position).getGoodsName()); // 상품명 설정

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 처리: 상세 화면으로 이동
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("detailImageUrl", reborns.get(position).getDetailUrl()); // 상세 이미지 URL 전달
                intent.putExtra("goodsNames", reborns.get(position).getGoodsName()); // 상품명 전달
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reborns.size(); // 필터링된 아이템 개수 반환
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase(Locale.getDefault()); // 검색어를 소문자로 변환

                ArrayList<String> filteredGoodsName = new ArrayList<>(); // 필터링된 상품명을 담을 리스트

                if (searchText.isEmpty()) {
                    // 검색어가 비어있는 경우, 전체 상품명과 이미지 URL을 필터링된 리스트에 추가
                    filteredDetailUrl.addAll(reborns.get());
                } else {
                    // 검색어가 입력된 경우, 상품명을 검색하여 일치하는 상품만 필터링된 리스트에 추가
                    for (int i = 0; i < goodsName.size(); i++) {
                        String name = goodsName.get(i);
                        if (name.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            filteredGoodsName.add(name);
                            filteredProductUrl.add(productUrl.get(i));
                            filteredDetailUrl.add(detailImgUrl.get(i));
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredGoodsName;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<String> filteredGoodsName = (ArrayList<String>) results.values; // 필터링된 상품명 리스트 가져오기
                // 상품명에 일치하는 상품의 이미지 URL을 찾아서 필터링된 이미지 URL 리스트를 생성
                for (String name : filteredGoodsName) {
                    int index = reborns.indexOf(name);
                    filteredProductUrl.add(productUrl.get(index));
                    filteredDetailUrl.add(detailImgUrl.get(index));
                }

                setFilteredData(fil); // 필터링된 데이터 설정
                notifyDataSetChanged(); // 데이터 변경을 알림
            }
        };
    }

    // 필터링된 데이터 설정
    public void setFilteredData(ArrayList<Reborn> reborns) {
        this.filteredreborns = reborns;
    }


}
