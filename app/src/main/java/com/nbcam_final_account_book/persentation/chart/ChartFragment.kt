package com.nbcam_final_account_book.persentation.chart

import android.app.DatePickerDialog
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ChartFragmentBinding
import com.nbcam_final_account_book.persentation.tag.TagViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PieChartWithStyles(expenses: List<ChartTagModel>) {
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

    // LaunchedEffect를 사용하여 애니메이션 시작을 제어
    val animationStateList = expenses.map {
        mutableStateOf(0f)
    }

    LaunchedEffect(expenses) {
        val animationSpec = tween<Float>(
            durationMillis = animationDuration,
            easing = LinearOutSlowInEasing
        )

        // 각 카테고리별로 애니메이션의 진행 상태를 업데이트
        animationStateList.forEachIndexed { index, state ->
            val category = expenses[index]
            val sweep = (360.0 * (category.amount / totalExpense)).toFloat()
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
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
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
            var startAngle = 0f
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
                    val halfAngle = startAngle + animatedProgress / 2
                    val lineStartOffset = Offset(
                        center.x + arcRadius * cos(halfAngle.toDouble() * (Math.PI / 180)).toFloat(),
                        center.y + arcRadius * sin(halfAngle.toDouble() * (Math.PI / 180)).toFloat()
                    )
                    val lineEndOffset = Offset(
                        center.x + (arcRadius + 60.dp.toPx()) * cos(halfAngle.toDouble() * (Math.PI / 180)).toFloat(), // 선의 길이 조정
                        center.y + (arcRadius + 60.dp.toPx()) * sin(halfAngle.toDouble() * (Math.PI / 180)).toFloat()
                    )

                    // 연결선 그리기
                    val path = androidx.compose.ui.graphics.Path()
                    path.moveTo(lineStartOffset.x, lineStartOffset.y)
                    val controlPoint = Offset(
                        center.x + (arcRadius + 15.dp.toPx()) * cos(halfAngle.toDouble() * (Math.PI / 180)).toFloat(),
                        center.y + (arcRadius + 15.dp.toPx()) * sin(halfAngle.toDouble() * (Math.PI / 180)).toFloat()
                    )
                    path.quadraticBezierTo(
                        controlPoint.x,
                        controlPoint.y,
                        lineEndOffset.x,
                        lineEndOffset.y
                    )

                    drawPath(
                        path = path,
                        color = category.color,
                        style = Stroke(width = 2f.dp.toPx())
                    )

                    // 텍스트 그리기
                    val textOffset = Offset(
                        lineEndOffset.x + (if (lineEndOffset.x > center.x) 50.dp else (-50).dp).toPx(), // 텍스트 위치 조정
                        lineEndOffset.y
                    )
                    val text = "${category.name} ${(category.amount / totalExpense * 100).toInt()}%"
                    val paint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 13f.spToPx(density)
                        typeface = Typeface.create(typeface, Typeface.BOLD)
                        textAlign = Paint.Align.CENTER
                    }

                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            text,
                            textOffset.x,
                            textOffset.y + (paint.descent() - paint.ascent()) / 2 - paint.descent(),
                            paint
                        )
                    }
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

    // 중앙에 원형 차트
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        PieChartWithStyles(expenses)
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