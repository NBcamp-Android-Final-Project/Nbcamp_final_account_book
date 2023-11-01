package com.nbcam_final_account_book.persentation.entry

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.ActivityEntryBinding
import com.nbcam_final_account_book.persentation.home.HomeFragment

class EntryActivity : AppCompatActivity() {

	companion object {
		fun newIntent(context: Context): Intent {
			return Intent(context, EntryActivity::class.java)
		}

		private val TAG = EntryActivity::class.java.simpleName
		const val EXTRA_ENTRY = "extra_entry"
	}

	private val binding by lazy { ActivityEntryBinding.inflate(layoutInflater) }
	private lateinit var viewModel: EntryViewModel

	private val extraTemplate: TemplateEntity? by lazy {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getParcelableExtra(
				HomeFragment.EXTRA_CURRENT_TEMPLATE,
				TemplateEntity::class.java
			)
		} else {
			intent.getParcelableExtra<TemplateEntity>(HomeFragment.EXTRA_CURRENT_TEMPLATE)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		initViewModel()
		initView()
	}

	private fun initViewModel() {
		viewModel =
			ViewModelProvider(
				this@EntryActivity,
				EntryViewModelFactory(this@EntryActivity)
			)[EntryViewModel::class.java]


	}

	private fun initView() {

		if (extraTemplate != null) {
			updateCurrentTemplate(extraTemplate)
		}
	}

	private fun updateCurrentTemplate(item: TemplateEntity?) {
		viewModel.updateCurrentTemplateEntry(item)
	}


}