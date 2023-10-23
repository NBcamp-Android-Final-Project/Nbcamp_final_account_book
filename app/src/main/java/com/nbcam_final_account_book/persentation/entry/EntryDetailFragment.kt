package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.FragmentEntryDetailBinding
import com.nbcam_final_account_book.persentation.tag.Tag
import com.nbcam_final_account_book.persentation.tag.TagListAdapter
import java.security.KeyStore.Entry

class EntryDetailFragment : Fragment() {

    private var _binding: FragmentEntryDetailBinding? = null
    private val binding get() = _binding!!
    private var tagPosition: Int? = null
    private val viewModel: EntryViewModel by activityViewModels()

    private val tagListAdapter by lazy {
        TagListAdapter(onItemClick = { position ->
            onItemClickEvent(position)
        })
    }

    private fun onItemClickEvent(position: Int) {
        Toast.makeText(requireActivity(), "$position 번째 클릭", Toast.LENGTH_SHORT).show()
        tagPosition = position
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initTag()
    }

    private fun initView() = with(binding) {

        ivBack.setOnClickListener {

        }

        // 마이페이지 - 태그 관리 액티비티로 이동
        ivTagAdd.setOnClickListener {

        }

        ivSave.setOnClickListener {
            // Firebase RTDB 에 `태그`, `결제 수단`, `메모`, `금액` 저장 후, ModalBottomSheet 및 EntryActivity 종료
            val title = edtTitle.text.toString()
            val tag = tagPosition ?: 0
            val tablayout = tabLayout.selectedTabPosition
            val description = edtDescription.text.toString()
            val result = getData()
            val amount = result.first
            val type = result.second
            val currentTemplate = getCurrentTemplateEntry()
            Log.d("현재 템플릿", currentTemplate.toString())

            if (amount != null && type != null && currentTemplate != null) {
                val entryEntity = EntryEntity(
                    id = 0,
                    key = currentTemplate.id,
                    type = type,
                    dateTime = "",
                    value = amount,
                    tag = tag.toString(),
                    title = title,
                    description = description
                )
                insertEntry(entryEntity)

            }

            requireActivity().finish()

            Log.d("detail", "$title, $tag, $tablayout, $description")
        }
    }

    private fun getCurrentTemplateEntry(): TemplateEntity? {
        return viewModel.getCurrentTemplateEntry()
    }

    private fun insertEntry(item: EntryEntity) {
        viewModel.insertEntity(item)
    }

    private fun getData(): Pair<String?, String?> {
        return viewModel.getData()
    }

    private fun initTag() {

        // 임시 데이터
        val newList = mutableListOf<Tag>()
        newList.apply {
            add(Tag(R.drawable.ic_tag, "달력"))
            add(Tag(R.drawable.ic_chart, "차트"))
            add(Tag(R.drawable.ic_help, "도움"))
            add(Tag(R.drawable.ic_home, "집"))
            add(Tag(R.drawable.ic_lock, "잠금"))
            add(Tag(R.drawable.ic_more_vert, "수직"))
            add(Tag(R.drawable.ic_mypage, "페이지"))
            add(Tag(R.drawable.ic_backup, "백업"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
            add(Tag(R.drawable.ic_check, "확인"))
        }

        binding.rvTagContainer.adapter = tagListAdapter
        tagListAdapter.submitList(newList)
    }

    companion object {
        val TAG = EntryDetailFragment::class.simpleName
    }
}