package no.mhl.swap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.mhl.swap.data.model.Currency
import no.mhl.swap.data.model.Exchange
import no.mhl.swap.data.model.defaultExchange
import no.mhl.swap.repository.ExchangeRatesRepository

class HomeViewModel(
    private val exchangeRatesRepository: ExchangeRatesRepository
) : ViewModel() {

    // region Properties
    var valueToExchangeAsString: String = "0"
    var exchangedValueAsString: String = "0.00"
    // endregion

    // region Clear Persistence
    fun clearFragmentState() {
        valueToExchangeAsString = "0"
        exchangedValueAsString = "0.00"
    }
    // endregion

    // region Store and fetch exchange
    fun fetchCurrentExchange() = liveData<Exchange?>(Dispatchers.IO) {
        fun checkExchangeCount() = exchangeRatesRepository.exchangeCount()

        suspend fun emitResultsIfPossible() {
            if (checkExchangeCount() > 0) {
                emit(exchangeRatesRepository.fetchExchangeFromDb())
            } else {
                storeDefaultExchange()
                emitResultsIfPossible()
            }
        }

        emitResultsIfPossible()
    }

    private fun storeDefaultExchange() = CoroutineScope(Dispatchers.IO).launch {
        exchangeRatesRepository.storeExchangeInDb(defaultExchange())
    }

    fun fetchRateForBase(base: String) = liveData(Dispatchers.IO) {
        emit(exchangeRatesRepository.fetchRateForBaseFromDb(base))
    }

    fun replaceExchange(exchange: Exchange) = CoroutineScope(Dispatchers.IO).launch {
        exchangeRatesRepository.replaceExchange(exchange)
    }
    // endregion

    // region Update Exchange
    fun updateExchangeBase(currency: Currency) = CoroutineScope(Dispatchers.IO).launch {
        val currentExchange = exchangeRatesRepository.fetchExchangeFromDb()
        currentExchange?.let { updatedExchange ->
            updatedExchange.from = currency
            exchangeRatesRepository.updateExchange(updatedExchange)
        }
    }

    fun updateExchangeToCurrency(currency: Currency) = CoroutineScope(Dispatchers.IO).launch {
        val currentExchange = exchangeRatesRepository.fetchExchangeFromDb()
        currentExchange?.let { updatedExchange ->
            updatedExchange.to = currency
            exchangeRatesRepository.updateExchange(updatedExchange)
        }
    }
    // endregion

}