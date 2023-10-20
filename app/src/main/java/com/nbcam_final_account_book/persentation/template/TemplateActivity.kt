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

    private lateinit var binding: TemplateActivityBinding

    // 생각 2 = 템플릿 안에 데이터가 없으면 처음 로그인 하는 것이다.

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