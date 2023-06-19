package com.sherif.ocfeature.ui

import androidx.navigation.fragment.findNavController
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.fragment.BaseFragment
import com.sherif.ocfeature.R
import com.sherif.ocfeature.databinding.FragmentFirstBinding
import com.sherif.ocfeature.domain.Data
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirstFragment : BaseFragment<FirstVM, FragmentFirstBinding>(
    FirstVM::class.java,
    FragmentFirstBinding::inflate
) {


    override fun initView(isInitialState: Boolean) {
        binding.textviewFirst.text = "Looding....!!"
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.buttonResend.setOnClickListener {
            updateIntent(FirstIntent.GetData)
        }
    }

    override fun onAction() {
        updateIntent(FirstIntent.GetData)
    }

    override fun render(state: BaseState) {
        super.render(state)
        when(state){
            is FirstStates.DATA ->handleDataState(state.data)
            is FirstStates.ERROR -> handleErrorState(state.msg)
        }

    }

    private fun handleErrorState(msg: String?) {
        binding.textviewFirst.text = msg ?: "Error"
    }

    private fun handleDataState(dataList: List<Data>) {
        binding.textviewFirst.text = dataList.toString()
    }
}