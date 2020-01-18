package com.clean.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.clean.R
import com.clean.constant.Constants.Companion.POOLING
import com.clean.constant.Constants.Companion.TAXI
import com.clean.data.model.vehicles.Vehicle
import com.clean.databinding.ListItemBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/** A view holder for the map and title. */
class MapViewHolder(
    view: View,
    private val binding: ListItemBinding,
    context: Context,
    onItemClick: (Vehicle?) -> Unit
) :
    RecyclerView.ViewHolder(view),
    OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var vehicle: Vehicle


    companion object {
        fun create(parent: ViewGroup, context: Context, onItemClick: (Vehicle?) -> Unit): MapViewHolder {
            val binding: ListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item,
                parent,
                false
            )
            return MapViewHolder(binding.root, binding, context, onItemClick)
        }
    }

    var context: Context
    var onItemClick: (Vehicle?) -> Unit

    /** Initialises the MapView by calling its lifecycle methods */
    init {
        this.context = context
        this.onItemClick = onItemClick
        with(binding.liteListrowMap) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync(this@MapViewHolder)
        }
    }

    private fun setMapLocation(onItemClick: (Vehicle) -> Unit) {
        if (!::map.isInitialized) return
        with(map) {

            val latLng = LatLng(vehicle.coordinate.latitude, vehicle.coordinate.longitude)
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            uiSettings.isMapToolbarEnabled = false
            addMarker(
                MarkerOptions().position(latLng).anchor(0.5f, 0.5f).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_car)
                )
            ).rotation = vehicle.heading.toFloat()
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                onItemClick(vehicle)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        //Check If map is initialised properly
        map = googleMap ?: return
        setMapLocation(onItemClick)
    }

    /** This function is called when the RecyclerView wants to bind the ViewHolder. */
    fun bindView(vehicle: Vehicle?) {
        vehicle.let {
            this.vehicle = vehicle!!
            binding.liteListrowMap.tag = this
            binding.liteListrowText.text = vehicle.fleetType
            // We need to call setMapLocation from here because RecyclerView might use the
            // previously loaded maps
            setMapLocation(onItemClick)

            with(binding.liteListrowText) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        when (vehicle.fleetType) {
                            TAXI -> R.color.colorBlack
                            POOLING -> R.color.colorLightGrey
                            else -> R.color.colorAccent
                        }
                    )
                )
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        when (vehicle.fleetType) {
                            TAXI -> R.color.colorWhite
                            POOLING -> R.color.colorBlack
                            else -> R.color.colorBlack
                        }
                    )
                )
            }
        }

    }


    /** This function is called by the recycleListener, when we need to clear the map. */
    fun clearView() {
        if (::map.isInitialized)
            with(map) {
                // Clear the map and free up resources by changing the map type to none
                clear()
                mapType = GoogleMap.MAP_TYPE_NONE
            }
    }
}