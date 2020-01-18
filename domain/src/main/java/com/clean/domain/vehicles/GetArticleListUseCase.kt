package com.clean.domain.vehicles

import androidx.lifecycle.LiveData
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.repo.VehicleRepo
import com.clean.data.utils.Resource
import com.clean.data.utils.executors.AppExecutors
import com.clean.domain.baseusecase.UseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetVehicleListUseCase @Inject constructor(
    private val vehicleRepo: VehicleRepo,
    private val appExecutors: AppExecutors
) : UseCase<Resource<List<Vehicle>>, Array<Int>>(appExecutors) {

    override fun buildUseCase(
        appExecutors: AppExecutors,
        params: Array<Int>?
    ): LiveData<Resource<List<Vehicle>>> {
        return vehicleRepo.getVehicles(appExecutors, params!![0], params[1])
    }
}

