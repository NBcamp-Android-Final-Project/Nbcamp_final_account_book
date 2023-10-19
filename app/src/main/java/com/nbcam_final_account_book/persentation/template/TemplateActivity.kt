package com.nbcam_final_account_book.persentation.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateActivityBinding
import com.nbcam_final_account_book.persentation.main.MainActivity
import java.util.regex.Pattern

class TemplateActivity : AppCompatActivity() {

    private lateinit var binding: TemplateActivityBinding
    private lateinit var viewModel: TemplateViewModel
    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TemplateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

    }

    private fun initView() = with(binding) {

        templateBtnOk.setOnClickListener {

            val templateTitle = templateEdtInput.text.toString()

            if (isEnable(templateTitle)) {
                Toast.makeText(
                    this@TemplateActivity,
                    "금지된 문자가 포함되어 있습니다. : '#' , '[' , ']' , '.' , '$' ",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {

                saveIsFirst(isFirst)
//                toMainActivity()
            }


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

    private fun isEnable(input: String): Boolean {
        val pattern = Pattern.compile("[#\\[\\].$]")

        return pattern.matcher(input).find()
    }
}