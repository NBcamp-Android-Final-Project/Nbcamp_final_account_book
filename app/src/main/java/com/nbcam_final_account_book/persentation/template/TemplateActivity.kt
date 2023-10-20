package com.nbcam_final_account_book.persentation.template

import android.content.ContentUris
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateActivityBinding
import com.nbcam_final_account_book.persentation.main.MainActivity
import java.util.regex.Pattern

class TemplateActivity : FragmentActivity() {

    //TODO 다중 템플릿을 구현해야합니다
    //TODO 진입 시 경로를 파악하고 해당 템플릿을 재사용 할 수 있도록 만들어야합니다.(esnum class이용 등)

    private lateinit var binding: TemplateActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TemplateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.template_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.template_graph)

    }

}