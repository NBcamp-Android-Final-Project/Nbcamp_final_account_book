package com.nbcam_final_account_book.persentation.firstpage.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nbcam_final_account_book.databinding.FragmentEmailVerificationBinding

class EmailVerificationFragment : Fragment() {

	private var _binding: FragmentEmailVerificationBinding? = null
	private val binding get() =  _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


	}
}