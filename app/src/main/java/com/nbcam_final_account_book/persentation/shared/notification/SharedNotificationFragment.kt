package com.nbcam_final_account_book.persentation.shared.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.SharedNotificationFragmentBinding
import com.nbcam_final_account_book.persentation.shared.SharedTemplateViewModel
import com.nbcam_final_account_book.persentation.shared.SharedTemplateViewModelFactory

class SharedNotificationFragment : Fragment() {

	private var _binding: SharedNotificationFragmentBinding? = null
	private val binding get() = _binding!!

	private lateinit var navController: NavController
	private lateinit var viewModel: SharedTemplateViewModel

	val database = Firebase.database.reference

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = SharedNotificationFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		navController = findNavController()
		initView()
		initViewModel()
		getSharedTemplate()
	}

	private fun initView() = with(binding) {
		// 뒤로가기 버튼
		sharedNotificationBack.setOnClickListener {
			navController.navigate(R.id.action_sharedNotificationFragment_to_menu_mypage)
		}
	}

	private fun initViewModel() {
		viewModel = ViewModelProvider(
			requireActivity(),
			SharedTemplateViewModelFactory()
		)[SharedTemplateViewModel::class.java]
	}

	//
	private fun getSharedTemplate() {
//		viewModel.sharedTemplateInfo.observe(viewLifecycleOwner) {
//			for (template in it) {
//				database.child(template.id).addValueEventListener(object : ValueEventListener {
//					override fun onDataChange(snapshot: DataSnapshot) {
//						Log.d("template", snapshot.children.toString())
//					}
//
//					override fun onCancelled(error: DatabaseError) {}
//				})
//			}
//		}
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}