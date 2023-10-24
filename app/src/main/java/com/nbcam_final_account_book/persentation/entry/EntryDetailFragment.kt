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
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import java.security.KeyStore.Entry
import kotlin.random.Random

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

            val dateTimeList = listOf(
                "2023-10-25",
                "2023-10-24",
                "2023-10-23",
                "2023-10-10",
                "2023-10-11",
                "2023-10-12",
                "2023-10-13",
                "2023-10-14",
            )

            val payTagList = listOf(
                "식비",
                "교통비",
                "의료비",
                "쇼핑"
            )
            val inComeList = listOf(
                "월급",
                "용돈",
                "당근",
                "불로소득"
            )

            val dayListSize = dateTimeList.size
            val payTagListSize = payTagList.size
            val inComeListSize = inComeList.size

            val dayRandomIndex = Random.nextInt(0, dayListSize)
            val payRandomIndex = Random.nextInt(0, payTagListSize)
            val inComeRandomIndex = Random.nextInt(0, inComeListSize)

            val dateTime = dateTimeList[dayRandomIndex]
            val payTag = payTagList[payRandomIndex]
            val incomeTag = inComeList[inComeRandomIndex]

            //TODO 여기 날짜 랜덤으로 넣을 수 있도록 해두겠습니다

            if (amount != null && type != null && currentTemplate != null) {
                if (type == INPUT_TYPE_INCOME) {
                    val entryEntity = EntryEntity(
                        id = 0,
                        key = currentTemplate.id,
                        type = type,
                        dateTime = dateTime,
                        value = amount,
                        tag = incomeTag,
                        title = title,
                        description = description
                    )
                    insertEntry(entryEntity)
                } else {
                    val entryEntity = EntryEntity(
                        id = 0,
                        key = currentTemplate.id,
                        type = type,
                        dateTime = dateTime,
                        value = amount,
                        tag = payTag,
                        title = title,
                        description = description
                    )
                    insertEntry(entryEntity)
                }


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