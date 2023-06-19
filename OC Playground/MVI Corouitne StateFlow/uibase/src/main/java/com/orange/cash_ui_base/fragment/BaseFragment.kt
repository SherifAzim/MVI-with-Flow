package com.orange.cash_ui_base.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.orange.cash_ui_base.BaseIntent

import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.R
import com.orange.cash_ui_base.viewmodel.BaseViewModel
import com.orange.cash_ui_base.viewmodel.CashLoading
import com.orange.cash_ui_base.viewmodel.IdleState


import kotlinx.coroutines.CoroutineScope

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>(
    private val vmClass: Class<VM>,
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    val TAG = this.javaClass.simpleName ?: "BaseFragment"
    private var loadingDialog: Dialog? = null

    protected open fun viewModelStoreOwner(): ViewModelStoreOwner? = null

    protected val viewModel: VM by lazy {
        ViewModelProvider(viewModelStoreOwner() ?: this)[vmClass]
    }


    protected val fragmentScope: CoroutineScope by lazy { viewLifecycleOwner.lifecycleScope }


    abstract fun initView(isInitialState:Boolean)
    abstract fun onAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        initView(isInitialState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLoadingDialog()
        onAction()
        bindVM()
    }

    private fun bindVM() {
        initStates()
        viewModel.initIntents()
    }

    @CallSuper
    open fun render(state: BaseState) {
    }

    private var isInitialState = false

     fun retainLastState(action:(BaseState) -> Unit):Boolean{
        var isNotIdle = false
         viewModel.retainLastState {
             isNotIdle = when(it){
                 is IdleState -> false
                 else -> {
                     action(it)
                     true
                 }
             }
        }
         return isNotIdle
    }
    fun onRetainLastState(action:(BaseState) -> Unit){
       retainLastState(action).also { isInitialState =  !it }
    }

    private fun initStates() {
        viewModel.initSingleEvent(action = { render(it) }, loading = { showCashLoadingUI(it as CashLoading) })
        viewModel.initStates(action = { render(it) }, loading = { showCashLoadingUI(it as CashLoading) })
    }

    private fun showCashLoadingUI(state: CashLoading) {
        //Implement Here the default Loading UI..
        if (state.isLoading)
            showLoadingDialog()
        else
            hideLoadingDialog()
    }

    fun updateIntent(intent: BaseIntent) {
        Log.d(TAG, "updateIntent: ${intent.toString()}")
        viewModel.sendIntent(intent)
    }

    private fun createLoadingDialog() {
        context?.let {
            loadingDialog = Dialog(it, R.style.CashLoadingDialogTheme)
            loadingDialog?.setContentView(R.layout.cash_general_loading)
            loadingDialog?.setCancelable(false)
        }
    }

    fun showLoadingDialog() {
        loadingDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    fun hideLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}