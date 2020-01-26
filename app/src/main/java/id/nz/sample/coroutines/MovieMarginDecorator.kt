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

    private fun linearHorizontal(outRect: Rect, position: Int, isEnd: Boolean) {
        if (position == 0) {
            outRect.left = startEnd
        } else {
            outRect.left = margin / 2
        }

        if (isEnd) {
            outRect.right = startEnd
        } else {
            outRect.right = margin / 2
        }
    }

    private fun linearVertical(outRect: Rect, position: Int) {
        if (position == 0) {
            outRect.top = margin
        }

        outRect.left = startEnd
        outRect.right = startEnd
        outRect.bottom = margin
    }

    private fun setGridMargin(
        outRect: Rect,
        position: Int,
        spanIndex: Int,
        spanCount: Int
    ) {
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