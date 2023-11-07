package com.nbcam_final_account_book.persentation.mypage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.FirstActivity
import com.nbcam_final_account_book.persentation.main.MainViewModel
import com.nbcam_final_account_book.persentation.mypage.mypagedialog.MyPageDeniedDialog
import com.nbcam_final_account_book.persentation.mypage.mypagedialog.MyPageEditNameDialog
import com.nbcam_final_account_book.persentation.tag.TagActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyPageFragment : Fragment() {

    private lateinit var database: DatabaseReference

    companion object {
        const val REQUEST_IMAGE_PICK = 101
    }

    // 더미데이터 - 공유하는 사람
    private val dummyUser = listOf(
        SharedUser(
            "1",
            "David",
            "https://images.pexels.com/photos/14661/pexels-photo-14661.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        SharedUser(
            "2",
            "Ian",
            "https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        SharedUser(
            "3",
            "Chloe",
            "https://images.pexels.com/photos/5186/person-woman-coffee-cup.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        SharedUser(
            "4",
            "Felix",
            "https://images.pexels.com/photos/25733/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        SharedUser(
            "5",
            "Bentley",
            "https://images.pexels.com/photos/109851/pexels-photo-109851.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        SharedUser(
            "6",
            "Aurora",
            "https://images.pexels.com/photos/55789/pexels-photo-55789.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        )
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
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
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
            updateProfileImage(requestCode, resultCode, data)
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
        val title = sharedViewModel.mainLiveCurrentTemplate.value?.templateTitle
        mypageTvUsingName.text = title

        getUserName()
        getUserEmail()
//        getUserPhotoUrl()

        loadProfileImage()

        mypageIvProfile.setOnClickListener {
            galleryBottomSheet()
        }

        val spacingInDp = 5
        val spacingInPixels = (spacingInDp * resources.displayMetrics.density).toInt()
        mypageRvSharedUsers.addItemDecoration(ItemSpacingDecoration(spacingInPixels))
        mypageRvSharedUsers.adapter = sharedUsersAdapter

        mypageIvEdit.setOnClickListener {
            showEditNameDialog()
        }

        mypageTvTag.setOnClickListener {
            val intent = TagActivity.newIntent(requireActivity())
            startActivity(intent)
        }

        mypageSwitchPin.setOnClickListener {
            if (sharedViewModel.pin.value.isNullOrEmpty()) {
                navController.navigate(R.id.action_menu_mypage_to_pinFragment)
            } else {
                sharedViewModel.removeSharedPrefPinNumber()
            }
        }

        // TODO: 비밀번호 변경 기능 나중에 추가!

        mypageContainerBackup.setOnClickListener {
            val currentTime = getCurrentTime()
            mypageTvBackupDate.text = "최근 백업 시간 $currentTime"
            setBackupTime(currentTime)

            backupDate()
        }

        mypageContainerSync.setOnClickListener {
            val currentTime = getCurrentTime()
            mypageTvSyncDate.text = "최근 동기화 시간 $currentTime"
            setSyncTime(currentTime)
            syncData()
        }

        backupTime()?.let { backupTime ->
            if (backupTime != "") {
                mypageTvBackupDate.text = "최근 백업 시간 $backupTime"
            } else {
                mypageTvBackupDate.text = ""
            }
        }

        syncTime()?.let { syncTime ->
            if (syncTime != "") {
                mypageTvSyncDate.text = "최근 동기화 시간 $syncTime"
            } else {
                mypageTvSyncDate.text = ""
            }
        }

        mypageTvLogout.setOnClickListener {

            val auth = FirebaseAuth.getInstance()
            backupDataByLogOut()
            auth.signOut()
            removeSharedPrefPinNum()
            cleanRoom()
            val intent = Intent(requireContext(), FirstActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        mypageTvWithdraw.setOnClickListener {

            val auth = FirebaseAuth.getInstance()
            backupDataByLogOut()
            withdrawAccount(auth)
            auth.signOut()
            removeSharedPrefPinNum()
            cleanRoom()
            val intent = Intent(requireContext(), FirstActivity::class.java)
            startActivity(intent)
        }

        mypageTvChangePassword.setOnClickListener {
            val editText = EditText(requireActivity())
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle("패스워드 변경")
            alertDialog.setMessage("변경하고 싶은 패스워드를 입력하세요")
            alertDialog.setView(editText)
            alertDialog.setPositiveButton(
                "변경"
            ) { dialogInterface, i -> changePassword(editText.text.toString()) }
            alertDialog.setNegativeButton("취소") { dialogInterface, i -> dialogInterface.dismiss() }
            alertDialog.show()
        }
    }

    //TODO: name, email, photoUrl 함수를 각각 만들어서....여기에서 bindng 언급ㄴㄴ
    private fun initViewModel() = with(viewModel) {

        with(binding) {
            getUserName()
            name.observe(viewLifecycleOwner) { name ->
                if (name != null) {
                    mypageEtName.text = name
                }
            }

            getUserEmail()
            email.observe(viewLifecycleOwner) { email ->
                if (email != null) {
                    mypageTvEmail.text = email
                }
            }

//            getUserPhotoUrl()
            loadProfileImage()
            photoUrl.observe(viewLifecycleOwner) { photoUrl ->
                if (photoUrl != null) {
                    mypageIvProfile.load(photoUrl) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        listener { _, _ ->
                            mypageIvProfile.setPadding(0, 0, 0, 0)
                        }
                    }
                }
            }

            Log.d("MyPageFragment", "Name: ${name.value}")
            Log.d("MyPageFragment", "Email: ${email.value}")
            Log.d("MyPageFragment", "Photo URL: ${photoUrl.value}")

            sharedViewModel.isPinSet.observe(viewLifecycleOwner) {
                mypageSwitchPin.isChecked = it
            }
        }
    }

    private fun loadProfileImage() = with(binding) {
        viewModel.downloadProfileImage(
            onSuccess = { uri ->
                mypageIvProfile.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    listener { _, _ ->
                        mypageIvProfile.setPadding(0, 0, 0, 0)
                    }
                }
            },
            onFailure = { exception ->
                Log.e("MyPageFragment", "Profile image download failed: $exception")
            }
        )
    }

    private fun backupDate() {
        sharedViewModel.backupData()
    }

    private fun setBackupTime(time: String) = with(viewModel) {
        saveBackupTime(time)
    }

    private fun setSyncTime(time: String) = with(viewModel) {
        saveSyncTime(time)
    }

    private fun backupTime() = with(viewModel) {
        getBackupTime()
    }

    private fun syncTime() = with(viewModel) {
        getSyncTime()
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private fun backupDataByLogOut() {

        sharedViewModel.backupDataByLogOut()
    }

    private fun cleanRoom() = with(viewModel) {
        cleanRoom()
    }

    private fun getUserName() = with(viewModel) {
        getName()
    }

    private fun getUserEmail() = with(viewModel) {
        getEmail()
    }

    /*    private fun getUserPhotoUrl() = with(viewModel) {
            getPhotoUrl()
        }*/

    private fun withdrawAccount(auth: FirebaseAuth) {
        database = Firebase.database.reference
        val user = auth.currentUser!!
        val uid = user.uid

        // Firebase Auth 에서 사용자 삭제
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireActivity(), "정상적으로 회원 탈퇴 되었습니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        // Firebase RTDB 에서 사용자 UID 가 존재할 경우 삭제
        database.child(uid).removeValue()
    }

    private fun updateProfileName(newName: String) = with(viewModel) {
        updateUserName(newName)
    }

    private fun updateProfileImage(requestCode: Int, resultCode: Int, data: Intent?) =
        with(viewModel) {
            handleImageSelection(requestCode, resultCode, data)
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

    private fun deniedDialog() {
        MyPageDeniedDialog(requireContext())
    }

    private fun syncData() {
        sharedViewModel.synchronizationDataWithBtn()
    }

    private fun checkGalleryPermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGalleryPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_IMAGE_PICK
        )
    }

    private fun showEditNameDialog() = with(binding) {
        val currentName = mypageEtName.text.toString()
        MyPageEditNameDialog(requireContext(), currentName) { newName ->
            mypageEtName.text = newName
            updateProfileName(newName)
        }
    }

    private fun removeSharedPrefPinNum() = with(sharedViewModel) {
        removeSharedPrefPinNumber()
    }

    private fun changePassword(password: String) {
        FirebaseAuth.getInstance().currentUser!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireActivity(), "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireActivity(), task.exception.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}