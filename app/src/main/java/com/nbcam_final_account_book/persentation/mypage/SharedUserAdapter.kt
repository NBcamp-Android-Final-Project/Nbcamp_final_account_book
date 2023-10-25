package com.nbcam_final_account_book.persentation.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.R

class SharedUserAdapter(private val sharedUserList: List<String>) :
    RecyclerView.Adapter<SharedUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.shared_user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val accountBookName = sharedUserList[position]
        holder.bind(accountBookName)
    }

    override fun getItemCount(): Int {
        return sharedUserList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val accountBookNameTextView: TextView =
            itemView.findViewById(R.id.account_book_tv_name)

        fun bind(accountBookName: String) {
            accountBookNameTextView.text = accountBookName
        }
    }
}