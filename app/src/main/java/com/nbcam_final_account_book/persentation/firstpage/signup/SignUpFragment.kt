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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.databinding.FirstSignUpFragmentBinding
import java.security.SecureRandom


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

		//TextWatcher
		val nameTextWatcher = createTextWatcher(signupEvName, signupInputLayoutName)
		val emailTextWatcher = createTextWatcher(signupEvEmail, signupInputLayoutEmail)
		val passwordTextWatcher = createTextWatcher(signupEvPassword, signupInputLayoutPassword)
		val passwordCheckTextWatcher =
			createTextWatcher(signupEvPasswordCheck, signupInputLayoutPasswordCheck)

		signupEvName.addTextChangedListener(nameTextWatcher)
		signupEvEmail.addTextChangedListener(emailTextWatcher)
		signupEvPassword.addTextChangedListener(passwordTextWatcher)
		signupEvPasswordCheck.addTextChangedListener(passwordCheckTextWatcher)

		signupBtnOk.setOnClickListener {
			val name = signupEvName.text.toString()
			val email = signupEvEmail.text.toString()
			val password = signupEvPassword.text.toString()
			val passwordCheck = signupEvPasswordCheck.text.toString()

			auth = Firebase.auth

			if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
				makeShortToast("공란을 채워주세요")
			} else {
				auth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							makeShortToast("회원가입이 완료되었습니다.")

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
							val action = R.id.action_signUpFragment_to_loginFragment
							findNavController().navigate(action)
						} else {
							makeShortToast("이미 가입된 이메일입니다.")
						}
					}
			}
		}
	}

	private fun initViewModel() {
		with(viewModel) {

		}

	}

	private fun getRandomPassword(): String {
		return SecureRandom().nextDouble().toString().replace(".", "")
	}

	private fun makeShortToast(text: String) {
		Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
	}

	private fun nameCheck(name: String): Boolean {
		val nameRegex = Regex("^[a-zA-Z가-힣\\d!@#\$%^&*]{1,10}\$")
		return nameRegex.matches(name) && !name.contains(" ")
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

	// TextField 유효성 검사
	private fun createTextWatcher(
		editText: TextInputEditText,
		inputLayout: TextInputLayout
	): TextWatcher = with(binding) {
		return object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

			override fun afterTextChanged(s: Editable?) {
				val name = signupEvName.text.toString()
				val email = signupEvEmail.text.toString()
				val password = signupEvPassword.text.toString()
				val passwordCheck = signupEvPasswordCheck.text.toString()

				// 각 입력 필드의 유효성 검사하여 Boolean으로 저장
				val isNameValid = name.isNotEmpty() && nameCheck(name)
				val isEmailValid = email.isNotEmpty() && emailCheck(email)
				val isPasswordValid = password.isNotEmpty() && passwordCheck(password)
				val isPasswordMatch =
					password.isNotEmpty() && passwordCheck.isNotEmpty() && (password == passwordCheck)

				// 입력 필드에 따라 해당 에러 메시지를 설정, 입력 필드가 하거나 비어있거나 유효한 경우 에러 메시지 제거
				inputLayout.error = when (editText) {
					signupEvName -> if (isNameValid || name.isEmpty()) "" else "이름은 1~10자 이내로 입력해주세요."
					signupEvEmail -> if (isEmailValid || email.isEmpty()) "" else "유효한 이메일을 입력해주세요"
					signupEvPassword -> if (isPasswordValid || password.isEmpty()) "" else "비밀번호는 알파벳, 숫자, 특수문자(.!@#$%)를 혼합하여 8~20자로 입력해주세요."
					signupEvPasswordCheck -> if (isPasswordMatch || passwordCheck.isEmpty()) "" else "비밀번호가 일치하지 않습니다."
					else -> ""
				}

				// 모든 필드가 유효한 경우에만 버튼을 활성화
				signupBtnOk.isEnabled =
					isNameValid && isEmailValid && isPasswordValid && isPasswordMatch
			}
		}
	}
}