package com.nbcam_final_account_book.data.repository.room

import androidx.lifecycle.LiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity

interface RoomRepository {

    // 사용방법
    // viewModel에 repository를 연결해줍니다
    // 연결 하는 방법은 MainViewModel을 확인해주시면 됩니다.(Factory 사용)
    // Fragment예시는 HomeViewModel 참고하시면 됩니다.


    //Template
    fun getAllListTemplate(): List<TemplateEntity> //list형태로 모든 템플릿 이름을 가져옵니다
    fun getAllLiveTemplate(): LiveData<List<TemplateEntity>> // live형태로 모든 템플릿 이름을 가져옵니다
    suspend fun insertFirstTemplate(text: String): String // 템플릿을 추가합니다.
    suspend fun insertTemplate(text: String): List<TemplateEntity> // 템플릿을 추가한 뒤 list로 반환합니다.
    suspend fun insertTemplateList(item: List<TemplateEntity>) // 템플릿을 list형태로 대량으로 추가합니다
    suspend fun deleteTemplate(item: TemplateEntity) // 템플릿의 id를 추적해 삭제한 뒤 템플릿 리스트를 반환합니다.
    suspend fun deleteAllTemplate() // 모든 템플릿을 삭제합니다
    suspend fun selectTemplateByKey(key: String): TemplateEntity // id값을 추적해서 템플릿을 반환합니다.
    suspend fun updateTemplate(item: TemplateEntity)

    //Data
    suspend fun insertData(item: DataEntity) //data를 추가합니다
    suspend fun insertDataList(itemList: List<DataEntity>) //list로 data를 대량으로 추가합니다.
    suspend fun getDataByKey(key: String): DataEntity? // key값으로 데이터를 추적합니다. 동일한 템플릿을 식별할 때 사용하면 됩니다.
    suspend fun getAllData(): List<DataEntity> // 모든 데이터를 가져옵니다.
    suspend fun deleteAllData()  // 모든 데이터를 삭제합니다.
    suspend fun updateData(item: DataEntity) // 데이터를 업데이트합니다.

    //Entry
    fun getAllLiveEntry(): LiveData<List<EntryEntity>> // 모든 템플릿을 라이브로 반환합니다.
    fun getAllListEntry(): List<EntryEntity> //데이터 백업 시 반환되는 데이터

    fun getEntryById(id: Int): EntryEntity // key값으로 etntry를 추적해 반환합니다. 템플릿 전환 시 필요합니다.
    fun getLiveEntryByKey(key: String?): LiveData<List<EntryEntity>> // key값으로 추적해 live형태로 템플릿을 반환합니다.
    fun getListEntryKey(key: String?): List<EntryEntity> //템플릿 선택 백업 시

    suspend fun insertEntry(item: EntryEntity) // entry를 하나 삽입합니다.
    suspend fun insertEntryList(item: List<EntryEntity>) // entrty를 리스트로
    suspend fun deleteAllEntry() // 모든 엔트리 테이블 자체를 삭제합니다.
    suspend fun deleteEntry(id: Int) /// id를 추적헤 entry를 삭제합니다.
    suspend fun deleteEntryByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터

    suspend fun updateEntry(item: EntryEntity)


    //Budget
    suspend fun insertBudget(item: BudgetEntity) // 예산을 단일로 삽입합니다.
    suspend fun insertBudgetList(item: List<BudgetEntity>) // 예산을 list형태로 삽입합니다. 데이터 싱크 시 필요합니다.

    fun getAllLiveBudget(): LiveData<List<BudgetEntity>> // 모든 예산을 livedata로 반환합니다.
    fun getAllListBudget(): List<BudgetEntity> //데이터 백업 시 반환되는 데이터
    fun getEBudgetById(id: Int): BudgetEntity // 예산을 id를 추적해 반환합니다. 데이터 수정 시 필요 할 수도 있습니다.
    fun getLiveBudgetByKey(key: String?): LiveData<List<BudgetEntity>> // 예산을 키값으로 추적해 라이브데이터를 반환합니다.
    fun getListBudgetByKey(key: String?): List<BudgetEntity> //템플릿 선택 백업 시

    suspend fun deleteBudgetById(id: Int) // id로 추적해서 예산을 삭제합니다. 특정 예산을 삭제할 때 사용합니다.
    suspend fun deleteBudgetByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터. key값을 추적해서 해당하는 모든 값을 삭제합니다.
    suspend fun deleteAllBudget() // 모든 예산을 삭제합니다. 로그아웃 시 불러지면 됩니다.
    suspend fun updateBudget(item: BudgetEntity) // 들어온 예산의 값을 수정합니다.

    //Tag

    suspend fun insertTag(item: TagEntity) // tag를 추가합니다.
    suspend fun insertTagList(item: List<TagEntity>) // tag를 list형태로 추가합니다. 데이터 싱크 시 필요합니다.

    fun getAllLiveTag(): LiveData<List<TagEntity>>//데이터 백업 시 반환되는 데이터
    fun getAllListTag(): List<TagEntity> //데이터 백업 시 반환되는 데이터
    fun getTagById(id: Int): TagEntity // 데이터 수정시 수정할 데이터
    fun getLiveTagByKey(key: String?): LiveData<List<TagEntity>> //템플릿 선택 시 반환되는 데이터
    fun getListTagKey(key: String?): List<TagEntity> //템플릿 선택 백업 시


    suspend fun deleteTagById(id: Int) // id를 추적해서 삭제합니다. 특정 태그 삭제 시 필요합니다.
    suspend fun deleteTagByKey(key: String?) // key값을 추적해서 삭제합니다. 공유하는 key값을 가진 모든 데이터를 삭제합니다.
    suspend fun deleteAllTag() // 모든 데이터를 삭제합니다. 로그아웃 시 필요합니다.

    suspend fun updateTag(item: TagEntity) // 태그 수정 시 필요합니다.

}