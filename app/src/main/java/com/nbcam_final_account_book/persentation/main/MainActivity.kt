package com.nbcam_final_account_book.persentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: MainActivityBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = MainActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)

		initView()
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
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
		val navController = navHostFragment.navController
		val appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.menu_home, R.id.menu_statistics, R.id.menu_chat, R.id.menu_more
			)
		)
		setupActionBarWithNavController(navController, appBarConfiguration)
		mainBottomNavi.setupWithNavController(navController)
	}
}