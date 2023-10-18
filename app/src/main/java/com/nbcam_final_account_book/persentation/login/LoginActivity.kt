package com.nbcam_final_account_book.persentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.LoginActivityBinding
import com.nbcam_final_account_book.databinding.MainActivityBinding
import com.nbcam_final_account_book.persentation.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

    }

    private fun initView() = with(binding) {
        auth = Firebase.auth // 항상 가장 먼저 초기화 해줘야 함

        val nowCurrentUser = auth.currentUser


        if (nowCurrentUser != null) {
            Log.d("유저", nowCurrentUser.toString())
            toMainActivity()
        }

        loginBtnLogin.setOnClickListener {
            val currentUser = auth.currentUser

            val email = loginEdtEmail.text.toString()
            val password = loginEdtPassword.text.toString()

            Log.d("에러", "$email $password")

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "로그인 성공",
                                Toast.LENGTH_SHORT
                            ).show()

                            toMainActivity()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "이메일 혹은 비밀번호를 확인해주세요",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        Log.d("유저", currentUser.toString())

                    }

            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "이메일 혹은 비밀번호를 확인해주세요",
                    Toast.LENGTH_SHORT
                )
                    .show()

                Log.d("유저", currentUser.toString())


            }

        }


    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this@LoginActivity
        )[LoginViewModel::class.java]
    }

    private fun toMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}