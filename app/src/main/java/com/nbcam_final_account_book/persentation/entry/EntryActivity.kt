package com.nbcam_final_account_book.persentation.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ActivityEntryBinding

class EntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryBinding
    private lateinit var viewModel: EntryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로 가기 콜백
        this.onBackPressedDispatcher.addCallback(this, callback)

        initViewModel()
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(
            this@EntryActivity,
            EntryViewModelFactory()
        )[EntryViewModel::class.java]

        with(viewModel) {

            dummyLiveEntryList.observe(this@EntryActivity, Observer { newData ->

            })

        }

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