package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nbcam_final_account_book.data.model.remote.ResponseTemplateModel

@Entity(tableName = "template_table")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "key")
    val key: String = "",
    @ColumnInfo(name = "template")
    val templateTitle: String = ""
)

// Todo 고유값 설정
// key 값의 초기 설정은 ""
// key는 firebase의 key값
// room에 처음 들어갈 때는 빈값입니다
// firebase에 삽입할 때 key값을 받아와야 통신을 총 3번
// 한 번으로 압축하고 싶은데
// 1. 앱에서 유니크한 key값을 올리기
// 2. 중립 서버에서 key값을 받아오기
// 3. 현재는 여러번 통신해도 상관 없지 않을 것 같다.
// 4. 유니크한 키값을 어떻게 만들 수 있을까? <-datatime기준으로도 만들 수 있음
// datetiem을 받아서 16진수로 변환해볼까? <<
//  useruid+YYYY-mm-dd-까지 해서< 숫자를 16진수로 변환 한다면? 고유값

fun TemplateEntity.toResponse(key: String): ResponseTemplateModel {

    return ResponseTemplateModel(
        key = key,
        id = id,
        templateTitle = templateTitle
    )
}
