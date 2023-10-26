package com.nbcam_final_account_book.persentation.mypage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.FirstActivity
import com.nbcam_final_account_book.persentation.lock.LockActivity
import com.nbcam_final_account_book.persentation.main.MainViewModel

class MyPageFragment : Fragment() {

    companion object {
        const val REQUEST_IMAGE_PICK = 101
    }

    // 더미데이터 - 공유하는 사람
    private val dummyUser = listOf(
        SharedUser("1", "사용자1", "https://images.pexels.com/photos/14661/pexels-photo-14661.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
        SharedUser("2", "사용자2", "https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
        SharedUser("3", "사용자3", "https://images.pexels.com/photos/5186/person-woman-coffee-cup.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
        SharedUser("4", "사용자4", "https://images.pexels.com/photos/25733/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
        SharedUser("5", "사용자5", "https://images.pexels.com/photos/109851/pexels-photo-109851.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
        SharedUser("6", "사용자6", "https://images.pexels.com/photos/55789/pexels-photo-55789.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
    )

    private var _binding: MyPageFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, MyPageViewModelFactory(
                requireContext()
            )
        )[MyPageViewModel::class.java]
    }
    private val sharedViewModel: MainViewModel by activityViewModels()
    private val sharedUsersAdapter = SharedUsersAdapter(dummyUser)

    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) openGallery()
            else deniedDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        with(binding) {
            super.onActivityResult(requestCode, resultCode, data)
            selectedImageResult(requestCode, resultCode, data, mypageIvProfile)
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {

        mypageIvProfile.load(R.drawable.ic_mypage_profile) {
            listener { _, _ ->
                mypageIvProfile.setPadding(10, 10, 10, 10)
            }
        }

        mypageIvProfile.setOnClickListener {
            galleryBottomSheet()
            true
        }

        mypageRvSharedUsers.adapter = sharedUsersAdapter

        mypageIvEdit.setOnClickListener {
            isEditing = !isEditing
            editName()
        }

        mypageTvLock.setOnClickListener {
            val intent = Intent(requireContext(), LockActivity::class.java)
            startActivity(intent)
        }

        mypageTvTag.setOnClickListener {
            findNavController().navigate(R.id.action_menu_more_to_tagFragment)
        }

        mypageTvLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
//            sharedViewModel.backupDatabyLogOut() //백업 테스트 코드
            cleanRoom()
            val intent = Intent(requireContext(), FirstActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initViewModel() = with(viewModel) {

    }

    private fun cleanRoom() = with(viewModel) {
        cleanRoom()
    }

    private fun galleryBottomSheet() {
        val bottomSheet = layoutInflater.inflate(R.layout.my_page_gallery_bottom_sheet, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheet)

        val optionGallery = bottomSheet.findViewById<View>(R.id.option_gallery)
        optionGallery.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }

        val optionRemove = bottomSheet.findViewById<View>(R.id.option_remove)
        optionRemove.setOnClickListener {
            removeCurrentImage()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun removeCurrentImage() = with(binding) {
        mypageIvProfile.load(R.drawable.ic_mypage_profile) {
            listener { _, _ ->
                mypageIvProfile.setPadding(10, 10, 10, 10)
            }
        }
    }

    private fun openGallery() {
        // 권한 확인
        if (checkGalleryPermission()) {
            // 권한이 있는 경우, 갤러리 앱을 열어 이미지 선택
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
        } else {
            requestGalleryPermission()
        }
    }

    private fun selectedImageResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imageView: ImageView
    ) {
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                imageView.load(selectedImageUri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    listener { _, _ ->
                        imageView.setPadding(0, 0, 0, 0)
                    }
                }
            }
        }
    }

    private fun deniedDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("갤러리를 열려면 권한이 필요합니다. 권한을 설정하시겠습니까?")
            .setPositiveButton("설정") { _, _ ->
                // 앱 설정 화면으로 이동
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkGalleryPermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGalleryPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_IMAGE_PICK
        )
    }

    private fun editName() = with(binding) {
        if (isEditing) {
            mypageEtName.isEnabled = true
            mypageEtName.isFocusable = true
            mypageEtName.isFocusableInTouchMode = true
            mypageEtName.requestFocus()
            mypageEtName.setSelection(mypageEtName.text.length)
        } else {
            mypageEtName.isEnabled = false
            mypageEtName.isFocusable = false
            mypageEtName.isFocusableInTouchMode = false
        }
    }
}