package com.wisethan.bestrefur1.OnlineOrder;

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

public class OnlineOrderAdapter extends RecyclerView.Adapter<OnlineOrderAdapter.ImageViewHolder> {


    private final List<OnlineOrderUrl> onlineOrderUrlsList;

    public OnlineOrderAdapter(List<OnlineOrderUrl> onlineOrderUrlsList) {
        this.onlineOrderUrlsList = onlineOrderUrlsList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_order_recycler_item_pdf, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        OnlineOrderUrl onlineOrderUrl = onlineOrderUrlsList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(onlineOrderUrl.getOnlineOrderUrl())
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .centerCrop()
                .into(holder.photoView);
        holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }



    @Override
    public int getItemCount() {
        return onlineOrderUrlsList.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        PhotoView photoView;

        public ImageViewHolder(@NonNull View itemView) {

            super(itemView);
            photoView = itemView.findViewById(R.id.photo_view);
        }
    }
}