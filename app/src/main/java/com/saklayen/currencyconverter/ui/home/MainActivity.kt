package com.saklayen.currencyconverter.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.saklayen.currencyconverter.R
import com.saklayen.currencyconverter.base.ui.BaseActivity
import com.saklayen.currencyconverter.databinding.ActivityMainBinding
import com.saklayen.currencyconverter.utils.positiveButton
import com.saklayen.currencyconverter.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: CurrencyConvertViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.message.collect {
                    showDialog {
                        setTitle(getString(R.string.currency_converted))
                        setMessage(it)
                        positiveButton(getString(R.string.ok)) {
                            //toastShort("ok")
                        }
                    }
                }
            }
            launch {
                viewModel.errorMessage.collect {
                    showDialog {
                        setIcon(getDrawable(R.drawable.ic_baseline_error_outline_24))
                        setTitle(getString(R.string.error))
                        setMessage(it)
                        positiveButton(getString(R.string.ok))
                    }
                }
            }
        }
    }
}