package com.sherif.ocfeature.ui


import androidx.navigation.fragment.findNavController
import com.orange.cash_ui_base.BaseState
import com.orange.cash_ui_base.fragment.BaseFragment
import com.sherif.ocfeature.databinding.FragmentSecondBinding
import com.sherif.ocfeature.domain.Data
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseFragment<SecondVM, FragmentSecondBinding>(
    vmClass = SecondVM::class.java,
    inflate = FragmentSecondBinding::inflate
) {

    override fun initView(isInitialState: Boolean) {
        binding.textviewSecond.text = "Looding...!!"
        binding.buttonSecond.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.buttonResend.setOnClickListener { updateIntent(SecondIntent.GetData) }

    }

    override fun onAction() {
        updateIntent(SecondIntent.GetData)
    }

    override fun render(state: BaseState) {
        super.render(state)
        when(state){
            is SecondState.DATA -> handleDateState(state.data)
        }

    }

    private fun handleDateState(data: List<Data>) {
        binding.textviewSecond.text  = data.first().toString()
    }

}
