package com.nbcam_final_account_book.presentation.mypage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageFragmentBinding
import com.nbcam_final_account_book.presentation.firstpage.FirstActivity
import com.nbcam_final_account_book.presentation.main.MainActivity
import com.nbcam_final_account_book.presentation.main.MainViewModel
import com.nbcam_final_account_book.presentation.mypage.mypagedialog.MyPageChangePasswordDialog
import com.nbcam_final_account_book.presentation.mypage.mypagedialog.MyPageDeniedDialog
import com.nbcam_final_account_book.presentation.mypage.mypagedialog.MyPageEditNameDialog
import com.nbcam_final_account_book.presentation.mypage.mypagedialog.MyPageLogoutDialog
import com.nbcam_final_account_book.presentation.mypage.mypagedialog.MyPageWithdrawDialog
import com.nbcam_final_account_book.presentation.tag.TagActivity
import com.nbcam_final_account_book.util.ContextUtil.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyPageFragment : Fragment() {

    private val firebase by lazy { Firebase }
    private lateinit var key: String
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var img: String

    companion object {
        const val REQUEST_IMAGE_PICK = 101
    }

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

    //    private val sharedUsersAdapter = SharedUsersAdapter(dummyUser)
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
        (requireActivity() as MainActivity).toggleToolbar(false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).toggleToolbar(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
        // 알림 - 가계부 공유 요청
        /*toolbarNotifications.setOnClickListener {
            navController.navigate(R.id.action_menu_mypage_to_sharedNotificationFragment)
        }*/

        //현재 사용중인 가계부 이름
        /*val title = sharedViewModel.mainLiveCurrentTemplate.value?.templateTitle
        mypageTvUsingName.text = title*/

        getUserName()
        getUserEmail()

        loadProfileImage()

        mypageIvProfile.setOnClickListener {
            galleryBottomSheet()
        }

        val spacingInDp = 5
        val spacingInPixels = (spacingInDp * resources.displayMetrics.density).toInt()
        /*mypageRvSharedUsers.addItemDecoration(ItemSpacingDecoration(spacingInPixels))
        mypageRvSharedUsers.adapter = sharedUsersAdapter*/

        mypageEtName.setOnClickListener {
            showEditNameDialog()
        }

        /*mypageTvShare.setOnClickListener {
            navController.navigate(R.id.action_menu_mypage_to_sharedTemplateFragment)
        }*/

        mypageTvBook.setOnClickListener {
            (requireActivity() as MainActivity).showTemplateDialog()
        }

        mypageTvTag.setOnClickListener {
            val intent = TagActivity.newIntent(requireActivity())
            startActivity(intent)
        }

        /*mypageSwitchPin.setOnClickListener {
            if (sharedViewModel.pin.value.isNullOrEmpty()) {
                navController.navigate(R.id.action_menu_mypage_to_pinFragment)
            } else {
                sharedViewModel.removeSharedPrefPinNumber()
            }
        }*/

        // TODO: 비밀번호 변경 기능 나중에 추가!

        mypageContainerBackup.setOnClickListener {
            val currentTime = getCurrentTime()
            mypageTvBackupDate.text = "$currentTime"
            setBackupTime(currentTime)

            backupDate()

            showToast(requireContext(), "데이터 저장하기가 완료되었습니다.")
        }

        mypageContainerSync.setOnClickListener {
            val currentTime = getCurrentTime()
            mypageTvSyncDate.text = "$currentTime"
            setSyncTime(currentTime)
            syncData()

            showToast(requireContext(), "데이터 가져오기가 완료되었습니다.")
        }

        backupTime()?.let { backupTime ->
            mypageTvBackupDate.text = if (backupTime != "") "$backupTime" else ""
        }

        syncTime()?.let { syncTime ->
            mypageTvSyncDate.text = if (syncTime != "") "$syncTime" else ""
        }

        //로그아웃
        mypageTvLogout.setOnClickListener {
            showLogoutDialog()
        }

        //계정 삭제
        mypageTvWithdraw.setOnClickListener {
            showWithdrawDialog()
        }

        // Change the password (except Social Login users)
        mypageTvChangePassword.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                for (userInfo in user.providerData) {
                    if (userInfo.providerId == "google.com") {
                        showToast(
                            requireContext(),
                            "SNS 로그인 사용자는 비밀번호를 변경할 수 없습니다."
                        )
                        return@setOnClickListener
                    }
                }
            }

            showChangePasswordDialog()
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

            photoUrl.observe(viewLifecycleOwner) { photoUrl ->
                showProgressBar()

                if (photoUrl != null) {
                    mypageIvProfile.load(photoUrl) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        listener { _, _ ->
                            hideProgressBar()
                        }
                    }
                } else {
                    hideProgressBar()
                }
            }

            // Check the status of the pin settings
            /*sharedViewModel.isPinSet.observe(viewLifecycleOwner) {
                mypageSwitchPin.isChecked = it
            }*/
        }
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
        return SimpleDateFormat("MM월 dd일 a hh:mm", Locale.getDefault()).format(Date())
    }

    private fun syncData() {
        sharedViewModel.synchronizationDataWithBtn()
    }

    private suspend fun backupDataByLogOut(): Boolean {
        return sharedViewModel.backupDataByLogOut()
    }

    private fun cleanRoom() = with(viewModel) {
        cleanRoom()
    }

    // LockScreen - Remove the pin number
    private fun removeSharedPrefPinNum() = with(sharedViewModel) {
        removeSharedPrefPinNumber()
    }

    private fun getUserName() = with(viewModel) {
        getName()
    }

    private fun getUserEmail() = with(viewModel) {
        getEmail()
    }

    private fun galleryBottomSheet() {
        val bottomSheet = layoutInflater.inflate(R.layout.my_page_gallery_bottom_sheet, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog)
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


    private fun openGallery() {
        if (checkGalleryPermission()) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
        } else {
            requestGalleryPermission()
        }
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

    private fun deniedDialog() {
        MyPageDeniedDialog(requireContext())
    }

    private fun updateProfileImage(requestCode: Int, resultCode: Int, data: Intent?) =
        with(viewModel) {
            if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
                val imageUri = data.data
                if (imageUri != null) {
                    uploadProfileImage(imageUri)
                    showToast(
                        requireContext(),
                        "프로필 이미지가 변경되었습니다."
                    )
                }
            }
        }

    private fun removeCurrentImage() {
        // Storage에서 이미지 삭제
        viewModel.deleteProfileImageFromStorage(
            onSuccess = {
                // Firestore 및 Room 이미지 URL 삭제
                viewModel.deleteProfileImageFromFirestoreAndRoom()
                // UI를 기본 이미지로 업데이트
                updateProfileImageUI(R.drawable.default_profile)
                showToast(requireContext(), "프로필 이미지가 삭제되었습니다.")
            },
            onFailure = { exception ->
                Log.e("MyPageFragment", "프로필 이미지 삭제 오류: $exception")
            }
        )
    }

    private fun updateProfileImageUI(defaultImageResId: Int) = with(binding) {
        mypageIvProfile.load(defaultImageResId) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun loadProfileImage() = with(binding) {
        showProgressBar()

        viewModel.downloadProfileImage(
            onSuccess = { uri ->
                mypageIvProfile.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    listener { _, _ ->
                        hideProgressBar()
                    }
                }
            },
            onFailure = { exception ->
                Log.e("MyPageFragment", "프로필 이미지 다운로드 실패: $exception")
                hideProgressBar()
            }
        )
    }

    // Edit user name
    private fun showEditNameDialog() = with(binding) {
        val currentName = mypageEtName.text.toString()
        val dialog = MyPageEditNameDialog(currentName) { newName ->
            updateName(newName)
        }
        dialog.show(parentFragmentManager, "MyPageEditNameDialog")
    }

    private fun updateName(newName: String) = with(binding) {
        val currentUser = firebase.auth.currentUser
        mypageEtName.text = newName
        updateProfileName(newName)

        key = currentUser?.uid ?: ""
        name = newName
        id = currentUser?.email ?: ""
        img = (currentUser?.photoUrl ?: "").toString()
        viewModel.storeUser(key = key, name = name, id = id, img = img)
    }

    private fun updateProfileName(newName: String) = with(viewModel) {
        updateUserName(newName)
    }

    // Change the password
    private fun showChangePasswordDialog() {
        val dialog = MyPageChangePasswordDialog()
        dialog.show(parentFragmentManager, "MyPageChangePasswordDialog")
    }

    // Logout
    private fun showLogoutDialog() {
        val dialog = MyPageLogoutDialog {
            CoroutineScope(Dispatchers.Main).launch {
                if (backupDataByLogOut()) {
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
//            removeSharedPrefPinNum()
                    cleanRoom()

                    viewModel.clearSharedPreferences()

                    val intent = Intent(requireContext(), FirstActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
        dialog.show(parentFragmentManager, "MyPageLogoutDialog")
    }

    // Withdraw
    private fun showWithdrawDialog() {
        val dialog = MyPageWithdrawDialog {
            val auth = FirebaseAuth.getInstance()
            withdrawAccount(auth)

            viewModel.clearSharedPreferences()
        }
        dialog.show(parentFragmentManager, "MyPageWithdrawDialog")
    }


    // Unregister from dutoom
    private fun withdrawAccount(auth: FirebaseAuth) {
        val user = Firebase.auth.currentUser
        val name = user?.displayName.toString()
        val email = user?.email.toString()
        val uid = user?.uid.toString()

        // Realtiem DB에서 사용자 데이터 삭제
        viewModel.deleteUserInRealtimeDB(uid)

        // Firestore에서 사용자 데이터 삭제
        viewModel.deleteAllData(user = name, email = email, key = uid)

        // Firebase Auth에서 사용자 삭제
        Log.d("result", "${auth.currentUser}")
        auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(requireActivity(), "정상적으로 회원 탈퇴 되었습니다.")
                    // 로그아웃 및 화면 전환
                    auth.signOut()
                    cleanRoom()
                    val intent = Intent(requireContext(), FirstActivity::class.java)
                    startActivity(intent)
                } else {
                    showToast(requireActivity(), "회원 탈퇴에 실패했습니다.")
                    Log.e("result", "Error: ${task.exception}")
                }
            }
    }

    private fun showProgressBar() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressIndicator.visibility = View.GONE
    }
}