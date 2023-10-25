package com.nbcam_final_account_book.persentation.chart

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ChartFragmentBinding
import kotlin.math.cos
import kotlin.math.sin

data class ExpenseCategory(val name: String, val amount: Float, val color: Color)

@Composable
fun PieChartWithStyles(expenses: List<ExpenseCategory>) {
    val totalExpense = expenses.sumByDouble { it.amount.toDouble() }.toFloat()

    // 애니메이션 지속 시관과 지연 시간
    val animationDuration = 700
    val animationDelay = 150

    // 시작값은 0f로 초기화합니다.
    // 각 카테고리 별로 애니메이션의 진행 상태를 초기화
    val animatedProgresses = expenses.mapIndexed { index, category ->
        val sweep = 360f * (category.amount / totalExpense)
        animateFloatAsState(
            targetValue = 0f,
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

    LaunchedEffect(Unit) {
        val animationSpec = tween<Float>(
            durationMillis = animationDuration,
            easing = LinearOutSlowInEasing
        )

        // 각 카테고리별로 애니메이션의 진행 상태를 업데이트
        animationStateList.forEachIndexed { index, state ->
            val sweep = 360f * (expenses[index].amount / totalExpense)
            animate(
                initialValue = 0f,
                targetValue = sweep,
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

            // 중앙 흰원
            drawCircle(
                color = Color.White,
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
                    sweepAngle = animatedProgress,
                    useCenter = false,
                    size = Size(size.width, size.height),
                    style = Stroke(width = arcRadius, cap = StrokeCap.Butt)
                )

                // 카테고리의 이름과 퍼센트
                if (animatedProgress > 0) {
                    val halfAngle = startAngle + animatedProgress / 2
                    val textOffset = Offset(
                        center.x + textRadius * cos(
                            Math.toRadians(halfAngle.toDouble()).toFloat()
                        ) - 20.dp.toPx(),
                        center.y + textRadius * sin(Math.toRadians(halfAngle.toDouble()).toFloat())
                    )
                    val text = "${category.name} ${(category.amount / totalExpense * 100).toInt()}%"
                    val paint = Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 12f.spToPx(density)
                        typeface = Typeface.create(typeface, Typeface.BOLD)
                        textAlign = Paint.Align.CENTER
                    }

                    // 텍스트를 캔버스에
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
fun ExpenseScreen() {
    // 테스트 데이터~~~
    val expenses = listOf(
        ExpenseCategory("식비", 120f, Color(0xFFA0FFA1)),
        ExpenseCategory("취미", 80f, Color(0xFF8BE5F5)),
        ExpenseCategory("교통비", 70f, Color(0xFFE6FFA1)),
        ExpenseCategory("쇼핑", 150f, Color(0xFFF2AAFF))
    )

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

        // ComposeView에서 Compose UI
        binding.composeView.setContent {
            MaterialTheme {
                ExpenseScreen()
            }
        }
    }

    private fun initView() = with(binding) { // 레이아웃 제어
        chartTabContainer.setOnCheckedChangeListener { _, selectedRadioButtonId ->
            val white = ContextCompat.getColor(requireContext(), R.color.white)
            val blue = ContextCompat.getColor(requireContext(), R.color.chart_tab_unselected_text)
            chartTabDay.setTextColor(blue)
            chartTabWeek.setTextColor(blue)
            chartTabMonth.setTextColor(blue)
            chartTabPeriod.setTextColor(blue)

            when (selectedRadioButtonId) {
                R.id.chart_tab_day -> chartTabDay.setTextColor(white)
                R.id.chart_tab_week -> chartTabWeek.setTextColor(white)
                R.id.chart_tab_month -> chartTabMonth.setTextColor(white)
                R.id.chart_tab_period -> chartTabPeriod.setTextColor(white)
            }
        }
    }

    private fun initViewModel() = with(viewModel) { //뷰 모델 제어
        liveEntryListInChart.observe(viewLifecycleOwner) {

        }
    }
}