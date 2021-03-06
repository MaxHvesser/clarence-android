package no.mhl.swap.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.mhl.swap.data.model.Rate
import no.mhl.swap.data.model.mapToRate
import no.mhl.swap.repository.ExchangeRatesRepository
import no.mhl.swap.util.currencyList
import java.lang.Exception

class SplashViewModel(
    private val exchangeRatesRepository: ExchangeRatesRepository
) : ViewModel() {

    // region Live Data Properties
    val downloadStatus: MutableLiveData<Int> = MutableLiveData()
    // endregion

    // region Initialisation
    init { downloadCurrencyRates() }
    // endregion

    // region Rate Downloading
    private fun downloadCurrencyRates() = CoroutineScope(Dispatchers.IO).launch {
        downloadStatus.postValue(0)
        val rates: MutableList<Rate> = mutableListOf()

        currencyList().flatMap{ it.currencies }.forEach { currency ->
            try {
                val latest = exchangeRatesRepository.fetchLatestExchangeRatesForBase(currency.name)
                rates.add(mapToRate(latest))
            } catch (exception: Exception) {
                Log.e("DownloadRates -> ", "Exception: ", exception)
            }
        }

        storeAllRates(rates)
    }
    // endregion

    // region Store Rates
    private fun storeAllRates(rates: List<Rate>) = CoroutineScope(Dispatchers.IO).launch {
        exchangeRatesRepository.clearRatesAndStore(rates)
        downloadStatus.postValue(1)
    }
    // endregion

}