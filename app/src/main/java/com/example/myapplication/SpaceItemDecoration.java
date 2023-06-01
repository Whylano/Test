package com.example.myapplication;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//리사이클러뷰의 아이템들 사이에 간격을 추가 클래스
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    //간격 값을 매개변수로 받아서 space 변수에 할당
    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // 첫 번째 아이템의 경우에만 위쪽에 space 값을 적용하여 아이템들 사이의 간격이 중복되지 않도록 하는 조건문.
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
