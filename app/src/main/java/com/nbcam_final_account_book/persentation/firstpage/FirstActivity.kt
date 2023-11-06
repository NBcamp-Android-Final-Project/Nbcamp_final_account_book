package com.nbcam_final_account_book.persentation.firstpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstActivityBinding


class FirstActivity : AppCompatActivity() {

    private lateinit var binding: FirstActivityBinding
    private lateinit var viewModel: FirstViewModel

    private var isFirst: Boolean = true //앱 최초 실행 판정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()


    }

    private fun initView() = with(binding) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.first_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_first_graph)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this@FirstActivity,
            FirstViewModelFactory(this@FirstActivity)
        )[FirstViewModel::class.java]
        with(viewModel) {
        }
    }
}