package com.nbcam_final_account_book.persentation.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.nbcam_final_account_book.R

class EntryActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_entry)

		// 뒤로 가기 콜백
		this.onBackPressedDispatcher.addCallback(this, callback)
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