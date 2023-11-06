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
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstSignUpFragmentBinding
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.data.model.local.UserDataEntity


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var _binding: FirstSignUpFragmentBinding? = null

    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(
            this@SignUpFragment,
            SignUpViewModelFactory(requireContext())
        )[SignUpViewModel::class.java]
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

        signupBtnOk.setOnClickListener {

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

                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                val newUser = UserDataEntity(
                                    key = user.uid,
                                    name = name,
                                    id = email
                                )
                                updateUser(newUser)
                            }



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

        //이름 체크
        signupEvName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val name = s.toString()

                if (name.length < 10) {
                    signupTvNameWarning.visibility = View.GONE
                } else {
                    signupTvNameWarning.visibility = View.VISIBLE
                }

            }

        })

        //email 체크
        signupEvEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()

                if (emailCheck(email)) {
                    signupTvEmailWarning.visibility = View.GONE
                } else {
                    signupTvEmailWarning.visibility = View.VISIBLE
                }
            }

        })

        //password 생성
        signupEvPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val passwordCheck = signupEvPasswordCheck.text.toString()

                if (password.isEmpty()) {
                    signupTvPasswordCheckWarning.visibility = View.GONE
                } else if (password == passwordCheck) {
                    signupTvPasswordCheckWarning.visibility = View.GONE
                } else if (passwordCheck(password)) {
                    signupTvPasswordWarning.visibility = View.GONE
                } else {
                    signupTvPasswordWarning.visibility = View.VISIBLE
                }
            }

        })

        //password 체크
        signupEvPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val password = signupEvPassword.text.toString()

                if (s.toString().isEmpty() || s.toString() == password) {
                    signupTvPasswordCheckWarning.visibility = View.GONE
                } else if (s.toString() != password) {
                    signupTvPasswordCheckWarning.visibility = View.VISIBLE
                }
            }

        })


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
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,20}\$")
        return passwordRegex.matches(password)
    }

    private fun updateUser(user: UserDataEntity) {
        viewModel.updateUser(user)
    }


}