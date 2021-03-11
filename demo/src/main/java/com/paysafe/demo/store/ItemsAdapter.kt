/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paysafe.demo.data.phones.Phone
import com.paysafe.demo.databinding.ItemStoreBinding

class ItemsAdapter(
    private val mClickListener: StoreFragment.OnBuyClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mPhones: MutableList<Phone> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ItemStoreViewHolder(
            ItemStoreBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            ), mClickListener
        )

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemStoreViewHolder) {
            viewHolder.bindItem(mPhones[position])
        }
    }

    override fun getItemCount(): Int = mPhones.size

    fun setItems(phones: List<Phone>) {
        mPhones.clear()
        mPhones.addAll(phones)
        notifyDataSetChanged()
    }

    private class ItemStoreViewHolder(
        private val mBinding: ItemStoreBinding,
        private val mClickListener: StoreFragment.OnBuyClickListener?
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bindItem(phone: Phone) {
            mBinding.tvTitle.text = phone.title
            mBinding.tvDescription.text = phone.description
            mBinding.tvPrice.text = "$ ${phone.price}"
            mBinding.ivIcon.setImageResource(phone.icon)
            mClickListener?.let {
                mBinding.btnBuy.setOnClickListener {
                    mClickListener.onBuyClicked(phone.price)
                }
            }
        }
    }

}
