package com.saklayen.currencyconverter.ui.home

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saklayen.currencyconverter.R
import com.saklayen.currencyconverter.database.model.Transaction
import com.saklayen.currencyconverter.database.model.Wallet
import com.saklayen.currencyconverter.database.repositories.WalletRepositories
import com.saklayen.currencyconverter.domain.Result
import com.saklayen.currencyconverter.domain.currencyrate.CurrencyRateUseCase
import com.saklayen.currencyconverter.model.CurrencyRate
import com.saklayen.currencyconverter.utils.EMPTY
import com.saklayen.currencyconverter.utils.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyConvertViewModel @Inject constructor(
    application: Application,
    currencyRateUseCase: CurrencyRateUseCase,
    private val walletRepositories: WalletRepositories
) :
    ViewModel() {
    /** Using the currencyrateMock data because the api requires a access_key,
    but I haven't provided the key*/
    var currencyRateMock = MutableStateFlow(
        CurrencyRate(
            base = "EUR",
            date = "01/01/2022",
            rates = CurrencyRate.Rates(
                gBP = 0.84,
                jPY = 130.89,
                uSD = 1.14
            )
        )
    )
    private val syncPeriod : Long= 5000
    var currencies = application.resources.getStringArray(R.array.array_currency).toList()
    var fromAmount = MutableStateFlow("0.00")
    var toAmount = MutableStateFlow("0.00")
    var commissionFee = MutableStateFlow(0.00)
    var commissionRate = MutableStateFlow(0.007)

    var fromIndex = 0
    var toIndex = 0

    var walletList = MutableStateFlow(
        Result.success(
            mutableListOf(
                Wallet(rowid = 1, currencyName = "x", balance = 0f),
                Wallet(rowid = 2, currencyName = "y", balance = 0f),
                Wallet(rowid = 3, currencyName = "z", balance = 0f)
            )
        )
    )
    var toWalletList =
        MutableStateFlow(Result.success(mutableListOf(Wallet(currencyName = "", balance = 0f))))
    var fromWalletList =
        MutableStateFlow(Result.success(mutableListOf(Wallet(currencyName = "", balance = 0f))))

    var fromCurrency = MutableStateFlow(String.EMPTY)
    var toCurrency = MutableStateFlow(String.EMPTY)


    private val _message = Channel<String>(Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    private val _errorMessage = Channel<String>(Channel.CONFLATED)
    val errorMessage = _errorMessage.receiveAsFlow()

    private val _submit = Channel<Unit>(Channel.CONFLATED)
    val submit = _submit.receiveAsFlow()

    private val fetchCurrencyRate =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val currencyRate: StateFlow<Result<CurrencyRate>> = fetchCurrencyRate.flatMapLatest {
        currencyRateUseCase(String.EMPTY)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = Result.nothing()
    )

    var numberOfTransactions = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    //fetching currency rates
                    fetchCurrencyRate()
                }
            }, 0, syncPeriod)

        }

        viewModelScope.launch {
            currencyRate.collect {
                Timber.d("rate--> message:  %s", it.message)

            }
        }

        viewModelScope.launch {
            walletRepositories.getTransactionCounts()?.collect {
                numberOfTransactions.value = it
            }
        }
        viewModelScope.launch {
            walletRepositories.getWallets()?.collect {
                if (it.isNotEmpty()) {
                    walletList.value = Result.success(it.toMutableList())
                    fromCurrency.value = walletList.value.data?.get(0)?.currencyName.toString()
                    val toDataList = mutableListOf<Wallet>()
                    walletList.value.data?.forEach { wallet ->
                        if (wallet.currencyName != fromCurrency.value) {
                            toDataList.add(wallet)
                        }
                    }
                    toWalletList.value = Result.success(toDataList)
                    fromWalletList.value =
                        Result.success(mutableListOf(walletList.value.data?.get(0)!!))
                    toCurrency.value = toWalletList.value.data?.get(0)!!.currencyName
                    Timber.d("wallets %s", walletList.value.data?.get(1)?.rowid)
                }
            }
        }
        viewModelScope.launch {
            walletRepositories.getWalletCounts()?.collect {
                Timber.d("wallets count $it")
                if (it == 0) {
                    walletRepositories.insertWallets(
                        Wallet(
                            currencyName = "EUR",
                            balance = 1000.00F
                        )
                    )
                    walletRepositories.insertWallets(
                        Wallet(
                            currencyName = "USD",
                            balance = 0.00F
                        )
                    )
                    walletRepositories.insertWallets(
                        Wallet(
                            currencyName = "GBP",
                            balance = 0.00F
                        )
                    )
                }
            }

        }

        viewModelScope.launch {
            fromAmount.collect {
                convertCurrency(it)
            }
        }
        viewModelScope.launch {
            submit.collect {
                val msg =
                    "You have converted ${fromAmount.value} ${fromCurrency.value} to ${toAmount.value} ${toCurrency.value}. Commission fee  ${commissionFee.value} ${fromCurrency.value}"
                _message.trySend(msg)
                fromAmount.value = (0.00).toString()
            }
        }
    }

    fun fetchCurrencyRate() {
        Timber.d("API emitting call--->")
        fetchCurrencyRate.tryEmit(Unit)
    }

    fun onSelectCurrency(item: Any, type: Int) {
        if (type == 1) {
            fromCurrency.value = item.toString()
        } else {
            toCurrency.value = item.toString()
            convertCurrency(fromAmount.value)
        }
    }

    fun convertCurrency(amount: String) {
        if (amount.isNotBlank()) {
            var rate = 0.00
            when (toCurrency.value) {
                "USD" -> rate = currencyRateMock.value.rates?.uSD!!
                "GBP" -> rate = currencyRateMock.value.rates?.gBP!!
                "JPY" -> rate = currencyRateMock.value.rates?.jPY!!
            }
            toAmount.value = String.format("%.2f", ((amount.toFloat() * rate)))
        } else {
            toAmount.value = String.format("%.2f", 0.00)
        }
    }

    fun submit() {

        if (fromAmount.value.isBlank() || fromAmount.value.toFloat() == 0f){
            _errorMessage.trySend("Invalid input amount")
            return
        }
        else if((walletList.value.data?.get(
                fromIndex
            )?.balance?.minus(
                fromAmount.value.toFloat() + commissionFee.value
            ))!! <= 0){
            _errorMessage.trySend("Base wallet balance can not be zero after transaction")
            return
        }
        else {

            if (numberOfTransactions.value > 5 || fromAmount.value.toInt() > 200) {
                commissionFee.value = (fromAmount.value.toDouble() * commissionRate.value)
            }

            walletList.value.data?.forEach {
                if (toCurrency.value == it.currencyName) toIndex = it.rowid
            }
            walletList.value.data?.get(fromIndex)?.balance?.minus(fromAmount.value.toFloat() + commissionFee.value.toFloat())
                ?.let {
                    walletList.value.data?.get(fromIndex)?.currencyName?.let { it1 ->
                        Wallet(
                            rowid = 1,
                            balance = it,
                            currencyName = it1
                        )
                    }
                }?.let {
                    viewModelScope.launch {
                        walletRepositories.updateWallets(
                            it
                        )
                    }

                }

            walletList.value.data?.get(toIndex - 1)?.balance?.plus(toAmount.value.toFloat() - commissionFee.value.toFloat())
                ?.let {
                    walletList.value.data?.get(toIndex - 1)?.currencyName?.let { it1 ->
                        Wallet(
                            rowid = toIndex,
                            balance = it,
                            currencyName = it1
                        )
                    }
                }?.let {
                    viewModelScope.launch {
                        walletRepositories.updateWallets(
                            it
                        )
                    }

                }

            viewModelScope.launch {
                walletRepositories.insertTransaction(
                    Transaction(
                        fromWalletId = fromIndex.toString(),
                        toWalletId = toIndex.toString(),
                        fromAmount = fromAmount.value,
                        toAmount = toAmount.value,
                        commission = commissionFee.value.toString()
                    )
                )
            }
            _submit.trySend(Unit)
        }
    }

}
