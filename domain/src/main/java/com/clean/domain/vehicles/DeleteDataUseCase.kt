package com.clean.domain.vehicles

import androidx.lifecycle.LiveData
import com.clean.data.repo.VehicleRepo
import com.clean.data.utils.executors.AppExecutors
import com.clean.domain.baseusecase.UseCase
import javax.inject.Inject

class DeleteDataUseCase @Inject constructor(
    private val vehicleRepo: VehicleRepo,
    private val appExecutors: AppExecutors
) : UseCase<Int, Any>(appExecutors) {

    override fun buildUseCase(appExecutors: AppExecutors, params: Any?): LiveData<Int> {
        return vehicleRepo.delete(appExecutors)
    }

}
