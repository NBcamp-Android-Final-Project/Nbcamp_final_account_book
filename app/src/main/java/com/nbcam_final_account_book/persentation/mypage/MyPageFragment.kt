package com.nbcam_final_account_book.persentation.mypage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
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
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.FirstActivity
import com.nbcam_final_account_book.persentation.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyPageFragment : Fragment() {

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

    private var isSwitch = false

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
            editNameDialog()
        }

        mypageTvTag.setOnClickListener {
            navController.navigate(R.id.action_menu_mypage_to_tagFragment)
        }

        // TODO: 스위치가 true여도 다른 프래그먼트 갔다 왔을 때는 실행 안되게!
        mypageSwitchPin.setOnCheckedChangeListener { _, isChecked ->
            isSwitch = if (!isSwitch) {
                navController.navigate(R.id.action_menu_mypage_to_pinFragment)
                true // 스위치가 true일 때 네비게이션 실행 후 상태 변경
            } else {
                false
            }
        }
        // TODO: 비밀번호 변경 기능 나중에 추가!

        // TODO: 백업버튼 누르면 그 시간을 표현해주기!

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
            mypageTvBackupDate.text = "최근 백업 시간 $backupTime"
        }

        syncTime()?.let { syncTime ->
            mypageTvSyncDate.text = "최근 동기화 시간 $syncTime"
        }

        mypageTvLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            backupDataByLogOut()
            cleanRoom()
            val intent = Intent(requireContext(), FirstActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
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

    private fun syncData(){
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

    private fun editNameDialog() = with(binding) {
        val dialogView = layoutInflater.inflate(R.layout.my_page_edit_name_dialog, null)
        val editName = dialogView.findViewById<EditText>(R.id.ev_edit_name)
        val tvWarning = dialogView.findViewById<TextView>(R.id.tv_warning)

        editName.setText(mypageEtName.text.toString())

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("이름 수정")
            .setView(dialogView)
            .setPositiveButton("저장") { _, _ ->
                val newName = editName.text.toString()
                mypageEtName.text = newName
                updateProfileName(newName)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        val textWatcher = nameTextWatcher(tvWarning, alertDialog)
        editName.addTextChangedListener(textWatcher)

        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    private fun nameTextWatcher(tvWarning: TextView, alertDialog: AlertDialog) =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val newName = s.toString()
                val isValid = isNameValid(newName)

                if (isValid) {
                    tvWarning.visibility = View.INVISIBLE
                } else {
                    tvWarning.visibility = View.VISIBLE
                    if (newName.length > 10) {
                        tvWarning.text = "이름은 10글자 이하로 입력하세요."
                    } else {
                        tvWarning.text = "2~10자 이내로, 영어, 한글, 숫자만 입력 가능합니다."
                    }
                }

                // 저장 버튼 활성화/비활성화
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isValid
            }
        }

    private fun isNameValid(name: String): Boolean {
        if (name.length < 2 || name.length > 10) {
            return false
        }

        val regex = "^[a-zA-Z0-9가-힣]*$".toRegex()
        return regex.matches(name)
    }
}