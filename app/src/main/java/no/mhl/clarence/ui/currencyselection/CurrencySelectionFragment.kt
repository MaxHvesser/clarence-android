package no.mhl.clarence.ui.currencyselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.mhl.clarence.R
import no.mhl.clarence.data.model.Currency
import no.mhl.clarence.data.model.Rates
import no.mhl.clarence.ui.currencyselection.adapter.CurrencyRecyclerAdapter
import no.mhl.clarence.util.generateCurrencyList
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencySelectionFragment : Fragment() {

    // region Properties
    private val currencySelectionViewModel: CurrencySelectionViewModel by viewModel()
    private val linearLayoutManager = LinearLayoutManager(context)
    private val currencyRates: List<Currency> = generateCurrencyList()
    private val currencyRecyclerAdapter: CurrencyRecyclerAdapter = CurrencyRecyclerAdapter(currencyRates)
    // endregion

    // region View Properties
    private lateinit var parent: ConstraintLayout
    private lateinit var currencyRecycler: RecyclerView
    // endregion

    // region Initialisation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_currency_selection, container, false)
        setupViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)
    }
    // endregion

    // region View Setup
    private fun setupViews(view: View) {
        parent = view.findViewById(R.id.parent)
        currencyRecycler = view.findViewById(R.id.currency_recycler)

        setupViewInsets()
        setupCurrencyRecycler()
    }

    private fun setupViewInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(parent) { v, insets ->
            v.updatePadding(
                top = insets.systemWindowInsetTop,
                bottom = insets.systemWindowInsetBottom
            )
            insets
        }
    }
    // endregion

    // region Recycler Setup
    private fun setupCurrencyRecycler() {
        currencyRecycler.apply {
            adapter = currencyRecyclerAdapter
            layoutManager = linearLayoutManager
        }
    }
    // endregion

}