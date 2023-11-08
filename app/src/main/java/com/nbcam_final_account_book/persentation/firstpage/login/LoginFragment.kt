package com.nbcam_final_account_book.persentation.firstpage.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.databinding.FirstLoginFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.FirstViewModel
import com.nbcam_final_account_book.persentation.main.MainActivity
import com.nbcam_final_account_book.persentation.template.TemplateActivity
import com.nbcam_final_account_book.persentation.template.TemplateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FirstLoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FirstViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var isFirst: Boolean = true //앱 최초 실행 판정

    private val viewModel by lazy {
        ViewModelProvider(
            this@LoginFragment,
            LoginViewModelFactory(requireContext())
        )[LoginViewModel::class.java]
    }

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
        initViewModel()
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
                                "환영합니다 ${auth.currentUser?.displayName}님",
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

        loginTvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        // TextView 내 텍스트 색상 및 굵기 설정
        val originalWelcome = loginTvWelcome.text.toString()
        val welcomeSpannable = SpannableStringBuilder(originalWelcome)
        val welcomeStartIndex = originalWelcome.indexOf("두툼 가계부")
        val welcomeEndIndex = welcomeStartIndex + "두툼 가계부".length
        val accentColor = ResourcesCompat.getColor(resources, R.color.text_highlight, null)
        welcomeSpannable.setSpan(ForegroundColorSpan(accentColor), welcomeStartIndex, welcomeEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        welcomeSpannable.setSpan(StyleSpan(Typeface.BOLD), welcomeStartIndex, welcomeEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginTvWelcome.text = welcomeSpannable

        val originalSignup = loginTvSignup.text.toString()
        val signupSpannable = SpannableStringBuilder(originalSignup)
        val signupStartIndex = originalSignup.indexOf("회원가입")
        val signupEndIndex = signupStartIndex + "회원가입".length
        signupSpannable.setSpan(ForegroundColorSpan(accentColor), signupStartIndex, signupEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        signupSpannable.setSpan(StyleSpan(Typeface.BOLD), signupStartIndex, signupEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginTvSignup.text = signupSpannable
    }

    private fun initViewModel() {
        with(viewModel) {

        }
        with(sharedViewModel) {

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

    private fun loadisFirst(): Boolean = with(sharedViewModel) {
        loadSharedisFirst()
    }

    private suspend fun isFirstLogin(): Boolean {
        val data = sharedViewModel.getAllTemplateSize()

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
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            val user = UserDataEntity(
                                key = currentUser.uid,
                                id = currentUser.email ?: "",
                                name = currentUser.displayName ?: "",
                                img = currentUser.photoUrl.toString() ?: ""
                            )

                            updateUser(user)
                        }



                        Toast.makeText(
                            requireContext(),
                            "환영합니다 ${auth.currentUser?.displayName}님",
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

    private fun updateUser(user: UserDataEntity) {
        viewModel.updateUser(user)
    }


}