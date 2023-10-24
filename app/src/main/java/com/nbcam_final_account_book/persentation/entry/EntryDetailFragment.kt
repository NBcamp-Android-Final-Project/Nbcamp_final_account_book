package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nbcam_final_account_book.databinding.FragmentEntryDetailBinding
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_PAY
import java.text.DecimalFormat

class EntryDetailFragment : Fragment() {

	private var _binding: FragmentEntryDetailBinding? = null
	private val binding get() = _binding!!
	private val viewModel: EntryViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEntryDetailBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		editTextFormat()
		initView()

	}

	private fun initView() = with(binding) {
		val amount = edtNum.text.toString()
		Log.d("amount", amount.toString())

		// 수입 버튼

		btnIncome.setOnClickListener {
			val amount1 = edtNum.text.toString()
			updateData(amount = amount1, type = INPUT_TYPE_INCOME)
//                findNavController().navigate(R.id.action_entryFragment2_to_entryDetailFragment)
		}

		// 지출 버튼
		btnSpending.setOnClickListener {
			val amount2 = edtNum.text.toString()
			updateData(amount = amount2, type = INPUT_TYPE_PAY)
//                findNavController().navigate(R.id.action_entryFragment2_to_entryDetailFragment)
		}

		binding.ivBack.setOnClickListener {

//			requireActivity().finish()
		}


	}

	private fun updateData(amount: String, type: String) {

		viewModel.updateAmount(amount = amount, type = type)

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
}