package ga.forntoh.bableschool.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class InsetDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private int left, right, top, bottom;

    public InsetDecoration(Context context, int inset) {
        this.spacing = (int) Utils.dpToPixels(context, inset);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int size = state.getItemCount();

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int cols = ((GridLayoutManager) layoutManager).getSpanCount();

            left = position % cols != 1 ? spacing : spacing / 2;
            right = position % cols != 0 ? spacing : spacing / 2;
            top = position / cols == 0 ? spacing : spacing / 2;
            bottom = spacing / 2;
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                left = position == 0 ? spacing : spacing / 2;
                right = position == size - 1 ? spacing : spacing / 2;
                top = spacing;
                bottom = spacing;
            } else {
                left = spacing;
                right = spacing;
                top = position == 0 ? spacing : spacing / 2;
                bottom = position == size - 1 ? spacing : spacing / 2;
            }
        }

        outRect.left = left;
        outRect.right = right;
        outRect.top = top;
        outRect.bottom = bottom;
    }
}
