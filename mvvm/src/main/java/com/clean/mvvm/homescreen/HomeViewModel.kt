package com.clean.mvvm.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.clean.data.constant.Constants
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.utils.Resource
import com.clean.domain.vehicles.DeleteDataUseCase
import com.clean.domain.vehicles.GetVehicleListUseCase
import com.clean.mvvm.BuildConfig
import com.clean.mvvm.utils.forceRefresh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getVehicleUserCase: GetVehicleListUseCase,
    private val deleteDataUseCase: DeleteDataUseCase
) :
    ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()
    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, we can cancel all coroutines
     * launched by uiScope by calling viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _delete = MutableLiveData<Boolean>()
    private val _getList = MutableLiveData<Int>()

    val getList: LiveData<Resource<List<Vehicle>>> = Transformations
        .switchMap(_getList) {
            getVehicleUserCase.execute(viewModelScope, arrayOf(it, BuildConfig.PAGE_SIZE))
        }

    val deleteStatus: LiveData<Int> = Transformations.switchMap(_delete) {
        deleteDataUseCase.execute(viewModelScope)
    }

    fun computeResult(data: Int) {
        this._getList.value = data
    }

    fun deleteData(data: Boolean) {
        this._delete.value = data
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun retryRequest(networkTag: String) {
        if (networkTag.equals(Constants.REQUEST_KEY_FOR_VEHICLES)) _getList.forceRefresh()
    }


}