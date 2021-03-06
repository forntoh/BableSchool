package ga.forntoh.bableschool.internal

import android.content.Context
import android.util.AttributeSet

class SquareConstraintLayout : androidx.constraintlayout.widget.ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            val params = this.layoutParams as LayoutParams
            if (params.height == 0)
                super.onMeasure(heightMeasureSpec, heightMeasureSpec)
            else
                super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        } catch (e: Exception) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }

    }

}
