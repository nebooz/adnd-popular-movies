package com.abnd.mdiaz.popularmovies.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.abnd.mdiaz.popularmovies.R;

public class MarginDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = MarginDecoration.class.getSimpleName();

    private Context mContext;
    private int mCounter;

    public MarginDecoration(Context context) {
        mContext = context;
        mCounter = 1;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        /*
        This took way too much work to figure out... I mean, if anyone is using a GridLayoutManager...
        is it really that hard to add a bool parameter to center elements in the spans?
        */
        int cardWidth = mContext.getResources().getDimensionPixelSize(R.dimen.movie_list_card_width);
        int managerWidth = parent.getLayoutManager().getWidth();
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int managerSpans = manager.getSpanCount();
        int recyclerWidth = parent.getWidth();

        int availableSpace = managerWidth - (cardWidth * managerSpans);
        int mTrueMargin = availableSpace / (managerSpans * 2);

        Log.d(TAG, "getItemOffsets - ManagerWidth: " + String.valueOf(managerWidth) + " - RecyclerWidth: " + String.valueOf(recyclerWidth + " - Span Count: " + String.valueOf(managerSpans)));
        Log.d(TAG, "getItemOffsets: TrueMargin: " + String.valueOf(mTrueMargin));

        if (mCounter > managerSpans) {

            outRect.set(mTrueMargin, mTrueMargin / 2, mTrueMargin, mTrueMargin / 2);

        } else {

            outRect.set(mTrueMargin, mTrueMargin, mTrueMargin, mTrueMargin / 2);
        }

        mCounter++;

    }
}
