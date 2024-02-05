package com.nbcam_final_account_book.presentation.lock

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.LockActivityBinding

class LockActivity : FragmentActivity() {

    private lateinit var binding: LockActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LockActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.lock_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun initView() = with(binding) {
    }
}