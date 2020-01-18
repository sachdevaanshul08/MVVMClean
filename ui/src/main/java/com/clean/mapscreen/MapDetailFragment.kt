package com.clean.mapscreen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.clean.R
import com.clean.base.BaseFragment
import com.clean.customviews.CustomHighlightView
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.utils.Status
import com.clean.databinding.FragmentMapBinding
import com.clean.mvvm.homescreen.HomeViewModel
import com.clean.utils.ConnectionObserver
import com.clean.utils.extensions.getFirstChar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapDetailFragment : BaseFragment<HomeViewModel, FragmentMapBinding>() {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var dataList: List<Vehicle>
    private var map: GoogleMap? = null
    private var isLoadedOnce = false
    private var isConnected = false
    lateinit var connectionObserver: ConnectionObserver
    private lateinit var view: CustomHighlightView
    var clickedMapId: Int = -1

    override val layoutId: Int
        get() = R.layout.fragment_map

    override val title: Int
        get() = R.string.map_fragment

    companion object {
        fun newInstance() = MapDetailFragment()
        const val MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG"
        const val MAP_FRAGMENT_BUNDLE_KEY = "MAP_FRAGMENT_BUNDLE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreation(savedInstanceState: Bundle?) {
        arguments?.getInt(MAP_FRAGMENT_BUNDLE_KEY)?.let {
            clickedMapId = it
        }
        init()
        setUpObserver()
        fetchData()
    }

    private fun init() {
        connectionObserver = ConnectionObserver(parentActivity)
        binding.retryButton.setOnClickListener { view ->
            view.visibility = View.GONE
            doWork(isConnected)
        }
    }

    private fun setUpObserver() {
        viewModel = getViewModel(HomeViewModel::class.java)
        connectionObserver.observe(this, Observer { isConnected ->
            isConnected?.let {
                this.isConnected = isConnected
                doWork(isConnected)
            }
        })
    }

    private fun doWork(isConnected: Boolean) {
        if (!isConnected) {
            binding.retryButton.visibility = View.VISIBLE
            binding.retryButton.text =
                resources.getString(if (isLoadedOnce) R.string.offline_map else R.string.reload_map)
            showSnackBar(binding.root, this.resources.getString(R.string.no_network))
        } else {
            if (::dataList.isInitialized)
                setupMap()
        }
    }

    /**
     * setup map
     *
     */
    private fun setupMap() {
        editIdlingResource(true)
        //Check if mapFragment is not null, than get the retained map fragment and add the market on top of it
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()

            mapFragment?.getMapAsync { googleMap ->
                map = googleMap
                setMapLoadedCallBack(map)
                addMarker(googleMap)
            }
            // binding.framemap.id is a FrameLayout, not a Fragment so add the fragment inside it
            childFragmentManager.beginTransaction()
                .replace(binding.frameMap.id, mapFragment as Fragment, MAP_FRAGMENT_TAG)
                .commit()
        } else {
            val tempFragment: SupportMapFragment? =
                childFragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG) as SupportMapFragment
            tempFragment?.getMapAsync { googleMap ->
                addMarker(googleMap)
                setMapLoadedCallBack(map)
            }
        }

    }

    /**
     * Set the maploaded call back and start the add marker process
     *
     * @param map
     */
    private fun setMapLoadedCallBack(map: GoogleMap?) {
        map?.setOnMapLoadedCallback {
            isLoadedOnce = true
            editIdlingResource(false)
            if (binding.retryButton.isVisible) {
                if (isConnected) {
                    binding.retryButton.visibility = View.GONE
                } else {
                    binding.retryButton.text = resources.getString(R.string.offline_map)
                }
            }
        }
    }


    /**
     * Add marker over the loaded google map
     *
     * @param googleMap
     */
    private fun addMarker(googleMap: GoogleMap) {
        var clickedllng: LatLng? = null
        var latLng: LatLng? = null
        for (data in dataList) {
            latLng = LatLng(data.coordinate.latitude, data.coordinate.longitude)

            googleMap.addMarker(
                MarkerOptions().position(latLng)
                    .title(data.fleetType).anchor(
                        0.5f,
                        0.5f
                    ).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)).rotation(data.heading.toFloat())
            )

            //Check if the vehicle ID is same as the vehicle info clicked on previous screen.
            //If yes the add the circle animation over the clicked vehicle and register to trace its location in case
            // vehicle location changed on screen(not on the map) ( on zoom in/zoom out)
            //This needs to be done as coordinated has been projected to screen location to show the animation.
            // as map moves than screen location gets updated therefore animation will appear on the right vehicle
            if (clickedMapId == data.id) {
                clickedllng = latLng
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clickedllng, 11.0f))
                val point = googleMap.projection.toScreenLocation(clickedllng)
                view = CustomHighlightView(
                    parentActivity, clickedllng,
                    point, data.fleetType.getFirstChar()
                )
                binding.frameOverlay.addView(view)
                view.show()
                view.refresh(point)
                googleMap.setOnCameraIdleListener(null)
                googleMap.setOnCameraMoveListener { view.refresh(googleMap.projection.toScreenLocation(clickedllng)) }
                view.animateView()
            }

        }
    }

    /**
     * Fetch the data from the database
     *
     */
    private fun fetchData() {
        viewModel.getList.observe(this,
            Observer { response ->
                when (response.status) {
                    Status.LOADING -> {
                        println("Loading")
                    }
                    Status.ERROR -> {
                        println(response.message)
                    }
                    Status.SUCCESS -> {
                        response.data?.let { dataList = response.data!! }
                        doWork(isConnected)
                    }
                }

            })

        viewModel.computeResult(0)
    }

}
