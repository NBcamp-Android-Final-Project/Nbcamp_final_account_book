package com.nbcam_final_account_book.persentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
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
import com.nbcam_final_account_book.databinding.LoginActivityBinding
import com.nbcam_final_account_book.databinding.MainActivityBinding
import com.nbcam_final_account_book.persentation.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var launcher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    googleSignInResult(task)
                }

            })
    }

    private fun initView() = with(binding) {
        auth = Firebase.auth // 항상 가장 먼저 초기화 해줘야 함

        val nowCurrentUser = auth.currentUser
//
//
//        if (nowCurrentUser != null) {
//            Log.d("유저", nowCurrentUser.toString())
//            toMainActivity()
//        }

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

        loginBtnGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_login_default_url))
                .requestEmail()
                .build()

            val googleSignInClient: GoogleSignInClient =
                GoogleSignIn.getClient(this@LoginActivity, gso)

            val signInIntent = googleSignInClient.signInIntent

            launcher.launch(signInIntent)
        }


    }


    private fun toMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun googleSignInResult(task: Task<GoogleSignInAccount>) {
        try {

            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this@LoginActivity) { firebaseTask ->
                    if (firebaseTask.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Google login successful",
                            Toast.LENGTH_SHORT
                        ).show()
                      toMainActivity()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Firebase login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(this@LoginActivity, "Google login failed", Toast.LENGTH_SHORT).show()
        }
    }

}