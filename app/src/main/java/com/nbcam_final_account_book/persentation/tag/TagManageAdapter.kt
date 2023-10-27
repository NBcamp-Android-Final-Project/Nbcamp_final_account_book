package com.nbcam_final_account_book.persentation.tag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ItemTagManageBinding
import java.util.Collections

interface OnStartDragListener {
	fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}

class TagManageAdapter(
	private val tagList: MutableList<TagModel>,
	private val onItemClick: (Int, TagModel) -> Unit
) : RecyclerView.Adapter<TagManageAdapter.ViewHolder>(), ItemTouchHelperListener {

	private lateinit var dragListener: OnStartDragListener

	fun startDrag(listener: OnStartDragListener) {
		this.dragListener = listener
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = ItemTagManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.onBind(tagList[position])
	}

	override fun getItemCount(): Int {
		return tagList.size
	}

	inner class ViewHolder(private val binding: ItemTagManageBinding) :
		RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("ClickableViewAccessibility")
		fun onBind(item: TagModel) = with(binding) {
			ivTagImg.setImageResource(item.img)
			tvTagName.text = item.tagName

			vHandlerTouch.setOnClickListener {
				onItemClick(adapterPosition, item)
			}

			ivHandlerDrag.setOnTouchListener { _, motionEvent ->
				if (motionEvent.action == MotionEvent.ACTION_DOWN) {
					dragListener.onStartDrag(this@ViewHolder)
				}
				return@setOnTouchListener false
			}
		}
	}

	override fun onItemMove(fromPosition: Int, toPosition: Int) {
		Collections.swap(tagList, fromPosition, toPosition)
		notifyItemMoved(fromPosition, toPosition)
	}

	override fun onItemSwipe(position: Int) {
		tagList.removeAt(position)
		notifyItemRemoved(position)
	}
}