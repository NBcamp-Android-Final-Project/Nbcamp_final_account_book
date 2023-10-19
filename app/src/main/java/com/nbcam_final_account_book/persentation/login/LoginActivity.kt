package com.nbcam_final_account_book.persentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
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
import com.nbcam_final_account_book.persentation.template.TemplateActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var isFirst: Boolean = true //앱 최초 실행 판정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        val load = loadisFirst()

        isFirst = load
        Log.d("로드.create", isFirst.toString())
        isFirst = true

        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    googleSignInResult(task)
                }

            })

        initView()


    }

    private fun initView() = with(binding) {
        auth = Firebase.auth // 항상 가장 먼저 초기화 해줘야 함

        val nowCurrentUser = auth.currentUser

        Log.d("로드", isFirst.toString())

        if (nowCurrentUser != null) {
            Log.d("유저", nowCurrentUser.email.toString())
            toMainActivity()
        }


        loginBtnLogin.setOnClickListener {
            val currentUser = auth.currentUser

            val email = loginEdtEmail.text.toString()
            val password = loginEdtPassword.text.toString()

            //TODO viewmodel로 옮겨보자!(후순위)
            //email&password 로그인
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "로그인 성공",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (isFirst) toTemplateActivity()
                            else toMainActivity()

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

        // google 로그인
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

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this@LoginActivity,
            LoginViewModelFactory(this@LoginActivity)
        )[LoginViewModel::class.java]

        with(viewModel) {


        }


    }


    private fun toMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toTemplateActivity() {
        val intent = Intent(this@LoginActivity, TemplateActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadisFirst(): Boolean = with(viewModel) {
        loadSharedisFirst()
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

                        if (isFirst) toTemplateActivity()
                        else toMainActivity()
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
            Log.e("구글.로그인.에러", "로그인 에러 ")
        }
    }

}