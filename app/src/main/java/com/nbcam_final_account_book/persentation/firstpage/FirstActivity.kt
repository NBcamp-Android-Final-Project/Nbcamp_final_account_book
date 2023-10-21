package com.nbcam_final_account_book.persentation.firstpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstActivityBinding
import com.nbcam_final_account_book.persentation.main.MainActivity
import com.nbcam_final_account_book.persentation.template.TemplateActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: FirstActivityBinding
    private lateinit var viewModel: LoginViewModel

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
            LoginViewModelFactory(this@FirstActivity)
        )[LoginViewModel::class.java]

        with(viewModel) {


        }


    }


}