package com.sherif.ocfeature.domain

import com.orange.cash_ui_base.BaseResult
import com.orange.cash_ui_base.flowFromSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class Repo {

     fun getData(): Flow<FirstResult> = flowFromSuspend {
        withContext(Dispatchers.IO){
            delay(2000)
            FirstResult.SUCCESS(
                listOf(
                    Data("Orange", 10),
                    Data("Black", 20),
                    Data("Yellow", 30)
                )
            )
        }
    }


}



sealed class FirstResult : BaseResult {
    class SUCCESS(val data: List<Data>) : FirstResult()
    class ERROR(val msg: String?) : FirstResult()
}

data class Data(
    val name: String?,
    val number: Int?
)