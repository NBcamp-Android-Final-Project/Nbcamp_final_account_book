package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FragmentEntryBinding
import java.text.DecimalFormat

class EntryFragment : Fragment() {

	private var _binding: FragmentEntryBinding? = null
	private val binding get() = _binding!!
	private lateinit var viewModel: EntryViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEntryBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
		initViewModel()
		editTextFormat()
	}

	private fun initView() = with(binding) {

		// 수입 버튼
		btnIncome.setOnClickListener {
			findNavController().navigate(R.id.action_entryFragment2_to_entryDetailFragment)
		}

		// 지출 버튼
		btnSpending.setOnClickListener {
			findNavController().navigate(R.id.action_entryFragment2_to_entryDetailFragment)
		}

		binding.ivBack.setOnClickListener {

		}
	}

	private fun initViewModel() {

		viewModel = ViewModelProvider(
			this,
			EntryViewModelFactory()
		)[EntryViewModel::class.java]

		with(viewModel) {

			dummyLiveEntryList.observe(requireActivity(), Observer { newData ->

			})
		}
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