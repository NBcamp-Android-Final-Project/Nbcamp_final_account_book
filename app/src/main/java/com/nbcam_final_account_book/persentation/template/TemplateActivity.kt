package com.nbcam_final_account_book.persentation.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateActivityBinding
import com.nbcam_final_account_book.persentation.main.MainActivity

class TemplateActivity : AppCompatActivity() {

    private lateinit var binding: TemplateActivityBinding
    private lateinit var viewModel: TemplateViewModel
    private val isFirst = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TemplateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

    }

    private fun initView() = with(binding) {

        val templateTitle = templateEdtInput.text.toString()


        templateBtnOk.setOnClickListener {
            toMainActivity()
            saveIsFirst(isFirst)
        }
    }

    private fun initViewModel() = with(binding) {

        viewModel = ViewModelProvider(
            this@TemplateActivity,
            TemplateViewModelFactory(this@TemplateActivity)
        )[TemplateViewModel::class.java]

        with(viewModel) {

        }

    }

    private fun toMainActivity() {
        val intent = Intent(this@TemplateActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveIsFirst(isFirst: Boolean) = with(viewModel) {
        saveIsFirst(isFirst)
    }
}