package com.clean.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.clean.data.api.ApiEmptyResponse
import com.clean.data.api.ApiErrorResponse
import com.clean.data.api.ApiResponse
import com.clean.data.api.ApiSuccessResponse
import com.clean.data.utils.Resource
import com.clean.data.utils.executors.AppExecutors

/**
 *
 * Reference from
 * https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 *
 * have customized it and made it generic by using the coroutine for switching the thread and
 * tracking the every active request based their unique header tag returned from @getNetworkRequestTag
 *
 *
 * @param Arg1
 * @param Arg2
 * @property appExecutors
 */
abstract class DataAccessProtocol<Arg1, Arg2> constructor(private val appExecutors: AppExecutors) {
    //arg1=vehicles  arg2=List<Vehicle>
    private val result = MediatorLiveData<Resource<Arg2>>()

    init {
        result.postValue(Resource.loading(null))
        @Suppress("LeakingThis")
        val dbSource: LiveData<Arg2> = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun setValue(newValue: Resource<Arg2>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<Arg2>) {
        val apiResponse: LiveData<ApiResponse<Arg1>> = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.networkIo {
                        saveCallResult(processResponse(response))
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        appExecutors.mThread {
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    // reload from disk whatever we had
                    onEmptyResponse()
                    appExecutors.mThread {
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }

                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.error.message, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() = ""
    protected open fun onEmptyResponse() = ""

    fun asLiveData() = result as LiveData<Resource<Arg2>>


    protected open fun processResponse(response: ApiSuccessResponse<Arg1>) = response.body


    protected abstract fun saveCallResult(item: Arg1)


    protected abstract fun shouldFetch(data: Arg2?): Boolean


    protected abstract fun loadFromDb(): LiveData<Arg2>


    protected abstract fun createCall(): LiveData<ApiResponse<Arg1>>

    protected abstract fun getNetworkRequestTag(): String
}