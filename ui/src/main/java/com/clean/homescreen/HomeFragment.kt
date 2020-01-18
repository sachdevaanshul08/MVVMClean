package com.clean.homescreen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clean.R
import com.clean.adapters.MapAdapter
import com.clean.adapters.MapViewHolder
import com.clean.base.BaseFragment
import com.clean.data.constant.Constants
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.utils.Status
import com.clean.databinding.FragmentVehicleHomeBinding
import com.clean.mapscreen.MapDetailFragment
import com.clean.mvvm.homescreen.HomeViewModel


class HomeFragment : BaseFragment<HomeViewModel, FragmentVehicleHomeBinding>() {


    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var isLoading = false
    private var dataList: ArrayList<Vehicle?> = ArrayList<Vehicle?>()
    private lateinit var mapAdapter: MapAdapter
    private var hasLoaded = false

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(parentActivity)
    }

    /**
     * RecycleListener that completely clears the [com.google.android.gms.maps.GoogleMap]
     * attached to a row in the RecyclerView.
     * Sets the map type to [com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE] and clears
     * the map.
     */
    private val recycleListener = RecyclerView.RecyclerListener { holder ->
        if (holder is MapViewHolder) {
            holder.clearView()
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_vehicle_home
    override val title: Int
        get() = R.string.home_fragment

    override fun onViewCreation(savedInstanceState: Bundle?) {
        viewModel = getViewModel(HomeViewModel::class.java)
        mapAdapter = MapAdapter(parentActivity, dataList, ::onItemClick)
        fetchData(mapAdapter)
        // Initialise the RecyclerView.
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mapAdapter
            setRecyclerListener(recycleListener)
        }
        initListener()

    }

    /**
     * Fetch the data from database
     *
     */
    private fun fetchData(adapter: MapAdapter) {
        viewModel.getList.observe(this,
            Observer { response ->
                when (response.status) {
                    Status.LOADING -> {
                        binding.btnRetry.visibility = View.GONE
                        showProgressBar(true)
                    }
                    Status.ERROR -> {
                        showProgressBar(false)
                        showSnackBar(mRootView, response.message)
                        binding.btnRetry.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        showProgressBar(false)
                        binding.btnRetry.visibility = View.GONE
                        response.data?.let {

                            if (dataList.size == 0) {
                                dataList = ArrayList(response.data!!)
                                adapter.updateList(dataList)
                            } else {
                                if (response.data!!.isEmpty()) hasLoaded = true
                                loadedData(ArrayList(response.data!!))
                            }
                        }

                    }
                }

            })
        //Delete the data from database from initial launch (Before fetching from database)
        //  as we require fresh data each time
        viewModel.deleteStatus.observe(this, Observer {
            viewModel.computeResult(0)
        })
        viewModel.deleteData(true)
    }

    /**
     * Initialize all the listener
     *
     */
    private fun initListener() {
        binding.btnRetry.setOnClickListener { viewModel.retryRequest(Constants.REQUEST_KEY_FOR_VEHICLES) }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading && !hasLoaded) {
                    if (linearLayoutManager != null && linearLayoutManager.itemCount <= linearLayoutManager.findLastVisibleItemPosition() + 3 &&
                        linearLayoutManager.itemCount > 3
                    ) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    /**
     * Load more gets triggered on reaching on end of the recyclerview
     *
     */
    private fun loadMore() {
        viewModel.computeResult(dataList.size)
        dataList.add(null)
        mapAdapter.notifyItemInserted(dataList.size - 1)
    }

    /**
     * Populate the loaded data on recyclerview
     *
     * @param list
     */
    private fun loadedData(list: ArrayList<Vehicle?>) {
        dataList.removeAt(dataList.size - 1)
        mapAdapter.notifyItemRemoved(dataList.size)
        if (list.size > 0) {
            dataList.addAll(list)
            mapAdapter.updateList(data = dataList)
        }
        isLoading = false
    }

    private fun onItemClick(data: Vehicle?) {
        val fragment = MapDetailFragment.newInstance()
        val bundle = Bundle()
        data.let {
            bundle.putInt(MapDetailFragment.MAP_FRAGMENT_BUNDLE_KEY, data!!.id)
            fragment.arguments = bundle
        }
        parentActivity.openFragment(fragment)
    }

}