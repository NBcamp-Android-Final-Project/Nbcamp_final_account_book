package com.nbcam_final_account_book.persentation.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.MainActivityBinding
import com.nbcam_final_account_book.persentation.template.addbudget.TemplateBudgetFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel
    private val navHostFragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }

    private val extraTemplate: TemplateEntity? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                TemplateBudgetFragment.EXTRA_RESULT,
                TemplateEntity::class.java
            )
        } else {
            intent.getParcelableExtra<TemplateEntity>(TemplateBudgetFragment.EXTRA_RESULT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() = with(binding) {

        Log.d("도착", extraTemplate.toString())
        if (extraTemplate != null) {
            updateTemplate(extraTemplate)
        }


        //toolbar 연결
        setSupportActionBar(mainToolbar)

        //bottom navigation 연결
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menu_home, R.id.menu_statistics, R.id.menu_chat, R.id.menu_more
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        mainBottomNavi.setupWithNavController(navController)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this@MainActivity,
            MainViewModelFactory(this@MainActivity)
        )[MainViewModel::class.java]

        with(viewModel) {

            mainLiveCurrentTemplate.observe(this@MainActivity, Observer { it ->
                if (it != null) {
                    saveSharedPrefCurrentUser(it)
                    Log.d("키값.현재", it.toString())
                    setKey()
                }
                Log.d("옵저빙.템플릿", it.toString())
            })
            mainLiveEntryList.observe(this@MainActivity, Observer { it ->
                if (it != null) {
                    Log.d("옵저빙.엔트리 리스트", it.toString())
//                    insertData() // 백업 테스트코드
                }
            })
        }

    }

    private fun updateTemplate(item: TemplateEntity?) {
        viewModel.updateCurrentTemplate(item)
    }

    private fun setKey() {
        viewModel.setKey()
    }


}


// 현재 이게 완료가 되면은
// 로그아웃 상태에서 데이터처리랑
// 비로그인 회원의 데이터 처리가 같이 수행이 될 수도 있어서
// fireabse 연결 시점도 고민

