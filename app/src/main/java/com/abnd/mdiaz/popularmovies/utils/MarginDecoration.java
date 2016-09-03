package com.abnd.mdiaz.popularmovies.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abnd.mdiaz.popularmovies.R;

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int mMargin;

    public MarginDecoration(Context context) {
        mMargin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        outRect.set(mMargin, mMargin, mMargin, mMargin);
    }
}
