package com.sherif.ocfeature.ui

import com.orange.cash_ui_base.BaseIntent
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.viewmodel.BaseViewModel
import com.sherif.ocfeature.domain.Data
import com.sherif.ocfeature.domain.FirstResult
import com.sherif.ocfeature.domain.Repo
import kotlin.reflect.KProperty

class SecondVM : BaseViewModel() {

    private val repo: Repo by lazy { Repo() }

    override suspend fun handleIntent(intent: BaseIntent) {
        when (intent) {
            is SecondIntent.GetData -> getDate()
        }
    }

    private fun getDate() {
        launch(
            request = repo.getData(),
            successHandler = {
                when (it) {
                    is FirstResult.SUCCESS -> updateState(SecondState.DATA(it.data))
                    is FirstResult.ERROR -> updateState(SecondState.ERROR(it.msg))
                }
            },
            errorHandler = {}
        )
    }
}

sealed class SecondIntent : BaseIntent {
    object GetData : SecondIntent()
}

sealed class SecondState : BaseState {
    class DATA(val data: List<Data>) : SecondState()
    class ERROR(val msg: String?) : SecondState()
}