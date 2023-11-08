package com.nbcam_final_account_book.persentation.template

import android.content.ContentUris
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateActivityBinding


class TemplateActivity : FragmentActivity() {

    //TODO 다중 템플릿을 구현해야합니다
    //TODO 진입 시 경로를 파악하고 해당 템플릿을 재사용 할 수 있도록 만들어야합니다.(esnum class이용 등)

    companion object {
        const val EXTRA_TEMPLATE_TYPE = "extra_template_type"
    }

    private lateinit var binding: TemplateActivityBinding
    private lateinit var viewModel: TemplateViewModel

    private val templateType by lazy {
        TemplateType.templateType(intent.getStringExtra(EXTRA_TEMPLATE_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TemplateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.primary_main);
        }

        initViewModel()
        initView()


    }

    private fun initView() = with(binding) {
        updateType(templateType)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.template_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.template_graph)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            TemplateViewModelFactory(this@TemplateActivity)
        )[TemplateViewModel::class.java]

        with(viewModel) {
            liveType.observe(this@TemplateActivity, Observer { it ->
                if (it != null) {
                    Log.d("타입", it.name)
                    saveType(it)
                }
            })
        }


    }

    private fun updateType(type: TemplateType?) {
        viewModel.updateType(type)
    }
}