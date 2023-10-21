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
    private lateinit var auth: FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var isFirst: Boolean = true //앱 최초 실행 판정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        val load = loadisFirst()

        isFirst = load
        Log.d("로드.create", isFirst.toString())
//        isFirst = true

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

        //TODO 첫 로그인 판정 중 화면 조작할 수 없도록 로딩 화면 필요
        if (nowCurrentUser != null) {
            Log.d("유저", nowCurrentUser.email.toString())
            CoroutineScope(Dispatchers.Main).launch {
                if (isFirstLogin()) {
                    toMainActivity()
                } else {
                    toTemplateActivity()
                }
            }
//            toMainActivity() // 테스팅을 위한 코드
        }


        loginBtnLogin.setOnClickListener {
            val currentUser = auth.currentUser

            val email = loginEdtEmail.text.toString()
            val password = loginEdtPassword.text.toString()

            //TODO viewmodel로 옮겨보자!(후순위)
            //email&password 로그인
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@FirstActivity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@FirstActivity,
                                "로그인 성공",
                                Toast.LENGTH_SHORT
                            ).show()

                            CoroutineScope(Dispatchers.Main).launch {
                                if (isFirstLogin()) {
                                    toMainActivity()
                                } else {
                                    toTemplateActivity()
                                }
                            }


                        } else {
                            Toast.makeText(
                                this@FirstActivity,
                                "이메일 혹은 비밀번호를 확인해주세요",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        Log.d("유저", currentUser.toString())

                    }

            } else {
                Toast.makeText(
                    this@FirstActivity,
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
                GoogleSignIn.getClient(this@FirstActivity, gso)

            val signInIntent = googleSignInClient.signInIntent

            launcher.launch(signInIntent)
        }


    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this@FirstActivity,
            LoginViewModelFactory(this@FirstActivity)
        )[LoginViewModel::class.java]

        with(viewModel) {


        }


    }


    private fun toMainActivity() {
        val intent = Intent(this@FirstActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toTemplateActivity() {
        val intent = Intent(this@FirstActivity, TemplateActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadisFirst(): Boolean = with(viewModel) {
        loadSharedisFirst()
    }

    private suspend fun isFirstLogin(): Boolean {
        val data = viewModel.getAllTemplateSize()

        return data > 0
    }


    private fun googleSignInResult(task: Task<GoogleSignInAccount>) {
        try {

            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this@FirstActivity) { firebaseTask ->
                    if (firebaseTask.isSuccessful) {
                        Toast.makeText(
                            this@FirstActivity,
                            "Google login successful",
                            Toast.LENGTH_SHORT
                        ).show()

//                        CoroutineScope(Dispatchers.Main).launch {
//                            if (isFirstLogin()) {
//                                toMainActivity()
//                            } else {
//                                toTemplateActivity()
//                            }
//                        }

                        toTemplateActivity() // 테스팅 코드

                    } else {
                        Toast.makeText(
                            this@FirstActivity,
                            "Firebase login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(this@FirstActivity, "Google login failed", Toast.LENGTH_SHORT).show()
            Log.e("구글.로그인.에러", "로그인 에러 ")
        }
    }

}