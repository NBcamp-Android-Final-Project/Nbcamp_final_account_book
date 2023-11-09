package com.nbcam_final_account_book.persentation.chart

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ChartFragmentBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun PieChartWithStyles(expenses: List<ChartTagModel>, isEmpty: Boolean, alpha: Float = 1f) {
    val coroutineScope = rememberCoroutineScope()
    val totalExpense = expenses.sumByDouble { it.amount }

    // 애니메이션 지속 시관과 지연 시간
    val animationDuration = 700
    val animationDelay = 150

    // 시작값은 0f로 초기화합니다.
    // 각 카테고리 별로 애니메이션의 진행 상태를 초기화
    val animatedProgresses = expenses.mapIndexed { index, category ->
        val sweep = 360.0 * (category.amount / totalExpense)
        animateFloatAsState(
            targetValue = sweep.toFloat(),
            animationSpec = tween(
                durationMillis = animationDuration,
                delayMillis = animationDelay * index,
                easing = LinearOutSlowInEasing
            ),
            label = ""
        )
    }

    val animationStateList = expenses.map {
        mutableStateOf(0f)
    }

    val animationSpec = tween<Float>(
        durationMillis = animationDuration,
        easing = LinearOutSlowInEasing
    )

    // 각 카테고리별로 애니메이션의 진행 상태를 업데이트
    animationStateList.forEachIndexed { index, state ->
        val category = expenses[index]
        val sweep = (360.0 * (category.amount / totalExpense)).toFloat()

        coroutineScope.launch {
            animate(
                initialValue = 0f,
                targetValue = sweep.toFloat(),
                animationSpec = animationSpec
            ) { value, _ ->
                state.value = value
            }
        }
    }

    // 차트 전체의 레이아웃 부분
    Box(
        modifier = Modifier
            .size(150.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)) {
            // 원형 차트의 반경 / 텍스트의 위치
            val innerCircleRadius = size.width * 0.3f
            val arcRadius = size.width * 0.4f
            val textRadius = size.width * 0.5f

            // 중앙 원
            drawCircle(
                color = Color(0xFF1C1C1C),
                radius = innerCircleRadius,
                center = center
            )

            // 각 카테고리별 차트 부분
            var startAngle = -90f
            for (index in expenses.indices) {
                val category = expenses[index]
                val animatedProgress = animationStateList[index].value

                // 카테고리별 그림그리기
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = animatedProgress.toFloat(),
                    useCenter = false,
                    size = Size(size.width, size.height),
                    style = Stroke(width = arcRadius, cap = StrokeCap.Butt)
                )

                // 카테고리의 이름과 퍼센트
                if (animatedProgress > 0) {
                    // 각도를 라디안으로 변환
                    val halfAngleRadians = Math.toRadians((startAngle + animatedProgress / 2).toDouble())

                    // 텍스트를 그릴 위치를 결정
                    val textRadius = arcRadius / 2 + innerCircleRadius
                    val textX = (center.x + textRadius * cos(halfAngleRadians)).toFloat()
                    val textY = (center.y + textRadius * sin(halfAngleRadians)).toFloat()

                    // 텍스트 그리기
                    val text = "${category.name} ${(category.amount / totalExpense * 100).toInt()}%"
                    val textPaint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 13f.spToPx(density)
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        textAlign = Paint.Align.CENTER
                    }

                    // 텍스트의 상하 위치를 정렬하기 위해 텍스트의 높이를 계산
                    val textHeight = textPaint.descent() - textPaint.ascent()
                    drawContext.canvas.nativeCanvas.drawText(
                        text,
                        textX,
                        textY - textHeight / 2 - textPaint.ascent(), // 텍스트를 수직 중앙에 맞춤
                        textPaint
                    )
                }

                startAngle += animatedProgress
            }
        }
    }
}

// sp 값을 px 값으로 변환하는 확장 함수
private fun Float.spToPx(density: Float): Float = this * density

@Composable
fun ExpenseScreen(expenses: List<ChartTagModel>) {

    val isEmpty = expenses.isEmpty()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 중앙에 원형 차트 - 데이터가 없으면 더미 데이터와 희미한 알파값을 적용
        PieChartWithStyles(expenses = expenses, isEmpty = isEmpty, alpha = if (isEmpty) 0.3f else 1f)

        // 데이터가 없을 때 텍스트 표시
        if (isEmpty) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "현재 데이터가 없습니다",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

class ChartFragment : Fragment() {

    private var _binding: ChartFragmentBinding? = null

    private val chartListAdapter by lazy {
        ChartListAdapter()
    }

    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ChartViewModelFactory(requireContext())
        )[ChartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    private fun initView() = with(binding) { // 레이아웃 제어

        chartTabDay.performClick()

        chartTabRecyclerView.apply {
            adapter = chartListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // 현재 날짜를 가져오기
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(currentDate)
        chartDateTitle.text = dateString

        chartTabContainer.setOnCheckedChangeListener { _, selectedRadioButtonId ->
            val white = ContextCompat.getColor(requireContext(), R.color.white)
            val blue = ContextCompat.getColor(requireContext(), R.color.chart_tab_unselected_text)
            chartTabDay.setTextColor(blue)
            chartTabWeek.setTextColor(blue)
            chartTabMonth.setTextColor(blue)
            chartTabPeriod.setTextColor(blue)

            when (selectedRadioButtonId) {
                R.id.chart_tab_day -> {
                    chartTabDay.setTextColor(white)
                    viewModel.setToday()
                }

                R.id.chart_tab_week -> {
                    chartTabWeek.setTextColor(white)
                    viewModel.setThisWeekRange()
                }

                R.id.chart_tab_month -> {
                    chartTabMonth.setTextColor(white)
                    viewModel.setThisMonthRange()
                }

                R.id.chart_tab_period -> chartTabPeriod.setTextColor(white)
            }
        }

        // 기간 선택하기
        chartTabPeriod.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { dateRange ->
            val startDate = Calendar.getInstance()
            startDate.timeInMillis = dateRange.first

            val endDate = Calendar.getInstance()
            endDate.timeInMillis = dateRange.second

            viewModel.setDateRange(startDate, endDate)
        }

        dateRangePicker.show(childFragmentManager, dateRangePicker.toString())
    }


    private fun initViewModel() = with(viewModel) {
        liveEntryListInChart.observe(viewLifecycleOwner) { entryList ->

            val  filter= entryListToExpense(entryList)
            firstExpense(filter)

        }

        expenses.observe(viewLifecycleOwner){
            if (it != null){
                binding.composeView.setContent {
                    MaterialTheme {
                        ExpenseScreen(it)
                    }
                }
            }
        }

        chartItems.observe(viewLifecycleOwner) { chartItems ->
            chartListAdapter.setItems(chartItems)
        }

        dateRangeText.observe(viewLifecycleOwner) { dateRangeText ->
            binding.chartDateTitle.text = dateRangeText
        }
    }
}