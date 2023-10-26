package com.nbcam_final_account_book.persentation.tag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ItemTagManageBinding

class TagManageAdapter(private val onItemClick: (Int, TagModel) -> Unit) :
	ItemTouchHelperCallback.OnItemMoveListener,
	ListAdapter<TagModel, TagManageAdapter.ViewHolder>(object :
		DiffUtil.ItemCallback<TagModel>() {
		override fun areItemsTheSame(oldItem: TagModel, newItem: TagModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: TagModel, newItem: TagModel): Boolean {
			return oldItem == newItem
		}
	}) {

	private lateinit var dragListener: OnStartDragListener

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = ItemTagManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.onBind(getItem(position))
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

			ivHandlerDrag.setOnTouchListener { view, motionEvent ->
				if (motionEvent.action == MotionEvent.ACTION_DOWN) {
					dragListener.onStartDrag(this@ViewHolder)
				}
				return@setOnTouchListener false
			}
		}
	}

	interface OnStartDragListener {
		fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
	}

	fun startDrag(listener: OnStartDragListener) {
		this.dragListener = listener
	}

	override fun onItemMoved(fromPosition: Int, toPosition: Int) {
		val item: TagModel = currentList[fromPosition]
		val newList = mutableListOf<TagModel>()
		newList.addAll(currentList)
		newList.removeAt(fromPosition)
		newList.add(toPosition, item)
		submitList(newList)
	}
}