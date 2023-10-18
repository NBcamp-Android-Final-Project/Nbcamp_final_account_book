package com.nbcam_final_account_book.persentation.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nbcam_final_account_book.R

class EntryActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_entry)
	}

	companion object {
		fun newIntent(context: Context): Intent {
			return Intent(context, EntryActivity::class.java)
		}
	}
}