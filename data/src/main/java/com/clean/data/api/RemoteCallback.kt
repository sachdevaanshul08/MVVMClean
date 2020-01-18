package com.clean.data.api

import com.clean.data.constant.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class RemoteCallback<T>(call: Call<T>, tag: String) :
    Callback<T> {
    private var mTag: String? = null
    private var mCall: Call<T>? = null

    init {
        mTag = tag
        mCall = call
        NETWORKLIST.add(this)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        removeRequest(call.request().header(Constants.REQUEST_HEADER_TAG))
    }

    override fun onFailure(call: Call<T>, error: Throwable) {
        removeRequest(call.request().header(Constants.REQUEST_HEADER_TAG))
    }

    companion object {

        private val TAG = RemoteCallback::class.java.canonicalName
        private val NETWORKLIST = ArrayList<RemoteCallback<*>>()

        /**
         * Remove the saved network call from the ArrayList
         *
         * @param requestTag A tag which identifies the network call
         */
        fun removeRequest(requestTag: String?) {
            if (requestTag != null) {
                val iterator = NETWORKLIST.iterator()
                var item: RemoteCallback<*>?
                while (iterator.hasNext()) {
                    item = iterator.next()
                    if (requestTag.equals(item.mTag)) iterator.remove()
                }
            }
        }

        fun cancelRequest(requestTag: String?) {
            if (requestTag != null) {
                val iterator = NETWORKLIST.iterator()
                var item: RemoteCallback<*>?
                while (iterator.hasNext()) {
                    item = iterator.next()
                    if (requestTag.equals(item.mTag)) {
                        item.mCall?.cancel()
                        iterator.remove()
                    }
                }
            }
        }

        fun cancelAllRequest() {
            val iterator = NETWORKLIST.iterator()
            var item: RemoteCallback<*>?
            while (iterator.hasNext()) {
                item = iterator.next()
                item.mCall?.cancel()
                iterator.remove()
            }
        }
    }


}