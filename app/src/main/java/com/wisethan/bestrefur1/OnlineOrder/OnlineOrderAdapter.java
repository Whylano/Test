package com.wisethan.bestrefur1.OnlineOrder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.wisethan.bestrefur1.OnlineOrder.model.OnlineOrderUrl;
import com.wisethan.bestrefur1.R;

import java.util.List;

//Adapter클래스 선언
public class OnlineOrderAdapter extends RecyclerView.Adapter<OnlineOrderAdapter.ImageViewHolder> {


    //필드 선언
    private final List<OnlineOrderUrl> onlineOrderUrlsList;

    //ImageAdapter생성자 (imageUrls 필드를 초기화)
    @SuppressLint("NotifyDataSetChanged")
    public OnlineOrderAdapter(List<OnlineOrderUrl> onlineOrderUrlsList) {
        setHasStableIds(true);
        this.onlineOrderUrlsList = onlineOrderUrlsList;
        notifyDataSetChanged();
    }

    //onCreateViewHolder 메서드
    //아이템 뷰를 위한 뷰 홀더 객체를 생성
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_image 레이아웃을 inflate하여 View 객체 생성(LayoutInflater.from(context)사용)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_order_recycler_item_pdf, parent, false);
        //생성된 뷰 객체를 ImageViewHolder 객체로 감싸서 반환
        return new ImageViewHolder(view);
    }

    //onBindViewHolder 데이터를 뷰에 바인딩하기 위한 메서드
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position/* 현재 아이템의 위치*/) {
        // 이미지 URL 가져오기
        OnlineOrderUrl onlineOrderUrl = onlineOrderUrlsList.get(position);

        // Glide를 사용하여 이미지 로딩 (필요에 따라 커스터마이즈 가능)
        Glide.with(holder.itemView.getContext())
                .load(onlineOrderUrl.getOnlineOrderUrl())
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .centerCrop()
                .into(holder.photoView);
        holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }


    //이미지 URL의 개수가 리사이클러뷰의 아이템 개수로 사용
    @Override
    public int getItemCount() {
        // 이미지 URL의 개수 반환
        return onlineOrderUrlsList.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        PhotoView photoView;

        public ImageViewHolder(@NonNull View itemView) {

            super(itemView);
            // ViewHolder의 뷰 요소인 PhotoView 가져오기
            photoView = itemView.findViewById(R.id.photo_view);
        }
    }
}

// RecyclerView를 사용하여 이미지를 표시하기 위한 어댑터 역할을 합니다.
// imageUrls를 통해 이미지 URL을 받아오고, Glide를 사용하여 이미지를 로딩하며,
// RecyclerView.Adapter의 메서드를 오버라이딩하여 아이템 개수 및 뷰 홀더를 관리합니다.