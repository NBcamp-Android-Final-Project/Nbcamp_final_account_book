package com.nbcam_final_account_book.persentation.entry

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ActivityEntryBinding
import java.text.DecimalFormat

class EntryActivity : AppCompatActivity() {

	private val binding by lazy { ActivityEntryBinding.inflate(layoutInflater) }
	private val viewPagerAdapter by lazy { ViewPagerAdapter() }
	private lateinit var viewModel: EntryViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		// 뒤로 가기 콜백
		this.onBackPressedDispatcher.addCallback(this, callback)

		initView()
		initViewModel()
		editTextFormat()
	}

	private fun initViewModel() {

		viewModel = ViewModelProvider(
			this@EntryActivity,
			EntryViewModelFactory()
		)[EntryViewModel::class.java]

		with(viewModel) {

			dummyLiveEntryList.observe(this@EntryActivity, Observer { newData ->

			})
		}
	}

	private fun initView() = with(binding) {

		// 날짜 클릭 시 다이얼로그 출력
		tvDate.setOnClickListener {

		}

		// 수입 버튼
		btnIncome.setOnClickListener {
			setSwipeAnim(android.R.color.holo_green_light, R.color.primary)
			onClickBottomSheet()
		}

		// 지출 버튼
		btnSpending.setOnClickListener {
			setSwipeAnim(R.color.primary, android.R.color.holo_green_light)
			onClickBottomSheet()
		}

		ivCancel.setOnClickListener {
			finish()
			overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit)
		}
	}

	// 카테고리 클릭 시 bottom sheet 출력
	private fun onClickBottomSheet() {
		val modal = ModalBottomFragment()
		modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
		modal.show(supportFragmentManager, ModalBottomFragment.TAG)
	}

	// 배경색 전환 애니메이션 (안 쓸듯?)
	private fun setSwipeAnim(fromColor: Int, toColor: Int) {
		val valueAnimator = ValueAnimator.ofObject(
			ArgbEvaluator(),
			ContextCompat.getColor(this, fromColor),
			ContextCompat.getColor(this, toColor)
		)
		valueAnimator.duration = 250
		valueAnimator.addUpdateListener { animator -> binding.linearAnim.setBackgroundColor(animator.animatedValue as Int) }
		valueAnimator.start()
	}

	// EditText 화폐 단위
	private fun editTextFormat() {
		val decimalFormat = DecimalFormat("#,###")
		var result: String = ""

		binding.edtNum.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
				if (!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != result) {
					result =
						decimalFormat.format(charSequence.toString().replace(",", "").toDouble())
					binding.edtNum.setText(result)
					binding.edtNum.setSelection(result.length)
				}
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
	}

	// 뒤로 가기 콜백 인스턴스 생성
	private val callback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			finish()

			// 뒤로 가기 애니메이션
			overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit)
		}
	}

	companion object {
		fun newIntent(context: Context): Intent {
			return Intent(context, EntryActivity::class.java)
		}

		private val TAG = EntryActivity::class.java.simpleName
	}
}