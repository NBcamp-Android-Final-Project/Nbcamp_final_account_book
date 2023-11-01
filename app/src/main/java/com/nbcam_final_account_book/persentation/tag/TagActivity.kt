package com.nbcam_final_account_book.persentation.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nbcam_final_account_book.databinding.ActivityTagBinding

class TagActivity : AppCompatActivity() {

	private val binding by lazy { ActivityTagBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}

	companion object {
		fun newIntent(context: Context): Intent {
			return Intent(context, TagActivity::class.java)
		}
	}
}