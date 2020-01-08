package no.mhl.clarence.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.mhl.clarence.R
import no.mhl.clarence.ui.home.adapter.CurrencyRecyclerAdapter
import no.mhl.clarence.ui.views.keypad.KeypadView
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    // region Properties
    private val homeViewModel: HomeViewModel by viewModel()

    private val testItems = arrayListOf(10000, 20, 3)
    private val currencyRecyclerAdapter = CurrencyRecyclerAdapter(testItems)
    private val currencyLayoutManager = LinearLayoutManager(context)
    // endregion

    // region Initialisation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setupView(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.requestApplyInsets(view)
    }
    // endregion

    // region View Setup
    private fun setupView(view: View) {
        val keypadParent = view.findViewById<ConstraintLayout>(R.id.home_keypad_parent)
        val currencyRecycler = view.findViewById<RecyclerView>(R.id.home_currency_recycler)

        setupViewInsets(keypadParent, currencyRecycler)
        setupCurrencyRecycler(currencyRecycler)
    }

    private fun setupViewInsets(keypadParent: View, currencyRecycler: View) {
        ViewCompat.setOnApplyWindowInsetsListener(keypadParent) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(currencyRecycler) { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }
    // endregion

    // region Recycler Setup
    private fun setupCurrencyRecycler(recycler: RecyclerView) {
        recycler.apply {
            adapter = currencyRecyclerAdapter
            layoutManager = currencyLayoutManager
        }
    }
    // endregion

}