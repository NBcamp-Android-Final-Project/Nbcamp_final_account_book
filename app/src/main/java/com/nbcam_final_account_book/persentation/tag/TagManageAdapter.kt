package com.nbcam_final_account_book.persentation.tag

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.databinding.ItemTagManageBinding
import java.util.Collections

interface OnDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    fun onEndDrag(items: MutableList<TagEntity>)
}

interface OnDeleteItemListener {
    fun onDeleteItem(position: Int, item: MutableList<TagEntity>)
}

class TagManageAdapter(
    private val tagList: MutableList<TagEntity>,
    private val onItemClick: (Int, TagEntity) -> Unit
) : RecyclerView.Adapter<TagManageAdapter.ViewHolder>(), ItemTouchHelperListener {

    private lateinit var dragListener: OnDragListener
    private lateinit var deleteListener: OnDeleteItemListener

    fun updateList(list:List<TagEntity>){
        tagList.clear()
        tagList.addAll(list)
        notifyDataSetChanged()
    }

    fun startDrag(listener: OnDragListener) {
        this.dragListener = listener
    }

    fun deleteItem(listener: OnDeleteItemListener) {
        this.deleteListener = listener
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
        fun onBind(item: TagEntity) = with(binding) {
            ivTagImg.setImageResource(item.img)
            tvTagName.text = item.title

            vHandlerTouch.setOnClickListener {
                onItemClick(adapterPosition, item)
            }

            // 다이얼로그 확인용 (추후 휴지통 아이콘에 연결할 예정)
            ivHandlerRemove.setOnClickListener {
                deleteListener.onDeleteItem(adapterPosition, tagList)
            }

            ivHandlerDrag.setOnTouchListener { _, motionEvent ->

                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this@ViewHolder)
                } else if (motionEvent.action == MotionEvent.ACTION_UP) {

                }
                Log.d("모션", motionEvent.action.toString())

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

    override fun onItemChanged() {
        dragListener.onEndDrag(tagList)
    }
}