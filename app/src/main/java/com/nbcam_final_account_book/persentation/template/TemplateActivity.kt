package com.nbcam_final_account_book.persentation.template

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateActivityBinding

class TemplateActivity : AppCompatActivity() {

    private lateinit var binding: TemplateActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TemplateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {



    }
}