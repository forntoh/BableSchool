package ga.forntoh.bableschool.internal

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import ga.forntoh.bableschool.utilities.inPx

class InsetDecoration internal constructor(inset: Int) : ItemDecoration() {

    private val spacing: Int = inset.inPx
    private var left: Int = 0
    private var right: Int = 0
    private var top: Int = 0
    private var bottom: Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        var position = parent.getChildViewHolder(view).adapterPosition

        val size = state.itemCount
        val layoutManager = parent.layoutManager

        left = this.spacing
        right = this.spacing
        top = this.spacing
        bottom = this.spacing

        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val column = position % spanCount

            left = spacing - column * spacing / spanCount
            right = (column + 1) * spacing / spanCount
            top = if (position < spanCount) spacing else 0
            bottom = spacing
        } else if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                left = if (position == 0) this.spacing else this.spacing / 2
                right = if (position == size - 1) this.spacing else this.spacing / 2
            } else {
                top = if (position == 0) this.spacing else this.spacing / 2
                bottom = if (position == size - 1) this.spacing else this.spacing / 2
            }
        }

        outRect.left = left
        outRect.right = right
        outRect.top = top
        outRect.bottom = bottom
    }
}
