package no.mhl.swap.ui.currencyselection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.mhl.swap.data.model.Currency
import no.mhl.swap.databinding.FragmentCurrencySelectionBinding
import no.mhl.swap.ui.currencyselection.adapter.CurrencyRecyclerAdapter
import no.mhl.swap.ui.home.HomeViewModel
import no.mhl.swap.util.currencyItemList
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrencySelectionFragment : Fragment() {

    // region Properties
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private val linearLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
    private val currencyItems = currencyItemList()
    private val currencyRecyclerAdapter: CurrencyRecyclerAdapter = CurrencyRecyclerAdapter(currencyItems)
    // endregion

    // region View Properties
    private lateinit var binding: FragmentCurrencySelectionBinding
    // endregion

    // region Initialisation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrencySelectionBinding.inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)
    }
    // endregion

    // region View Setup
    private fun setupViews() {
        setupViewInsets()
        setupCurrencyRecycler()
        setupSearchField()
    }

    private fun setupViewInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.parent) { v, insets ->
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
        binding.currencyRecycler.apply {
            adapter = currencyRecyclerAdapter
            layoutManager = linearLayoutManager
        }

        currencyRecyclerAdapter.currencyClickEvent.observe(viewLifecycleOwner, Observer { currency ->
            updateExchange(currency)
            homeViewModel.clearFragmentState()
            findNavController().popBackStack()
        })
    }
    // endregion

    // region Search Field Setup
    private fun setupSearchField() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                currencyRecyclerAdapter.filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun afterTextChanged(s: Editable?) { }
        })
    }
    // endregion

    // region Misc
    private fun updateExchange(currency: Currency) {
        arguments?.let {
            when (CurrencySelectionFragmentArgs.fromBundle(it).isBaseSelection) {
                true -> homeViewModel.updateExchangeBase(currency)
                false -> homeViewModel.updateExchangeToCurrency(currency)
            }
        }
    }
    // endregion

}