/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paysafe.demo.PreviewViewModel
import com.paysafe.demo.R
import com.paysafe.demo.data.cards.PaymentCard
import com.paysafe.demo.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {

    private var adapter = CardsAdapter(object : OnCardClickListener {
        override fun onCardClicked(card: PaymentCard) {
            viewModel.setCard(card)
            findNavController().popBackStack()
        }
    })

    private lateinit var binding: FragmentCardsBinding
    private lateinit var cardsRecyclerView: RecyclerView
    private lateinit var viewModel: PreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cards, container, false
        )
        initRecyclerView()
        binding.btnAddCard.setOnClickListener {
            findNavController().navigate(R.id.addCardFragment)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())[PreviewViewModel::class.java]
        viewModel.cards.observe(this, Observer<List<PaymentCard>> {
            adapter.setCards(it)
        })
    }

    private fun initRecyclerView() {
        cardsRecyclerView = binding.rvCards
        cardsRecyclerView.layoutManager = LinearLayoutManager(context)
        cardsRecyclerView.adapter = adapter
    }

    interface OnCardClickListener {
        fun onCardClicked(card: PaymentCard)
    }
}