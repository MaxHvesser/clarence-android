package no.mhl.swap.ui.currencyselection.adapter.viewholder

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import no.mhl.swap.R
import no.mhl.swap.data.model.Currency
import no.mhl.swap.data.model.view.CurrencyListItem
import no.mhl.swap.data.model.view.HEADER
import no.mhl.swap.util.currencyAsDrawable
import no.mhl.swap.util.extension.fromDpToFloat

class CurrencyItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // region View Properties
    private val name: TextView = view.findViewById(R.id.row_currency_name)
    private val fullName: TextView = view.findViewById(R.id.row_currency_full_name)
    private val flag: ImageView = view.findViewById(R.id.row_currency_flag)
    private val parent: ConstraintLayout = view.findViewById(R.id.parent)
    // endregion

    // region Properties
    private val small = 8f.fromDpToFloat(itemView.context)
    // endregion

    // region View Details Setup
    fun bind(
        currency: Currency,
        currencyClickEvent: MutableLiveData<Currency>,
        previousItem: CurrencyListItem?,
        followingItem: CurrencyListItem?
    ) {
        setupRowDetails(currency)
        setupViewBackground(previousItem, followingItem)
        itemView.setOnClickListener { currencyClickEvent.postValue(currency) }
    }

    private fun setupRowDetails(currency: Currency) {
        name.text = currency.name
        fullName.text = currency.fullName
        flag.setImageResource(currencyAsDrawable(currency.name))
    }

    private fun setupViewBackground(
        previousItem: CurrencyListItem?,
        followingItem: CurrencyListItem?
    ) {
        if (previousItem == null) return

        parent.background = backgroundForPlacement(when {
            followingItem == null -> Placement.BOTTOM
            previousItem.type == HEADER && followingItem.type == HEADER -> Placement.SINGLE
            previousItem.type == HEADER -> Placement.TOP
            followingItem.type == HEADER -> Placement.BOTTOM
            else -> Placement.MIDDLE
        })
    }
    // endregion

    // region Background Resource Generation
    private fun backgroundForPlacement(placement: Placement): Drawable {
        fun getDrawable(@DrawableRes resource: Int) =
            ContextCompat.getDrawable(itemView.context, resource)

        return getDrawable(when (placement) {
            Placement.TOP -> R.drawable.ripple_item_currency_top
            Placement.MIDDLE -> R.drawable.ripple_item_currency_middle
            Placement.BOTTOM -> R.drawable.ripple_item_currency_bottom
            Placement.SINGLE -> R.drawable.ripple_item_currency_single
        })?: GradientDrawable()
    }
    // endregion

    // region Item Background Enumeration
    private enum class Placement { TOP, MIDDLE, BOTTOM, SINGLE }
    // endregion

}