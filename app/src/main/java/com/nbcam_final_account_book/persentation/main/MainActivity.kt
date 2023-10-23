package com.nbcam_final_account_book.persentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MainActivityBinding
import com.nbcam_final_account_book.persentation.entry.ModalBottomFragment
import com.nbcam_final_account_book.persentation.more.MoreFragment



class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
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
        val auth = FirebaseAuth.getInstance()

        //toolbar 연결
        setSupportActionBar(mainToolbar)

        //bottom navigation 연결
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = navHostFragment.navController
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

        with(viewModel){

        }

    }

    // More 액티비티 이동
    fun intentToMore(): ModalBottomFragment.IntentToMore =
        ModalBottomFragment.IntentToMore {
            supportFragmentManager.commit {
                add(R.id.main_fragment, MoreFragment())
            }
        }
}


// 현재 이게 완료가 되면은
// 로그아웃 상태에서 데이터처리랑
// 비로그인 회원의 데이터 처리가 같이 수행이 될 수도 있어서
// fireabse 연결 시점도 고민

