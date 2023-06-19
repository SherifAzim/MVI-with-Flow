package com.sherif.mviflow

import androidx.lifecycle.viewModelScope
import com.orange.cash_ui_base.BaseIntent
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.flowFromSuspend
import com.orange.cash_ui_base.launchRequest
import com.orange.cash_ui_base.viewmodel.BaseViewModel
import com.orange.cash_ui_base.viewmodel.CashLoading
import com.sherif.ocfeature.domain.Data
import com.sherif.ocfeature.domain.FirstResult
import com.sherif.ocfeature.ui.SecondState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainVM : BaseViewModel() {

    override suspend fun handleIntent(intent: BaseIntent) {
        when (intent) {
            is MainIntent.GetData -> getDate()
        }
    }

    private fun getDate() {

        viewModelScope.launch {
            buildData().launchRequest(
                successHandler = {
                    when (it) {
                        is MainState.DATA -> updateState(it)
                    }
                }, errorHandler = {},
                loadingHandler =  { updateState(CashLoading(it , false)) }
            )
        }

    }

    private fun buildData(): Flow<MainState> = flowFromSuspend {
        delay(2000)
        MainState.DATA(
            Data("Main Fragment", 100)
        )
    }
}

sealed class MainIntent : BaseIntent {
    object GetData : MainIntent()
}

sealed class MainState : BaseState {
    class DATA(val data: Data) : MainState()
    class ERROR(val msg: String?) : MainState()
}