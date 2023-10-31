package com.nbcam_final_account_book.persentation.firstpage.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
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
import com.nbcam_final_account_book.databinding.FirstLoginFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.LoginViewModel
import com.nbcam_final_account_book.persentation.main.MainActivity
import com.nbcam_final_account_book.persentation.template.TemplateActivity
import com.nbcam_final_account_book.persentation.template.TemplateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FirstLoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var isFirst: Boolean = true //앱 최초 실행 판정
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstLoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                if (result.resultCode == AppCompatActivity.RESULT_OK) {
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

        loginBtnLogin.setOnClickListener {
            val currentUser = auth.currentUser

            val email = loginEdtEmail.text.toString()
            val password = loginEdtPassword.text.toString()

            //TODO viewmodel로 옮겨보자!(후순위)
            //email&password 로그인
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
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
                                requireContext(),
                                "이메일 혹은 비밀번호를 확인해주세요",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        Log.d("유저", currentUser.toString())

                    }

            } else {
                Toast.makeText(
                    requireContext(),
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
                GoogleSignIn.getClient(requireContext(), gso)

            val signInIntent = googleSignInClient.signInIntent

            launcher.launch(signInIntent)
        }


    }


    private fun toMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun toTemplateActivity() {
        val intent = Intent(requireContext(), TemplateActivity::class.java).apply {
            putExtra(TemplateActivity.EXTRA_TEMPLATE_TYPE, TemplateType.NEW.name)
        }
        startActivity(intent)
        requireActivity().finish()
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
                .addOnCompleteListener() { firebaseTask ->
                    if (firebaseTask.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Google login successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        CoroutineScope(Dispatchers.Main).launch {
                            if (isFirstLogin()) {
                                toMainActivity()
                            } else {
                                toTemplateActivity()
                            }
                        }

//                        toTemplateActivity() // 테스팅 코드

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Firebase login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Google login failed", Toast.LENGTH_SHORT).show()
            Log.e("구글.로그인.에러", "로그인 에러 ")
        }
    }


}