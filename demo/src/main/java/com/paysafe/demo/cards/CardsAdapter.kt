/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paysafe.demo.data.cards.PaymentCard
import com.paysafe.demo.databinding.RowCardBinding
import com.paysafe.demo.util.formatAsCardNumber

class CardsAdapter(private val mClickListener: CardsFragment.OnCardClickListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mCards: MutableList<PaymentCard> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder = CardViewHolder(
        RowCardBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        ), mClickListener
    )

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is CardViewHolder) {
            viewHolder.bindItem(mCards[position])
        }
    }

    override fun getItemCount() = mCards.size

    fun setCards(cards: List<PaymentCard>) {
        mCards.apply {
            clear()
            addAll(cards)
        }
        notifyDataSetChanged()
    }

    private class CardViewHolder(private val mBinding: RowCardBinding,
                                 private val mClickListener: CardsFragment.OnCardClickListener?) : RecyclerView.ViewHolder(mBinding.root) {
        fun bindItem(card: PaymentCard) {
            mBinding.tvCardNumber.text = card.number.formatAsCardNumber()
            card.flow3DS?.let {
                mBinding.tvFlow3ds.text = it
            }
            mClickListener?.let {
                mBinding.cvItem.setOnClickListener {
                    mClickListener.onCardClicked(card)
                }
            }

        }
    }

}