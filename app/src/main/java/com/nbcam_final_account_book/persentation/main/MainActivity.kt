package com.nbcam_final_account_book.persentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewpagerAdapter by lazy {
        MainViewpagerAdapter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        mainViewpager.adapter = viewpagerAdapter

//         뷰 페이저 스와이프 금지
        mainViewpager.run {
            isUserInputEnabled = false
        }

        //뷰 페이저 네비게이션바 연결
        mainViewpager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mainBottomNavi.menu.getItem(position).isChecked = true
                }
            }
        )

        mainBottomNavi.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> mainViewpager.currentItem = 0
                R.id.menu_statistics -> mainViewpager.currentItem = 1
                R.id.menu_chat -> mainViewpager.currentItem = 2
                R.id.menu_more -> mainViewpager.currentItem = 3
            }

            true
        }
    }
}