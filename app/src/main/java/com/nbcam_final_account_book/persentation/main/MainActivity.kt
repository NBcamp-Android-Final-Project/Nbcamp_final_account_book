package com.nbcam_final_account_book.persentation.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.MainActivityBinding
import com.nbcam_final_account_book.persentation.template.dialog.template.TemplateDialogFragment
import com.nbcam_final_account_book.persentation.template.addbudget.TemplateBudgetFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel
    private val navHostFragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }
    private var isLogin: Boolean = false

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
        hideActionBar()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() = with(binding) {

        loadLogin()

        if (!isLogin) {
            synchronizationDataFromFireBase()
            Toast.makeText(this@MainActivity, "동기화가 완료 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        Log.d("도착", extraTemplate.toString())
        if (extraTemplate != null) {
            updateTemplate(extraTemplate)
        }

        mainToolbar.setOnClickListener {
            showTemplateDialog()
        }

        // Bottom Navigation View 초기화
        mainBottomNavi.apply {
            setupWithNavController(navController)
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            itemIconTintList = ContextCompat.getColorStateList(context, R.color.bottom_nav_colors)
            itemTextColor = ContextCompat.getColorStateList(context, R.color.bottom_nav_colors)
        }

        mainBottomNavi.setupWithNavController(navController)

        // Bottom Navigation Item Selceted 설정
        mainBottomNavi.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    navController.navigate(R.id.menu_home)
                    true
                }

                R.id.menu_statistics -> {
                    navController.navigate(R.id.menu_statistics)
                    true
                }

                R.id.menu_chat -> {
                    navController.navigate(R.id.menu_chat)
                    true
                }

                R.id.menu_mypage -> {
                    navController.navigate(R.id.menu_mypage)
                    true
                }

                else -> false
            }
        }
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
                    setTitle(it)
                }
                Log.d("옵저빙.템플릿", it.toString())
            })

            //todo 업데이트 타이밍 조절해보기
            mainLiveEntryList.observe(this@MainActivity, Observer { it ->
                if (it != null) {
                    Log.d("옵저빙.엔트리 리스트", it.toString())
                    if (it.isNotEmpty()) {
                        updataBackupData() // 백업 테스트코드
                    }
                }
            })
            mainLiveTagList.observe(this@MainActivity, Observer { it ->
                if (it != null) {
                    if (it.isNotEmpty()) {
                        updataBackupData() // 백업 테스트코드
                    }
                }
            })
            mainBudgetList.observe(this@MainActivity, Observer { it ->
                if (it != null) {
                    if (it.isNotEmpty()) {
                        updataBackupData() // 백업 테스트코드
                    }
                }
            })


        }

    }

    private fun setTitle(item: TemplateEntity?) {
        if (item == null) return
        binding.toolbarTitle.text = item.templateTitle
    }

    private fun hideActionBar() {
        // MyPage 태그 관리
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.tagFragment || destination.id == R.id.editTagFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }

    private fun updateTemplate(item: TemplateEntity?) {
        viewModel.updateCurrentTemplate(item)
    }

    private fun setKey() {
        viewModel.setKey()
    }

    private fun synchronizationDataFromFireBase() {
        viewModel.firstStartSynchronizationData()
    }

    private fun loadLogin() {
        isLogin = viewModel.loadSharedPrefIsLogin()
        Log.d("로그인여부", isLogin.toString())
    }

    fun toggleBottomNavi(show: Boolean) = with(binding) {
        mainBottomNavi.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun toggleToolbar(show: Boolean) = with(binding) {
        mainToolbar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showTemplateDialog() {
        val customDialog = TemplateDialogFragment()

        customDialog.show(supportFragmentManager, "templateDialog")
    }
}


// 현재 이게 완료가 되면은
// 로그아웃 상태에서 데이터처리랑
// 비로그인 회원의 데이터 처리가 같이 수행이 될 수도 있어서
// fireabse 연결 시점도 고민