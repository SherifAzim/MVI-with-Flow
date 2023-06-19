package com.orange.cash_ui_base


import android.util.Log
import com.orange.cash_ui_base.viewmodel.CashLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


suspend fun <T> Flow<T>.launchRequest(
    successHandler: suspend (T) -> Unit,
    errorHandler: suspend (Throwable?) -> Unit,
    loadingHandler: suspend (Boolean) -> Unit
) {
    onStart {
        Log.d("Flow Launch", "launchRequest: onStart")
        loadingHandler(true)

    }.catch {
        Log.d("Flow Launch", "launchRequest: onCatch")
        errorHandler(it)
    }.onCompletion {
        Log.d("Flow Launch", "launchRequest: onComplete")
        loadingHandler(false)
    }.collect {
        Log.d("Flow Launch", "launchRequest: collect")
        successHandler(it)
    }
}


fun <T> flowFromSuspend(function: suspend () -> T): Flow<T> = flow { emit(function()) }


