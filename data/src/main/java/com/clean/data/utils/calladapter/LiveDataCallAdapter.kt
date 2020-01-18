package com.clean.data.utils.calladapter

import androidx.lifecycle.LiveData
import com.clean.data.api.ApiResponse
import com.clean.data.api.RemoteCallback
import com.clean.data.constant.Constants

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * /**
 * Reference from
 * https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 *
 * To convert the retrofit api response to live data
 *
*/
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 *
 *
 * Here I have done little customization
 * @RemoteCallback keeps track of every active network request running through out the application.
 * so that it would be easy to cancel the active request if need be
 * @param <R>
</R> */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object :
                        RemoteCallback<R>(call, call.request().header(Constants.REQUEST_HEADER_TAG)!!) {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            super.onResponse(call, response)
                            if (call.isCanceled) return;
                            postValue(ApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, error: Throwable) {
                            super.onFailure(call, error)
                            if (call.isCanceled) return;
                            postValue(ApiResponse.create(error))
                        }
                    })
                }
            }
        }
    }
}
