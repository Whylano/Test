package com.wisethan.bestrefur1.RebornOrder;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.RebornOrder.model.Reborn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TheRebornAdapter extends RecyclerView.Adapter<TheRebornAdapter.RebornViewHolder> implements Filterable {

    private final List<Reborn> rebornsList; // 리본 리스트

    private final List<Reborn> filteredRebornList; // 필터링된 상품명 리스트
    public TheRebornAdapter(List<Reborn> rebornsList) {
        this.rebornsList = rebornsList;
        this.filteredRebornList = new ArrayList<>(rebornsList);
    }

    @NonNull
    @Override
    public RebornViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.the_reborn_recycler_item, parent, false);
        return new RebornViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RebornViewHolder holder, int position) {
        Reborn reborn = this.filteredRebornList.get(position); // 필터링된 상품 이미지 URL 가져오기

        Glide.with(holder.itemView.getContext())
                .load(reborn.getProductImgUrl())
                //기본적으로 메모리 캐싱을 하기때문에, 메모리 캐싱을 위해 추가적으로 할 일은 없다.
                //단, URL 이미지 로딩 시 한번 로드한 이미지는 chache 에 저장되어 서버에서 해당 이미지를 변경해도
                //App 의 이미지는 갱신되지 않는다.
                //이런 경우, skipMemoryCache(true) 로 메모리 캐시를 사용하지 않을 수 있어 skipMemoryCache()메서드를 사용.
                // 디스크캐시 건너띄기 iskCacheStrategy(DiskCacheStrategy.NONE)
                // 둘다 건너띄기는 같이 쓰면됨
                // 일반적으로 캐시를 건너 뛰지 않는걸 권장.
                // 이미지를 검색, 디코딩 및 변환하여 새 썸네일을 만드는 것보다 캐시에서 이미지를 load하는 것이 빠름
                .skipMemoryCache(true)// 메모리 캐시 저장 off
                .diskCacheStrategy(DiskCacheStrategy.ALL)// 디스크 캐시 저장
                .into(holder.goods_view); // Glide를 사용하여 이미지 로드

        holder.goods_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        holder.goods_name.setText(reborn.getGoodsName()); // 상품명 설정

        holder.itemView.setOnClickListener(v -> {
            // 클릭 이벤트 처리: 상세 화면으로 이동
            Intent intent = new Intent(holder.itemView.getContext(), TheRebornDetailActivity.class);
            intent.putExtra("detailImageUrl", reborn.getDetailImgUrl()); // 상세 이미지 URL 전달
            intent.putExtra("goodsNames", reborn.getGoodsName()); // 상품명 전달
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredRebornList.size(); // 필터링된 아이템 개수 반환
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase(Locale.getDefault()); // 검색어를 소문자로 변환
                //List<Reborn> filteredGoodsName = new ArrayList<>();
                filteredRebornList.clear();
                if (searchText.length() == 0) {
                    // 검색어가 비어있는 경우, 전체 상품명과 이미지 URL을 필터링된 리스트에 추가
                    filteredRebornList.addAll(rebornsList);

                    Log.e(TAG, "검색어가 없습니다! || " + rebornsList);
                } else {
                    // 검색어가 입력된 경우, 상품명을 검색하여 일치하는 상품만 필터링된 리스트에 추가
                    for (Reborn item : rebornsList) {
                        if (item.getGoodsName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                            filteredRebornList.add(item);
                            Log.e(TAG, "아이템 입니다 || " + item.getGoodsName());
                        }
                    }
                }
                return new FilterResults();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged(); // 데이터 변경을 알림
                Log.e(TAG, "데이터 변경알림완료!!");
            }
        };
    }

    public static class RebornViewHolder extends RecyclerView.ViewHolder {

        ImageView goods_view; // 상품 이미지를 보여주는 ImageView
        final TextView goods_name; // 상품명을 보여주는 TextView

        public RebornViewHolder(@NonNull View itemView) {
            super(itemView);
            goods_view = itemView.findViewById(R.id.goods_view);
            goods_name = itemView.findViewById(R.id.goods_name);

        }
    }
}
