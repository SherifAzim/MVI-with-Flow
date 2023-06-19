package com.sherif.mviflow

import androidx.navigation.fragment.findNavController
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.CashDeepLinkDest
import com.orange.cash_ui_base.deepLinkNavigateTo
import com.orange.cash_ui_base.fragment.BaseFragment
import com.sherif.mviflow.databinding.FragmentMainBinding
import com.sherif.ocfeature.domain.Data

class MainFragment : BaseFragment<MainVM, FragmentMainBinding>(
    MainVM::class.java,
    FragmentMainBinding::inflate
) {

    override fun initView(isInitialState: Boolean) {
        if (isInitialState) binding.tvState.text = "Looding.... !!"

        binding.btnNext.setOnClickListener {
            findNavController().deepLinkNavigateTo(CashDeepLinkDest.FirstFragment)
        }

        binding.btnResend.setOnClickListener {updateIntent(MainIntent.GetData)}
    }

    override fun onAction() {
        updateIntent(MainIntent.GetData)

    }

    override fun render(state: BaseState) {
        super.render(state)
        when(state){
            is MainState.DATA -> handleData(state.data)
        }
    }

    private fun handleData(data: Data) {
        binding.tvState.text = data.toString()
    }

}