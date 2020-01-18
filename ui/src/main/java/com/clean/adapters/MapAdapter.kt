package com.clean.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clean.data.model.vehicles.Vehicle

/**
 *  Recycler view adapter to display text and map on each item of recycler view
 */
class MapAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> MapViewHolder.create(parent, context, onItemClick)
            else -> LoadingViewHolder.create(parent, context = context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MapViewHolder -> holder.bindView(dataList[position]) ?: return
            is LoadingViewHolder -> { //DO Nothing
            }
        }
    }

    lateinit var dataList: List<Vehicle?>
    lateinit var onItemClick: (Vehicle?) -> Unit

    constructor(context: Context, data: List<Vehicle?>, onItemClick: (Vehicle?) -> Unit) : this(context) {
        this.dataList = data
        this.onItemClick = onItemClick
    }

    fun updateList(data: ArrayList<Vehicle?>) {
        this.dataList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (dataList.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

}