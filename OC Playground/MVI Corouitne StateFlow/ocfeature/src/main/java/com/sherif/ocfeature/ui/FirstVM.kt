package com.sherif.ocfeature.ui

import com.orange.cash_ui_base.BaseIntent
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.viewmodel.BaseViewModel
import com.sherif.ocfeature.domain.Data
import com.sherif.ocfeature.domain.FirstResult
import com.sherif.ocfeature.domain.Repo


class FirstVM : BaseViewModel() {

    private val repo: Repo by lazy { Repo() }
    override suspend fun handleIntent(intent: BaseIntent) {
        when (intent) {
            is FirstIntent.GetData -> getData()
        }
    }

    private fun getData() {
        launch(
            request = repo.getData(),
            successHandler = {
                when (it) {
                    is FirstResult.SUCCESS -> updateState(FirstStates.DATA(it.data))
                    is FirstResult.ERROR -> updateState(FirstStates.ERROR(it.msg))
                }
            },
            errorHandler = {
                it?.printStackTrace()
                updateState(FirstStates.ERROR(it?.message))
            }
        )
    }
}

sealed class FirstIntent:BaseIntent  { object GetData : FirstIntent() }

sealed class FirstStates : BaseState {
    class DATA(val data: List<Data>) : FirstStates()
    class ERROR(val msg: String?) : FirstStates()
}