package com.orange.cash_ui_base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.launchRequest
import com.orange.cash_ui_base.BaseIntent
import com.orange.cash_ui_base.BaseResult
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class BaseViewModel : ViewModel() {


    init {

    }

    private val _intent: MutableSharedFlow<BaseIntent> = MutableSharedFlow(replay = 10)
    val intents: SharedFlow<BaseIntent> = _intent.asSharedFlow()

    private val _states: MutableStateFlow<BaseState> = MutableStateFlow(IdleState)
    val states: StateFlow<BaseState> = _states.asStateFlow()


    private val eventChannel = Channel<BaseState>(Channel.UNLIMITED)
    val singleEvent: Flow<BaseState> = eventChannel.receiveAsFlow()

    private val jobs = SupervisorJob()

    val TAG = this.javaClass.simpleName ?: "BaseViewModel"

    fun sendIntent(intent: BaseIntent) = _intent.tryEmit(intent)


    /**
     *loadingHandler is optional, if haven't set it will show the default loading ui,
     * by emitting a CashLoading state which handled in BaseFragment  */

    fun <T : BaseResult> launch(
        request: Flow<T>,
        successHandler: suspend (T) -> Unit,
        errorHandler: suspend (Throwable?) -> Unit,
        hasLoadingUi: Boolean = false,
        loadingHandler: suspend (Boolean) -> Unit = { updateState(CashLoading(it, hasLoadingUi = hasLoadingUi)) }
    ) {
        Log.d(TAG, "launch: ${request.toString()}")
        val job = viewModelScope.launch {
            request.launchRequest(
                successHandler = successHandler,
                errorHandler = errorHandler,
                loadingHandler = loadingHandler
            )
        }
        jobs.children.plus(job)
    }


    fun sendSingleEvent(state: BaseState) = eventChannel.trySend(state)
    fun updateState(state:BaseState) =  _states.apply { this.value = state }

    fun retainLastState(action: (BaseState) -> Unit){
        val lastState =  _states.value
        action(lastState)
    }

    fun initSingleEvent(action: (BaseState) -> Unit, loading: (BaseState) -> Unit) {
        viewModelScope.launch {
            singleEvent
                .catch { e -> e.printStackTrace() }
                .collect {
                    Log.d(TAG, "onStateChanged: $it")
                    it.takeIf { it is CashLoading }
                        ?.also { state -> loading(state as CashLoading) }
                    action(it)
                }
        }
    }

    fun initStates(action: (BaseState) -> Unit, loading: (BaseState) -> Unit) {
        viewModelScope.launch {
            states.onEach {
                Log.d(TAG, "onStateChanged: $it")
                it.takeIf { it is CashLoading }
                    ?.also { state -> loading(state as CashLoading) }
                action(it)
            }.collect()
        }
    }


    fun initIntents() {
        viewModelScope.launch {
            intents.onEach {
                Log.d(TAG, "onEachIntent: $it ")
                handleIntent(it)
            }.collect()
        }
    }


    abstract suspend fun handleIntent(intent: BaseIntent)


    override fun onCleared() {
        super.onCleared()
        jobs.cancelChildren()
        eventChannel.close()

    }


}


class CashLoading(val isLoading: Boolean, val hasLoadingUi: Boolean) : BaseState

object IdleState : BaseState

object IdleIntent : BaseIntent


