package com.albertoruvel.credit.app.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jose.rubalcaba on 10/27/2017.
 */

public class RecyclerViewMarginDecorator extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = 4;
    }
}
