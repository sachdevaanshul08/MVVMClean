package com.clean.data.repo

import androidx.lifecycle.LiveData
import com.clean.data.DataAccessProtocol
import com.clean.data.api.ApiResponse
import com.clean.data.api.VehicleApi
import com.clean.data.constant.Constants
import com.clean.data.db.dao.vehicles.VehiclesDao
import com.clean.data.db.database.AppDatabase
import com.clean.data.model.vehicles.Coordinate
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.model.vehicles.Vehicles
import com.clean.data.utils.Resource
import com.clean.data.utils.executors.AppExecutors
import javax.inject.Inject

class VehicleRepo @Inject constructor(
    private val vehicleApi: VehicleApi,
    private val vehicleDao: VehiclesDao,
    private val db: AppDatabase
) {

    /**
     * Get the data from database.
     * here database is acting as single source of truth. If data not available than it will fetch from network and
     * add into itself for handling further queries.that means further requests will not go to network.
     * As of now inside  "ShouldFetch" is taking of that. It can be modified as per project basis.
     *
     *
     * @param appExecutors kotlin couroutines taking care of database transactions on other thread
     * @param offset initial value
     * @param limit limit
     * @return
     */
    fun getVehicles(appExecutors: AppExecutors, offset: Int, limit: Int): LiveData<Resource<List<Vehicle>>> {
        return object : DataAccessProtocol<Vehicles, List<Vehicle>>(appExecutors) {

            override fun saveCallResult(item: Vehicles) {
                if (item.vehicleList.isEmpty()) onEmptyResponse()
                db.runInTransaction {
                    vehicleDao.delete()
                    item.let { data ->
                        db.runInTransaction {
                            val start = offset
                            val items = data.vehicleList.mapIndexed { pointer, child ->
                                child.index = start + pointer
                                child
                            }
                            vehicleDao.insert(items)
                        }
                    }
                }

            }

            override fun shouldFetch(data: List<Vehicle>?): Boolean {
                return offset == 0 && (data == null || data.isEmpty())
            }

            override fun loadFromDb(): LiveData<List<Vehicle>> {
                return vehicleDao.getDataRange(offset, limit)
            }

            override fun createCall(): LiveData<ApiResponse<Vehicles>> {
                //This one is temporary added here
                //IN case be directly added in vehicle api in the URL
                //OR it can be accessed from buildconfig/constants or anything like that.
                var c = Coordinate(53.694865, 9.757589)
                var c1 = Coordinate(53.394655, 10.099891)

                return vehicleApi.getVehicles(
                    getNetworkRequestTag(),
                    c.latitude,
                    c.longitude,
                    c1.latitude,
                    c1.longitude
                )
            }

            override fun getNetworkRequestTag(): String {
                return Constants.REQUEST_KEY_FOR_VEHICLES
            }

        }.asLiveData()
    }

    /**
     * To Delete the data from database
     *
     * @param appExecutors
     * @return
     */
    fun delete(appExecutors: AppExecutors): LiveData<Int> {
        vehicleDao.delete()
        return vehicleDao.vehicleCount()

    }


}