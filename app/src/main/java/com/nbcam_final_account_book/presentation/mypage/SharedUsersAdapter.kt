package com.nbcam_final_account_book.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nbcam_final_account_book.databinding.MyPageSharedUserItemBinding

class SharedUsersAdapter(private val userList: List<SharedUser>) :
    RecyclerView.Adapter<SharedUsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            MyPageSharedUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(private val binding: MyPageSharedUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: SharedUser) = with(binding) {
            itemView.apply {
                userItemTvName.text = user.name

                userItemIvProfile.load(user.profileImageUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}

// 공유하는 기능 없으니까 잠깐 더미테이터 쓰려고 만듦
data class SharedUser(
    val id: String, // 사용자의 고유 ID 또는 식별자
    val name: String, // 사용자 이름
    val profileImageUrl: String // 사용자 프로필 이미지 URL
)