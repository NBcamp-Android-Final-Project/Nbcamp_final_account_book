package com.nbcam_final_account_book.persentation.tag

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
	fun onItemMove(fromPosition: Int, toPosition: Int)
	fun onItemSwipe(position: Int)
}

class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener) :
	ItemTouchHelper.Callback() {

	private val itemTouchHelperListener: ItemTouchHelperListener = listener

	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder
	): Int {
		val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
		val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

		return makeMovementFlags(dragFlags, swipeFlags)
	}

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		itemTouchHelperListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

		return true
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		itemTouchHelperListener.onItemSwipe(viewHolder.adapterPosition)
	}

	override fun isLongPressDragEnabled(): Boolean = false

	override fun isItemViewSwipeEnabled(): Boolean = false
}