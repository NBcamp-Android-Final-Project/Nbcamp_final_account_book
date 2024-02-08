package com.nbcam_final_account_book.presentation.mypage

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        // 첫 번째 아이템 (position 0)은 여백을 주지 않음
        if (position > 0) {
            outRect.left = spacing
        }

        // 마지막 아이템 (position itemCount - 1)은 여백을 주지 않음
        if (position < itemCount - 1) {
            outRect.right = spacing
        }
    }
}