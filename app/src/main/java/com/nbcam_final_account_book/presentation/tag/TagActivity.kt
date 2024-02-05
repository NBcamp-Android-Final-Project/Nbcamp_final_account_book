package com.nbcam_final_account_book.presentation.tag

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ActivityTagBinding

class TagActivity : AppCompatActivity() {

	private val binding by lazy { ActivityTagBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.statusBarColor = ContextCompat.getColor(this, R.color.primary_main);
		}
	}

	companion object {
		fun newIntent(context: Context): Intent {
			return Intent(context, TagActivity::class.java)
		}
	}
}