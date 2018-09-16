package ga.forntoh.bableschool.utils;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public class SquareConstraintLayout extends ConstraintLayout {

    public SquareConstraintLayout(Context context) {
        super(context);
    }

    public SquareConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            LayoutParams params = (LayoutParams) this.getLayoutParams();
            if (params.height == 0)
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            else
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

}
