package com.nbcam_final_account_book.persentation.entry

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ActivityEntryBinding

class EntryActivity : AppCompatActivity() {

	private val binding by lazy { ActivityEntryBinding.inflate(layoutInflater) }
	private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		initView()
		initViewPager()

		// 뒤로 가기 콜백
		this.onBackPressedDispatcher.addCallback(this, callback)
	}

	private fun initView() = with(binding) {

	}

	// viewpager <-> tablayout 연결
	private fun initViewPager() = with(binding) {
		viewpager.run {
			offscreenPageLimit = 1
			getChildAt(0).overScrollMode = android.view.View.OVER_SCROLL_NEVER
			CompositePageTransformer().addTransformer(MarginPageTransformer(8))
			adapter = viewPagerAdapter
			registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
				override fun onPageSelected(position: Int) {
					when (position) {
						0 -> {
							setSwipeAnim(android.R.color.holo_green_light, com.nbcam_final_account_book.R.color.primary)
//							amount = edtNum.text.toString()
//							val absAmount = abs(amount?.toInt() ?: 0)
//							edtNum.setText(abs(amount))
						}

						1 -> {
							setSwipeAnim(com.nbcam_final_account_book.R.color.primary, android.R.color.holo_green_light)
//							edtNum.setText(abs(amount) * -1)
						}
					}
				}
			})
		}
		TabLayoutMediator(tablayout, viewpager) { tab, position ->
			when (position) {
				0 -> tab.text = "지출"
				1 -> tab.text = "수입"
			}
		}.attach()
	}

	// 스와이프 시 배경색 전환 애니메이션
	private fun setSwipeAnim(fromColor: Int, toColor: Int) {
		val valueAnimator = ValueAnimator.ofObject(
			ArgbEvaluator(),
			ContextCompat.getColor(this, fromColor),
			ContextCompat.getColor(this, toColor)
		)
		valueAnimator.duration = 250
		valueAnimator.addUpdateListener { animator -> binding.viewpager.setBackgroundColor(animator.animatedValue as Int) }
		valueAnimator.start()
	}

	// 뒤로 가기 콜백 인스턴스 생성
	private val callback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			finish()
			overridePendingTransition(
				R.anim.slide_down_enter,
				R.anim.slide_down_exit
			)  // 뒤로 가기 애니메이션
			Log.e(TAG, "...onBack...")
		}
	}

	companion object {
		fun newIntent(context: Context): Intent {
			return Intent(context, EntryActivity::class.java)
		}

		private val TAG = EntryActivity::class.java.simpleName
	}
}