package sourceset564.samples.mvvm.feature.imdbmovie.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Anwar on 26/01/2020.
 */
class MovieMarginDecorator(
    private var startEnd: Int = INVALID,
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    init {
        if (startEnd <= INVALID) {
            startEnd = margin
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        if (position < spanCount) {
            outRect.top = margin
        }

        val lastSpan = spanCount - 1
        if (spanIndex == 0) {
            outRect.left = startEnd
            outRect.right = margin / 2
        } else if (spanIndex != lastSpan) {
            outRect.left = margin / 2
            outRect.right = margin / 2
        } else {
            outRect.left = margin / 2
            outRect.right = startEnd
        }
        outRect.bottom = margin

    }

    companion object {
        const val INVALID = -1
    }
}