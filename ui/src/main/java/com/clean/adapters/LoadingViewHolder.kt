package com.clean.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.clean.R
import com.clean.databinding.ItemLoadingBinding


//* A view holder for the loading.

class LoadingViewHolder(view: View, private val binding: ItemLoadingBinding, context: Context) :
    RecyclerView.ViewHolder(view) {


    companion object {
        fun create(parent: ViewGroup, context: Context): LoadingViewHolder {
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_loading,
                parent,
                false
            )
            return LoadingViewHolder(binding.root, binding, context)
        }
    }

    var context: Context

    init {
        this.context = context
    }

}
