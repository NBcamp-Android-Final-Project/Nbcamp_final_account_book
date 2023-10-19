package com.nbcam_final_account_book.persentation.home

import com.nbcam_final_account_book.data.model.DummyData.liveDummyEntry
import com.nbcam_final_account_book.persentation.entry.EntryModel

object DummyData {

    // 더미 데이터 맵
    private val dummyDataMap: MutableMap<String, List<EntryModel>> = mutableMapOf()

    init {
        // 여러 날짜에 대한 더미 데이터를 생성하고 저장
        for (year in 2023..2023) { // 시작 연도부터 끝 연도까지 설정
            for (month in 1..12) { // 1월부터 12월까지 설정
                for (day in 1..31) { // 1일부터 31일까지 설정
                    val date = String.format("%d-%02d-%02d", year, month, day)
                    val entries = generateDummyEntries(date)
                    dummyDataMap[date] = entries
                }
            }
        }
    }

    // 특정 날짜에 해당하는 더미 데이터를 가져오는 함수
    fun getDummyEntriesForDate(date: String): List<EntryModel> {
        val allDummyEntries = liveDummyEntry.value.orEmpty()
        // 날짜에 맞는 더미 데이터를 필터링하여 반환
        return allDummyEntries.filter { it.dateTime == date }
    }

    // 특정 날짜에 대한 더미 데이터 생성
    private fun generateDummyEntries(date: String): List<EntryModel> {
        val entries = mutableListOf<EntryModel>()

        // 카테고리, 제목, 금액을 설정하여 더미 데이터 생성
        entries.add(EntryModel(1, "수입", date, "10000", "월급", "월급 지급"))
        entries.add(EntryModel(2, "지출", date, "1500", "교통비", "지하철비 지출"))
        entries.add(EntryModel(3, "지출", date, "12000", "문화생활", "영화 비용 지출"))
        // 필요한 만큼 데이터를 추가하십시오.

        return entries
    }
}
