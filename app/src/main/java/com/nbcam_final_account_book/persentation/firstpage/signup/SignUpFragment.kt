package com.nbcam_final_account_book.persentation.firstpage.signup

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstSignUpFragmentBinding


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var _binding: FirstSignUpFragmentBinding? = null

    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this@SignUpFragment)[SignUpViewModel::class.java]
    }

    //todo 약관 표시하기

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //백버튼 콜백 제어
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        //백버튼 콜백 해제
        onBackPressedCallback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstSignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initView()

    }

    private fun initView() = with(binding) {

        //클릭

        signupIvBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        /*signupBtnOk.setOnClickListener {

            val name = signupEvName.text.toString()
            val email = signupEvEmail.text.toString()
            val password = signupEvPassword.text.toString()
            val passwordCheck = signupEvPasswordCheck.text.toString()

            auth = Firebase.auth

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
                makeShortToast("공란을 채워주세요")
            } else if (!emailCheck(email)) {
                makeShortToast("유효한 이메일을 입력해주세요")
            } else if (!passwordCheck(password)) {
                makeShortToast("유효한 비밀번호를 입력해주세요")
            } else if (password != passwordCheck) {
                makeShortToast("비밀번호가 일치하지 않습니다.")
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            makeShortToast("회원 가입이 완료되었습니다.")
                            val profileUpdate = userProfileChangeRequest {
                                displayName = name
                            }
                            auth.currentUser?.updateProfile(profileUpdate)

                            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                        } else {
                            makeShortToast("이미 가입된 이메일입니다.")
                        }
                    }
            }*/
        signupBtnOk.setOnClickListener {

            val name = signupEvName.text.toString()
            val email = signupEvEmail.text.toString()
            val password = signupEvPassword.text.toString()
            val passwordCheck = signupEvPasswordCheck.text.toString()

            auth = Firebase.auth

            signupInputLayoutName.error = null
            signupInputLayoutEmail.error = null
            signupInputLayoutPassword.error = null
            signupInputLayoutPasswordCheck.error = null

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
                makeShortToast("공란을 채워주세요")
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            makeShortToast("회원 가입이 완료되었습니다.")
                            val profileUpdate = userProfileChangeRequest {
                                displayName = name
                            }
                            auth.currentUser?.updateProfile(profileUpdate)
                            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                        } else {
                            makeShortToast("이미 가입된 이메일입니다.")
                        }
                    }
            }
        }

        //TextWatcher
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val name = signupEvName.text.toString()
                val email = signupEvEmail.text.toString()
                val password = signupEvPassword.text.toString()
                val passwordCheck = signupEvPasswordCheck.text.toString()

                val isNameValid = name.isNotEmpty() && name.length < 10
                val isEmailValid = email.isNotEmpty() && emailCheck(email)
                val isPasswordValid = password.isNotEmpty() && passwordCheck(password)
                val isPasswordMatch = password.isNotEmpty() && passwordCheck.isNotEmpty() && (password == passwordCheck)

                signupInputLayoutEmail.error = if (isEmailValid) null else "유효한 이메일을 입력해주세요"
                signupInputLayoutPassword.error =
                    if (isEmailValid) null else "비밀번호는 알파벳, 숫자, 특수문자(.!@#$%)를 혼합하여 8~20자로 입력해주세요."
                signupInputLayoutPasswordCheck.error =
                    if (password == passwordCheck) null else "비밀번호가 일치하지 않습니다."

                signupBtnOk.isEnabled =
                    isNameValid && isEmailValid && isPasswordValid && isPasswordMatch
            }
        }

        signupEvName.addTextChangedListener(textWatcher)
        signupEvEmail.addTextChangedListener(textWatcher)
        signupEvPassword.addTextChangedListener(textWatcher)
        signupEvPasswordCheck.addTextChangedListener(textWatcher)
    }

    private fun initViewModel() {
        with(viewModel) {

        }

    }

    private fun makeShortToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun emailCheck(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }

    private fun passwordCheck(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,20}\$")
        return passwordRegex.matches(password)
    }
}